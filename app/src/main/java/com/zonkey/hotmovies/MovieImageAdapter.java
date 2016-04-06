package com.zonkey.hotmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zonkey.hotmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;


public class MovieImageAdapter extends BaseAdapter {


    private Context mContext;
    private List<Movie> mMovieList;


    public MovieImageAdapter(Context movieContext, List<Movie> movieList) {
        mContext = movieContext;
        mMovieList = new ArrayList<>(movieList);
    }

    public int getCount() {
        return mMovieList.size();
    }

    public Object getItem(int moviePosterPosition) {
        return null;
    }

    public long getItemId(int moviePosterPosition) {
        return 0;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int moviePosterPosition, View convertView, ViewGroup parent) {
        ImageView posterImageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            posterImageView = new ImageView(mContext);
            posterImageView.setAdjustViewBounds(true);
//            posterImageView.setLayoutParams(new GridView.LayoutParams(170, 170));
            posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            posterImageView.setPadding(8, 8, 8, 8);
        } else {
            posterImageView = (ImageView) convertView;

        }
        Movie movie = mMovieList.get(moviePosterPosition);
        Picasso.with(mContext).setLoggingEnabled(true);
        Picasso.with(mContext).load(movie.getPosterURL()).into(posterImageView);

        return posterImageView;
    }


}

