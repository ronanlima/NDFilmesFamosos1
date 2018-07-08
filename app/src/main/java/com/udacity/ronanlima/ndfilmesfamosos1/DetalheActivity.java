package com.udacity.ronanlima.ndfilmesfamosos1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.udacity.ronanlima.ndfilmesfamosos1.service.TheMovieDBConsumer;
import com.udacity.ronanlima.ndfilmesfamosos1.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class DetalheActivity extends AppCompatActivity implements TheMovieDBConsumer.ListenerResultSearchTMDB {

    private Integer idMovie;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tv_average_fab)
    TextView tvAverageFab;
    @BindView(R.id.pb)
    ProgressBar progressBar;
    @BindView(R.id.iv_poster)
    ImageView ivPoster;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_sinopse)
    TextView tvSinopse;
    @BindView(R.id.tv_data_lancamento)
    TextView tvDataLancamento;
    @BindView(R.id.ll_no_wifi)
    LinearLayout linearNoWifi;
    @BindView(R.id.detail_container)
    NestedScrollView layoutMovieDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar.setVisibility(View.VISIBLE);
        if (getIntent().hasExtra("movieId")) {
            collapsingToolbarLayout.setTitle(getIntent().getStringExtra("originalTitle"));
            idMovie = getIntent().getIntExtra("movieId", 0);
            verifyInternetConnection();
        }
    }

    private Bitmap textAsBitmap(String text, float size, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(size);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.CENTER);
        float baseline = -paint.ascent();
        int width = (int) (paint.measureText(text) + 0.0f);
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
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
        layoutMovieDetail.setVisibility(View.VISIBLE);
        if (result.get("poster_path") != null) {
            Picasso.get().load(String.format("%s%s", BuildConfig.BASE_URL_IMG_POSTER, result.get("poster_path").getAsString().substring(1))).into(ivPoster);
        }
//        fab.setImageBitmap(textAsBitmap(result.get("vote_average").getAsString(), 10f, Color.WHITE));
        tvAverageFab.setText(result.get("vote_average").getAsString());
        tvSinopse.setText(result.get("overview").getAsString());
        tvDataLancamento.setText(result.get("release_date").getAsString());
    }

    @Override
    public void onSearchError(Call<JsonObject> call, Throwable throwable) {
        progressBar.setVisibility(View.INVISIBLE);
        layoutMovieDetail.setVisibility(View.VISIBLE);
    }

    private void verifyInternetConnection() {
        if (NetworkUtils.isConnected(this)) {
            linearNoWifi.setVisibility(View.INVISIBLE);
            TheMovieDBConsumer.getInfoAboutMovie(this, idMovie);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            linearNoWifi.setVisibility(View.VISIBLE);
            layoutMovieDetail.setVisibility(View.INVISIBLE);
        }
    }
}
