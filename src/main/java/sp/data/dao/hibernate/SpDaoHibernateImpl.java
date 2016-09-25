package sp.data.dao.hibernate;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.SpDao;
import sp.data.entities.Order;
import sp.data.entities.Sp;

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
		root.fetch("status");

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
		CriteriaBuilder cb = currentSession().getCriteriaBuilder();
		CriteriaQuery<Sp> q = cb.createQuery(Sp.class);
		Root<Sp> root = q.from(Sp.class);

		Fetch<Sp, Order> spOrdedFetch = root.fetch("orders");
		spOrdedFetch.fetch("orderPositions", JoinType.LEFT).fetch("product", JoinType.LEFT);
		spOrdedFetch.fetch("client");
		root.fetch("status");

		q.select(root);
		q.where(cb.equal(root.<String>get("id"), id));

		TypedQuery<Sp> tq = currentSession().createQuery(q);

		return tq.getResultList().get(0);
	}
}
