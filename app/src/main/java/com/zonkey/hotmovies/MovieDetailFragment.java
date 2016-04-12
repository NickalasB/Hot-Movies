package com.zonkey.hotmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class MovieDetailFragment extends Fragment {

    private String movieTitle;
    private String moviePosterURL;
    private String movieSummary;
    private String movieRating;
    private String movieReleaseDate;


    ImageView posterDetailImageView;
    TextView movieTitleTextView;
    TextView movieSummaryTextView;
    TextView movieRatingTextView;
    TextView movieReleaseDateTextView;

    public MovieDetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        getActivity().setTitle("Details");

        getPosterDetailImageView();
        getMovieTitleTextView();
        getMovieSummaryTextView();
        getMovieRatingTextView();
        getMovieReleaseDateTextView();

        return rootView;
    }

    public ImageView getPosterDetailImageView() {
        return posterDetailImageView;
    }

    public TextView getMovieTitleTextView() {
        return movieTitleTextView;
    }

    public TextView getMovieSummaryTextView() {
        return movieSummaryTextView;
    }

    public TextView getMovieRatingTextView() {
        return movieRatingTextView;
    }

    public TextView getMovieReleaseDateTextView() {
        return movieReleaseDateTextView;
    }

}
