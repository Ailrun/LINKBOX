package org.sopt.linkbox.custom.helper;

import android.text.format.DateUtils;
import android.text.method.DateTimeKeyListener;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by MinGu on 2015-08-19.
 */
public class DateCalculator {

    public static Date calculateDate(String sDate){
        DateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date dConvertedDate = new Date();
        try {
            dConvertedDate = sdfFormat.parse(sDate);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        return dConvertedDate;
    }

    public static String compareDates(String sPrevious){
        SimpleDateFormat sdfCurrent = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");

        int year = Integer.parseInt(sPrevious.substring(0, 4));
        Log.e("Year", String.valueOf(year));
        int month = Integer.parseInt(sPrevious.substring(5, 7));
        Log.e("month", String.valueOf(month));
        int day = Integer.parseInt(sPrevious.substring(8, 10));
        Log.e("day", String.valueOf(day));
        int hour = Integer.parseInt(sPrevious.substring(11, 13));
        Log.e("hour", String.valueOf(hour));
        int minute = Integer.parseInt(sPrevious.substring(14, 16));
        Log.e("minute", String.valueOf(minute));
        int second = Integer.parseInt(sPrevious.substring(17, 19));
        Log.e("second", String.valueOf(second));

        Calendar currentTime = Calendar.getInstance();
        String sCurrent = currentTime.getTime().toString();

        Log.e("sPrevious Log", sPrevious);
        GregorianCalendar gcPrevious = new GregorianCalendar(year - 1900, month - 1, day, hour, minute, second);
        Date dPrevious = gcPrevious.getTime();
        Log.e("Date class previous", dPrevious.toString());
        Date dCurrent = new Date();

        try {
            // dPrevious = sdfPrevious.parse(sPrevious);
            dCurrent = sdfCurrent.parse(sCurrent);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long timeDifference = dCurrent.getTime() - dPrevious.getTime();
        Log.e("TimeDifference", String.valueOf(timeDifference));
        Log.e("Previous backtime", String.valueOf(dPrevious));

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        int elapsedDays = (int)(timeDifference / daysInMilli);
        timeDifference = timeDifference % daysInMilli;

        int elapsedHours = (int)(timeDifference / hoursInMilli);
        timeDifference = timeDifference % hoursInMilli;

        int elapsedMinutes = (int)(timeDifference / minutesInMilli);
        timeDifference = timeDifference % minutesInMilli;

        int elapsedSeconds = (int)(timeDifference / secondsInMilli);

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

        String elapsedTime = "";
        if(elapsedMinutes < 60){
            return elapsedTime.valueOf(elapsedMinutes).concat("분 전");
        }
        else if(elapsedMinutes >= 60 && elapsedHours < 24){
            return elapsedTime.valueOf(elapsedHours).concat("시간 전");
        }
        else if(elapsedHours > 24 && elapsedDays < 7){
            return elapsedTime.valueOf(elapsedDays).concat("일 전");
        }
        else if(elapsedDays > 7 && elapsedDays < 30){
            return elapsedTime.valueOf(elapsedDays % 7).concat("주 전");
        }
        else if(elapsedDays > 30 && elapsedDays < 365){
            return elapsedTime.valueOf(elapsedDays % 30).concat("월 전");
        }
        else{
            return elapsedTime.valueOf(elapsedDays % 365).concat("년 전");
        }
    }

}

