package com.udacity.ronanlima.ndfilmesfamosos1;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.ronanlima.ndfilmesfamosos1.bean.Movie;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.ReviewList;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.VideoList;
import com.udacity.ronanlima.ndfilmesfamosos1.service.TheMovieDBConsumer;

public class MovieDetailViewModel extends ViewModel {
    private MutableLiveData<VideoList> liveDataVideos;
    private MutableLiveData<ReviewList> liveDataReviews;
    private MutableLiveData<Movie> liveDataDetail;

    public MovieDetailViewModel initDetail(Integer movieId) {
        if (liveDataDetail == null) {
            liveDataDetail = new MutableLiveData<>();
            TheMovieDBConsumer.getMovieDetail(this, movieId);
        }
        return this;
    }

    public MovieDetailViewModel initSearchReviews(Integer movieId) {
        if (liveDataReviews == null) {
            liveDataReviews = new MutableLiveData<>();
            TheMovieDBConsumer.getMovieReview(null, this, movieId);
        }
        return this;
    }

    public MovieDetailViewModel initSearchVideos(Integer movieId) {
        if (liveDataVideos == null) {
            liveDataVideos = new MutableLiveData<>();
            TheMovieDBConsumer.getVideos(this, movieId);
        }
        return this;
    }

    public MutableLiveData<VideoList> getLiveDataVideos() {
        return liveDataVideos;
    }

    public MutableLiveData<ReviewList> getLiveDataReviews() {
        return liveDataReviews;
    }

    public MutableLiveData<Movie> getLiveDataDetail() {
        return liveDataDetail;
    }

}
