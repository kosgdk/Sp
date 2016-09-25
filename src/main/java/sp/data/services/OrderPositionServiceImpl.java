package sp.data.services;

import org.springframework.stereotype.Service;
import sp.data.entities.OrderPosition;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.OrderPositionService;

@Service("OrderPositionService")
public class OrderPositionServiceImpl extends GenericServiceImpl<OrderPosition, Integer> implements OrderPositionService {
	

}
