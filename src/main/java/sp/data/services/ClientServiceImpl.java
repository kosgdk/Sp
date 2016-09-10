package sp.data.services;

import org.springframework.stereotype.Service;
import sp.data.entities.Client;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.ClientService;

@Service("ClientService")
public class ClientServiceImpl extends GenericServiceImpl<Client, Integer> implements ClientService {
	

}
