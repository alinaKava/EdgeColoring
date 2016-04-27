package com.kava.android.edgecoloringmobileapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kava.android.edgecoloringmobileapp.R;
import com.kava.android.edgecoloringmobileapp.db.ColoringsDbHelper;
import com.kava.android.edgecoloringmobileapp.model.Algorithm;
import com.kava.android.edgecoloringmobileapp.utils.AlgorithmsHelper;
import com.kava.android.edgecoloringmobileapp.utils.FileUtil;
import com.kava.android.edgecoloringmobileapp.utils.PrintQueueHelper;
import com.kava.android.imageprocessing.ImageProcessing;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageWorkActivity extends AppCompatActivity {

    private ColoringsDbHelper dbHelper;
    private Toolbar toolbar;
    private FloatingActionButton myFab;
    private ImageView imageView;
    private boolean isSaved = false;
    private String imagePath;
    private Bitmap image;
    private String algorithmName = "Alg1";
    private Algorithm algorithm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageView = (ImageView) findViewById(R.id.image);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        imagePath = getIntent().getStringExtra("imagePath");
        int pos;
        if ((pos = getIntent().getIntExtra("position", -1)) == -1){
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            algorithmName = sharedPref.getString(SettingsActivity.KEY_PREF_SYNC_CONN, "");
        }
        else {
            algorithmName = AlgorithmsHelper.getAlgorithmName(this, pos);
        }

        image = loadImage(imagePath);

        myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doPhotoPrint();
            }
        }); //!!!
        myFab.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            isSaved = savedInstanceState.getBoolean("is_saved");
        }

        dbHelper = new ColoringsDbHelper(this);
        algorithm = dbHelper.getAlgorithmByName(algorithmName);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_saved", isSaved);
    }

    private void saveColoring(File file) {

        if (file.exists())
            file.delete();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file.getAbsoluteFile());
            image.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imagePath = file.getAbsolutePath();
        isSaved = true;
        dbHelper.addColoring(FileUtil.convertFileToColoring(file, algorithm, ColoringsDbHelper.IS_NOT_DEFAULT));
        myFab.setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
    }

    private void doPhotoPrint() {
        File file = new File(imagePath);
        if (!PrintQueueHelper.print(this, file.getAbsolutePath(), file.getName(), false))
            Toast.makeText(this, "No internet connection, coloring has been added to print queue", Toast.LENGTH_LONG).show();
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("OpenCv", "OpenCV loaded successfully");
//                    ImageProcessing proc = new ImageProcessing();
//                    proc.startImageWorkActivity("/sdcard/Images/");
                    //m=new Mat();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        else if (id == R.id.menu_save)
        {
            File file = new File(getFilesDir() + "/user", "Coloring_" + System.currentTimeMillis() + ".jpg");
            saveColoring(file);
            Toast.makeText(this, "Coloring has been saved", Toast.LENGTH_LONG).show();

        }
        else if (id == R.id.menu_more_alg)
        {
            Intent intent = new Intent(this, PreviewActivity.class);
            intent.putExtra("path", imagePath);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_save);
        item.setVisible(!isSaved);
        item = menu.findItem(R.id.menu_more_alg);
        item.setVisible(!isSaved);
        item = menu.findItem(R.id.menu_delete);
        item.setVisible(isSaved);
        item = menu.findItem(R.id.menu_more_info);
        item.setVisible(isSaved);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    private Bitmap loadImage(String path) {
        ImageProcessing proc = new ImageProcessing();
        Bitmap colority = proc.loadImage(path, algorithmName);
        imageView.setImageBitmap(colority);
        return colority;
    }

}


