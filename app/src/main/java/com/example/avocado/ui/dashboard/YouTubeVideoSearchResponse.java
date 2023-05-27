package com.example.avocado.ui.dashboard;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YouTubeVideoSearchResponse {
    @SerializedName("items")
    private List<YouTubeVideoItem> items;

    public List<YouTubeVideoItem> getItems() {
        return items;
    }
}
