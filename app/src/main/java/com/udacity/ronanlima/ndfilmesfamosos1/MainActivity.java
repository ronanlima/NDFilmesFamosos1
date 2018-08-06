package com.udacity.ronanlima.ndfilmesfamosos1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.gson.JsonObject;
import com.udacity.ronanlima.ndfilmesfamosos1.data.AppDataBase;
import com.udacity.ronanlima.ndfilmesfamosos1.view.FragmentPopularMovie;
import com.udacity.ronanlima.ndfilmesfamosos1.view.FragmentTopRatedMovie;
import com.udacity.ronanlima.ndfilmesfamosos1.view.MovieAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity /**implements TheMovieDBConsumer.ListenerResultSearchTMDB */
{
    public static final Integer COD_PERMISSION_NETWORK = 1;
    public static final int NUMBER_OF_GRID_COLUMNS = 2;
    public static final String PATH_POPULAR_MOVIE = "popular";
    public static final String PATH_TOP_RATED_MOVIE = "top_rated";

    private BottomNavigationView.OnNavigationItemSelectedListener NAVIGATION_ITEM_SELECTED_LISTENER = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_bottom_popular:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.action_bottom_rated:
                    viewPager.setCurrentItem(1);
                    break;

                case R.id.action_bottom_favorite:
                    viewPager.setCurrentItem(2);
                    break;
                default:
                    throw new RuntimeException("Ação desconhecida");
            }
            return true;
        }
    };


    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private AppDataBase mDB;
    private JsonObject jsonPopularMovies;
    private JsonObject jsonTopRatedMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), this));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                bottomNavigationView.setSelectedItemId(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(NAVIGATION_ITEM_SELECTED_LISTENER);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (grantResults.length != 0) {
//            if (PermissionUtils.isPermissaoConcedida(grantResults)) {
//                init();
//            }
//        }
//    }

//    private void init() {
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_GRID_COLUMNS, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        movieAdapter = new MovieAdapter();
//        movieAdapter.setAdapterClickListener(createListenerToDetailMovie());
//        recyclerView.setAdapter(movieAdapter);
//        verifyInternetConnection();
//    }

//    public void verifyInternetConnection() {
//        if (NetworkUtils.isConnected(this)) {
//            TheMovieDBConsumer.getMovies(this, PATH_POPULAR_MOVIE);
//        } else {
////            showLayoutNoConnectivity(View.VISIBLE);
//        }
//    }

//    private void showLayoutNoConnectivity(int visible) {
//        progressBar.setVisibility(View.INVISIBLE);
//        linearNoWifi.setVisibility(visible);
//    }

    public MovieAdapter.AdapterClickListener createListenerToDetailMovie() {
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        movieAdapter.setListMovies(null);
////        progressBar.setVisibility(View.VISIBLE);
//
//        switch (item.getItemId()) {
//            case R.id.action_top_rated:
//                if (NetworkUtils.isConnected(this)) {
//                    TheMovieDBConsumer.getMovies(this, PATH_TOP_RATED_MOVIE);
//                } /**else {
//                    showLayoutNoConnectivity(View.VISIBLE);
//                }*/
//                break;
//            case R.id.action_most_popular:
//                if (NetworkUtils.isConnected(this)) {
//                    TheMovieDBConsumer.getMovies(this, PATH_POPULAR_MOVIE);
//                } /**else {
//                    showLayoutNoConnectivity(View.VISIBLE);
//                }*/
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public void onSearchSuccess(JsonObject result) {
////        showLayoutNoConnectivity(View.INVISIBLE);
//        JsonArray results = result.getAsJsonArray("results");
//        Iterator<JsonElement> iterator = results.iterator();
//        List<TheMovieDB> list = new ArrayList<>();
//        while (iterator.hasNext()) {
//            JsonElement json = iterator.next();
//            TheMovieDB tmdb = new Gson().fromJson(json, new TypeToken<TheMovieDB>() {
//            }.getType());
//            list.add(tmdb);
//            Log.d("TAG", json.toString());
//        }
//
//        movieAdapter.setListMovies(list);
//        setupMovieViewModel();
//    }

//    private void setupMovieViewModel() {
//        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        mainViewModel.getMovieLiveData().observe(this, new Observer<List<TheMovieDB>>() {
//            @Override
//            public void onChanged(@Nullable List<TheMovieDB> movies) {
//                movieAdapter.setListMovies(movies);
//            }
//        });
//    }

//    @Override
//    public void onSearchError(Call<JsonObject> call, Throwable throwable) {
//        showLayoutNoConnectivity(View.INVISIBLE);
//    }
}

class TabLayoutListener extends TabLayout.TabLayoutOnPageChangeListener {

    public TabLayoutListener(TabLayout tabLayout) {
        super(tabLayout);
    }
}

class TabsAdapter extends FragmentStatePagerAdapter {
    private MainActivity activity;

    public TabsAdapter(FragmentManager fm, MainActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FragmentPopularMovie();
        }
        return new FragmentTopRatedMovie();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 2;
    }
}