package com.kava.android.edgecoloringmobileapp.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kava.android.edgecoloringmobileapp.R;
import com.kava.android.edgecoloringmobileapp.adapters.PreviewPagerAdapter;
import com.kava.android.edgecoloringmobileapp.db.ColoringsDbHelper;

import java.util.List;

/**
 * Created by adminn on 24.04.2016.
 */
public class PreviewFragment extends Fragment {

    private String imagePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        imagePath = getArguments().getString("path");

        return inflater.inflate(R.layout.fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.preview_tab_layout);
        ColoringsDbHelper dbHelper = new ColoringsDbHelper(getActivity());
        List<String> tabsNames = dbHelper.getAlgorithmsNames();
        for (String name : tabsNames) {
            tabLayout.addTab(tabLayout.newTab().setText(name));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.preview_pager);
        final PreviewPagerAdapter adapter = new PreviewPagerAdapter
                (getFragmentManager(), tabLayout.getTabCount(), imagePath);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.fabChoose);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
