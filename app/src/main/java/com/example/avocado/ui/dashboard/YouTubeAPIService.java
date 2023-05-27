package com.example.avocado.ui.dashboard;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YouTubeAPIService {
    @GET("search?part=snippet&type=video&maxResults=10")
    Call<YouTubeVideoSearchResponse> searchVideos(
            @Query("key") String apiKey,
            @Query("q") String searchQuery
    );
}
