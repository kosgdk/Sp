package sp.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import sp.data.entities.OrderStatus;
import sp.data.services.interfaces.OrderStatusService;


public class StringToOrderStatusConverter implements Converter<String, OrderStatus> {

	@Autowired
	OrderStatusService orderStatusService;

	@Override
	public OrderStatus convert(String string) {
		if(string.matches("\\d*")){
			return orderStatusService.getById(Integer.parseInt(string));
		}else {
			return orderStatusService.getByName(string);
		}
	}
}
