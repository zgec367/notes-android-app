package com.example.notes;

import android.content.res.Resources;

import java.util.concurrent.TimeUnit;

public class TimeAgo {

    public static String getTimeAgo(long time, Resources resources) {
        long now = System.currentTimeMillis();
        final long diff = now - time;

        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);


        if (seconds < 60) {
            return resources.getString(R.string.just_now);
        } else if (seconds == 60) {
            if (resources.getConfiguration().locale.toString().equalsIgnoreCase("hr")) {
                return resources.getString(R.string.before) + " " + resources.getString(R.string.a_minute_ago);
            } else {
                return resources.getString(R.string.a_minute_ago);
            }
        } else if (minutes < 60) {
            if (resources.getConfiguration().locale.toString().equalsIgnoreCase("hr")) {
                return resources.getString(R.string.before) + " " + minutes + " " + resources.getString(R.string.minutes_ago);
            } else {
                return minutes + " " + resources.getString(R.string.minutes_ago);
            }
        } else if (hours < 24) {
            if (resources.getConfiguration().locale.toString().equalsIgnoreCase("hr")) {
                return resources.getString(R.string.before) + " " + hours + resources.getString(R.string.hours_ago);
            } else {
                return hours + " " + resources.getString(R.string.hours_ago);
            }
        } else if (hours < 48) {
            return resources.getString(R.string.yesterday);
        } else {
            if (resources.getConfiguration().locale.toString().equalsIgnoreCase("hr")) {
                return resources.getString(R.string.before) + " " + days + resources.getString(R.string.days_ago);
            } else {
                return days + " " + resources.getString(R.string.days_ago);
            }
        }
    }
}
