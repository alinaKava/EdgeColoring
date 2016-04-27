package com.kava.android.edgecoloringmobileapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kava.android.edgecoloringmobileapp.R;
import com.kava.android.edgecoloringmobileapp.db.ColoringsDbHelper;
import com.kava.android.edgecoloringmobileapp.model.Coloring;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageDefaultGridFragment extends Fragment {

    private GridView mListView;
    private ColoringsDbHelper dbHelper;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = new ColoringsDbHelper(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_grid_view, container, false);
        View view = inflater.inflate(R.layout.home_view, container, false);
        mListView = (GridView) rootView.findViewById(R.id.gridView);
        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.fabCreate);
        myFab.setVisibility(View.GONE);
        
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListView.setAdapter(new ImageUserGridFragment.ImageAdapter(getActivity(), getColorings()));
    }

    protected void startImagePagerActivity(int position) {
        Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
        intent.putExtra("path", getColorings().get(position).getPath());
        intent.putExtra("isDefault", true);
        startActivity(intent);
    }

    private List<Coloring> getColorings(){
        return dbHelper.getColorings(ColoringsDbHelper.IS_DEFAULT);
    }

}
