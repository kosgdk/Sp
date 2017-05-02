package sp.data.dao.hibernate;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sp.data.dao.generic.GenericDaoForNamedEntitiesHibernateImpl;
import sp.data.dao.interfaces.ProductDao;
import sp.data.entities.Product;
import sp.data.entities.Sp;

import java.util.ArrayList;
import java.util.List;

@Repository("ProductDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class ProductDaoHibernateImpl extends GenericDaoForNamedEntitiesHibernateImpl<Product,Long> implements ProductDao{

    @Override
    public List<Product> getZeroWeightProducts(Long spId) {
        if (spId == null) return new ArrayList<>();

        String hql = "select distinct p from Order o " +
                        "right outer join o.orderPositions op " +
                        "right outer join op.product p " +
                        "where o.sp.id=:spId and p.weight=0 " +
                        "order by p.name";

        Query<Product> query = currentSession().createQuery(hql, Product.class)
                                .setParameter("spId", spId)
                                .setCacheable(true);
        return query.getResultList();
    }
}