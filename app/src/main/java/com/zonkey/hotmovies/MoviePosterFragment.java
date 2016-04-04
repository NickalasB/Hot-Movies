package com.zonkey.hotmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


public class MoviePosterFragment extends Fragment {

    public MoviePosterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the view from the specified fragment layout
        View rootView = inflater.inflate(R.layout.fragment_movie_poster_main, container, false);
        //delare the GridView contained in the fragment_movie_poster_main layout
        GridView moviePosterGridView = (GridView) rootView.findViewById(R.id.movie_gridview);
        //uses the view to get the context instead of getActivity
        moviePosterGridView.setAdapter(new MovieImageAdapter(rootView.getContext()));
        return rootView;
    }



}
