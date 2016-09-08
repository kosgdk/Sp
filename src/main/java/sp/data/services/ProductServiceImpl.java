package sp.data.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sp.data.dao.interfaces.ProductDao;
import sp.data.entities.Product;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.ProductService;

@Service("ProductService")
public class ProductServiceImpl extends GenericServiceImpl<Product, Integer> implements ProductService  {
	
	@Autowired
	ProductDao dao;

	@Override
	public List<Product> searchByName(String name) {
		return dao.searchByName(name);
	}

}
