package sp.data.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.SpStatusDao;
import sp.data.entities.SpStatus;

@Repository
@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
public class SpStatusDaoHibernateImpl extends GenericDaoHibernateImpl<SpStatus, Integer> implements SpStatusDao{

	//TODO: Add specific methods for SpStatus


}
