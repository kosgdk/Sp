package data.entities.services;


import java.util.Calendar;
import java.util.Date;


public class DateService {

    public static Date getShiftedDate(int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }
}
