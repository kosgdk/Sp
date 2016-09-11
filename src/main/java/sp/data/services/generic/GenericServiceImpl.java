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


	@Override
	public E getById(I id){
		return genericDao.getById(id);
	}

	@Override
	public List<E> searchByName(String name) {
		return genericDao.searchByName(name);
	}

	@Override
	public List<E> getAll(){
		return genericDao.getAll();
	}

	@Override
	public void save(E entity){
		genericDao.save(entity);
	}

	@Override
	public void update(E entity){
		genericDao.update(entity);
	}

	@Override
	public void delete(E entity){
		genericDao.delete(entity);
	}

	@Override
	public void deleteById(I id){
		genericDao.deleteById(id);
	}

}
