package com.zonkey.hotmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zonkey.hotmovies.models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String MOVIE = "movie";
    private static final String MOVIEPOSTERFRAGMENT_TAG = "MPFTAG";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_movie_details);
            Movie currentMovie = getIntent().getParcelableExtra(MainActivity.MOVIE);
            MovieDetailFragment fragment = new MovieDetailFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable(MOVIE, currentMovie);
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container, fragment, MOVIEPOSTERFRAGMENT_TAG)
                    .commit();

            setTitle(getString(R.string.details_title));
        }
    }

