package com.zonkey.hotmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.zonkey.hotmovies.adapters.MovieImageAdapter;
import com.zonkey.hotmovies.models.Movie;
import com.zonkey.hotmovies.retrievers.MovieListRetriever;

import java.util.List;

public class MoviePosterFragment extends Fragment implements MovieListRetriever.MovieListRetrieverCallback {

    public static final String LOG_TAG = "MoviePosterFragment";
    public static final String MOVIE = "movie";

    //Strings and ints for SharedPreferences
    private static String SORT_ORDER = "sort_order";
    private static final int SORT_ORDER_POPULAR = 0;
    private static final int SORT_ORDER_HIGHEST_RATED = 1;

    private GridView moviePosterGridView;
    private MovieImageAdapter mMovieImageAdapter;

    private MovieListRetriever mMovieListRetriever;

    public MoviePosterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieListRetriever = new MovieListRetriever(this);
        //ensures that a menu is happening in this fragment or activity
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_highest_rated:
                item.setChecked(!item.isChecked());
                updateSortOrder(SORT_ORDER_HIGHEST_RATED);
                updateMovies();
                return true;
            case R.id.action_popular:
                item.setChecked(!item.isChecked());
                updateSortOrder(SORT_ORDER_POPULAR);
                updateMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSortOrder(int order) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.edit().putInt(SORT_ORDER, order).apply();
    }

    private int getSortOrder() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return sharedPreferences.getInt(SORT_ORDER, SORT_ORDER_POPULAR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the view from the specified fragment layout
        View rootView = inflater.inflate(R.layout.fragment_movie_poster_main, container, false);
        moviePosterGridView = (GridView) rootView.findViewById(R.id.movie_gridview);
        moviePosterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent detailIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                detailIntent.putExtra(MOVIE, mMovieImageAdapter.getItem(position));
                startActivity(detailIntent);
            }
        });

        return rootView;
    }

    /**
     * this method updates the movies from the task created below
     */
    private void updateMovies() {
        int sortOrder = getSortOrder();
        Log.v(LOG_TAG, "SAVED URL " + sortOrder);
        switch (sortOrder) {
            case SORT_ORDER_POPULAR:
                mMovieListRetriever.getPopularMovies();
                break;
            case SORT_ORDER_HIGHEST_RATED:
                mMovieListRetriever.getHighestRatedMovies();
                break;
        }
    }

    //This ensures the movies refresh when this fragment is started
    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public void onMoviesRetrieved(final List<Movie> movies) {
        if (moviePosterGridView != null) {
            mMovieImageAdapter = new MovieImageAdapter(getContext(), movies);
            moviePosterGridView.setAdapter(mMovieImageAdapter);
        }
    }

    @Override
    public void onError(final String errorMessage) {
        Toast.makeText(getActivity(), getString(R.string.poster_fragment_movies_error), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}

