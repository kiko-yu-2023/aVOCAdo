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
import com.example.avocado.db.record_with_quizes_and_tests.Quiz;

import java.util.ArrayList;

public class Quiz3Fragment extends QuizDataFragment {
    private FragmentQuiz3Binding binding;

    WebView webView;
    EditText inputWordQuiz3;
    ImageView completeQuiz3;

    private String title;//사전 이름
    private int correctAnswer;//정답 개수
    private ArrayList<Quiz> quiz; //이 퀴즈 저장
    private Word word;
    private boolean isCorrect;

    public Quiz3Fragment(String title, Word word, int correctAnswer, ArrayList<Quiz> quiz) {
        this.title = title;
        this.word = word;
        this.correctAnswer = correctAnswer;
        this.quiz = quiz;
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
                isCorrect = inputWordQuiz3.getText().toString().equals(word.getContent());

                if (isCorrect) {
                    correctAnswer++;
                } else {
                    Toast.makeText(getContext(), "Incorrect answer", Toast.LENGTH_SHORT).show();
                }

                updateQuizList();

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

    private ArrayList<Quiz> updateQuizList(){
        quiz.add(new Quiz(isCorrect,3,
                word.getContent(),inputWordQuiz3.getText().toString(), word.getWordID(),0));

        return quiz;
    }

    private void showVideo() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String javascriptCode = "fetchSearchWord('" + word.getContent() + "');";
                webView.evaluateJavascript(javascriptCode, null);
            }
        });
        webView.loadUrl("file:///android_asset/youglish.html");
    }

//    private void isCorrect(String inputWord) {
//        if(inputWord.equals(word.getContent())){
//            isCorrect = true;
//        }else{
//            isCorrect = false;
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}