package com.zonkey.hotmovies.gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.zonkey.hotmovies.models.Review;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReviewListDeserializer implements JsonDeserializer<List<Review>> {
    @Override
    public List<Review> deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {

        JsonArray content = je.getAsJsonObject().getAsJsonArray("results");
        List<Review> reviews = new ArrayList<>(content.size());
        for (JsonElement element : content) {
            reviews.add(new Gson().fromJson(element, Review.class));
        }
        return reviews;
    }
}