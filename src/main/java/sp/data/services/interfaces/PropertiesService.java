package sp.data.services.interfaces;

import sp.data.entities.Properties;
import sp.data.services.generic.GenericService;

public interface PropertiesService {

	Properties getProperties();

	void save(Properties properties);

	void update(Properties properties);
	
}
