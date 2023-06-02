package com.example.avocado.ui.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.avocado.api.SingletonRetrofitObject;
import com.example.avocado.R;
import com.example.avocado.databinding.FragmentQuiz3Binding;
import com.example.avocado.db.WordsDatabase;
import com.example.avocado.models.VideoModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Quiz3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Quiz3Fragment extends Fragment {
    private FragmentQuiz3Binding binding;

    // YouTube API Constants
//    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
//    private static final String API_KEY = "AIzaSyC_aignp8RpKZpJczIJrQ3XDYNSaAjaMD8";
//    private YouTubeAPIService apiService;

    YouTubePlayerView youtubePlayerView;
    TextView subtitleTextView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private WordsDatabase db;

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

        youtubePlayerView = binding.youtubePlayerView;
        subtitleTextView = binding.subtitleTextView;
        subtitleTextView.setText("hi");

        db = Room.databaseBuilder(requireContext(), WordsDatabase.class, "words")
                .allowMainThreadQueries()
                .build();

//        Words word = db.getWordsDao().getNthExample(1);
//
//        String searchWord;
//        if (word != null) {//단어가 넣어진 경우 가정
//            searchWord = word.word;
//        } else {//단어가 아무 것도 없는 경우(테스트용)
//            searchWord = "example";
//        }

        // Initialize the YouTubePlayerView
        getLifecycle().addObserver(youtubePlayerView);

//        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                String videoId = "YUNlIhDfEkU";
//                youTubePlayer.loadVideo(videoId, 0);
//            }
//        });

        doYoutubeAPICall();

        return root;
    }

    private void doYoutubeAPICall() {
        SingletonRetrofitObject singletonRetrofitObject = SingletonRetrofitObject.getSingletonRetrofitObject();

        Call<VideoModel> videoModelCall = singletonRetrofitObject.getYoutubeAPI().getVideoDetails(
                "snippet",
                "UCGfUuxBzB8E30XjCjOvji2w",
                "50",
                "video",
                getString(R.string.YoutubeAPIKey)
        );

        videoModelCall.enqueue(new Callback<VideoModel>() {
            @Override
            public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                if (response.isSuccessful()) {
                    VideoModel videoModel = response.body();
                    if (videoModel != null && videoModel.getItems() != null && videoModel.getItems().length > 0) {
                        String videoId = videoModel.getItems()[0].getId().getVideoId();
                        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                youTubePlayer.loadVideo(videoId, 0);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoModel> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch video details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}