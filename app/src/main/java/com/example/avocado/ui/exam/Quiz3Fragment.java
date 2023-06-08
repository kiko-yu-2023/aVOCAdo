package com.example.avocado.ui.exam;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.avocado.databinding.FragmentQuiz3Binding;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;

public class Quiz3Fragment extends QuizDataFragment {
    private FragmentQuiz3Binding binding;

    WebView webView;
    EditText inputWordQuiz3;
    ImageView completeQuiz3;

    private String title;
    private int correctAnswer;
    private String searchWord;

    public Quiz3Fragment(String title, Word word, int correctAnswer) {
        this.title = title;
        searchWord = word.getContent();
        this.correctAnswer = correctAnswer;
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
        inputWordQuiz3 = binding.inputWordQuiz3;
        completeQuiz3 = binding.completeQuiz3;
        completeQuiz3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCorrect(inputWordQuiz3.getText().toString())) {
                    correctAnswer++;
                } else {
                    // Incorrect answer
                    Toast.makeText(getContext(), "Incorrect answer", Toast.LENGTH_SHORT).show();
                }
                ExamFragment parentFragment = (ExamFragment) getParentFragment();
                if (parentFragment != null) {
                    parentFragment.openNextQuizFragment(correctAnswer);
                }
            }
        });

        loadData(title);

        return root;
    }

    protected void handleData(DictWithWords dictWithWords) {
        showVideo();
    }

    private void showVideo() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String javascriptCode = "fetchSearchWord('" + searchWord + "');";
                webView.evaluateJavascript(javascriptCode, null);
            }
        });
        webView.loadUrl("file:///android_asset/youglish.html");
    }

    private boolean isCorrect(String inputWord) {
        if(inputWord.equals(searchWord)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}