package com.udacity.ronanlima.ndfilmesfamosos1.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.udacity.ronanlima.ndfilmesfamosos1.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rlima on 04/08/18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewVH> {
    private JsonArray reviews;

    @Override
    public ReviewVH onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_review, parent, false);
        return new ReviewVH(view);
    }

    @Override
    public void onBindViewHolder(ReviewVH holder, int position) {
        JsonElement jsonElement = getReviews().get(position);
        holder.tvUserName.setText(jsonElement.getAsJsonObject().get("author").getAsString());
        holder.tvReview.setText(jsonElement.getAsJsonObject().get("content").getAsString());
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }

    public JsonArray getReviews() {
        return reviews;
    }

    public void setReviews(JsonArray reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    class ReviewVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_user_name)
        public TextView tvUserName;
        @BindView(R.id.tv_review)
        public TextView tvReview;

        public ReviewVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
