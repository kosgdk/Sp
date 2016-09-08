package sp.data.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.RefererDao;
import sp.data.entities.Referer;

@Repository("RefererDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
public class RefererDaoHibernateImpl extends GenericDaoHibernateImpl<Referer, Integer> implements RefererDao{

	//TODO: Add specific methods for


}
