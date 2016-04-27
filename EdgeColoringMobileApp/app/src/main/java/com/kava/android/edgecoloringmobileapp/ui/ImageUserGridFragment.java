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
import com.kava.android.edgecoloringmobileapp.db.ColoringsDbHelper;
import com.kava.android.edgecoloringmobileapp.model.Coloring;

import java.io.File;
import java.util.List;

/**
 * Created by adminn on 24.04.2016.
 */
public class ImageUserGridFragment extends Fragment {

    private GridView mListView;
    private ColoringsDbHelper dbHelper;

    public ImageUserGridFragment() {
        // Requireda empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_grid_view, container, false);
        mListView = (GridView) rootView.findViewById(R.id.gridView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = new ColoringsDbHelper(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        mListView.setAdapter(new ImageAdapter(getActivity(), getColorings()));
    }

    protected void startImagePagerActivity(int position) {
        Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
        intent.putExtra("path", getColorings().get(position).getPath());
        intent.putExtra("isDefault", false);
        startActivity(intent);
    }

    private List<Coloring> getColorings(){
        return dbHelper.getColorings(ColoringsDbHelper.IS_NOT_DEFAULT);
    }

    public static class ImageAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<Coloring> files;

        ImageAdapter(Context context, List<Coloring> files) {
            inflater = LayoutInflater.from(context);
            this.files = files;
        }

        @Override
        public int getCount() {
            return files.size();
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
                    .load(new File(files.get(position).getPath()))
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
