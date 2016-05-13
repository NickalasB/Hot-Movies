package com.zonkey.hotmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zonkey.hotmovies.models.Movie;

public class MovieTrailerActivity extends AppCompatActivity {

    private TextView previewTextView;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

    }




}
