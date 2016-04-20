package com.kava.android.edgecoloringmobileapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.kava.android.edgecoloringmobileapp.ui.ImageWorkActivity;
import com.kava.android.edgecoloringmobileapp.ui.MainActivity;

/**
 * Created by adminn on 27.03.2016.
 */
public class ActivityLifecycleHelper {

    private static final int REQUEST_IMAGE_PICKER = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 3;

    private static final int READ_EXTERNAL_STORAGE_REQUEST = 1;

    private MainActivity mActivity;
    private Uri mPhotoUri;

    public ActivityLifecycleHelper(MainActivity activity) {
        mActivity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPhotoUri = savedInstanceState.getParcelable("photo_uri");
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("photo_uri", mPhotoUri);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICKER) {
                if (data != null && data.getData() != null) {
                    Uri selectedResourceUri = data.getData();
                    String selectedImagePath = PathUtil.getPath(mActivity, selectedResourceUri);
                    if (selectedImagePath != null) {
                        mActivity.onImagePicked(selectedImagePath);
                    }
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                String selectedImagePath = PathUtil.getPath(mActivity, mPhotoUri);
                if (selectedImagePath != null) {
                    mActivity.onImagePickedTaken(selectedImagePath);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_REQUEST: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                }
                break;
            }
        }
    }

    public void startImagePicking() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_REQUEST);
        } else {
            pickImage();
        }
    }

    public void startImageTaking() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = PathUtil.getPhotoFileUri(mActivity);
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null && uri != null) {
            mPhotoUri = uri;
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            mActivity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void pickImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        mActivity.startActivityForResult(pickIntent, REQUEST_IMAGE_PICKER);
    }

}