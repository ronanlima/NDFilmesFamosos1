package com.udacity.ronanlima.ndfilmesfamosos1.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.udacity.ronanlima.ndfilmesfamosos1.MainActivity;
import com.udacity.ronanlima.ndfilmesfamosos1.MovieListViewModel;
import com.udacity.ronanlima.ndfilmesfamosos1.R;
import com.udacity.ronanlima.ndfilmesfamosos1.data.AppDataBase;
import com.udacity.ronanlima.ndfilmesfamosos1.service.TheMovieDBConsumer;

import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by rlima on 05/08/18.
 */

public class FragmentPopularMovie extends BaseMovieFragment implements TheMovieDBConsumer.ListenerResultSearchTMDB {

    private MovieListViewModel movieListViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        ButterKnife.bind(this, view);
        movieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        progressBar.setVisibility(View.VISIBLE);
        init();
        verifyInternetConnection(this, movieListViewModel, MainActivity.PATH_POPULAR_MOVIE);
        observeMultableLiveData(movieListViewModel);
        return view;
    }

    @Override
    public void onSearchSuccess(JsonObject result) {
        observeMultableLiveData(movieListViewModel);
    }

    @Override
    public void onSearchError(Call<JsonObject> call, Throwable throwable) {
        showLayoutNoConnectivity(View.INVISIBLE);
    }
}
