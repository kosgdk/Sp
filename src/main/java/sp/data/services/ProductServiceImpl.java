package sp.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sp.data.dao.interfaces.ProductDao;
import sp.data.entities.Product;
import sp.data.services.generic.GenericServiceForNamedEntitiesImpl;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.ProductService;

import java.util.List;

@Service("ProductService")
public class ProductServiceImpl extends GenericServiceForNamedEntitiesImpl<Product, Long> implements ProductService  {

    @Autowired
    ProductDao productDao;

    @Override
    public List<Product> getZeroWeightProducts(Long spId) {
        return productDao.getZeroWeightProducts(spId);
    }
}
