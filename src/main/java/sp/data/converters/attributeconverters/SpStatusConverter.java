package sp.data.converters.attributeconverters;

import sp.data.entities.enumerators.SpStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.Serializable;

@Converter
public class SpStatusConverter implements AttributeConverter<SpStatus, Integer>, Serializable {

	@Override
	public Integer convertToDatabaseColumn(SpStatus status) {
		return status == null ? null : status.getId();
	}

	@Override
	public SpStatus convertToEntityAttribute(Integer id) {
		return id == null ? null : SpStatus.getById(id);
	}
}
