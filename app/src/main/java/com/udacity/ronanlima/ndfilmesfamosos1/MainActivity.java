package com.udacity.ronanlima.ndfilmesfamosos1;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.TheMovieDB;
import com.udacity.ronanlima.ndfilmesfamosos1.service.TheMovieDBConsumer;
import com.udacity.ronanlima.ndfilmesfamosos1.utils.NetworkUtils;
import com.udacity.ronanlima.ndfilmesfamosos1.utils.PermissionUtils;
import com.udacity.ronanlima.ndfilmesfamosos1.view.MovieAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements TheMovieDBConsumer.ListenerResultSearchTMDB {
    public static final Integer COD_PERMISSION_NETWORK = 1;
    public static final int NUMBER_OF_GRID_COLUMNS = 2;

    @BindView(R.id.ll_no_wifi)
    LinearLayout linearNoWifi;
    @BindView(R.id.rv_movies)
    RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    @BindView(R.id.pb)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);
        if (PermissionUtils.validate(this, COD_PERMISSION_NETWORK, Manifest.permission.INTERNET)) {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length != 0) {
            if (PermissionUtils.isPermissaoConcedida(grantResults)) {
                init();
            }
        }
    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_GRID_COLUMNS, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        movieAdapter = new MovieAdapter();
        movieAdapter.setAdapterClickListener(createListenerToDetailMovie());
        recyclerView.setAdapter(movieAdapter);
        verifyInternetConnection();
    }

    private void verifyInternetConnection() {
        if (NetworkUtils.isConnected(this)) {
            TheMovieDBConsumer.getPopularMovies(this);
        } else {
            showLayoutNoConnectivity(View.VISIBLE);
        }
    }

    private void showLayoutNoConnectivity(int visible) {
        progressBar.setVisibility(View.INVISIBLE);
        linearNoWifi.setVisibility(visible);
    }

    private MovieAdapter.AdapterClickListener createListenerToDetailMovie() {
        return new MovieAdapter.AdapterClickListener() {
            @Override
            public void onMovieClicked(Integer idMovie, String originalTitle) {
                Intent i = new Intent(getBaseContext(), DetalheActivity.class);
                i.putExtra("movieId", idMovie);
                i.putExtra("originalTitle", originalTitle);
                startActivity(i);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        movieAdapter.setListMovies(null);
        progressBar.setVisibility(View.VISIBLE);

        switch (item.getItemId()) {
            case R.id.action_top_rated:
                if (NetworkUtils.isConnected(this)) {
                    TheMovieDBConsumer.getTopRatedMovies(this);
                } else {
                    showLayoutNoConnectivity(View.VISIBLE);
                }
                break;
            case R.id.action_most_popular:
                if (NetworkUtils.isConnected(this)) {
                    TheMovieDBConsumer.getPopularMovies(this);
                } else {
                    showLayoutNoConnectivity(View.VISIBLE);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
    }

    @Override
    public void onSearchError(Call<JsonObject> call, Throwable throwable) {
        showLayoutNoConnectivity(View.INVISIBLE);
    }
}
