package sp.data.services.generic;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sp.data.dao.generic.GenericDao;

@Service
public abstract class GenericServiceImpl <E, I extends Serializable> implements GenericService<E, I> {
	
	@Autowired
	private GenericDao<E, I> genericDao;
 
    public GenericServiceImpl() {
    }
	
	public E getById(I id){
		return genericDao.getById(id);
	}

	public List<E> getAll(){
		return genericDao.getAll();
	}

	public void save(E entity){
		genericDao.save(entity);
	}

	public void update(E entity){
		genericDao.update(entity);
	}

	public void delete(E entity){
		genericDao.delete(entity);
	}

	public void deleteById(I id){
		genericDao.deleteById(id);
	}

}
