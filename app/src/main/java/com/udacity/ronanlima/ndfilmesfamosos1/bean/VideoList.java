package com.udacity.ronanlima.ndfilmesfamosos1.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoList {

    @SerializedName("id")
    private long id;

    @SerializedName("results")
    private List<Video> videos;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
