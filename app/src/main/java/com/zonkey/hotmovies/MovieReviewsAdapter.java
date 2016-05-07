package com.zonkey.hotmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zonkey.hotmovies.models.Reviews;

import java.util.List;

/**
 * Created by nickbradshaw on 5/7/16.
 */
public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ReviewViewHolder> {

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView titleView;
        TextView reviewAuthor;
        TextView reviewText;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView)itemView.findViewById(R.id.detail_reviews_title_textView);
            reviewAuthor = (TextView)itemView.findViewById(R.id.detail_reviews_author_textView);
            reviewText = (TextView)itemView.findViewById(R.id.detail_reviews_textView);
        }
    }

    List<Reviews> reviews;
    LayoutInflater mLayoutInflater;
    MovieReviewsAdapter(Context context, List<Reviews> reviews){
        mLayoutInflater = LayoutInflater.from(context);
        this.reviews = reviews;
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
    public void onBindViewHolder(ReviewViewHolder reviewViewHolder, int i) {
        Reviews review = reviews.get(i);
        reviewViewHolder.reviewAuthor.setText(review.author);
        reviewViewHolder.reviewText.setText(review.content);

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
