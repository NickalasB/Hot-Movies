package com.zonkey.hotmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MoviePosterFragment extends Fragment {


    public MoviePosterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the view from the specified fragment layout
        View rootView = inflater.inflate(R.layout.fragment_movie_poster_main, container, false);
        //delare the GridView contained in the fragment_movie_poster_main layout
        GridView moviePosterGridView = (GridView) rootView.findViewById(R.id.movie_gridview);
        //uses the view to get the context instead of getActivity
        moviePosterGridView.setAdapter(new MovieImageAdapter(rootView.getContext()));


        moviePosterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
                updateMovies();
            }
        });

        return rootView;
    }

    /**
     * this method updates the movies from the task created below
     */
    private void updateMovies() {
        FetchMoviesTask movieTask = new FetchMoviesTask();
        movieTask.execute();

    }

    //This ensures the movies refresh when this fragment is started
    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[] getMovieDataFromJson(String movieInfoJsonStr, int numMovies)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String MDB_POSTER = "poster_path";

            //declaring the json objects
            JSONObject jsonMoviePoster = new JSONObject(movieInfoJsonStr);
            JSONArray movieArray = jsonMoviePoster.getJSONArray(MDB_POSTER);

            //creating a string from the json objects
            String[] posterResultsStr = new String[numMovies];
            for (int i = 0; i < movieArray.length(); i++) {
                // For now, using the format just "poster"
                String poster;

                // poster is in a child array
                JSONObject posterObject = jsonMoviePoster.getJSONArray(MDB_POSTER).getJSONObject(0);
                poster = posterObject.getString(MDB_POSTER);

                posterResultsStr[i] = poster;

            }

            for (String s : posterResultsStr) {
            }
            return posterResultsStr;


        }


        @Override
        protected String[] doInBackground(String... params) {
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
            int numMovies = 25;


            //this is the param as defined in the API
            String poster = "poster_path";

            try {
                // Construct the URL for the movieDb query
                // Possible parameters are available at OWM's forecast API page, at
                // http://docs.themoviedb.apiary.io/#
                final String MOVIE_INFO_BASE_URL = "https://api.themoviedb.org/3/movie/";
                final String MOVIE_ID_PARAM = "id";//the unique id of each movie
                final String APPID_PARAM = "?api_key";//my api key
                final String POSTER_PARAM = "poster_path";//the link to the corresponding movie poster


                //This is where the link with the info we seek is actually created
                Uri builtUri = Uri.parse(MOVIE_INFO_BASE_URL).buildUpon()
                        .appendQueryParameter(MOVIE_ID_PARAM, params[0])
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                        .appendQueryParameter(POSTER_PARAM, poster)
                        .build();

                //this is where the URL is created
                URL url = new URL(builtUri.toString());
                //thisi s for logging it
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
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
                return getMovieDataFromJson(posterJsonStr, numMovies);

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();

            }
            // This will only happen if there was an error getting or parsing the forecast.
            return null;

        }


    }
}

