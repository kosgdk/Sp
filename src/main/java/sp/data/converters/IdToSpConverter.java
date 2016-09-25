package sp.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import sp.data.entities.Sp;
import sp.data.services.interfaces.SpService;


public class IdToSpConverter implements Converter<String, Sp> {

	@Autowired
	SpService spService;

	@Override
	public Sp convert(String id) {
		System.out.println("Inside IdToSpConverter");
		return spService.getById(Integer.parseInt(id));
	}
}
