package sp.data.services.generic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sp.data.dao.generic.GenericDao;
import sp.data.dao.generic.GenericDaoForNamedEntities;

import java.io.Serializable;
import java.util.List;

@Service("GenericServiceForNamedEntities")
public abstract class GenericServiceForNamedEntitiesImpl<E, I extends Serializable> extends GenericServiceImpl<E, I> implements GenericServiceForNamedEntities<E, I> {

	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	private GenericDaoForNamedEntities<E, I> genericDao;

    public GenericServiceForNamedEntitiesImpl() {
    }


	@Override
	public List<E> searchByName(String name) {
		return genericDao.searchByName(name);
	}

	@Override
	public E getByName(String name) {
		return genericDao.getByName(name);
	}

}
