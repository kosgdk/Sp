package sp.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sp.data.dao.interfaces.SpDao;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.OrderService;
import sp.data.services.interfaces.SpService;

import java.util.*;


@Service("SpService")
public class SpServiceImpl extends GenericServiceImpl<Sp, Long> implements SpService {

	@Autowired
	SpDao spDao;

    @Autowired
    OrderService orderService;
	
	@Override
	public Long getLastNumber() {
		return spDao.getLastNumber();
	}

	@Override
	public Sp getLastSp() {
		return spDao.getLastSp();
	}

	@Override
	public Sp getByIdWithAllChildren(Long id) {
		return spDao.getByIdWithAllChildren(id);
	}

	@Override
	public List<Long> getIdsByStatus(SpStatus... statuses) {
		return spDao.getIdsByStatus(statuses);
	}

	public void processOrdersStatuses(Sp sp){
		System.out.println("inside processOrdersStatuses()");

		Set<Order> orders = sp.getOrders();
        if (orders != null && !orders.isEmpty()) {

            Set<OrderStatus> ordersStatuses = new HashSet<>();
            orders.forEach(order -> ordersStatuses.add(order.getStatus()));

            switch (sp.getStatus()) {

                case PACKING: //Комплектуется
                    System.out.println("case: PACKING");
                    if (ordersStatuses.size() == 1 && ordersStatuses.contains(OrderStatus.PAID)) {
                        orderService.updateStatuses(sp, OrderStatus.PACKING);
                    }
                    break;

                case SENT: //Отправлен
                    System.out.println("case: SENT");
                    if (ordersStatuses.size() == 1 && ordersStatuses.contains(OrderStatus.PACKING)) {
                        orderService.updateStatuses(sp, OrderStatus.SENT);
                    }
                    break;

                case ARRIVED: //Получен
                    System.out.println("case: ARRIVED");
                    if (ordersStatuses.size() == 1 && ordersStatuses.contains(OrderStatus.SENT)) {
                        orderService.updateStatuses(sp, OrderStatus.ARRIVED);
                    }
                    break;
            }
        }

	}


}
