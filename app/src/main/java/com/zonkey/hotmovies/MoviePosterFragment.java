package com.zonkey.hotmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.zonkey.hotmovies.adapters.MoviePosterAdapter;
import com.zonkey.hotmovies.favorites.FavoritesManager;
import com.zonkey.hotmovies.json.MovieTags;
import com.zonkey.hotmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MoviePosterFragment extends Fragment {

    public static final String MOVIE = "movie";
    //The correct urls for popular and highest rated
    private final String POPULARITY_URL = "http://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.MOVIE_DB_API_KEY;
    private final String HIGHEST_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + BuildConfig.MOVIE_DB_API_KEY;


    //Strings and ints for SharedPreferences
    private static String SORT_ORDER = "sort_order";
    private static final int SORT_ORDER_POPULAR = 0;
    private static final int SORT_ORDER_HIGHEST_RATED = 1;
    private static final int SORT_ORDER_FAVORITE = 2;

    private GridView moviePosterGridView;
    private MoviePosterAdapter mMovieImageAdapter;


    public MoviePosterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ensures that a menu is happening in this fragment or activity
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.app_name);

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
        int id = item.getItemId();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        switch (id) {
            case R.id.action_highest_rated:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                sharedPreferences.edit().putInt(SORT_ORDER, SORT_ORDER_HIGHEST_RATED).apply();
                getActivity().setTitle("Highest Rated");
                updateMovies();
                return true;
            case R.id.action_popular:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                sharedPreferences.edit().putInt(SORT_ORDER, SORT_ORDER_POPULAR).apply();
                getActivity().setTitle("Most Popular");
                updateMovies();
                return true;
            case R.id.action_favorite:
                if (item.isChecked())
                    item.setChecked(false);
                else
                item.setChecked(true);
                sharedPreferences.edit().putInt(SORT_ORDER, SORT_ORDER_FAVORITE).apply();
                getActivity().setTitle("Favorites");
                updateMovies();
            default:
                return super.onOptionsItemSelected(item);
        }
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

        final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


        FetchMoviesTask movieTask = new FetchMoviesTask();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int sortOrder = sharedPreferences.getInt(SORT_ORDER, SORT_ORDER_POPULAR);


        switch (sortOrder) {
            case SORT_ORDER_POPULAR:
                //query popular URL here
                movieTask.execute(POPULARITY_URL); //gets the sort option from shared preferences
                break;
            case SORT_ORDER_HIGHEST_RATED:
                //query top rated URL here
                movieTask.execute(HIGHEST_RATED_URL); //gets the sort option from shared preferences
               break;
            case SORT_ORDER_FAVORITE:
                Set<String> movieIds =  FavoritesManager.getInstance().getFavoriteMovies(getContext());
                String[] args = movieIds.toArray(new String [movieIds.size()]);
                new FetchFavoritesTask().execute(args);

        }

        Log.v(LOG_TAG, "SAVED URL " + sortOrder);

    }

    //This ensures the movies refresh when this fragment is started
    @Override
    public void onStart() {
        super.onStart();
        updateMovies();

    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private List<Movie> getMovieDataFromJson(String movieInfoJsonStr)
                throws JSONException {
            //declaring the json object that we will call all the info we need from
            JSONObject jsonMovieInfo = new JSONObject(movieInfoJsonStr);
            //"results" is the child of the root json object ("results" is an array inside the Json object (as noted by [])
            JSONArray resultsArray = jsonMovieInfo.getJSONArray(MovieTags.RESULTS);

            //we need to go through each object within the resultsArray and get each value that we need for things we need
            //iterate over the JsonArray creating a movie object for each Json object in the Array
            List<Movie> movieList = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                // for every object in this array we need to create a movie object
                JSONObject movieObject = resultsArray.getJSONObject(i);
                Movie movie = new Movie(
                        movieObject.getString(MovieTags.POSTER),
                        movieObject.getString(MovieTags.TITLE),
                        movieObject.getString(MovieTags.OVERVIEW),
                        movieObject.getString(MovieTags.ID),
                        movieObject.getString(MovieTags.VOTE_COUNT),
                        movieObject.getString(MovieTags.VOTE_AVERAGE),
                        movieObject.getString(MovieTags.RELEASE_DATE),
                        movieObject.getString(MovieTags.BACKDROP_PATH));

                //this adds it all up into the big ol object we just created
                movieList.add(movie);
            }
            Log.v(LOG_TAG, "movie Json results:" + movieList);


            return movieList;
        }


        @Override
        protected List<Movie> doInBackground(String... params) {
            //if there's no code there's nothing to lookup. Verify size of Params
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String posterJsonStr = null;

            try {
//
//                final String BASE_URL = "http://api.themoviedb.org/3/movie/";
//                final String API_KEY_PARAM = "api_key";
//
//                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
//                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
//                        .build();

                URL url = new URL(params[0]);


                // Create the request to MovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                posterJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Poster  Json String:" + posterJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieDataFromJson(posterJsonStr);

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();

            }
            // This will only happen if there was an error getting or parsing the forecast.
            return null;

        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (moviePosterGridView != null && movies != null) {
                mMovieImageAdapter = new MoviePosterAdapter(getContext(), movies);
                moviePosterGridView.setAdapter(mMovieImageAdapter);
            } else {
                Toast.makeText(getActivity(), getString(R.string.poster_fragment_movies_error), Toast.LENGTH_SHORT).show();
            }

        }
    }


    public class FetchFavoritesTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchFavoritesTask.class.getSimpleName();

        private Movie getMovieDataFromJson(String movieInfoJsonStr)
                throws JSONException {
            //declaring the json object that we will call all the info we need from
            JSONObject movieObject = new JSONObject(movieInfoJsonStr);

            return new Movie(
                    movieObject.getString(MovieTags.POSTER),
                    movieObject.getString(MovieTags.TITLE),
                    movieObject.getString(MovieTags.OVERVIEW),
                    movieObject.getString(MovieTags.ID),
                    movieObject.getString(MovieTags.VOTE_COUNT),
                    movieObject.getString(MovieTags.VOTE_AVERAGE),
                    movieObject.getString(MovieTags.RELEASE_DATE),
                    movieObject.getString(MovieTags.BACKDROP_PATH));
        }


        @Override
        protected List<Movie> doInBackground(String... params) {
            //if there's no code there's nothing to lookup. Verify size of Params
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String posterJsonStr = null;

            final String BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String API_KEY_PARAM = "api_key";
            List<Movie> movies = new ArrayList<>();

            for (String movieId: params){

                try {


                Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendPath(movieId)
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                        .build();

                    URL url = new URL(builtUri.toString());


                    // Create the request to MovieDB, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();


                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }

                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    posterJsonStr = buffer.toString();

                    Log.v(LOG_TAG, "Poster  Json String:" + posterJsonStr);

                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error ", e);
                    // If the code didn't successfully get the movie data, there's no point in attempting
                    // to parse it.
                    return null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }
                try {
                    movies.add(getMovieDataFromJson(posterJsonStr));

                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();

                }

            }


            return movies;

        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (moviePosterGridView != null && movies != null) {
                mMovieImageAdapter = new MoviePosterAdapter(getContext(), movies);
                moviePosterGridView.setAdapter(mMovieImageAdapter);
            } else {
                Toast.makeText(getActivity(), getString(R.string.favorites_fragment_movies_error), Toast.LENGTH_SHORT).show();
            }

        }
    }



}

