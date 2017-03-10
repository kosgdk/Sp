package sp.data.dao.hibernate;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sp.data.dao.generic.GenericDaoForNamedEntitiesHibernateImpl;
import sp.data.dao.interfaces.ClientDao;
import sp.data.entities.Client;

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

        Query<Client> query = currentSession().createQuery(hql, Client.class)
                                .setParameter("id", id)
                                .setCacheable(true);

        return query.getSingleResult();
        // TODO: handle NoResultException
    }

}