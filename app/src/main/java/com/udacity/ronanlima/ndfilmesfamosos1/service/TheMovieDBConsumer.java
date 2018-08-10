package com.udacity.ronanlima.ndfilmesfamosos1.service;

import android.arch.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.udacity.ronanlima.ndfilmesfamosos1.BuildConfig;
import com.udacity.ronanlima.ndfilmesfamosos1.MovieDetailViewModel;
import com.udacity.ronanlima.ndfilmesfamosos1.MovieListViewModel;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.Movie;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.ReviewList;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TheMovieDBConsumer {
    private static RetrofitServiceSingleton serviceSingleton = RetrofitServiceSingleton.getInstance();

    public static void getMovies(final ListenerResultSearchTMDB listener, final MovieListViewModel movieListViewModel, String path) {
        serviceSingleton.getRetrofit().create(TMDBInterface.class).getMovies(path, BuildConfig.API_KEY).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieListViewModel.getMovieLiveData().postValue(response.body());
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

    public static void getMovieDetail(final MovieDetailViewModel detailViewModel, Integer id) {
        serviceSingleton.getRetrofit().create(TMDBInterface.class).getMovieDetail(id, BuildConfig.API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    detailViewModel.getLiveDataDetail().postValue(response.body());
                } else {
                    detailViewModel.getLiveDataDetail().postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                detailViewModel.getLiveDataDetail().postValue(null);
            }
        });
    }

    public static void getMovieReview(final ListenerResultSearchTMDB listener, final MovieDetailViewModel movieDetailViewModel, Integer id) {
        serviceSingleton.getRetrofit().create(TMDBInterface.class).getMovieReviews(id, BuildConfig.API_KEY).enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                if (response.isSuccessful() && response.body() != null) {
//                    listener.onSearchSuccess(response.body());
                    movieDetailViewModel.getLiveDataReviews().postValue(response.body());

                } else {
//                    listener.onSearchError(call, new Throwable());
                    movieDetailViewModel.getLiveDataReviews().setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
//                listener.onSearchError(call, t);
                movieDetailViewModel.getLiveDataReviews().setValue(null);
            }
        });
    }

    public interface ListenerResultSearchTMDB extends Serializable {
        void onSearchSuccess(JsonObject result);

        void onSearchError(Call<JsonObject> call, Throwable throwable);
    }
}
