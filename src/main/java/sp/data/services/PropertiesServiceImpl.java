package sp.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sp.data.dao.interfaces.PropertiesDao;
import sp.data.entities.Properties;
import sp.data.services.interfaces.PropertiesService;

@Service("PropertiesService")
public class PropertiesServiceImpl implements PropertiesService {

	@Autowired
	PropertiesDao propertiesDao;

	@Override
	public Properties getProperties() {
		return propertiesDao.getProperties();
	}

	@Override
	public void save(Properties properties) {
		propertiesDao.save(properties);
	}

	@Override
	public void update(Properties properties) {
		propertiesDao.update(properties);
	}
}
