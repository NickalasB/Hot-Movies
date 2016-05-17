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


public class MoviePosterAdapter extends BaseAdapter {


    private Context mContext;
    private List<Movie> mMovieList;


    public MoviePosterAdapter(Context movieContext, List<Movie> movieList) {
        mContext = movieContext;
        mMovieList = new ArrayList<>(movieList);
    }

    public int getCount() {
        return mMovieList.size();
    }

    public Movie getItem(int moviePosterPosition) {


        return mMovieList.get(moviePosterPosition);
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
            posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

         } else {
            posterImageView = (ImageView) convertView;
        }



        Movie movie = mMovieList.get(moviePosterPosition);
        Picasso.with(mContext)
                .load(movie.getPosterURL())
                .into(posterImageView);

        return posterImageView;
    }


}

