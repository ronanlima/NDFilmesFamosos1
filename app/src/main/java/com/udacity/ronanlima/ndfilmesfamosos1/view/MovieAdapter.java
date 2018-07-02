package com.udacity.ronanlima.ndfilmesfamosos1.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.ronanlima.ndfilmesfamosos1.BuildConfig;
import com.udacity.ronanlima.ndfilmesfamosos1.R;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.TheMovieDB;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<TheMovieDB> listMovies;

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutResId = R.layout.adapter_movie;
        View v = LayoutInflater.from(context).inflate(layoutResId, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        TheMovieDB theMovieDB = listMovies.get(position);
        holder.getTvMovieName().setText(theMovieDB.getTitle());
        Picasso.get().load(String.format("%s%s", BuildConfig.BASE_URL_TMDB, theMovieDB.getPoster_path().substring(1))).into(holder.getIvMovie());
    }

    @Override
    public int getItemCount() {
        return listMovies != null ? listMovies.size() : 0;
    }

    public List<TheMovieDB> getListMovies() {
        return listMovies;
    }

    public void setListMovies(List<TheMovieDB> listMovies) {
        this.listMovies = listMovies;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_movie)
        ImageView ivMovie;
        @BindView(R.id.tv_movie_name)
        TextView tvMovieName;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public ImageView getIvMovie() {
            return ivMovie;
        }

        public void setIvMovie(ImageView ivMovie) {
            this.ivMovie = ivMovie;
        }

        public TextView getTvMovieName() {
            return tvMovieName;
        }

        public void setTvMovieName(TextView tvMovieName) {
            this.tvMovieName = tvMovieName;
        }
    }
}
