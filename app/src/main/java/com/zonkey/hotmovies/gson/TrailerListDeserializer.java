package com.zonkey.hotmovies.gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.zonkey.hotmovies.models.Trailer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TrailerListDeserializer implements JsonDeserializer<List<Trailer>> {
    @Override
    public List<Trailer> deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {

        JsonArray content = je.getAsJsonObject().getAsJsonArray("results");
        List<Trailer> trailers = new ArrayList<>(content.size());
        for (JsonElement element : content) {
            trailers.add(new Gson().fromJson(element, Trailer.class));
        }
        return trailers;
    }
}