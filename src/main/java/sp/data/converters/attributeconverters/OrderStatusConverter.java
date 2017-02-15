package sp.data.converters.attributeconverters;

import sp.data.entities.enumerators.OrderStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {

	@Override
	public Integer convertToDatabaseColumn(OrderStatus status) {
		return status == null ? null : status.getId();
	}

	@Override
	public OrderStatus convertToEntityAttribute(Integer id) {
		return id == null ? null : OrderStatus.getById(id);
	}
}
