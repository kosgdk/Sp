package sp.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import sp.data.entities.OrderStatus;
import sp.data.entities.Referer;
import sp.data.services.RefererServiceImpl;
import sp.data.services.interfaces.OrderStatusService;


public class IdToOrderStatusConverter implements Converter<String, OrderStatus> {

	@Autowired
	OrderStatusService orderStatusService;

	@Override
	public OrderStatus convert(String id) {
		System.out.println("Inside IdToOrderStatusConverter");
		return orderStatusService.getById(Integer.parseInt(id));
	}
}
