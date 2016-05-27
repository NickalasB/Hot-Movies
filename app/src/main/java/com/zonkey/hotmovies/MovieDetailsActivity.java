package com.zonkey.hotmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zonkey.hotmovies.models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Movie currentMovie = getIntent().getParcelableExtra(MainActivity.MOVIE);
        MovieDetailFragment detailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(R.id.movie_details_container);
            detailFragment.setMovie(currentMovie);
            setTitle(getString(R.string.details_title));
    }
}

// TODO: 5/26/16 I need to utilize a bundle here

//    Bundle arguments = new Bundle();
//arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());
//        DetailFragment fragment = new DetailFragment();
//        fragment.setArguments(arguments);
//        getSupportFragmentManager().beginTransaction()
//        .add(R.id.weather_detail_container, fragment)
//        .commit();