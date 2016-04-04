package com.zonkey.hotmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MovieImageAdapter extends BaseAdapter {


    private Context mContext;
    ImageView posterImageView;


    public MovieImageAdapter(Context movieContext) {
        mContext = movieContext;
    }

    public int getCount() {
        return mMovieThumbIds.length;
    }

    public Object getItem(int moviePosterPosition) {
        return null;
    }

    public long getItemId(int moviePosterPosition) {
        return 0;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int moviePosterPosition, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            posterImageView = new ImageView(mContext);
            posterImageView.setAdjustViewBounds(true);
//            imageView.setLayoutParams(new GridView.LayoutParams(170, 170));
            posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);
        } else {
            posterImageView = (ImageView) convertView;

        }
        posterImageView.setImageResource(mMovieThumbIds[moviePosterPosition]);

        return posterImageView;
    }


    // references to our images
    private Integer[] mMovieThumbIds = {
            R.drawable.sample1, R.drawable.sample2, R.drawable.sample8,
            R.drawable.sample2, R.drawable.sample4, R.drawable.sample7,
            R.drawable.sample3, R.drawable.sample8, R.drawable.sample6,
            R.drawable.sample4, R.drawable.sample6, R.drawable.sample5,
            R.drawable.sample5, R.drawable.sample7, R.drawable.sample4,
            R.drawable.sample6, R.drawable.sample3, R.drawable.sample3,
            R.drawable.sample7, R.drawable.sample5, R.drawable.sample2,
            R.drawable.sample8, R.drawable.sample1, R.drawable.sample1,


    };
}
