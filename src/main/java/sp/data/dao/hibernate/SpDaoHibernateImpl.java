package sp.data.dao.hibernate;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.SpDao;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.SpStatus;

import javax.persistence.NoResultException;
import java.util.Arrays;
import java.util.List;

@Repository("SpDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class SpDaoHibernateImpl extends GenericDaoHibernateImpl<Sp,Long> implements SpDao{

	@Override
	public Long getLastNumber() {
		String sql = "SELECT max(id) FROM Sp";

		Query query = currentSession().createNativeQuery(sql)
						.setCacheable(true);
		return new Long((Integer)query.getSingleResult());
	}

	@Override
	public Sp getLastSp() {
		String hql = "from Sp s order by id desc";

		Query<Sp> query = currentSession().createQuery(hql, Sp.class)
							.setMaxResults(1)
							.setCacheable(true);
		return query.getSingleResult();
	}

	@Override
	public Sp getByIdWithAllChildren(Long id) throws NoResultException{
		String hql = "from Sp s " +
						"left join fetch s.orders o " +
						"left join fetch o.client " +
						"left join fetch o.orderPositions op " +
						"left join fetch op.product where s.id = :id";

		Query<Sp> query = currentSession().createQuery(hql, Sp.class)
							.setParameter("id", id)
							.setCacheable(true);
		return query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getIdsByStatus(SpStatus... statuses) {
		String hql = "select id from Sp where status in (:statuses) order by id asc";

		Query query = currentSession().createQuery(hql)
						.setParameter("statuses", Arrays.asList(statuses))
						.setCacheable(true);
		return query.getResultList();
	}

}
