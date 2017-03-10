package sp.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sp.data.dao.interfaces.ClientDao;
import sp.data.entities.Client;
import sp.data.services.generic.GenericServiceForNamedEntitiesImpl;
import sp.data.services.interfaces.ClientService;

@Service("ClientService")
public class ClientServiceImpl extends GenericServiceForNamedEntitiesImpl<Client, Long> implements ClientService {

    @Autowired
    ClientDao clientDao;

    @Override
    public Client getByIdWithAllChildren(Long id) {
        return clientDao.getByIdWithAllChildren(id);
    }

}