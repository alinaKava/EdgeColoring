package com.kava.android.edgecoloringmobileapp.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kava.android.edgecoloringmobileapp.R;
import com.kava.android.edgecoloringmobileapp.db.ColoringsDbHelper;
import com.kava.android.edgecoloringmobileapp.model.Coloring;
import com.kava.android.edgecoloringmobileapp.utils.PrintQueueHelper;

import java.io.File;

public class ImageDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageView;
    private String imagePath;
    private Boolean isDefault = true;
    private ColoringsDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new ColoringsDbHelper(this);
        setContentView(R.layout.activity_image_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        imagePath = getIntent().getStringExtra("path");
        isDefault = getIntent().getBooleanExtra("isDefault", true);

        imageView = (ImageView) findViewById(R.id.image);
        Glide
                .with(this)
                .load(new File(imagePath))
                .crossFade()
                .into(imageView);

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doPhotoPrint(imagePath);
            }
        });
    }

    private void doPhotoPrint(String filePath) {
        File file = new File(filePath);
        if (!PrintQueueHelper.print(this, file.getAbsolutePath(), file.getName(), false))
            Toast.makeText(this, "No internet connection, coloring has been added to print queue", Toast.LENGTH_LONG).show();

    }

    private void createDialog() {
        Coloring coloring = dbHelper.getColoringByPath(imagePath);
        String algName = dbHelper.getAlgorithmName(coloring.getIdAlgorithm());
        final CharSequence[] options = {"Path : " + coloring.getPath(), "Size : " + coloring.getSize()/1024 + " Kb", "Date : " + coloring.getDate().toString(), "Algorithm : " + algName};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Make new");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (id == R.id.menu_more_info) {
            createDialog();
            return true;
        }
        if (id == R.id.menu_delete) {
            dbHelper.removeColoring(dbHelper.getColoringIdByPath(imagePath));
            Toast.makeText(this, "Coloring has been deleted", Toast.LENGTH_LONG).show();
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_save);
        item.setVisible(false);
        item = menu.findItem(R.id.menu_more_alg);
        item.setVisible(false);
        item = menu.findItem(R.id.menu_delete);
        item.setVisible(!isDefault);
        item = menu.findItem(R.id.menu_more_info);
        item.setVisible(!isDefault);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
}
