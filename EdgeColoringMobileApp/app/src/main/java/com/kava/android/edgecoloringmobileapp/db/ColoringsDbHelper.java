package com.kava.android.edgecoloringmobileapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kava.android.edgecoloringmobileapp.model.Coloring;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adminn on 23.04.2016.
 */
public class ColoringsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.

    SQLiteDatabase db = getWritableDatabase();

    private static final String TABLE_FILES = "files";
    private static final String COLUMN_FILE_ID = "file_id";
    private static final String COLUMN_PATH = "path";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_SIZE = "size";
    private static final String TABLE_QUEUE = "queue";
    private static final String TABLE_ALGORITHMS = "algorithms";
    private static final String COLUMN_ALGORITHM_ID = "algorithm_id";
    public static final String[] ALGORITHMS_NAME = {"Alg1", "Alg2", "Alg3", "Alg4", "Alg5"};

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Colorings.db";
    private static final String TABLE_FILES_CREATE = "create table " + TABLE_FILES + "(" +
            COLUMN_FILE_ID + " integer primary key autoincrement, " +
            COLUMN_PATH + " text not null, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_SIZE + " numeric not null, " +
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
    }

    public void onCreate(SQLiteDatabase db) {
        for (String qeury : createQueries) {
            db.execSQL(qeury);
        }
        addAlgorithms();
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

    private void addAlgorithms(){
        for (String name : ALGORITHMS_NAME) {
            addAlgorithm(name);
        }
    }

    public List<Coloring> getColorings() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FILES, null);
        return toColorings(cursor);
    }

    public void addColoring(Coloring coloring) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, coloring.getName());
        values.put(COLUMN_PATH, coloring.getPath());
        values.put(COLUMN_DATE, coloring.getDate().getTime());
        values.put(COLUMN_SIZE, coloring.getSize());
        values.put(COLUMN_ALGORITHM_ID, coloring.getIdAlgorithm());
        db.insert(TABLE_FILES, null, values);
    }

    public void removeColoring(Coloring coloring){
        int id = coloring.getId();
        db.delete(TABLE_FILES, COLUMN_FILE_ID
                + " = " + id, null);
    }

    public List<Coloring> getQueue(){
        Cursor cursor = db.rawQuery("SELECT * " + " FROM " + TABLE_FILES + " LEFT JOIN " + TABLE_QUEUE + " ON " + TABLE_FILES + "." + COLUMN_FILE_ID + TABLE_QUEUE + "." + COLUMN_FILE_ID, null);
        return toColorings(cursor);
    }

    public void removeFromColoring(Coloring coloring){
        int id = coloring.getId();
        db.delete(TABLE_QUEUE, COLUMN_FILE_ID
                + " = " + id, null);
    }

    public void clearQueue(){
        db.delete(TABLE_QUEUE, null, null);
    }

    public void addAlgorithm(String name){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        db.insert(TABLE_ALGORITHMS, null, values);
    }

    private static List<Coloring> toColorings(Cursor cursor){
        List<Coloring> colorigs = new ArrayList<>();
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
                    colorigs.add(coloring);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return colorigs;
    }
}