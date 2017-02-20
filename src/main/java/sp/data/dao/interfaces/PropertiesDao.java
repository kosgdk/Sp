package sp.data.dao.interfaces;

import sp.data.dao.generic.GenericDao;
import sp.data.entities.Properties;

public interface PropertiesDao{

	void save(Properties properties);

	Properties getProperties();

	void update(Properties properties);
}
