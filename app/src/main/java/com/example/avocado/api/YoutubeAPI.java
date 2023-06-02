package com.example.avocado.api;

import com.example.avocado.models.VideoModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeAPI {
    @GET("search")
    Call<VideoModel> getVideoDetails(@Query("part") String part,
                                     @Query("channelId") String channelId,
                                     @Query("maxResults") String maxResults,
                                     @Query("type") String type,
                                     @Query("key") String key);


}

//https://www.googleapis.com/youtube/v3
// /search?part=snippet
// &channelId=UCGfUuxBzB8E30XjCjOvji2w
// &maxResults=10
// &type=video
// &key=AIzaSyC_aignp8RpKZpJczIJrQ3XDYNSaAjaMD8