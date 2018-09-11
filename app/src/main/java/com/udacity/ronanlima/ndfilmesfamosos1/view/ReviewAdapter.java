package com.udacity.ronanlima.ndfilmesfamosos1.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.ronanlima.ndfilmesfamosos1.R;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.Review;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.ReviewList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rlima on 04/08/18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewVH> {
    private ReviewList reviews;

    @Override
    public ReviewVH onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_review, parent, false);
        return new ReviewVH(view);
    }

    @Override
    public void onBindViewHolder(ReviewVH holder, int position) {
        Review review = getReviews().getReviews().get(position);
        holder.tvUserName.setText(review.getAuthor());
        holder.tvReview.setText(review.getContent());
        hidePreviousImage(holder, position);
        hideNextImage(holder, position);
    }

    private void hideNextImage(ReviewVH holder, int position) {
        if (position == getItemCount() - 1) {
            holder.ivNext.setVisibility(View.GONE);
        } else {
            holder.ivNext.setVisibility(View.VISIBLE);
        }
    }

    private void hidePreviousImage(ReviewVH holder, int position) {
        if (position == 0) {
            holder.ivPrev.setVisibility(View.GONE);
        } else {
            holder.ivPrev.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.getReviews().size() : 0;
    }

    public ReviewList getReviews() {
        return reviews;
    }

    public void setReviews(ReviewList reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    class ReviewVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_user_name)
        public TextView tvUserName;
        @BindView(R.id.tv_review)
        public TextView tvReview;
        @BindView(R.id.iv_next)
        ImageView ivNext;
        @BindView(R.id.iv_prev)
        ImageView ivPrev;

        public ReviewVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
