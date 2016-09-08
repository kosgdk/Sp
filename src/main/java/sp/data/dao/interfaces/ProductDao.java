package sp.data.dao.interfaces;

import java.util.List;

import sp.data.dao.generic.GenericDao;
import sp.data.entities.Product;

public interface ProductDao extends GenericDao<Product, Integer>{
	
	public List<Product> searchByName (String name);

}
