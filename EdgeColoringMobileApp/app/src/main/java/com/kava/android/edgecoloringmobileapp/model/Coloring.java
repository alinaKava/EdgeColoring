package com.kava.android.edgecoloringmobileapp.model;

import java.util.Date;

/**
 * Created by adminn on 23.04.2016.
 */
public class Coloring {
    private int id;
    private String name;
    private String path;
    private long size;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private int idAlgorithm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getIdAlgorithm() {
        return idAlgorithm;
    }

    public void setIdAlgorithm(int idAlgorithm) {
        this.idAlgorithm = idAlgorithm;
    }
}
