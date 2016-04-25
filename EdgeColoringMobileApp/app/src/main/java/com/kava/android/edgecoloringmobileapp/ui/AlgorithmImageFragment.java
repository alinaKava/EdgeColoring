package com.kava.android.edgecoloringmobileapp.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kava.android.edgecoloringmobileapp.R;
import com.kava.android.edgecoloringmobileapp.db.ColoringsDbHelper;
import com.kava.android.imageprocessing.ImageProcessing;

import java.util.List;

/**
 * Created by adminn on 24.04.2016.
 */
public class AlgorithmImageFragment extends Fragment {

    private int algorithmPosition = 0;
    private String imagePath;
    private ImageProcessing proc;
    private ImageView imageView;

    public AlgorithmImageFragment() {
        // Requireda empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.algorithm_image, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.preview_image);
        algorithmPosition = getArguments().getInt("position", 0);
        imagePath = getArguments().getString("path");
        proc = new ImageProcessing();
        Bitmap colority = proc.loadImage(imagePath, getAlgorithmName(algorithmPosition));
        imageView.setImageBitmap(colority);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private String getAlgorithmName(int position){
        ColoringsDbHelper dbHelper = new ColoringsDbHelper(getContext());
        List<String> names = dbHelper.getAlgorithmsNames();
        if (position < names.size())
            return names.get(position);
        else
            return names.get(0);
    }

}
