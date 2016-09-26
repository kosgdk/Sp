package sp.data.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.PropertiesDao;
import sp.data.entities.Properties;

@Repository("PropertiesDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class PropertiesDaoHibernateImpl extends GenericDaoHibernateImpl<Properties, Integer> implements PropertiesDao {

}
