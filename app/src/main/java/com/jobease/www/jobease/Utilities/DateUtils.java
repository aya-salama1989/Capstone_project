package com.jobease.www.jobease.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by Dell on 07/02/2017.
 */

public class DateUtils {

    public static String getCurrentDeviceDate(String format) {
//        "yyyy-MM-dd'T'HH:mm:ss'Z'"
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDateandTime = sdf.format(new Date(System.currentTimeMillis()));
        Logging.log(currentDateandTime);
        return currentDateandTime;
    }


    public static Date getCurrentDeviceDate() {
        return new Date(System.currentTimeMillis());
    }


    public static Date stringToDate(String dateString, String patternIn) {
//        String string = "2016-09-25T14:04:29.173";  "yyyy-MM-dd'T'mm:ss:SS"
        SimpleDateFormat format = new SimpleDateFormat(patternIn, Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String getDateFromString(String dateString, String patternIn, String patternOut) {
//        String string = "2016-09-25T14:04:29.173";  "yyyy-MM-dd'T'mm:ss:SS"
        SimpleDateFormat format = new SimpleDateFormat(patternIn, Locale.ENGLISH);

        String finalDate = "";
        try {
            Date date = format.parse(dateString);
            format.applyPattern(patternOut);
            finalDate = format.format(date);
            Logging.log(finalDate);
        } catch (ParseException e) {
            Logging.log("getDateFromString ParseException" + e.getMessage());
        }

        return finalDate;
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }


}
