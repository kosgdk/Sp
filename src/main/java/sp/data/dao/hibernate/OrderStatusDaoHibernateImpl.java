package sp.data.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.OrderStatusDao;
import sp.data.entities.OrderStatus;


@Repository("OrderStatusDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class OrderStatusDaoHibernateImpl extends GenericDaoHibernateImpl<OrderStatus, Integer> implements OrderStatusDao{

	//TODO: Add specific methods for OrderStatus


}
