package com.udacity.ronanlima.ndfilmesfamosos1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.udacity.ronanlima.ndfilmesfamosos1.data.AppDataBase;
import com.udacity.ronanlima.ndfilmesfamosos1.data.MovieEntry;

import java.util.List;

/**
 * Created by rlima on 05/08/18.
 */

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<MovieEntry>> movieLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDataBase mDB = AppDataBase.getInstance(application);
        movieLiveData = mDB.movieDAO().getAllMovies();
    }

    public LiveData<List<MovieEntry>> getMovieLiveData() {
        return movieLiveData;
    }
}
