package com.kava.android.edgecoloringmobileapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kava.android.edgecoloringmobileapp.ui.ImageUserGridFragment;
import com.kava.android.edgecoloringmobileapp.ui.ImageDefaultGridFragment;

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
                ImageDefaultGridFragment tab1 = new ImageDefaultGridFragment();
                return tab1;
            case 1:
                ImageUserGridFragment tab2 = new ImageUserGridFragment();
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
