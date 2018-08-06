package com.udacity.ronanlima.ndfilmesfamosos1.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.ronanlima.ndfilmesfamosos1.MovieListViewModel;
import com.udacity.ronanlima.ndfilmesfamosos1.R;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.Movie;
import com.udacity.ronanlima.ndfilmesfamosos1.data.AppDataBase;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by rlima on 05/08/18.
 */

public class FragmentFavoriteMovie extends BaseMovieFragment {
    private AppDataBase mDB;
    private MovieListViewModel movieListViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        ButterKnife.bind(this, view);
        movieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        progressBar.setVisibility(View.VISIBLE);
        setmDB(AppDataBase.getInstance(getActivity()));
        movieListViewModel.loadFavoriteMovies();
        observeMovieLiveData();
        init();
        return view;
    }

    private void observeMovieLiveData() {
        movieListViewModel.getFavoriteMoviesLiveData().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movieEntries) {
                showFavoriteMovies(movieEntries);
            }
        });
    }

    private void showFavoriteMovies(List<Movie> movies) {
        showLayoutNoConnectivity(View.INVISIBLE);
        getMovieAdapter().setListMovies(movies);
    }

    public AppDataBase getmDB() {
        return mDB;
    }

    public void setmDB(AppDataBase mDB) {
        this.mDB = mDB;
    }
}
