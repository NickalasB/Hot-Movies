package com.zonkey.hotmovies.gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.zonkey.hotmovies.models.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MovieListDeserializer implements JsonDeserializer<List<Movie>> {
    @Override
    public List<Movie> deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {

        JsonArray content = je.getAsJsonObject().getAsJsonArray("results");
        List<Movie> movies = new ArrayList<>(content.size());
        for (JsonElement element : content) {
            movies.add(new Gson().fromJson(element, Movie.class));
        }
        return movies;
    }
}