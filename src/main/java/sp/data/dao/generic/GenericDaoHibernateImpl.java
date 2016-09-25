package sp.data.dao.generic;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("GenericDao")
@Transactional(propagation=Propagation.REQUIRED)
public abstract class GenericDaoHibernateImpl <E, I extends Serializable> implements GenericDao<E, I> {
	
	@Autowired
	private SessionFactory sessionFactory;

	private Class<E> daoType;

    @SuppressWarnings("unchecked")
	public GenericDaoHibernateImpl() {
    	daoType = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    protected Session currentSession(){
    	return sessionFactory.getCurrentSession();
    }


	@Override
	public E getById(I id) {
		return currentSession().get(daoType, id);
	}

	@Override
	public List<E> searchByName(String name) {
		String[] words = name.split(" ");

		CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
		CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(daoType);
		Root<E> root = criteriaQuery.from(daoType);
		criteriaQuery.select(root);

		List<Predicate> predicates = new ArrayList<>();

		for (String word : words) {
			System.out.println(word);
			predicates.add(criteriaBuilder.like(root.get("name"), "%"+word+"%"));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[]{}));

		TypedQuery<E> typedQuery = currentSession().createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	@Override
	public E getByName(String name) {
		CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
		CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(daoType);
		Root<E> root = criteriaQuery.from(daoType);
		criteriaQuery.select(root);

		criteriaQuery.where(criteriaBuilder.equal(root.get("name"), name));

		TypedQuery<E> typedQuery = currentSession().createQuery(criteriaQuery);
		List<E> entities = typedQuery.getResultList();

		return entities.isEmpty() ? null : entities.get(0);
	}

	@Override
	public List<E> getAll() {
		CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
		CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(daoType);
		Root<E> root = criteriaQuery.from(daoType);
		criteriaQuery.select(root);
		TypedQuery<E> typedQuery = currentSession().createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	@Override
	public void save(E entity) {
		currentSession().save(entity);
	}

	@Override
	public void update(E entity) {
		currentSession().saveOrUpdate(entity);
	}

	@Override
	public void delete(E entity) {
		currentSession().delete(entity);
	}

	@Override
	public void deleteById(I id) {
		currentSession().delete(getById(id));
	}

}
