package com.zonkey.hotmovies;

import android.content.Intent;
import android.content.SharedPreferences;
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


public class MoviePosterFragment extends Fragment {

    //The correct urls for popular and highest rated
    private final String POPULARITY_URL = "http://api.themoviedb.org/3/movie/popular?api_key=cc19772c03a449027eaa0cb6559f304a";
    private final String HIGHEST_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=cc19772c03a449027eaa0cb6559f304a";


    private GridView moviePosterGridView;


    public MoviePosterFragment() {
        // Required empty public constructor
    }


    @Override public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //ensures that a menu is happening in this fragment or activity
        setHasOptionsMenu(true);

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
        switch (id) {
            case R.id.action_highest_rated:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                new FetchMoviesTask().execute(HIGHEST_RATED_URL);
                return true;
            case R.id.action_popular:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                new FetchMoviesTask().execute(POPULARITY_URL);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the view from the specified fragment layout
        View rootView = inflater.inflate(R.layout.fragment_movie_poster_main, container, false);
        //delare the GridView contained in the fragment_movie_poster_main layout
        moviePosterGridView = (GridView) rootView.findViewById(R.id.movie_gridview);


        moviePosterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent detailIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                startActivity(detailIntent);


                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        declares and retrieves the sort key from shared preferences

        String sortBy = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));
         

        if (sortBy == null && sortBy.isEmpty()){
            sortBy = POPULARITY_URL;
        }else{
            sortBy = HIGHEST_RATED_URL;
        }
        movieTask.execute(sortBy); //gets the sort option from shared preferences

        Log.v(LOG_TAG, "SAVED URL " + sortBy);

    }


    //This ensures the movies refresh when this fragment is started
    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


        /**
         * @param movieInfoJsonStr
         * @return
         * @throws JSONException
         */
        private List<Movie> getMovieDataFromJson(String movieInfoJsonStr)
                throws JSONException {


            //declaring the json object that we will call all the info we need from
            JSONObject jsonMovieInfo = new JSONObject(movieInfoJsonStr);
            //"results" is the child of the root json object ("results" is an array inside the Json object (as noted by [])
            JSONArray resultsArray = jsonMovieInfo.getJSONArray(MovieTags.RESULTS);

            //we need to go through each object within the resultsArray and get each value that we need for things we need, poster, description, title

            //iterate over the JsonArray creating a movie object for each Json object in the Array
            List<Movie> movieList = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                // for every object in this array we need to create a movie object
                JSONObject movieObject = resultsArray.getJSONObject(i);
                Movie movie = new Movie(
                        movieObject.getString(MovieTags.POSTER),
                        movieObject.getString(MovieTags.TITLE),
                        movieObject.getString(MovieTags.OVERVIEW));

                //this adds the big ol object we just created
                movieList.add(movie);
            }
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
                // Construct the URL for the movieDb query
                // Possible parameters are available at MovieDB's API page, at
                // http://docs.themoviedb.apiary.io/#


                //this is where the URL is created
                URL url = new URL(params[0]);

                //this is for logging it
                Log.v(LOG_TAG, "Built URI " + params[0]);

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
                // If the code didn't successfully get the weather data, there's no point in attemping
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
                moviePosterGridView.setAdapter(new MovieImageAdapter(getContext(), movies));
            }else{
                Toast.makeText(getActivity(), "you ain't got no movies", Toast.LENGTH_SHORT).show();
            }

        }
    }

}

