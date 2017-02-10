package sp.data.services.interfaces;

import sp.data.entities.Client;
import sp.data.services.generic.GenericService;
import sp.data.services.generic.GenericServiceForNamedEntities;

public interface ClientService extends GenericServiceForNamedEntities<Client, Long> {

    Client getByIdWithAllChildren(Long id);
	
}
