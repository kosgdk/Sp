package sp.data.services.interfaces;

import sp.data.entities.Client;
import sp.data.services.generic.GenericService;

public interface ClientService extends GenericService<Client, Integer> {

    Client getByIdWithAllChildren(int id);
	
}
