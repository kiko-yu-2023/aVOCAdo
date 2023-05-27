package com.example.avocado.ui.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.avocado.databinding.FragmentQuiz3Binding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Quiz3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Quiz3Fragment extends Fragment {

    private FragmentQuiz3Binding binding;

    // YouTube API Constants
    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    private static final String API_KEY = "AIzaSyAGQLLRl7jBZ7growgKvM0eW8TYZWBgE5o";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Quiz3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Quiz3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Quiz3Fragment newInstance(String param1, String param2) {
        Quiz3Fragment fragment = new Quiz3Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();

        binding = FragmentQuiz3Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        YouTubePlayerView youTubePlayerView = binding.youtubeplayerId;

        // Example search word
        String searchWord = "hi everyone";

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create YouTube API service
        YouTubeAPIService apiService = retrofit.create(YouTubeAPIService.class);

        // Execute the API request to search for videos
        Call<YouTubeVideoSearchResponse> call = apiService.searchVideos(API_KEY, searchWord);
        call.enqueue(new Callback<YouTubeVideoSearchResponse>() {
            @Override
            public void onResponse(Call<YouTubeVideoSearchResponse> call, Response<YouTubeVideoSearchResponse> response) {
                if (response.isSuccessful()) {
                    YouTubeVideoSearchResponse videoSearchResponse = response.body();
                    List<YouTubeVideoItem> videoItems = videoSearchResponse.getItems();
                    if (videoItems != null && !videoItems.isEmpty()) {
                        // Retrieve the first video from the search results
                        YouTubeVideoItem firstVideo = videoItems.get(0);
                        String videoId = firstVideo.getId().getVideoId();

                        // Load and play the video in the YouTubePlayerView
                        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(YouTubePlayer youTubePlayer) {
                                youTubePlayer.loadVideo(videoId, 0);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<YouTubeVideoSearchResponse> call, Throwable t) {
                // Handle failure
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}