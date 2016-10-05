package sp.data.services.interfaces;

import sp.data.entities.OrderStatus;
import sp.data.services.generic.GenericService;

import java.util.Map;

public interface OrderStatusService extends GenericService<OrderStatus, Integer> {

	Map<String, String> getAllForSelect();
	
}
