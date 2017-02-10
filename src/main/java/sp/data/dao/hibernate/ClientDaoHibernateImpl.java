package sp.data.dao.hibernate;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoForNamedEntitiesHibernateImpl;
import sp.data.dao.interfaces.ClientDao;
import sp.data.entities.Client;
import sp.data.entities.Order;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

@Repository("ClientDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class ClientDaoHibernateImpl extends GenericDaoForNamedEntitiesHibernateImpl<Client,Long> implements ClientDao{

    @Override
    public Client getByIdWithAllChildren(Long id) {

        String hql = "from Client c " +
                        "left join fetch c.orders o " +
                        "left join fetch o.sp " +
                        "left join fetch o.orderPositions op " +
                        "left join fetch op.product where c.id = :id";

        TypedQuery<Client> query = currentSession().createQuery(hql, Client.class);
        query.setParameter("id", id);

        try {
            return  query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public void delete(Client client){
//        if (client.getId() != null) {
//            String hql = "delete from Order where client=:client";
//            Query query = currentSession().createQuery(hql);
//            query.setParameter("client", client);
//            query.executeUpdate();
//            client.setOrders(null);
//        }
        super.delete(client);
    }

}
