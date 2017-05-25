package sp.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sp.data.dao.interfaces.OrderDao;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.OrderService;
import sp.data.services.interfaces.SpService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service("OrderService")
public class OrderServiceImpl extends GenericServiceImpl<Order, Long> implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    SpService spService;

    @Override
    public Order getByIdWithAllChildren(Long id) {
        return orderDao.getByIdWithAllChildren(id);
    }

    @Override
    public void updateStatuses(Sp sp, OrderStatus orderStatus) {
        orderDao.updateStatuses(sp, orderStatus);
    }

    @Override
    public void processSpStatus(Order order) {
        System.out.println("inside OrderService -> processSpStatus()");
        Sp sp = order.getSp();
        if (sp == null) return;

        Set<Order> allOrdersInSp = sp.getOrders();
        if (allOrdersInSp == null || allOrdersInSp.isEmpty()) return;

        Set<OrderStatus> ordersStatuses = new HashSet<>();
        for (Order orderInSp : allOrdersInSp) {
            if (orderInSp.getId().equals(order.getId())) {
                ordersStatuses.add(order.getStatus());
            }
            else {
                ordersStatuses.add(orderInSp.getStatus());
            }
        }

        switch (order.getStatus()){
            case COMPLETED:
                System.out.println("Case: COMPLETED");
                // Статус всех заказов должен быть COMPLETED
                if (ordersStatuses.size() == 1 && ordersStatuses.contains(OrderStatus.COMPLETED)){
                    // Статцс СП должен быть DISTRIBUTING
                    if (sp.getStatus() == SpStatus.DISTRIBUTING){
                        sp.setStatus(SpStatus.COMPLETED);
                        spService.update(sp);
                    }
                }
                break;
        }

    }

    @Override
    public void processStatus(Order order){

        switch (order.getStatus()){
            case UNPAID: // Не оплачен
                if (order.getPrepaid().compareTo(BigDecimal.ZERO) > 0) {
                    order.setStatus(OrderStatus.PAID);
                }
                break;
        }
    }

}
