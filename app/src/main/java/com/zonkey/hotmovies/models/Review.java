package com.zonkey.hotmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nickbradshaw on 4/28/16.
 */
public class Review implements Parcelable {

    public String author;
    public String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.content);
    }

    protected Review(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
