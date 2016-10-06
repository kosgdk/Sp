package sp.data.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.OrderDao;
import sp.data.entities.Order;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Repository("OrderDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class OrderDaoHibernateImpl extends GenericDaoHibernateImpl<Order, Integer> implements OrderDao{

    @Override
    public Order getByIdWithAllChildren(int id) {

        String hql = "from Order o " +
                "left join fetch o.client " +
                "left join fetch o.sp " +
                "left join fetch o.place " +
                "left join fetch o.orderPositions op " +
                "left join fetch op.product where o.id = :id";

        TypedQuery<Order> query = currentSession().createQuery(hql, Order.class);
        query.setParameter("id", id);

        try {
            return  query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }


}
