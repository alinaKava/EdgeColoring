package com.kava.android.edgecoloringmobileapp.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.kava.android.edgecoloringmobileapp.R;
import com.kava.android.edgecoloringmobileapp.utils.PrintQueueHelper;

import java.io.File;
import java.util.Set;

/**
 * Author: Yuriy Mysochenko
 * Date: 27.03.2016
 */
public class PrintQueueFragment extends Fragment {

    private ImageGridFragment.ImageAdapter mAdapter;
    private File[] mColorities;
    private GridView mListView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_grid_view, container, false);
        mListView = (GridView) rootView.findViewById(R.id.gridView);

        mColorities = getColorities();
        mAdapter = new ImageGridFragment.ImageAdapter(getActivity(), mColorities);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createDialog(position);
            }
        });
        return rootView;
    }

    private File[] getColorities(){
        Set<String> queue = PrintQueueHelper.getQueue(getActivity());
        File[] files = new File[queue.size()];
        int i = 0;
        for (String path : queue) {
            files[i++] = new File(path);
        }
        return files;
    }

    private void createDialog(final int position) {
        final CharSequence[] options = {"Print", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Make new");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    print(position);
                } else if (which == 1) {
                    delete(position);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void delete(int position) {
        Set<String> queue = PrintQueueHelper.getQueue(getActivity());
        File colority = mColorities[position];
        queue.remove(colority.getAbsolutePath());

        mColorities = getColorities();
        mAdapter = new ImageGridFragment.ImageAdapter(getActivity(), mColorities);
        mListView.setAdapter(mAdapter);
    }

    private void print(int position) {
        Set<String> queue = PrintQueueHelper.getQueue(getActivity());
        File colority = mColorities[position];
        if (!PrintQueueHelper.print(getContext(), colority.getAbsolutePath(), null, true))
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
        else
            delete(position);
    }

}
