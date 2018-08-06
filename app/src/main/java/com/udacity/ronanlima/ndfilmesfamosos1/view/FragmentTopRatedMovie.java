package com.udacity.ronanlima.ndfilmesfamosos1.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.udacity.ronanlima.ndfilmesfamosos1.MainActivity;
import com.udacity.ronanlima.ndfilmesfamosos1.R;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.TheMovieDB;
import com.udacity.ronanlima.ndfilmesfamosos1.data.AppDataBase;
import com.udacity.ronanlima.ndfilmesfamosos1.service.TheMovieDBConsumer;
import com.udacity.ronanlima.ndfilmesfamosos1.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by rlima on 05/08/18.
 */

public class FragmentTopRatedMovie extends Fragment implements TheMovieDBConsumer.ListenerResultSearchTMDB {
    @BindView(R.id.ll_no_wifi)
    LinearLayout linearNoWifi;
    @BindView(R.id.rv_movies)
    RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    @BindView(R.id.pb)
    ProgressBar progressBar;
    private JsonObject jsonMovies;
    private AppDataBase mDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_rated_movies, container, false);
        ButterKnife.bind(this, view);
        progressBar.setVisibility(View.VISIBLE);
        mDB = AppDataBase.getInstance(getActivity());
        init();
        return view;
    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), MainActivity.NUMBER_OF_GRID_COLUMNS, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        movieAdapter = new MovieAdapter();
        movieAdapter.setAdapterClickListener(((MainActivity) getActivity()).createListenerToDetailMovie());
        recyclerView.setAdapter(movieAdapter);
        verifyInternetConnection();
    }

    public void verifyInternetConnection() {
        if (NetworkUtils.isConnected(getActivity())) {
            TheMovieDBConsumer.getMovies(this, MainActivity.PATH_TOP_RATED_MOVIE);
        } else {
            showLayoutNoConnectivity(View.VISIBLE);
        }
    }

    private void showLayoutNoConnectivity(int visible) {
        progressBar.setVisibility(View.INVISIBLE);
        linearNoWifi.setVisibility(visible);
    }

    @Override
    public void onSearchSuccess(JsonObject result) {
        showLayoutNoConnectivity(View.INVISIBLE);
        JsonArray results = result.getAsJsonArray("results");
        Iterator<JsonElement> iterator = results.iterator();
        List<TheMovieDB> list = new ArrayList<>();
        while (iterator.hasNext()) {
            JsonElement json = iterator.next();
            TheMovieDB tmdb = new Gson().fromJson(json, new TypeToken<TheMovieDB>() {
            }.getType());
            list.add(tmdb);
            Log.d("TAG", json.toString());
        }

        movieAdapter.setListMovies(list);
//        setupMovieViewModel();
    }

    @Override
    public void onSearchError(Call<JsonObject> call, Throwable throwable) {
        showLayoutNoConnectivity(View.INVISIBLE);
    }
}
