package sp.data.services;

import org.springframework.stereotype.Service;
import sp.data.entities.Client;
import sp.data.entities.SpStatus;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.ClientService;
import sp.data.services.interfaces.SpStatusService;

@Service("SpStatusService")
public class SpStatusServiceImpl extends GenericServiceImpl<SpStatus, Integer> implements SpStatusService {
	

}
