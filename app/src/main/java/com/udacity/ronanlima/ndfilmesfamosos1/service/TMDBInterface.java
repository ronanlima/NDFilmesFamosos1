package com.udacity.ronanlima.ndfilmesfamosos1.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBInterface {
    @GET("/discover/movie?sort_by=popularity.desc/")
    Call<JsonObject> getMostPopularMovies(@Query("api_key") String apiKey);
}
