package sp.data.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.ClientDao;
import sp.data.entities.Client;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;


@Repository("ClientDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class ClientDaoHibernateImpl extends GenericDaoHibernateImpl<Client, Integer> implements ClientDao{

    @Override
    public Client getByIdWithAllChildren(int id) {

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

}
