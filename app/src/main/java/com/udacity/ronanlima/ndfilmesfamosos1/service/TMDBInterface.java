package com.udacity.ronanlima.ndfilmesfamosos1.service;

import com.google.gson.JsonObject;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.Movie;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.ReviewList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBInterface {
    @GET("3/movie/{end_path}")
    Call<JsonObject> getMovies(@Path("end_path") String path, @Query("api_key") String apiKey);
//
//    @GET("3/movie/top_rated")
//    Call<JsonObject> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("3/movie/{movie_id}")
    Call<Movie> getMovieDetail(@Path("movie_id") Integer movieId, @Query("api_key") String apiKey);

    @GET("3/movie/{movie_id}/reviews")
    Call<ReviewList> getMovieReviews(@Path("movie_id") Integer movieId, @Query("api_key") String apiKey);
}
