package com.kava.android.edgecoloringmobileapp.utils;

import android.content.Context;

import com.kava.android.edgecoloringmobileapp.db.ColoringsDbHelper;

import java.util.List;

/**
 * Created by adminn on 26.04.2016.
 */
public class AlgorithmsHelper {

    public static String getAlgorithmName(Context context, int position){
        ColoringsDbHelper dbHelper = new ColoringsDbHelper(context);
        List<String> names = dbHelper.getAlgorithmsNames();
        if (position < names.size())
            return names.get(position);
        else
            return names.get(0);
    }

    public static int getAlgorithmId(Context context, String name){
        ColoringsDbHelper dbHelper = new ColoringsDbHelper(context);
        List<String> names = dbHelper.getAlgorithmsNames();
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals(name)){
                return i;
            }
        }
        return 0;
    }

}
