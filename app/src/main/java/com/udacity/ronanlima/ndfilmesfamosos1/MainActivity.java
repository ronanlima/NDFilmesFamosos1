package com.udacity.ronanlima.ndfilmesfamosos1;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.udacity.ronanlima.ndfilmesfamosos1.view.FragmentPopularMovie;
import com.udacity.ronanlima.ndfilmesfamosos1.view.FragmentTopRatedMovie;
import com.udacity.ronanlima.ndfilmesfamosos1.view.MovieAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
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
    MovieListViewModel movieListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        movieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), this, movieListViewModel));
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

}

class TabsAdapter extends FragmentStatePagerAdapter {
    private MainActivity activity;
    private MovieListViewModel movieListViewModel;

    public TabsAdapter(FragmentManager fm, MainActivity activity, MovieListViewModel movieListViewModel) {
        super(fm);
        this.activity = activity;
        this.movieListViewModel = movieListViewModel;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentPopularMovie();
            case 1:
                return new FragmentTopRatedMovie();
            default:
                return null;
        }
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