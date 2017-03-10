package sp.data.dao.interfaces;

import sp.data.dao.generic.GenericDao;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;

public interface OrderDao extends GenericDao<Order,Long> {

    Order getByIdWithAllChildren(Long id);

    void updateStatuses(Sp sp, OrderStatus orderStatus);

}
