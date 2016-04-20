package com.kava.android.edgecoloringmobileapp.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kava.android.edgecoloringmobileapp.ui.ImageGridFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ImageGridFragment tab1 = new ImageGridFragment();
                Bundle bundleDef = new Bundle();
                bundleDef.putString("folder", "defaults");
                tab1.setArguments(bundleDef);
                return tab1;
            case 1:
                ImageGridFragment tab2 = new ImageGridFragment();
                Bundle bundle = new Bundle();
                bundle.putString("folder", "user");
                tab2.setArguments(bundle);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
