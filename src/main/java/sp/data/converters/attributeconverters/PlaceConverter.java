package sp.data.converters.attributeconverters;

import sp.data.entities.enumerators.Place;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PlaceConverter implements AttributeConverter<Place, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Place place) {
		return place == null ? null : place.getId();
	}

	@Override
	public Place convertToEntityAttribute(Integer id) {
		return id == null ? null : Place.getById(id);
	}
}
