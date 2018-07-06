package com.udacity.ronanlima.ndfilmesfamosos1.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBInterface {
    @GET("3/movie/popular")
    Call<JsonObject> getMostPopularMovies(@Query("api_key") String apiKey);

    @GET("3/movie/top_rated")
    Call<JsonObject> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("3/movie/{movie_id}")
    Call<JsonObject> getInfoAboutMovie(@Path("movie_id") Integer movieId, @Query("api_key") String apiKey);
}
