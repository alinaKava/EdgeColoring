package com.kava.android.edgecoloringmobileapp.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.kava.android.edgecoloringmobileapp.utils.ActivityLifecycleHelper;
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
    private ImageView imageView;
    private boolean isSaved = false;
    private String colorityPath;
    private Bitmap image;
    private String algorithmName = "Alg1";
    private Algorithm algorithm;

    private ActivityLifecycleHelper lifecycleHelper;

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

        final String imagePath = getIntent().getStringExtra("imagePath");
        image = loadImage(imagePath);

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doPhotoPrint();
            }
        }); //!!!

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
        isSaved = true;
        dbHelper.addColoring(FileUtil.convertFileToColoring(file, algorithm));
        invalidateOptionsMenu();
    }

    private void doPhotoPrint() {
        File file;
        if (!isSaved || colorityPath == null) {
            file = new File(getFilesDir() + "/user", "Colority_" + System.currentTimeMillis() + ".jpg");
            saveColoring(file);
        } else {
            file = new File(colorityPath);
        }

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
            File file = new File(getFilesDir() + "/user", "Colority_" + System.currentTimeMillis() + ".jpg");
            saveColoring(file);
            Toast.makeText(this, "Colority has been saved", Toast.LENGTH_LONG).show();

        }
        else if (id == R.id.menu_edit)
        {
            Toast.makeText(this, "Sorry, you're using the free version", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_save);
        item.setVisible(!isSaved);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    private Bitmap loadImage(String path) {
        ImageProcessing proc = new ImageProcessing();
        Bitmap colority = proc.loadImage(path);
        imageView.setImageBitmap(colority);
        return colority;
    }

}


