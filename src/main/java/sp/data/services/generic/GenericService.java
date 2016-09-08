package sp.data.services.generic;

import java.io.Serializable;
import java.util.List;


public interface GenericService <E, I extends Serializable> {

	public E getById(I id);

	public List<E> getAll();

	public void save(E entity);

	public void update(E entity);

	public void delete(E entity);

	public void deleteById(I id);
	
}
