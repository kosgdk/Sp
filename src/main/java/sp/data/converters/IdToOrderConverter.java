package sp.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sp.data.entities.Order;
import sp.data.services.interfaces.OrderService;

@Component
public class IdToOrderConverter implements Converter<String, Order> {

	@Autowired
	OrderService orderService;

	@Override
	public Order convert(String id) {
		//TODO: catch NumberFormatException
		return orderService.getById(Long.parseLong(id));
	}
}
