package sp.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sp.data.entities.OrderPosition;
import sp.data.services.interfaces.OrderPositionService;

@Component
public class IdToOrderPositionConverter implements Converter<String, OrderPosition> {

	@Autowired
	OrderPositionService orderPositionService;

	@Override
	public OrderPosition convert(String id) {
		return orderPositionService.getById(Long.parseLong(id));
	}
}
