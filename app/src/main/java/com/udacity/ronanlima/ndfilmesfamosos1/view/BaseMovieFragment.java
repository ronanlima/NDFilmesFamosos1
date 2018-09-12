package com.udacity.ronanlima.ndfilmesfamosos1.view;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.udacity.ronanlima.ndfilmesfamosos1.MainActivity;
import com.udacity.ronanlima.ndfilmesfamosos1.MovieListViewModel;
import com.udacity.ronanlima.ndfilmesfamosos1.R;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.Movie;
import com.udacity.ronanlima.ndfilmesfamosos1.service.TheMovieDBConsumer;
import com.udacity.ronanlima.ndfilmesfamosos1.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

public class BaseMovieFragment extends Fragment {
    @BindView(R.id.ll_no_wifi)
    LinearLayout linearNoWifi;
    @BindView(R.id.rv_movies)
    RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    @BindView(R.id.pb)
    ProgressBar progressBar;
    private JsonObject jsonMovies;

    protected void init() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), MainActivity.NUMBER_OF_GRID_COLUMNS, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setMovieAdapter(new MovieAdapter());
        movieAdapter.setAdapterClickListener(((MainActivity) getActivity()).createListenerToDetailMovie());
        recyclerView.setAdapter(movieAdapter);
//        verifyInternetConnection(listener, path);
    }

    protected void observeMultableLiveData(MovieListViewModel movieListViewModel) {
        Observer<JsonObject> observer = new Observer<JsonObject>() {
            @Override
            public void onChanged(@Nullable JsonObject result) {
                showMovies(result);
            }
        };
        movieListViewModel.getMovieLiveData().observe(this, observer);
    }

    protected void showMovies(JsonObject result) {
        showLayoutNoConnectivity(View.INVISIBLE);
        JsonArray results = result.getAsJsonArray("results");
        Iterator<JsonElement> iterator = results.iterator();
        List<Movie> list = new ArrayList<>();
        while (iterator.hasNext()) {
            JsonElement json = iterator.next();
            Movie tmdb = new Gson().fromJson(json, new TypeToken<Movie>() {
            }.getType());
            list.add(tmdb);
        }

        getMovieAdapter().setListMovies(list);
    }

    protected void verifyInternetConnection(TheMovieDBConsumer.ListenerResultSearchTMDB listener, MovieListViewModel movieListViewModel, String path) {
        if (NetworkUtils.isConnected(getActivity())) {
            TheMovieDBConsumer.getMovies(listener, movieListViewModel, path);
        } else {
            showLayoutNoConnectivity(View.VISIBLE);
        }
    }

    protected void showLayoutNoConnectivity(int visible) {
        progressBar.setVisibility(View.INVISIBLE);
        linearNoWifi.setVisibility(visible);
    }

    public MovieAdapter getMovieAdapter() {
        return movieAdapter;
    }

    public void setMovieAdapter(MovieAdapter movieAdapter) {
        this.movieAdapter = movieAdapter;
    }
}
