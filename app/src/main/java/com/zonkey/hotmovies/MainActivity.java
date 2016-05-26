package com.zonkey.hotmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

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

            //in pane mode show the detail view in this activity by adding or replacing
            //the detail fragment using a fragment transaction
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_details_container, new MovieDetailFragment(), MOVIEPOSTERFRAGMENT_TAG)
                        .commit();
            }

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

    // TODO: 5/25/16 Figure out how to implement this
//
//    public void onItemSelected(Uri contentUri) {
//        if (mTwoPane) {
//            // In two-pane mode, show the detail view in this activity by
//            // adding or replacing the detail fragment using a
//            // fragment transaction.
//            Bundle args = new Bundle();
//            args.putParcelable(MovieDetailFragment.DETAIL_URI, contentUri);
//
//            MovieDetailFragment fragment = new MovieDetailFragment();
//            fragment.setArguments(args);
//
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.movie_details_container, fragment, MOVIEPOSTERFRAGMENT_TAG)
//                    .commit();
//        } else {
//            Intent intent = new Intent(this, MovieDetailsActivity.class)
//                    .setData(contentUri);
//            startActivity(intent);
//        }
//    }



}
