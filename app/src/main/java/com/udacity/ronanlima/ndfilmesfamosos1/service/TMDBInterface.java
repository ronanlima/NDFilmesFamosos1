package com.udacity.ronanlima.ndfilmesfamosos1.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBInterface {
    @GET("3/discover/movie?sort_by=popularity.desc/")
    Call<JsonObject> getMostPopularMovies(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("3/movie/{movie_id}")
    Call<JsonObject> getInfoAboutMovie(@Path("movie_id") Integer movieId, @Query("api_key") String apiKey);
}
