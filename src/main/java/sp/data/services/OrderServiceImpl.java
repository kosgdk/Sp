package sp.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sp.data.dao.interfaces.OrderDao;
import sp.data.entities.Order;
import sp.data.entities.OrderPosition;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.OrderService;

import java.math.BigDecimal;
import java.util.List;

@Service("OrderService")
public class OrderServiceImpl extends GenericServiceImpl<Order, Long> implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Override
    public Order getByIdWithAllChildren(Long id) {
        return orderDao.getByIdWithAllChildren(id);
    }
}
