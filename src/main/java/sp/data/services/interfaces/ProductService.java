package sp.data.services.interfaces;

import sp.data.entities.Product;
import sp.data.services.generic.GenericService;
import sp.data.services.generic.GenericServiceForNamedEntities;

import java.util.List;

public interface ProductService extends GenericServiceForNamedEntities<Product, Long> {

    List<Product> getZeroWeightProducts(Long spId);

}
