package com.udacity.ronanlima.ndfilmesfamosos1;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.udacity.ronanlima.ndfilmesfamosos1.service.TheMovieDBConsumer;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class DetalheActivity extends AppCompatActivity implements TheMovieDBConsumer.ListenerResultSearchTMDB {

    private Integer idMovie;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.pb)
    ProgressBar progressBar;
    @BindView(R.id.iv_poster)
    ImageView ivPoster;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle(getTitle());
        progressBar.setVisibility(View.VISIBLE);
        idMovie = getIntent().getIntExtra("movieId", 0);
        TheMovieDBConsumer.getInfoAboutMovie(this, idMovie);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchSuccess(JsonObject result) {
        progressBar.setVisibility(View.INVISIBLE);
        Picasso.get().load(String.format("%s%s", BuildConfig.BASE_URL_IMG_POSTER, result.get("poster_path").getAsString().substring(1))).into(ivPoster);
    }

    @Override
    public void onSearchError(Call<JsonObject> call, Throwable throwable) {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
