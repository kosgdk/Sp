package sp.data.services;

import org.springframework.stereotype.Service;
import sp.data.entities.OrderStatus;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.OrderStatusService;

@Service("OrderStatusService")
public class OrderStatusServiceImpl extends GenericServiceImpl<OrderStatus, Integer> implements OrderStatusService {



}
