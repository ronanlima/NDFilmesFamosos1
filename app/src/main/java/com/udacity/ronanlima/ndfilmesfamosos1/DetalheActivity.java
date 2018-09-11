package com.udacity.ronanlima.ndfilmesfamosos1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.Movie;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.ReviewList;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.Video;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.VideoList;
import com.udacity.ronanlima.ndfilmesfamosos1.data.AppDataBase;
import com.udacity.ronanlima.ndfilmesfamosos1.service.TheMovieDBConsumer;
import com.udacity.ronanlima.ndfilmesfamosos1.utils.AppExecutor;
import com.udacity.ronanlima.ndfilmesfamosos1.utils.NetworkUtils;
import com.udacity.ronanlima.ndfilmesfamosos1.view.ReviewAdapter;
import com.udacity.ronanlima.ndfilmesfamosos1.view.VideoAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class DetalheActivity extends AppCompatActivity implements TheMovieDBConsumer.ListenerResultSearchTMDB, VideoAdapter.VideoClickListener {

    public static final boolean HAS_FIXED_SIZE = true;
    public static final String BUNDLE_MOVIE = "movie";
    public static final String BUNDLE_MOVIE_PARCELABLE = "MOVIE_PARCELABLE";
    public static final String BUNDLE_JSON_REVIEW = "JSON_REVIEW";
    public static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
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
    @BindView(R.id.rv_reviews)
    RecyclerView recyclerViewReviews;
    @BindView(R.id.rv_videos)
    RecyclerView recyclerViewVideos;
    @BindView(R.id.ll_reviews)
    LinearLayout linearLayoutReviews;
    private ReviewAdapter reviewAdapter;
    private VideoAdapter videoAdapter;
    private JsonObject jsonReview;
    private AppDataBase mDB;
    private Movie movie;
    private Movie movieDetail;
    private MenuItem menuItem;
    private Palette.Swatch mVibrantSwatch;
    private MovieDetailViewModel movieDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);
        ButterKnife.bind(this);
        movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar.setVisibility(View.VISIBLE);

        if (savedInstanceState != null) {
            initScreenFromSavedInstance(savedInstanceState);
        } else if (getIntent().hasExtra(BUNDLE_MOVIE)) {
            movie = getIntent().getParcelableExtra(BUNDLE_MOVIE);
            if (!movie.isFavorited()) {
                verifyInternetConnection();
            } else {
                initScreenJsonMovie(movie);
                linearLayoutReviews.setVisibility(View.GONE);
//                initScreenJsonReview(movieDetailViewModel.getLiveDataReviews().getValue());
            }
        }
        collapsingToolbarLayout.setTitle(movie.getOriginalTitle());
        setmDB(AppDataBase.getInstance(this));
    }

    private void retrieveMovieFromDB(Integer idMovie) {
        LiveData<Movie> movie = getmDB().movieDAO().findMovieById(idMovie);
        movie.observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    DetalheActivity.this.movie.setFavorited(true);
                    changeIconFavorite(true);
                }
            }
        });
    }

    private void initScreenFromSavedInstance(Bundle savedInstanceState) {
        if (savedInstanceState.getParcelable(BUNDLE_MOVIE_PARCELABLE) != null) {
            movieDetail = savedInstanceState.getParcelable(BUNDLE_MOVIE_PARCELABLE);
            initScreenJsonMovie(movieDetail);
        }
        if (savedInstanceState.getString(BUNDLE_JSON_REVIEW) != null) {
            jsonReview = (JsonObject) new JsonParser().parse(savedInstanceState.getString(BUNDLE_JSON_REVIEW));
            initScreenJsonReview(movieDetailViewModel.getLiveDataReviews().getValue());
        }
        if (savedInstanceState.getString(BUNDLE_MOVIE_PARCELABLE) == null || savedInstanceState.getString(BUNDLE_JSON_REVIEW) == null) {
            verifyInternetConnection();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        menuItem = menu.findItem(R.id.action_favorite_movie);
        retrieveMovieFromDB(movie.getId());
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_MOVIE, movie);
        if (movieDetail != null) {
            outState.putParcelable(BUNDLE_MOVIE_PARCELABLE, movieDetail);
        }
        if (jsonReview != null) {
            outState.putString(BUNDLE_JSON_REVIEW, jsonReview.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_favorite_movie:
                AppExecutor.getInstance().getDbIo().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (movie.isFavorited()) {
                            getmDB().movieDAO().delete(movie);
                            changeIconFavorite(false);
                            movie.setFavorited(false);
                        } else {
                            getmDB().movieDAO().insert(movie);
                            changeIconFavorite(true);
                            movie.setFavorited(true);
                        }
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeIconFavorite(final boolean isFavorite) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isFavoriteMovie(isFavorite);
            }
        });
    }

    private void isFavoriteMovie(boolean isFavorite) {
        if (menuItem != null) {
            menuItem.setIcon(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
            if (mVibrantSwatch != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                menuItem.getIcon().setTint(mVibrantSwatch.getTitleTextColor());
            }
        }
    }

    private void initScreenVideos(VideoList videoList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVideos.setLayoutManager(layoutManager);
        recyclerViewVideos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVideos.setHasFixedSize(HAS_FIXED_SIZE);
        videoAdapter = new VideoAdapter(this);
        recyclerViewVideos.setAdapter(videoAdapter);
        videoAdapter.setVideos(videoList);
    }

    private void initScreenJsonReview(ReviewList result) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewReviews.setLayoutManager(layoutManager);
        recyclerViewReviews.setItemAnimator(new DefaultItemAnimator());
        recyclerViewReviews.setHasFixedSize(HAS_FIXED_SIZE);
        reviewAdapter = new ReviewAdapter();
        recyclerViewReviews.setAdapter(reviewAdapter);
        reviewAdapter.setReviews(result);
    }

    private void initScreenJsonMovie(Movie result) {
        progressBar.setVisibility(View.INVISIBLE);
        layoutMovieDetail.setVisibility(View.VISIBLE);
        if (result.getPoster() != null) {
            final RequestCreator creator = Picasso.get().load(String.format("%s%s", BuildConfig.BASE_URL_IMG_POSTER, result.getPoster().substring(1)));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap bitmap = creator.get();
                        setToolbarColor(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            creator.into(ivPoster);
        }
        tvAverageFab.setText(String.valueOf(result.getVoteAverage()));
        tvSinopse.setText(result.getOverview());
        tvDataLancamento.setText(new SimpleDateFormat("yyyy-MM-dd").format(result.getReleaseDate()));
    }

    private void setToolbarColor(Bitmap bitmap) {
        Palette p = createPaletteSync(bitmap);
        final Palette.Swatch vibrantSwatch = p.getVibrantSwatch();
        mVibrantSwatch = vibrantSwatch;
        if (vibrantSwatch != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    collapsingToolbarLayout.setBackgroundColor(vibrantSwatch.getRgb());
                    collapsingToolbarLayout.setCollapsedTitleTextColor(vibrantSwatch.getTitleTextColor());
                    collapsingToolbarLayout.setExpandedTitleColor(vibrantSwatch.getTitleTextColor());
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                        menuItem.getIcon().setTint(vibrantSwatch.getTitleTextColor());
                    }
                }
            });

        }
    }

    private Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }

    @Override
    public void onSearchError(Call<JsonObject> call, Throwable throwable) {
        progressBar.setVisibility(View.INVISIBLE);
        layoutMovieDetail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchSuccess(JsonObject result) {

    }

    private void verifyInternetConnection() {
        if (NetworkUtils.isConnected(this)) {
            linearNoWifi.setVisibility(View.INVISIBLE);
            MovieDetailViewModel mDetailViewModel = movieDetailViewModel.initDetail(movie.getId());
            mDetailViewModel.getLiveDataDetail().observe(this, new Observer<Movie>() {
                @Override
                public void onChanged(@Nullable Movie movie) {
                    initScreenJsonMovie(movie);
                }
            });
            mDetailViewModel.initSearchReviews(movie.getId());
            mDetailViewModel.getLiveDataReviews().observe(this, new Observer<ReviewList>() {
                @Override
                public void onChanged(@Nullable ReviewList reviewList) {
                    initScreenJsonReview(reviewList);
                }
            });
            mDetailViewModel.initSearchVideos(movie.getId());
            mDetailViewModel.getLiveDataVideos().observe(this, new Observer<VideoList>() {
                @Override
                public void onChanged(@Nullable VideoList videoList) {
                    initScreenVideos(videoList);
                }
            });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            linearNoWifi.setVisibility(View.VISIBLE);
            layoutMovieDetail.setVisibility(View.INVISIBLE);
        }
    }

    public AppDataBase getmDB() {
        return mDB;
    }

    public void setmDB(AppDataBase mDB) {
        this.mDB = mDB;
    }

    @Override
    public void onClickVideo(Video video) {
        String urlVideo = String.format("%s%s", YOUTUBE_URL, video.getKey());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo));
        Intent chooser = Intent.createChooser(intent, "Abrir com");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
}
