package com.kava.android.edgecoloringmobileapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kava.android.edgecoloringmobileapp.R;
import com.kava.android.edgecoloringmobileapp.model.Algorithm;
import com.kava.android.edgecoloringmobileapp.model.Coloring;
import com.kava.android.edgecoloringmobileapp.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adminn on 23.04.2016.
 */
public class ColoringsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private SQLiteDatabase db;
    private static final String TABLE_FILES = "files";
    private static final String COLUMN_FILE_ID = "file_id";
    private static final String COLUMN_PATH = "path";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_SIZE = "size";
    private static final String COLUMN_IS_DEFAULT = "is_default";
    private static final String TABLE_QUEUE = "queue";
    private static final String TABLE_ALGORITHMS = "algorithms";
    private static final String COLUMN_ALGORITHM_ID = "algorithm_id";
    public static final int IS_DEFAULT = 1;
    public static final int IS_NOT_DEFAULT = 0;
    public static String[] ALGORITHMS_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Colorings.db";
    private static final String TABLE_FILES_CREATE = "create table " + TABLE_FILES + "(" +
            COLUMN_FILE_ID + " integer primary key autoincrement, " +
            COLUMN_PATH + " text not null, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_SIZE + " numeric not null, " +
            COLUMN_IS_DEFAULT + " integer not null, " +
            COLUMN_DATE + " date not null, " +
            COLUMN_ALGORITHM_ID + " integer not null);";
    private static final String TABLE_QUEUE_CREATE = "create table " + TABLE_QUEUE + "(" +
            COLUMN_FILE_ID + " integer not null);";
    private static final String TABLE_ALGORITHMS_CREATE = "create table " + TABLE_ALGORITHMS + "(" +
            COLUMN_ALGORITHM_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " text not null);";
    private static String[] createQueries = {TABLE_FILES_CREATE, TABLE_QUEUE_CREATE, TABLE_ALGORITHMS_CREATE};
    private static String[] tables = {TABLE_FILES, TABLE_QUEUE, TABLE_ALGORITHMS};

    public ColoringsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ALGORITHMS_NAME = new String[4];
        ALGORITHMS_NAME[0] = context.getResources().getString(R.string.alg1);
        ALGORITHMS_NAME[1] = context.getResources().getString(R.string.alg2);
        ALGORITHMS_NAME[2] = context.getResources().getString(R.string.alg3);
        ALGORITHMS_NAME[3] = context.getResources().getString(R.string.alg4);
    }

    public void onCreate(SQLiteDatabase db) {
        for (String qeury : createQueries) {
            db.execSQL(qeury);
        }
        addAlgorithms(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        for (String table : tables) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addDefaultColorings(File defaultDir){
        File[] files = defaultDir.listFiles();
        for (File file : files) {
            addColoring(FileUtil.convertFileToColoring(file, null, ColoringsDbHelper.IS_DEFAULT));
        }
    }

    private void addAlgorithms(SQLiteDatabase db){
        for (String name : ALGORITHMS_NAME) {
            addAlgorithm(db, name);
        }
    }

    public Algorithm getAlgorithmByName(String name){
        Algorithm algorithm = new Algorithm();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_ALGORITHMS + " WHERE " + COLUMN_NAME + " =? ", new String[]{name});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int colId = cursor.getColumnIndexOrThrow(COLUMN_ALGORITHM_ID);
                int colName = cursor.getColumnIndexOrThrow(COLUMN_NAME);
                algorithm.setId(cursor.getInt(colId));
                algorithm.setName(cursor.getString(colName));
            }
            cursor.close();
        }
        return algorithm;
    }

    public String getAlgorithmName(int id){
        String name = "";
        Cursor cursor = getWritableDatabase().rawQuery("SELECT " + COLUMN_NAME + " FROM " + TABLE_ALGORITHMS + " WHERE " + COLUMN_ALGORITHM_ID + " =? ", new String[]{Integer.toString(id)});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int colName = cursor.getColumnIndexOrThrow(COLUMN_NAME);
                name = cursor.getString(colName);
            }
            cursor.close();
        }
        return name;
    }

    public List<String> getAlgorithmsNames(){
        List<String> names = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT " + COLUMN_NAME + " FROM " + TABLE_ALGORITHMS, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int colName = cursor.getColumnIndexOrThrow(COLUMN_NAME);
                do {
                    names.add(cursor.getString(colName));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return names;
    }

    public List<Coloring> getColorings(int isDefault) {
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_FILES + " WHERE " + COLUMN_IS_DEFAULT + " =? ", new String[]{Integer.toString(isDefault)});
        List<Coloring> colorings = toColorings(cursor);
        cursor.close();
        return colorings;
    }

    public int getColoringIdByPath(String path){
        int id = 0;
        Cursor cursor = getWritableDatabase().rawQuery("SELECT " + COLUMN_FILE_ID + " FROM " + TABLE_FILES + " WHERE " + COLUMN_PATH + " =? ", new String[]{path});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int colId = cursor.getColumnIndexOrThrow(COLUMN_FILE_ID);
                id = cursor.getInt(colId);
            }
            cursor.close();
        }
        return id;
    }

    public Coloring getColoringByPath(String path){
        int id = 0;
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_FILES + " WHERE " + COLUMN_PATH + " =? ", new String[]{path});
        return toColorings(cursor).get(0);
    }

    public Boolean isInQueue(int coloringId){
        Boolean result = false;
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * " + " FROM " + TABLE_QUEUE + " WHERE " + COLUMN_FILE_ID + " =? ", new String[]{Integer.toString(coloringId)});
        if (cursor != null && cursor.moveToFirst()) {
            result = true;
            cursor.close();
        }
        return result;
    }

    public void addColoring(Coloring coloring) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, coloring.getName());
        values.put(COLUMN_PATH, coloring.getPath());
        values.put(COLUMN_DATE, coloring.getDate().getTime());
        values.put(COLUMN_SIZE, coloring.getSize());
        values.put(COLUMN_IS_DEFAULT, coloring.getIsDefault());
        values.put(COLUMN_ALGORITHM_ID, coloring.getIdAlgorithm());
        getWritableDatabase().insert(TABLE_FILES, null, values);
    }

    public void removeColoring(int id){
        getWritableDatabase().delete(TABLE_FILES, COLUMN_FILE_ID
                + " = " + id, null);
    }

    public List<Coloring> getQueue(){
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * " + " FROM " + TABLE_FILES + " INNER JOIN " + TABLE_QUEUE + " ON " + TABLE_FILES + "." + COLUMN_FILE_ID + " = " + TABLE_QUEUE + "." + COLUMN_FILE_ID, null);
        return toColorings(cursor);
    }

    public void addToQueue(int id){
        ContentValues values = new ContentValues();
        values.put(COLUMN_FILE_ID, id);
        getWritableDatabase().insert(TABLE_QUEUE, null, values);
    }

    public void removeFromQueue(int id){
        getWritableDatabase().delete(TABLE_QUEUE, COLUMN_FILE_ID
                + " = " + id, null);
    }

    public void clearQueue(){
        getWritableDatabase().delete(TABLE_QUEUE, null, null);
    }

    public void addAlgorithm(SQLiteDatabase db, String name){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        db.insert(TABLE_ALGORITHMS, null, values);
    }

    private static List<Coloring> toColorings(Cursor cursor){
        List<Coloring> colorings = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int colId = cursor.getColumnIndexOrThrow(COLUMN_FILE_ID);
                int colName = cursor.getColumnIndexOrThrow(COLUMN_NAME);
                int colPath = cursor.getColumnIndexOrThrow(COLUMN_PATH);
                int colDate = cursor.getColumnIndexOrThrow(COLUMN_DATE);
                int colSize = cursor.getColumnIndexOrThrow(COLUMN_SIZE);
                int colAlgorithm = cursor.getColumnIndexOrThrow(COLUMN_ALGORITHM_ID);
                do {
                    Coloring coloring = new Coloring();
                    coloring.setId(cursor.getInt(colId));
                    coloring.setName(cursor.getString(colName));
                    coloring.setPath(cursor.getString(colPath));
                    coloring.setDate(new Date(cursor.getLong(colDate)));
                    coloring.setSize(cursor.getInt(colSize));
                    coloring.setIdAlgorithm(cursor.getInt(colAlgorithm));
                    colorings.add(coloring);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return colorings;
    }
}
