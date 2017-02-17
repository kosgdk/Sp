package sp.data.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sp.data.entities.enumerators.ClientReferrer;

@Component
public class StringToClientReferrerConverter implements Converter<String, ClientReferrer> {

	@Override
	public ClientReferrer convert(String string) {
		if(string.matches("\\d*")){
			return ClientReferrer.getById(Integer.parseInt(string));
		}else {
			return ClientReferrer.getByName(string);
		}
	}
}
