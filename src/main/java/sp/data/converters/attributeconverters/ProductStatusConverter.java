package sp.data.converters.attributeconverters;

import sp.data.entities.enumerators.ProductStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ProductStatusConverter implements AttributeConverter<ProductStatus, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ProductStatus status) {
		return status == null ? null : status.getId();
	}

	@Override
	public ProductStatus convertToEntityAttribute(Integer id) {
		return id == null ? null : ProductStatus.getById(id);
	}
}
