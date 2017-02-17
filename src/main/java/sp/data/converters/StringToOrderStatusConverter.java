package sp.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sp.data.entities.enumerators.OrderStatus;

@Component
public class StringToOrderStatusConverter implements Converter<String, OrderStatus> {

	@Override
	public OrderStatus convert(String string) {
		if(string.matches("\\d*")){
			return OrderStatus.getById(Integer.parseInt(string));
		}else {
			return OrderStatus.getByName(string);
		}
	}
}
