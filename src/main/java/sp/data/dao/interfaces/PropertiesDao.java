package sp.data.dao.interfaces;

import sp.data.dao.generic.GenericDao;
import sp.data.entities.Properties;

public interface PropertiesDao{

	void save(Properties properties);

	Properties get();

	void update(Properties properties);
}
