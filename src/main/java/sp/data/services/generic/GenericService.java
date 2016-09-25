package sp.data.services.generic;

import java.io.Serializable;
import java.util.List;

public interface GenericService <E, I extends Serializable>{

	E getById(I id);

	List<E> searchByName(String name);

	E getByName(String name);

	List<E> getAll();

	void save(E entity);

	void update(E entity);

	void delete(E entity);

	void deleteById(I id);

}
