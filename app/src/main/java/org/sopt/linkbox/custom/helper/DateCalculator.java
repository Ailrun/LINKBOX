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
        // SimpleDateFormat sdfCurrent = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");

        int previousYear = Integer.parseInt(sPrevious.substring(0, 4));
        Log.e("Previous year", String.valueOf(previousYear));
        int previousMonth = Integer.parseInt(sPrevious.substring(5, 7));
        Log.e("Previous month", String.valueOf(previousMonth));
        int previousDay = Integer.parseInt(sPrevious.substring(8, 10));
        Log.e("Previous day", String.valueOf(previousDay));
        int previousHour = Integer.parseInt(sPrevious.substring(11, 13));
        Log.e("Previous hour", String.valueOf(previousHour));
        int previousMinute = Integer.parseInt(sPrevious.substring(14, 16));
        Log.e("Previous minute", String.valueOf(previousMinute));
        int previousSecond = Integer.parseInt(sPrevious.substring(17, 19));
        Log.e("Previous second", String.valueOf(previousSecond));

        Calendar currentTime = Calendar.getInstance();
        int currentYear = currentTime.get(Calendar.YEAR);
        Log.e("Current year", String.valueOf(currentYear));
        int currentMonth = currentTime.get(Calendar.MONTH);
        Log.e("Current month", String.valueOf(currentMonth));
        int currentDay = currentTime.get(Calendar.DAY_OF_MONTH);
        Log.e("Current day", String.valueOf(currentDay));
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        Log.e("Current hour", String.valueOf(currentHour));
        int currentMinute = currentTime.get(Calendar.MINUTE);
        Log.e("Current minute", String.valueOf(currentMinute));
        int currentSecond = currentTime.get(Calendar.SECOND);
        Log.e("Current second", String.valueOf(currentSecond));


        // Calendar currentTime = Calendar.getInstance();
        // String sCurrent = currentTime.getTime().toString();

        GregorianCalendar gcPrevious = new GregorianCalendar(previousYear, previousMonth - 1, previousDay, previousHour + 18 , previousMinute, previousSecond);
        Date dPrevious = gcPrevious.getTime();
        // Date dCurrent = new Date();

        GregorianCalendar gcCurrent = new GregorianCalendar(currentYear, currentMonth, currentDay, currentHour, currentMinute, currentSecond);
        Date dCurrent = gcCurrent.getTime();
        /*
        try {
            // dPrevious = sdfPrevious.parse(sPrevious);
            dCurrent = sdfCurrent.parse(sCurrent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */

        long timeDifference = dCurrent.getTime() - (dPrevious.getTime());
        Log.e("TimeDifference", String.valueOf(timeDifference));

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        int elapsedDays = (int)(timeDifference / daysInMilli);
        timeDifference = timeDifference % daysInMilli;
        Log.e("Elapsed Days", String.valueOf(elapsedDays));

        int elapsedHours = (int)(timeDifference / hoursInMilli);
        timeDifference = timeDifference % hoursInMilli;
        Log.e("Elapsed Hours", String.valueOf(elapsedHours));

        int elapsedMinutes = (int)(timeDifference / minutesInMilli);
        timeDifference = timeDifference % minutesInMilli;
        Log.e("Elapsed Minutes", String.valueOf(elapsedMinutes));

        int elapsedSeconds = (int)(timeDifference / secondsInMilli);
        Log.e("Elapsed Seconds", String.valueOf(elapsedSeconds));

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

        String elapsedTime = "";
        /*
        if(elapsedDays > 365){
            return elapsedTime.valueOf(elapsedDays % 365).concat("년 전");
        }
        else if(elapsedDays > 30 && elapsedDays <= 365){
            return elapsedTime.valueOf(elapsedDays % 30).concat("월 전");
        }
        else if(elapsedDays > 7 && elapsedDays <= 30){
            return elapsedTime.valueOf(elapsedDays % 7).concat("주 전");
        }
        else if(elapsedHours > 24 && elapsedDays <= 7){
            return elapsedTime.valueOf(elapsedDays).concat("일 전");
        }
        else if(elapsedMinutes >= 60 && elapsedHours <= 24){
            return elapsedTime.valueOf(elapsedHours).concat("시간 전");
        }
        else{
            return elapsedTime.valueOf(elapsedMinutes).concat("분 전");
        }
        */
        if(elapsedDays != 0) {
            if (elapsedDays >= 365) {
                return elapsedTime.valueOf(elapsedDays % 365).concat("년 전");
            }
            else if(elapsedDays >= 30){
                return elapsedTime.valueOf(elapsedDays % 30).concat("월 전");
            }
            else if(elapsedDays >= 7){
                return elapsedTime.valueOf(elapsedDays % 7).concat("주 전");
            }
            else{
                return elapsedTime.valueOf(elapsedDays).concat("일 전");
            }
        }
        else if(elapsedHours != 0){
            return elapsedTime.valueOf(elapsedHours).concat("시간 전");
        }
        else{
            return elapsedTime.valueOf(elapsedMinutes).concat("분 전");
        }

    }

}