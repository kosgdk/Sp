package sp.data.dao.hibernate;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.OrderPositionDao;
import sp.data.entities.OrderPosition;

import java.util.ArrayList;
import java.util.List;

@Repository("OrderPositionDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class OrderPositionDaoHibernateImpl extends GenericDaoHibernateImpl<OrderPosition,Long> implements OrderPositionDao{

	@Override
	public List<OrderPosition> getZeroWeightOrderPositions(Long spId) {
		if (spId == null) return new ArrayList<>();

		String hql = "select op from OrderPosition op " +
				"join fetch op.order o " +
				"join fetch op.product p " +
				"join fetch o.client " +
				"where op.order.sp.id=:spId and op.productWeight=0";


		Query<OrderPosition> query = currentSession().createQuery(hql, OrderPosition.class)
				.setParameter("spId", spId)
				.setCacheable(true);
		return query.getResultList();
	}

}