package sp.data.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.OrderPositionDao;
import sp.data.entities.OrderPosition;

@Repository("OrderPositionDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
public class OrderPositionDaoHibernateImpl extends GenericDaoHibernateImpl<OrderPosition, Integer> implements OrderPositionDao{

	//TODO: Add specific methods


}
