package sp.data.dao.hibernate;

import java.util.*;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.SpDao;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.SpStatus;

@Repository("SpDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class SpDaoHibernateImpl extends GenericDaoHibernateImpl<Sp,Long> implements SpDao{

	@SuppressWarnings("unchecked")
	@Override
	public Long getLastNumber() {
		Query query = currentSession().createNativeQuery("SELECT max(number) FROM Sp");
		List<Integer> numbers = query.getResultList();
		return new Long(numbers.get(numbers.size()-1));
	}

	@Override
	public Sp getLastSp() {
		CriteriaBuilder cb = currentSession().getCriteriaBuilder();
		CriteriaQuery<Sp> q = cb.createQuery(Sp.class);
		Root<Sp> root = q.from(Sp.class);

		q.select(root);	
		q.orderBy(cb.desc(root.get("id")));	
		
		TypedQuery<Sp> tq = currentSession().createQuery(q).setMaxResults(1);
		
		return tq.getResultList().get(0);
	}

	@Override
	public Sp getByIdWithAllChildren(Long id) throws NoResultException{

		String hql = "from Sp s " +
						"left join fetch s.orders o " +
						"left join fetch o.client " +
						"left join fetch o.orderPositions op " +
						"left join fetch op.product where s.id = :id";

		TypedQuery<Sp> query = currentSession().createQuery(hql, Sp.class);
		query.setParameter("id", id);

		return  query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public SortedSet<Long> getIdsByStatus(SpStatus... statuses) {
		String hql = "select id from Sp where status in (:statuses) order by id desc";
		Query query = currentSession().createQuery(hql);
		query.setParameter("statuses", Arrays.asList(statuses));
		SortedSet<Long> result = new TreeSet<>(Collections.reverseOrder());
		result.addAll(query.getResultList());

		return result;
	}

	@Override
	public void delete(Sp sp) {
//		String hql = "delete from Order where sp=:sp";
//		Query query = currentSession().createQuery(hql);
//		query.setParameter("sp", sp);
//		query.executeUpdate();
//		sp.getOrders().clear();

		super.delete(sp);
	}

	@Override
	public void deleteById(Long id) throws NoResultException {
		if (id == null) throw new NoResultException();
		delete(getById(id));
	}
}
