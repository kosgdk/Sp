package sp.data.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class StringToDateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		
		System.out.println("String date = " + source);
		
		Date date = null;
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    try {
			date = sdf.parse(source);
			System.out.println("Date date = " + date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

}
