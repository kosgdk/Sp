package sp.data.services.interfaces;

import java.util.List;

import sp.data.entities.Product;

public interface ProductService {

	public List<Product> searchByName (String name);
	
}
