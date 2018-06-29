package com.udacity.ronanlima.ndfilmesfamosos1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.JsonObject;
import com.udacity.ronanlima.ndfilmesfamosos1.service.TheMovieDBConsumer;
import com.udacity.ronanlima.ndfilmesfamosos1.view.MovieAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements TheMovieDBConsumer.ListenerResultSearchTMDB {
    @BindView(R.id.rv_movies) RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);
        getMostPopularMovies();
    }

    private void getMostPopularMovies() {
        TheMovieDBConsumer.getTopRatedMovies(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_top_rated:
                break;
            case R.id.action_most_popular:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchSuccess(JsonObject result) {
        movieAdapter.getListMovies().addAll(new ArrayList<String>());
    }

    @Override
    public void onSearchError(Call<JsonObject> call, Throwable throwable) {

    }
}
