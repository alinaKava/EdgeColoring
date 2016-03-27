package com.kava.android.edgecoloringmobileapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by adminn on 27.03.2016.
 */
public class PrintQueueHelper {

    public static final String KEY_QUEUE = "queue";

    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static void addJobToQueue(Context context, String colorityPath) {
        if (!isNetworkConnected(context)) {
            SharedPreferences pref = context.getSharedPreferences("print_queue", Context.MODE_PRIVATE);
            Set<String> queue = pref.getStringSet(KEY_QUEUE, new LinkedHashSet<String>());
            queue.add(colorityPath);
            pref.edit().putStringSet(KEY_QUEUE, queue).apply();
        } else {
            // TODO print
        }
    }

    public static Set<String> getQueue(Context context) {
        SharedPreferences pref = context.getSharedPreferences("print_queue", Context.MODE_PRIVATE);
        return pref.getStringSet(KEY_QUEUE, new LinkedHashSet<String>());
    }

}
