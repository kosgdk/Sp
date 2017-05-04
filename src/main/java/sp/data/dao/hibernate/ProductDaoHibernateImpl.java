package sp.data.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sp.data.dao.generic.GenericDaoForNamedEntitiesHibernateImpl;
import sp.data.dao.interfaces.ProductDao;
import sp.data.entities.Product;

@Repository("ProductDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class ProductDaoHibernateImpl extends GenericDaoForNamedEntitiesHibernateImpl<Product,Long> implements ProductDao{

}