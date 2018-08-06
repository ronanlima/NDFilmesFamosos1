package com.udacity.ronanlima.ndfilmesfamosos1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.udacity.ronanlima.ndfilmesfamosos1.data.AppDataBase;
import com.udacity.ronanlima.ndfilmesfamosos1.data.MovieEntry;

import java.util.List;

/**
 * Created by rlima on 05/08/18.
 */

public class MovieListViewModel extends AndroidViewModel {

    private MutableLiveData<JsonObject> movieLiveData;
    private LiveData<List<MovieEntry>> favoriteMoviesLiveData;
    private AppDataBase mDB;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        if (getMovieLiveData() == null) {
            movieLiveData = new MutableLiveData();
        }
        mDB = AppDataBase.getInstance(application);
    }

    public void loadFavoriteMovies() {
        favoriteMoviesLiveData = mDB.movieDAO().getAllMovies();
    }

    public MutableLiveData<JsonObject> getMovieLiveData() {
        return movieLiveData;
    }

    public LiveData<List<MovieEntry>> getFavoriteMoviesLiveData() {
        return favoriteMoviesLiveData;
    }
}
