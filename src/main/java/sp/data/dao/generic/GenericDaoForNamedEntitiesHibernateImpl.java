package sp.data.dao.generic;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Repository("GenericDaoForNamedEntities")
@Transactional(propagation=Propagation.REQUIRED)
public abstract class GenericDaoForNamedEntitiesHibernateImpl<E, I extends Serializable> extends GenericDaoHibernateImpl<E, I> implements GenericDaoForNamedEntities<E, I> {

	public GenericDaoForNamedEntitiesHibernateImpl() {
		super();
	}


	@Override
	public List<E> searchByName(String name) {
		if (name == null) return null;
		String[] words = name.split(" ");

		CriteriaBuilder cb = currentSession().getCriteriaBuilder();
		CriteriaQuery<E> criteriaQuery = cb.createQuery(daoType);
		Root<E> root = criteriaQuery.from(daoType);
		criteriaQuery.select(root);

		List<Predicate> predicates = new ArrayList<>();

		for (String word : words) {
			predicates.add(cb.like(cb.lower(root.get("name")), "%"+word.toLowerCase()+"%"));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[]{}));

		TypedQuery<E> typedQuery = currentSession().createQuery(criteriaQuery)
									.setMaxResults(30);
		return typedQuery.getResultList();
	}

	@Override
	public E getByName(String name) {
		if (name == null) return null;

		CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
		CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(daoType);
		Root<E> root = criteriaQuery.from(daoType);
		criteriaQuery.select(root);

		criteriaQuery.where(criteriaBuilder.equal(root.get("name"), name));

		Query<E> query = currentSession().createQuery(criteriaQuery)
						.setMaxResults(1)
						.setCacheable(true);
		return query.getSingleResult();
	}

}
