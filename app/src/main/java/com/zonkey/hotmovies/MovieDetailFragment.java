package com.zonkey.hotmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MovieDetailFragment extends Fragment {

    private String movieTitle;
    private String moviePosterURL;
    private String movieSummary;
    private String movieRating;
    private String movieReleaseDate;

    public MovieDetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        getActivity().setTitle("Details");



        return rootView;
    }

}
