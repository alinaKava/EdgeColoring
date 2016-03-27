package com.kava.android.edgecoloringmobileapp.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kava.android.edgecoloringmobileapp.R;
import com.kava.android.edgecoloringmobileapp.utils.PrintQueueHelper;

import java.io.File;

public class ImageDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageView;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);


        int imagePosition = getIntent().getIntExtra("imagePosition", -1);
        if (imagePosition >= 0) {
            imagePath = loadImage(imagePosition);
        }

        FloatingActionButton myFab = (FloatingActionButton)  findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doPhotoPrint(imagePath);
            }
        });
    }

    private void doPhotoPrint(String file) {
        PrintQueueHelper.print(this, file, null);
    }

    private String loadImage(int imagePosition) {
        File dir = new File(getFilesDir(), "defaults");
        File[] files = dir.listFiles();
        imageView = (ImageView) findViewById(R.id.image);
        Glide
                .with(this)
                .load(files[imagePosition])
                .crossFade()
                .into(imageView);
        return files[imagePosition].getAbsolutePath();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem item = menu.findItem(R.id.menu_save);
        item.setVisible(false);
        return true;
    }
}
