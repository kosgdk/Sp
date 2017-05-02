package sp.data.dao.interfaces;

import sp.data.dao.generic.GenericDaoForNamedEntities;
import sp.data.entities.Product;

import java.util.List;

public interface ProductDao extends GenericDaoForNamedEntities<Product, Long> {

    List<Product> getZeroWeightProducts(Long spId);

}
