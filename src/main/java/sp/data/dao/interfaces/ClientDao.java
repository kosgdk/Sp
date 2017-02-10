package sp.data.dao.interfaces;

import sp.data.dao.generic.GenericDao;
import sp.data.dao.generic.GenericDaoForNamedEntities;
import sp.data.entities.Client;


public interface ClientDao extends GenericDaoForNamedEntities<Client, Long> {

    Client getByIdWithAllChildren(Long id);

}
