package sp.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import sp.data.entities.Referer;
import sp.data.services.RefererServiceImpl;


public class IdToRefererConverter implements Converter<String, Referer> {

	@Autowired
	RefererServiceImpl refererService;

	@Override
	public Referer convert(String id) {
		return refererService.getById(Integer.parseInt(id));
	}
}
