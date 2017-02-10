package sp.data.services.generic;

import java.io.Serializable;
import java.util.List;

public interface GenericServiceForNamedEntities<E, I extends Serializable> extends GenericService<E, I>{

	List<E> searchByName(String name);

	E getByName(String name);

}
