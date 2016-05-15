package com.zonkey.hotmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MovieReviewActivity extends AppCompatActivity {

    TextView movieReviewAuthorTitleTextView;
    TextView movieReviewAuthorTextView;
    TextView movieReviewsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);
        setTitle("Reviews");

        movieReviewAuthorTitleTextView = (TextView) findViewById(R.id.detail_reviews_title_textView);
        movieReviewAuthorTextView = (TextView) findViewById(R.id.detail_reviews_author_textView);
        movieReviewsTextView = (TextView)findViewById(R.id.detail_reviews_textView);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String author_string = extras.getString("EXTRA_AUTHOR");
        String content_string = extras.getString("EXTRA_CONTENT");

        movieReviewAuthorTextView.setText("Review by: " + author_string);
        movieReviewsTextView.setText(content_string);


    }


}
