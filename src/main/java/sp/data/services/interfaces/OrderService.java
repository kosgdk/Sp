package sp.data.services.interfaces;

import sp.data.entities.Order;
import sp.data.entities.OrderPosition;
import sp.data.services.generic.GenericService;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService extends GenericService<Order, Long> {

    Order getByIdWithAllChildren(Long id);
	
}
