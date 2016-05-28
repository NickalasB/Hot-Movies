package com.zonkey.hotmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zonkey.hotmovies.models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    MovieDetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Movie currentMovie = getIntent().getParcelableExtra(MainActivity.MOVIE);
        detailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(R.id.movie_details_container);
        if (detailFragment != null) {
            detailFragment.setMovie(currentMovie);
        } else {
            Toast.makeText(MovieDetailsActivity.this, "Your detail fragment is null DUDE!", Toast.LENGTH_LONG).show();
        }
        setTitle(getString(R.string.details_title));
    }
}
