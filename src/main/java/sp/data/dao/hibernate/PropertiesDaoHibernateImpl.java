package sp.data.dao.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sp.data.dao.interfaces.PropertiesDao;
import sp.data.entities.Properties;

import java.math.BigDecimal;

@Repository("PropertiesDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class PropertiesDaoHibernateImpl implements PropertiesDao {

	@Autowired
	protected SessionFactory sessionFactory;

	@Override
	public void save(Properties properties) {
		sessionFactory.getCurrentSession().saveOrUpdate(properties);
	}

	@Override
	public Properties getProperties() {
		String hql = "from Properties order by id DESC";
		Query<Properties> query = sessionFactory.getCurrentSession().createQuery(hql, Properties.class)
									.setMaxResults(1)
									.setCacheable(true);
		Properties properties = query.getSingleResult();

		if (properties == null) {
			properties = new Properties();
			properties.setPercentSp(new BigDecimal(0.15));
			properties.setPercentDiscount(new BigDecimal(0.03));
			properties.setPercentBankCommission(new BigDecimal(0.01));
			save(properties);
		}

		return properties;
	}

	@Override
	public void update(Properties properties) {
		sessionFactory.getCurrentSession().merge(properties);
	}

}