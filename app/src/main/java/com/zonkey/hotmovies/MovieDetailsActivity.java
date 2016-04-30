package com.zonkey.hotmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zonkey.hotmovies.models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Movie currentMovie = getIntent().getParcelableExtra(MoviePosterFragment.MOVIE);
        MovieDetailFragment detailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(R.id.movie_details_fragment);
        detailFragment.setMovie(currentMovie);
        setTitle(getString(R.string.details_title));




    }


    public void launchPreview(View view) {
            Intent previewIntent = new Intent(this, MovieTrailerActivity.class);
            startActivity(previewIntent);
        }
}
