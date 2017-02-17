package sp.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sp.data.entities.Client;
import sp.data.services.interfaces.ClientService;

@Component
public class NameToClientConverter implements Converter<String, Client> {

	@Autowired
	ClientService clientService;

	@Override
	public Client convert(String name) {
		return clientService.getByName(name);
	}
}
