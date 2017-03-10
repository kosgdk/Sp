package sp.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sp.data.dao.interfaces.OrderDao;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.OrderService;

@Service("OrderService")
public class OrderServiceImpl extends GenericServiceImpl<Order, Long> implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Override
    public Order getByIdWithAllChildren(Long id) {
        return orderDao.getByIdWithAllChildren(id);
    }

    @Override
    public void updateStatuses(Sp sp, OrderStatus orderStatus) {
        orderDao.updateStatuses(sp, orderStatus);
    }

}
