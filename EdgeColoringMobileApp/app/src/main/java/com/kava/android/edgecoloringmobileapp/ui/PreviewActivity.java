package com.kava.android.edgecoloringmobileapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.kava.android.edgecoloringmobileapp.R;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        setTitle(R.string.algorithms_title);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            String path = intent.getStringExtra("path");
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            PreviewFragment fragment = new PreviewFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.framePreview, fragment).commit();
        }
    }

}
