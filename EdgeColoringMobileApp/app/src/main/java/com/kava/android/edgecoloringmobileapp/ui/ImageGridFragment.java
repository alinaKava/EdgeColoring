package com.kava.android.edgecoloringmobileapp.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kava.android.edgecoloringmobileapp.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageGridFragment extends Fragment {

    public ImageGridFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_grid_view, container, false);
        GridView listView = (GridView) rootView.findViewById(R.id.gridView);

        ((GridView) listView).setAdapter(new ImageAdapter(getActivity(), getColorities()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });
        return rootView;
    }

    protected void startImagePagerActivity(int position) {
        Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
        intent.putExtra("imagePosition", position);
        startActivity(intent);
    }

    private File[] getColorities(){
        File dir = new File(getActivity().getFilesDir(), "defaults");
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
