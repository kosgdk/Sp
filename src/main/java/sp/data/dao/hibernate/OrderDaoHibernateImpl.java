package sp.data.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.OrderDao;
import sp.data.entities.Order;

@Repository("OrderDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class OrderDaoHibernateImpl extends GenericDaoHibernateImpl<Order, Integer> implements OrderDao{

	//TODO: Add specific methods for Referer


}
