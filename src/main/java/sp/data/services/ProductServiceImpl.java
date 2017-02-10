package sp.data.services;

import org.springframework.stereotype.Service;

import sp.data.entities.Product;
import sp.data.services.generic.GenericServiceForNamedEntitiesImpl;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.ProductService;

@Service("ProductService")
public class ProductServiceImpl extends GenericServiceForNamedEntitiesImpl<Product, Long> implements ProductService  {

}
