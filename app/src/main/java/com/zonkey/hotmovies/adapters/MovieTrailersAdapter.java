package com.zonkey.hotmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zonkey.hotmovies.R;
import com.zonkey.hotmovies.models.Trailer;

import java.util.List;

/**
 * Created by nickbradshaw on 5/9/16.
 */
public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.ViewHolder> {

    private Context mContext;
    private List<Trailer> mTrailerList;

    public MovieTrailersAdapter(Context movieContext, List<Trailer> trailerList) {
        this.mContext = movieContext;
        this.mTrailerList = trailerList;

    }

    @Override
    public MovieTrailersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_trailers_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Picasso.with(mContext)
                .load(mTrailerList.get(position).getTrailerImagerURL())
                .into((viewHolder.trailerImageView));
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView trailerImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            trailerImageView = (ImageView) itemView.findViewById(R.id.trailers_imageView);

        }
    }
}
//    @Override
//    public int getCount() {
//        return mTrailerList.size();
//    }
//
//    @Override
//    public long getItemId(int trailerPosition) {
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int trailerPosition) {
//        return mTrailerList.get(trailerPosition);
//    }
//
//
//    @Override
//    public View getView(int movieTrailerPosition, View convertView, ViewGroup parent) {
//        ImageView trailerImageView;
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
//            trailerImageView = new ImageView(mContext);
//            trailerImageView.setAdjustViewBounds(true);
//            trailerImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//        } else {
//            trailerImageView = (ImageView) convertView;
//        }
//        Trailer trailer = mTrailerList.get(movieTrailerPosition);
//        Picasso.with(mContext)
//                .load(trailer.getTrailerImagerURL())
//                .into((trailerImageView));
//
//        return trailerImageView;




