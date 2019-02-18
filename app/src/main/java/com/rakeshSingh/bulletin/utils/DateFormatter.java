package com.rakeshSingh.bulletin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatter {

    public static String getFormattedTime(String utcTimeString) {
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        try {
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            long timeElapsedInSeconds =
                    (System.currentTimeMillis() - dateFormat.parse(utcTimeString).getTime()) / 1000;

            if (timeElapsedInSeconds < 60) {
                return "less than 1m";
            } else if (timeElapsedInSeconds < 3600) {
                timeElapsedInSeconds = timeElapsedInSeconds / 60;
                return timeElapsedInSeconds + "m";
            } else if (timeElapsedInSeconds < 86400) {
                timeElapsedInSeconds = timeElapsedInSeconds / 3600;
                return timeElapsedInSeconds + "h";
            } else {
                timeElapsedInSeconds = timeElapsedInSeconds / 86400;
                return timeElapsedInSeconds + "d";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
