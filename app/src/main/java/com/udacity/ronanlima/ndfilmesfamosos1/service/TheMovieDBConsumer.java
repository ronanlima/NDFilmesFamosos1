package com.udacity.ronanlima.ndfilmesfamosos1.service;

import com.google.gson.JsonObject;
import com.udacity.ronanlima.ndfilmesfamosos1.BuildConfig;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TheMovieDBConsumer {
    private static RetrofitServiceSingleton serviceSingleton = RetrofitServiceSingleton.getInstance();

    public static void getMovies(final ListenerResultSearchTMDB listener, String path) {
        serviceSingleton.getRetrofit().create(TMDBInterface.class).getMovies(path, BuildConfig.API_KEY).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSearchSuccess(response.body());
                } else {
                    listener.onSearchError(call, new Throwable());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                listener.onSearchError(call, t);
            }
        });
    }

    public static void getMovieDetail(final ListenerResultSearchTMDB listener, Integer id) {
        serviceSingleton.getRetrofit().create(TMDBInterface.class).getMovieDetail(id, BuildConfig.API_KEY).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSearchSuccess(response.body());
                } else {
                    listener.onSearchError(call, new Throwable());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                listener.onSearchError(call, t);
            }
        });
    }

    public static void getMovieReview(final ListenerResultSearchTMDB listener, Integer id) {
        serviceSingleton.getRetrofit().create(TMDBInterface.class).getMovieReviews(id, BuildConfig.API_KEY).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSearchSuccess(response.body());
                } else {
                    listener.onSearchError(call, new Throwable());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                listener.onSearchError(call, t);
            }
        });
    }

    public interface ListenerResultSearchTMDB extends Serializable {
        void onSearchSuccess(JsonObject result);

        void onSearchError(Call<JsonObject> call, Throwable throwable);
    }
}
