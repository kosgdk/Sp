package sp.data.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sp.data.dao.generic.GenericDaoHibernateImpl;
import sp.data.dao.interfaces.PlaceDao;
import sp.data.entities.Place;


@Repository("PlaceDaoHibernateImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class PlaceDaoHibernateImpl extends GenericDaoHibernateImpl<Place, Integer> implements PlaceDao{

	//TODO: Add specific methods for Place


}
