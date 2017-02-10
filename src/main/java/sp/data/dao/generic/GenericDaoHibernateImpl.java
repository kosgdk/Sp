package sp.data.dao.generic;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("GenericDao")
@Transactional(propagation=Propagation.REQUIRED)
public abstract class GenericDaoHibernateImpl <E, I extends Serializable> implements GenericDao<E, I> {
	
	@Autowired
	protected SessionFactory sessionFactory;

	protected Class<E> daoType;

    @SuppressWarnings("unchecked")
	public GenericDaoHibernateImpl() {
		daoType = (Class<E>) GenericTypeResolver.resolveTypeArguments(getClass(), GenericDaoHibernateImpl.class)[0];
    }
    
    public Session currentSession(){
    	return sessionFactory.getCurrentSession();
    }


	@Override
	public E getById(I id) throws NoResultException{
		if(id == null) throw new NoResultException();
		E entity = currentSession().get(daoType, id);
		if (entity == null) throw new NoResultException();
		return entity;
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
		if(entity != null) currentSession().save(entity);
	}

	@Override
	public void update(E entity) {
		currentSession().merge(entity);
	}

	@Override
	public void delete(E entity) {
		if(entity != null) currentSession().delete(entity);
	}

	@Override
	public void deleteById(I id) throws NoResultException{
		if (id == null) throw new NoResultException();
		delete(getById(id));
	}

}
