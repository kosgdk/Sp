package sp.data.services;

import org.springframework.stereotype.Service;
import sp.data.entities.Order;
import sp.data.entities.OrderPosition;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.OrderService;

import java.math.BigDecimal;
import java.util.List;

@Service("OrderService")
public class OrderServiceImpl extends GenericServiceImpl<Order, Integer> implements OrderService {


}
