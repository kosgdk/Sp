package sp.data.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sp.data.entities.enumerators.Place;

@Component
public class StringToPlaceConverter implements Converter<String, Place> {

	@Override
	public Place convert(String string) {
		if(string.matches("\\d*")){
			return Place.getById(Integer.parseInt(string));
		}else {
			return Place.getByName(string);
		}
	}
}
