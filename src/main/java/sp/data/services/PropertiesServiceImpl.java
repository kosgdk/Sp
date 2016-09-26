package sp.data.services;

import org.springframework.stereotype.Service;
import sp.data.entities.Properties;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.PropertiesService;

@Service("PropertiesService")
public class PropertiesServiceImpl extends GenericServiceImpl<Properties, Integer> implements PropertiesService {


}
