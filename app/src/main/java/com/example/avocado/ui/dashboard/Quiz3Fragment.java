package com.example.avocado.ui.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.avocado.databinding.FragmentQuiz3Binding;
import com.example.avocado.db.AppDatabase;
import java.util.List;

public class Quiz3Fragment extends Fragment {
    private FragmentQuiz3Binding binding;

    WebView webView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AppDatabase db;

    private List<String> videoIds;

    private String searchWord;

    public Quiz3Fragment() {
        // Required empty public constructor
    }

    public static Quiz3Fragment newInstance(String param1, String param2) {
        Quiz3Fragment fragment = new Quiz3Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();

        binding = FragmentQuiz3Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        webView = binding.webview;

//        AppDatabase word = db.getWordsDao().getNthExample(1);
//
//        String searchWord;
//        if (word != null) {//단어가 넣어진 경우 가정
//            searchWord = word.word;
//        } else {//단어가 아무 것도 없는 경우(테스트용)
//            searchWord = "example";
//        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);

        searchWord = "harry potter";

        // Inject the JavaScript code to fetch the search word dynamically
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String javascriptCode = "fetchSearchWord('" + searchWord + "');";
                webView.evaluateJavascript(javascriptCode, null);
            }
        });

        webView.loadUrl("file:///android_asset/youglish.html");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}