package com.udacity.ronanlima.ndfilmesfamosos1.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.ronanlima.ndfilmesfamosos1.R;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.Video;
import com.udacity.ronanlima.ndfilmesfamosos1.bean.VideoList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoVH> {

    private VideoList videos;
    private VideoClickListener listener;

    public VideoAdapter(VideoClickListener listener) {
        this.listener = listener;
    }

    @Override
    public VideoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_video, parent, false);
        return new VideoVH(view);
    }

    @Override
    public void onBindViewHolder(VideoVH holder, int position) {
        Video video = videos.getVideos().get(position);
        holder.tvTitle.setText(video.getName());
    }

    @Override
    public int getItemCount() {
        return videos != null ? videos.getVideos().size() : 0;
    }

    public void setVideos(VideoList videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    public interface VideoClickListener {
        void onClickVideo(Video video);
    }

    class VideoVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_video_name)
        TextView tvTitle;

        public VideoVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            listener.onClickVideo(videos.getVideos().get(position));
        }
    }
}
