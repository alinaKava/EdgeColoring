package com.kava.android.edgecoloringmobileapp.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kava.android.edgecoloringmobileapp.R;
import com.kava.android.edgecoloringmobileapp.utils.ActivityLifecycleHelper;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageGridFragment extends Fragment {

    private ActivityLifecycleHelper lifecycleHelper;
    private String folderColorings = "";

    public ImageGridFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_grid_view, container, false);
        View view = inflater.inflate(R.layout.home_view, container, false);
        GridView listView = (GridView) rootView.findViewById(R.id.gridView);
        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.fabCreate);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createDialog();
            }
        });

        folderColorings = getArguments().getString("folder");

        ((GridView) listView).setAdapter(new ImageAdapter(getActivity(), getColorities(folderColorings)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });

        return rootView;
    }

    private void createDialog() {
        final CharSequence[] options = {"Take photo", "Colority", "Choose from gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Make new");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    lifecycleHelper.startImageTaking();
                } else if (which == 1) {
                    lifecycleHelper.startImagePicking();
                } else if (which == 2) {
                    lifecycleHelper.startImagePicking();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    protected void startImagePagerActivity(int position) {
        Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
        intent.putExtra("imagePosition", position);
        intent.putExtra("folderColorings", folderColorings);
        startActivity(intent);
    }

    private File[] getColorities(String folder){
        File dir = new File(getActivity().getFilesDir(), folder);
        File[] files = dir.listFiles();
        return files;
    }

    public static class ImageAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private File[] files;

        ImageAdapter(Context context, File[] files) {
            inflater = LayoutInflater.from(context);
            this.files = files;
        }

        @Override
        public int getCount() {
            return files.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.item_grid_image, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.image);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            Glide
                    .with(parent.getContext())
                    .load(files[position])
                    .centerCrop()
                    .crossFade()
                    .into(holder.imageView);

            return view;
        }
    }

    static class ViewHolder {
        ImageView imageView;
    }

}
