package com.zonkey.hotmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zonkey.hotmovies.MovieReviewActivity;
import com.zonkey.hotmovies.R;
import com.zonkey.hotmovies.models.Reviews;

import java.util.List;

/**
 * Created by nickbradshaw on 5/7/16.
 */
public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ReviewViewHolder> {

    private Context mContext;
    List<Reviews> reviews;
    LayoutInflater mLayoutInflater;


    public MovieReviewsAdapter(Context context, List<Reviews> reviews) {
        mLayoutInflater = LayoutInflater.from(context);
        this.reviews = reviews;
        this.mContext = context;

    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView reviewAuthor;
        TextView reviewText;


        public ReviewViewHolder(final View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.detail_reviews_title_textView);
            reviewAuthor = (TextView) itemView.findViewById(R.id.detail_reviews_author_textView);
            reviewText = (TextView) itemView.findViewById(R.id.detail_reviews_textView);

        }

        public void displayReview(final Reviews reviews) {
            itemView.setOnClickListener(new View.OnClickListener() {
                Bundle extras = new Bundle();

                @Override
                public void onClick(View v) {
                    Intent reviewIntent = new Intent(mContext, MovieReviewActivity.class);
                    extras.putString("EXTRA_AUTHOR", reviews.author);
                    extras.putString("EXTRA_CONTENT", reviews.content);
                    reviewIntent.putExtras(extras);
                    mContext.startActivity(reviewIntent);

                }
            });
            reviewAuthor.setText("By: " + reviews.author);

        }
    }


    //this method is called when the custom ViewHolder needs to be initialized.
    // We specify the layout that each item of the RecyclerView should use.
    // This is done by inflating the layout using LayoutInflater,
    // passing the output to the constructor of the custom ViewHolder.
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.movie_review_card, viewGroup, false);
        return new ReviewViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ReviewViewHolder reviewViewHolder, int i) {
        Reviews review = reviews.get(i);
        reviewViewHolder.displayReview(review);
    }


    //This should return the number of items present in the data.
    // As our data is in the form of a List, we only need to call the size method on the List object
    @Override
    public int getItemCount() {
        return reviews.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
