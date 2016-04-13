package com.zonkey.hotmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zonkey.hotmovies.models.Movie;


public class MovieDetailFragment extends Fragment {

    ImageView posterDetailImageView;
    ImageView backdropDetailImageview;
    TextView movieTitleTextView;
    TextView movieSummaryTextView;
    TextView movieRatingTextView;
    TextView movieReleaseDateTextView;
    private Movie movie;

    public MovieDetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        posterDetailImageView = (ImageView) rootView.findViewById(R.id.detail_poster_imageView);
        backdropDetailImageview = (ImageView) rootView.findViewById(R.id.detail_backdrop_imageView);
        movieTitleTextView = (TextView) rootView.findViewById(R.id.detail_movie_title);
        movieSummaryTextView = (TextView) rootView.findViewById(R.id.detail_movie_overview);
        movieRatingTextView = (TextView) rootView.findViewById(R.id.detail_user_rating_text);
        movieReleaseDateTextView = (TextView) rootView.findViewById(R.id.detail_release_date);

        return rootView;
    }


    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public void onStart() {
        super.onStart();
        displayMovie();
    }

    private void displayMovie() {
        if (movie != null) {
            movieTitleTextView.setText(movie.title);
            movieSummaryTextView.setText(movie.overview);
            movieRatingTextView.setText(movie.vote_count);
            movieReleaseDateTextView.setText(movie.release_date);
            Picasso.with(getContext()).load(movie.getPosterURL()).into(posterDetailImageView);


        }
    }
}
