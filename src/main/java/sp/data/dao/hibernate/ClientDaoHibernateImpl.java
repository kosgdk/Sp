package sp.data.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.ClientDao;
import sp.data.entities.Client;
import sp.data.entities.Order;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;


@Repository("ClientDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class ClientDaoHibernateImpl extends GenericDaoHibernateImpl<Client, Integer> implements ClientDao{

    @Override
    public Client getByIdWithAllChildren(int id) {

        CriteriaBuilder cb = currentSession().getCriteriaBuilder();
        CriteriaQuery<Client> q = cb.createQuery(Client.class);
        Root<Client> root = q.from(Client.class);

        Fetch<Client, Order> ClientOrderFetch = root.fetch("orders");
        ClientOrderFetch.fetch("orderPositions", JoinType.LEFT).fetch("product", JoinType.LEFT);
        ClientOrderFetch.fetch("sp", JoinType.LEFT);

        q.select(root);
        q.where(cb.equal(root.<String>get("id"), id));

        TypedQuery<Client> tq = currentSession().createQuery(q);

        return tq.getResultList().get(0);
    }
}
