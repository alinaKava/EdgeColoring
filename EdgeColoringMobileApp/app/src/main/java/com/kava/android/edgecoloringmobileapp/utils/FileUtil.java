package com.kava.android.edgecoloringmobileapp.utils;

import com.kava.android.edgecoloringmobileapp.model.Algorithm;
import com.kava.android.edgecoloringmobileapp.model.Coloring;

import java.io.File;
import java.util.Date;

/**
 * Created by adminn on 24.04.2016.
 */
public class FileUtil {
    public static Coloring convertFileToColoring(File file, Algorithm algorithm, int isDefault){
        Coloring coloring = new Coloring();
        coloring.setName(file.getName());
        coloring.setPath(file.getAbsolutePath());
        coloring.setSize(file.length());
        coloring.setDate(new Date(System.currentTimeMillis()));
        if (algorithm != null){
            coloring.setIdAlgorithm(algorithm.getId());
        }
        coloring.setIsDefault(isDefault);
        return coloring;
    }

}
