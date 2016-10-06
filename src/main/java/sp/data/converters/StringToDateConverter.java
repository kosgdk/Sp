package sp.data.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.core.convert.converter.Converter;

public class StringToDateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {

		Date date = null;
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));

	    try {
			date = sdf.parse(source);
		} catch (ParseException e) {
            e.printStackTrace();
		}

		return date;
	}

}
