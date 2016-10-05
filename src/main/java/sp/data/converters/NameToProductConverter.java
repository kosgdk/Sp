package sp.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import sp.data.entities.Product;
import sp.data.services.interfaces.ProductService;


public class NameToProductConverter implements Converter<String, Product> {

	@Autowired
	ProductService productService;

	@Override
	public Product convert(String name) {
		return productService.getByName(name);
	}
}
