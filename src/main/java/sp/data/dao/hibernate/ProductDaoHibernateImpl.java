package sp.data.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.ProductDao;
import sp.data.entities.Product;

@Repository("ProductDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
public class ProductDaoHibernateImpl extends GenericDaoHibernateImpl<Product, Integer> implements ProductDao{

	@Override
	public List<Product> searchByName(String name) {
		
		String[] words = name.split(" ");
		
		CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);

		criteriaQuery.select(root);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		for (String word : words) {
			System.out.println(word);
			predicates.add(criteriaBuilder.like(root.<String>get("name"), "%"+word+"%"));
		}
		
		
		criteriaQuery.where(predicates.toArray(new Predicate[]{}));			
		//criteriaQuery.where(criteriaBuilder.like(root.<String>get("name"), "%"+word+"%"));			
		
		TypedQuery<Product> typedQuery = currentSession().createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}


}
