package sp.data.services.interfaces;

import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.services.generic.GenericService;

public interface OrderService extends GenericService<Order, Long> {

    Order getByIdWithAllChildren(Long id);

    void updateStatuses(Sp sp, OrderStatus orderStatus);

    void processSpStatus(Order order);

	void processStatus(Order order);

	// TODO: add deleteById(List<Integer> ids)

}
