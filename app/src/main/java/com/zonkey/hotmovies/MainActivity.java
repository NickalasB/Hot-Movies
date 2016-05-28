package com.zonkey.hotmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zonkey.hotmovies.models.Movie;

public class MainActivity extends AppCompatActivity implements MoviePosterFragment.OnMovieClickedListener {

    public static final String MOVIE = "movie";

    private static final String MOVIEPOSTERFRAGMENT_TAG = "MPFTAG";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_details_container) != null) {
            //the detail container will only be visible on screens with a smallest width of 600dp
            //if this view is present then the view should be in two-pane
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
    }

    // TODO: 5/26/16 What exactly do I want it to do onResume()?
    @Override
    protected void onResume() {
        super.onResume();
        MoviePosterFragment mpf = (MoviePosterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_movie_posters);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (mTwoPane){
            MovieDetailFragment fragment = new MovieDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(MOVIE, movie);
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container, fragment, MOVIEPOSTERFRAGMENT_TAG)
                    .commit();

        }else{

            Intent detailIntent = new Intent(this, MovieDetailsActivity.class);
            detailIntent.putExtra(MOVIE, movie);
            startActivity(detailIntent);
        }

    }

}
