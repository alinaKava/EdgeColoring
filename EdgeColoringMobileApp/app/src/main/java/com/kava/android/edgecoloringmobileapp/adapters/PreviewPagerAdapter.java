package com.kava.android.edgecoloringmobileapp.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kava.android.edgecoloringmobileapp.ui.AlgorithmImageFragment;

/**
 * Created by adminn on 24.04.2016.
 */
public class PreviewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private String imagePath;

    public PreviewPagerAdapter(FragmentManager fm, int NumOfTabs, String imagePath) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.imagePath = imagePath;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("path", imagePath);
        AlgorithmImageFragment fragment = new AlgorithmImageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
