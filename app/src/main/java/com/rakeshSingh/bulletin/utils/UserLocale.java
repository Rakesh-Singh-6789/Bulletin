package com.rakeshSingh.bulletin.utils;

import android.content.Context;
import android.os.Build;

public final class UserLocale {

    public static String getUserLocale(Context context) {
        String locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0).getCountry();

        } else {
            locale = context.getResources().getConfiguration().locale.getCountry();
        }
        return locale.toLowerCase();
    }

    public static String getUserCountry(Context context) {
        String country;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            country = "(" + context.getResources().getConfiguration()
                    .getLocales().get(0)
                    .getDisplayCountry() + ")";

        } else {
            country = "(" + context.getResources().getConfiguration()
                    .locale
                    .getDisplayCountry() + ")";
        }
        return country;
    }

}
