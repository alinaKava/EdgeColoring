package com.kava.android.edgecoloringmobileapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by adminn on 27.03.2016.
 */
public class PrintQueueHelper {
    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static void UpdatePrintQueue(Context context)
    {
        if (!isNetworkConnected(context))
        {

        }
    }

}
