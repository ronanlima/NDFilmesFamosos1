package com.udacity.ronanlima.ndfilmesfamosos1.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.udacity.ronanlima.ndfilmesfamosos1.MovieListViewModel;
import com.udacity.ronanlima.ndfilmesfamosos1.R;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.Movie;
import com.udacity.ronanlima.ndfilmesfamosos1.data.AppDataBase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rlima on 05/08/18.
 */

public class FragmentFavoriteMovie extends BaseMovieFragment {
    private AppDataBase mDB;
    private MovieListViewModel movieListViewModel;
    @BindView(R.id.ll_no_favorites)
    LinearLayout llNoFavorites;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_movies, container, false);
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
        if (movies != null && movies.isEmpty()) {
            llNoFavorites.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            llNoFavorites.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public AppDataBase getmDB() {
        return mDB;
    }

    public void setmDB(AppDataBase mDB) {
        this.mDB = mDB;
    }
}
