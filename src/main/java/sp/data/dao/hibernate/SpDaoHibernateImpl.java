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
public class SpDaoHibernateImpl extends GenericDaoHibernateImpl<Sp, Integer> implements SpDao{

	@SuppressWarnings("unchecked")
	@Override
	public int getLastNumber() {
		Query query = currentSession().createNativeQuery("SELECT max(number) FROM Sp");
		List<Integer> numbers = query.getResultList();
		return numbers.get(numbers.size()-1);
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
	public Sp getByIdLazy(int id) {
		
		CriteriaBuilder cb = currentSession().getCriteriaBuilder();
		CriteriaQuery<Sp> q = cb.createQuery(Sp.class);
		Root<Sp> root = q.from(Sp.class);

		q.select(root);	
		q.where(cb.equal(root.<String>get("id"), id));
		
		TypedQuery<Sp> tq = currentSession().createQuery(q);
		
		return tq.getResultList().get(0);
	}

	@Override
	public Sp getByIdWithAllChildren(int id) {

		String hql = "from Sp s " +
						"left join fetch s.orders o " +
						"left join fetch o.client " +
						"left join fetch o.orderPositions op " +
						"left join fetch op.product where s.id = :id";

		TypedQuery<Sp> query = currentSession().createQuery(hql, Sp.class);
		query.setParameter("id", id);

		try {
			return  query.getSingleResult();
		} catch (NoResultException e){
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public SortedSet<Integer> getIdsByStatus(SpStatus... statuses) {
		String hql = "select id from Sp where status in (:statuses) order by id desc";
		Query query = currentSession().createQuery(hql);
		query.setParameter("statuses", Arrays.asList(statuses));
		SortedSet<Integer> result = new TreeSet<>(Collections.reverseOrder());
		result.addAll(query.getResultList());

		return result;
	}

}
