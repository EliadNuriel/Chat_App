package com.example.chatapplication;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {



    public static String getTimeFromMillis(long millis) {
        Date d = new Date(millis);

        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.setTime(d);

        int hours = c.get(Calendar.HOUR);
        int minutes = c.get(Calendar.MINUTE);
        return hours + ":" + minutes;
    }
}
