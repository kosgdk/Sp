package sp.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sp.data.entities.Product;
import sp.data.services.interfaces.ProductService;

@Component
public class NameToProductConverter implements Converter<String, Product> {

	@Autowired
	ProductService productService;

	@Override
	public Product convert(String name) {
		return productService.getByName(name);
	}
}
