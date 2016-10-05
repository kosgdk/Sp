package sp.data.converters;

import sp.data.entities.enumerators.SpStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class SpStatusEnumConverter implements AttributeConverter<SpStatus, Integer> {

	@Override
	public Integer convertToDatabaseColumn(SpStatus status) {
		return status == null ? null : status.getId();
	}

	@Override
	public SpStatus convertToEntityAttribute(Integer id) {
		return id == null ? null : SpStatus.getById(id);
	}
}
