package me.franciscoigor.habits.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * LocaleHelper
 *
 * Common methods to set and change the current application localization (language)
 */
public class LocaleHelper {

    public static void setLocale(Activity activity, String lang) {
        String current = activity.getResources().getConfiguration().locale.getLanguage();

        Locale myLocale = new Locale(lang);
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        if (!current.equals(lang)){
            Intent refresh = new Intent(activity, activity.getClass());
            activity.startActivity(refresh);
            activity.finish();
        }

    }
}
