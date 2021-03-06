package com.zonkey.hotmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nickbradshaw on 4/25/16.
 */
public class Trailer implements Parcelable {

    public String id;
    public String key;
    public String name;
    public String site;
    public String type;

    public Trailer(String key) {
        this.key = key;
    }

    //this  constructs the base URL plus the "poster_path" defined in the API
    public String getTrailerURL() {
        return String.format("https://www.youtube.com/watch?v=%s", key);

    }


    //this constructs the base trailerImage URL plus the key from the youtube trailer
    public String getTrailerImagerURL(){
        return String.format("http://img.youtube.com/vi/" + key + "/0.jpg");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeString(this.type);
    }

    protected Trailer(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
