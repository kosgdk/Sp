package sp.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import sp.data.entities.Client;
import sp.data.services.interfaces.ClientService;


public class NameToClientConverter implements Converter<String, Client> {

	@Autowired
	ClientService clientService;

	@Override
	public Client convert(String name) {
		return clientService.getByName(name);
	}
}
