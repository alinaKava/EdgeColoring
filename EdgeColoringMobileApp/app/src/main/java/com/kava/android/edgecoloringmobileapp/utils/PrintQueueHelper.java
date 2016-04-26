package com.kava.android.edgecoloringmobileapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.kava.android.edgecoloringmobileapp.db.ColoringsDbHelper;
import com.kava.android.edgecoloringmobileapp.ui.PrintDialogActivity;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by adminn on 27.03.2016.
 */
public class PrintQueueHelper {

    public static final String KEY_QUEUE = "queue";

    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static boolean print(Context context, String path, String title, Boolean fromQueue) {
        boolean result = isNetworkConnected(context);
        if (result) {

            if (title == null) {
                title = "Coloring_" + System.currentTimeMillis();
            }

            File requestFile = new File(path);

            Uri fileUri = FileProvider.getUriForFile(
                    context,
                    "com.kava.android.edgecoloringmobileapp.fileprovider",
                    requestFile);

            Intent printIntent = new Intent(context, PrintDialogActivity.class);
            printIntent.setDataAndType(fileUri, "image/jpeg");
            printIntent.putExtra("title", title);
            context.startActivity(printIntent);

        } else if (!fromQueue){
            addJobToQueue(context, path);
        }

        return result;
    }

    private static void addJobToQueue(Context context, String coloringPath) {
        ColoringsDbHelper dbHelper = new ColoringsDbHelper(context);
        int id = dbHelper.getColoringIdByPath(coloringPath);
        if (!dbHelper.isInQueue(id))
        {
            dbHelper.addToQueue(id);
        }
    }

    public static Set<String> getQueue(Context context) {
        SharedPreferences pref = getPreferences(context);
        return pref.getStringSet(KEY_QUEUE, new LinkedHashSet<String>());
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("print_queue", Context.MODE_PRIVATE);
    }
}
