package sp.data.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sp.controllers.OrderPositionController;

@Component
public class StringToDateConverter implements Converter<String, Date> {

	Logger logger = LoggerFactory.getLogger(OrderPositionController.class);

	@Override
	public Date convert(String source) {

		Date date = null;
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));

	    try {
			date = sdf.parse(source);
		} catch (ParseException e) {
			logger.debug(e.getMessage());
		}

		return date;
	}

}
