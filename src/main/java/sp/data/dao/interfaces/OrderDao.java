package sp.data.dao.interfaces;


import sp.data.dao.generic.GenericDao;
import sp.data.entities.Order;

public interface OrderDao extends GenericDao<Order,Long> {

    Order getByIdWithAllChildren(Long id);

}
