package com.example.avocado.ui.dashboard;

import com.google.gson.annotations.SerializedName;

public class YouTubeVideoItem {
    @SerializedName("id")
    private YouTubeVideoId id;

    public YouTubeVideoId getId() {
        return id;
    }
}

class YouTubeVideoId {
    @SerializedName("videoId")
    private String videoId;

    public String getVideoId() {
        return videoId;
    }
}
