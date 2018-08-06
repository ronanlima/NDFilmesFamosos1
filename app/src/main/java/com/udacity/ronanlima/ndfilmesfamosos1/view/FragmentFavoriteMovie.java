package com.udacity.ronanlima.ndfilmesfamosos1.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.udacity.ronanlima.ndfilmesfamosos1.R;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.TheMovieDB;
import com.udacity.ronanlima.ndfilmesfamosos1.data.AppDataBase;
import com.udacity.ronanlima.ndfilmesfamosos1.service.TheMovieDBConsumer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by rlima on 05/08/18.
 */

public class FragmentFavoriteMovie extends BaseMovieFragment implements TheMovieDBConsumer.ListenerResultSearchTMDB {
    private AppDataBase mDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        ButterKnife.bind(this, view);
        progressBar.setVisibility(View.VISIBLE);
        setmDB(AppDataBase.getInstance(getActivity()));
        init();
        return view;
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

        getMovieAdapter().setListMovies(list);
//        setupMovieViewModel();
    }

    @Override
    public void onSearchError(Call<JsonObject> call, Throwable throwable) {
        showLayoutNoConnectivity(View.INVISIBLE);
    }

    public AppDataBase getmDB() {
        return mDB;
    }

    public void setmDB(AppDataBase mDB) {
        this.mDB = mDB;
    }
}
