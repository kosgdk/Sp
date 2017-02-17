package sp.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.expression.spel.ast.LongLiteral;
import org.springframework.stereotype.Component;
import sp.data.entities.Sp;
import sp.data.services.interfaces.SpService;

@Component
public class IdToSpConverter implements Converter<String, Sp> {

	@Autowired
	SpService spService;

	@Override
	public Sp convert(String id) {
		return spService.getById(Long.parseLong(id));
	}
}
