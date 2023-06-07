package com.example.avocado.ui.exam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.avocado.databinding.FragmentQuiz3Binding;
import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.dict_with_words.WordRepository;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class Quiz3Fragment extends Fragment {
    private FragmentQuiz3Binding binding;

    WebView webView;
    EditText inputWordQuiz3;
    Button completeQuiz3;

    private AppDatabase db;
    private DictRepository dr;
    private WordRepository wr;
    private String title;
    private String searchWord;

    public Quiz3Fragment(String title, Word word) {
        this.title = title;
        searchWord = word.getContent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //container.removeAllViews();

        binding = FragmentQuiz3Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        webView = binding.webview;
        inputWordQuiz3 = binding.inputWordQuiz3;
        completeQuiz3 = binding.completeQuiz3;
        completeQuiz3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCorrect(inputWordQuiz3.getText().toString())) {
                    ExamFragment parentFragment = (ExamFragment) getParentFragment();
                    if (parentFragment != null) {
                        parentFragment.openNextQuizFragment();
                    }
                } else {
                    // Incorrect answer
                    Toast.makeText(getContext(), "Incorrect answer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        db = AppDatabase.getDatabase(getContext());
        wr=new WordRepository(db.wordDao());
        dr=new DictRepository(db.dictDao(),db.wordDao());

        searchVideo();

        return root;
    }

    private void searchVideo() {
        //무결성을 위해 title 이란 이름의 단어장 검색
        dr.getDictByTitle(title).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            //성공적으로 단어장 검색
            @Override
            public void onSuccess(@NonNull Dict dict) {
                //단어장과 연결된 단어리스트 찾기
                dr.getWordsByDictId(dict.getDictID())
                        .subscribe(new SingleObserver<DictWithWords>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            //성공 단어장-단어리스트 객체 - dictWithWords
                            @Override
                            public void onSuccess(@NonNull DictWithWords dictWithWords) {
                                showVideo();
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e("로그wordsInDict", t.toString());
                            }
                        });
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그getDictByTitle", e.toString());
            }
        });
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