package sp.data.dao.hibernate;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.OrderDao;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Repository("OrderDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class OrderDaoHibernateImpl extends GenericDaoHibernateImpl<Order,Long> implements OrderDao{

    @Override
    public Order getByIdWithAllChildren(Long id) {
        if (id == null) throw new NoResultException();

        String hql = "from Order o " +
                "left join fetch o.client " +
                "left join fetch o.sp " +
                "left join fetch o.orderPositions op " +
                "left join fetch op.product where o.id = :id";

        TypedQuery<Order> query = currentSession().createQuery(hql, Order.class);
        query.setParameter("id", id);

        Order order = query.getSingleResult();
        if (order == null) throw new NoResultException();
        return order;

    }

    @Override
    public void updateStatuses(Sp sp, OrderStatus orderStatus) {
        if (sp != null & orderStatus != null) {
            String hql = "update Order set status=:orderStatus where sp.id=:sp";
            Query query = currentSession().createQuery(hql)
                            .setParameter("orderStatus", orderStatus)
                            .setParameter("sp", sp.getId());
            query.executeUpdate();
        }
    }

}
