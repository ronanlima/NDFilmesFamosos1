package com.udacity.ronanlima.ndfilmesfamosos1.service;

import com.google.gson.JsonObject;
import com.udacity.ronanlima.ndfilmesfamosos1.BuildConfig;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TheMovieDBConsumer {
    private static RetrofitServiceSingleton retrofit = RetrofitServiceSingleton.getInstance();

    public static void getPopularMovies(final ListenerResultSearchTMDB listener) {
        retrofit.getRetrofit().create(TMDBInterface.class).getMostPopularMovies(BuildConfig.API_KEY).enqueue(new Callback<JsonObject>() {
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

    public static void getTopRatedMovies(final ListenerResultSearchTMDB listener) {
        retrofit.getRetrofit().create(TMDBInterface.class).getTopRatedMovies(BuildConfig.API_KEY).enqueue(new Callback<JsonObject>() {
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

    public static void getInfoAboutMovie(final ListenerResultSearchTMDB listener, Integer id) {
        retrofit.getRetrofit().create(TMDBInterface.class).getInfoAboutMovie(id, BuildConfig.API_KEY).enqueue(new Callback<JsonObject>() {
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
