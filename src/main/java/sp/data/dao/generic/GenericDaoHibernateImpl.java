package sp.data.dao.generic;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

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
	public E getById(I id){
		if(id == null) return null;
		return currentSession().get(daoType, id);
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
	public void deleteById(I id){
		delete(getById(id));
	}

}