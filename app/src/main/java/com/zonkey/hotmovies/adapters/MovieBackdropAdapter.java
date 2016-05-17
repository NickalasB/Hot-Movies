package com.zonkey.hotmovies.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zonkey.hotmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickbradshaw on 5/12/16.
 */
public class MovieBackdropAdapter extends BaseAdapter {

    private Context mContext;
    private List<Movie> mMovieList;

    public MovieBackdropAdapter(Context movieContext, List<Movie> movieList) {
        mContext = movieContext;
        mMovieList = new ArrayList<>(movieList);
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Object getItem(int moviePosterPosition) {
        return mMovieList.get(moviePosterPosition);
    }

    @Override
    public long getItemId(int moviePosterPosition) {
        return 0;
    }

    @Override
    public View getView(int moviePosterPosition, View convertView, ViewGroup parent) {
        ImageView backdropImageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes

            backdropImageView = new ImageView(mContext);
            backdropImageView.setAdjustViewBounds(true);
            backdropImageView.setScaleType(ImageView.ScaleType.CENTER);
        } else {
            backdropImageView = (ImageView) convertView;
        }

        Movie movie = mMovieList.get(moviePosterPosition);
        Picasso.with(mContext).load(movie.getBackdropURL()).into(backdropImageView);

        return backdropImageView;
    }


}
