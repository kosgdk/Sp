package sp.data.dao.generic;

import java.io.Serializable;
import java.util.List;

public interface GenericDaoForNamedEntities<E, I extends Serializable> extends GenericDao<E, I>{

	E getByName(String name);

	List<E> searchByName(String name);
	
}
