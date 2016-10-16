package sp.data.converters;

import org.springframework.core.convert.converter.Converter;
import sp.data.entities.enumerators.SpStatus;


public class StringToSpStatusConverter implements Converter<String, SpStatus> {

	@Override
	public SpStatus convert(String string) {
		if(string.matches("\\d*")){
			return SpStatus.getById(Integer.parseInt(string));
		}else {
			return SpStatus.getByName(string);
		}
	}
}
