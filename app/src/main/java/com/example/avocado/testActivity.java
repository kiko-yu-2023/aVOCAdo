package com.example.avocado;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.dict_with_words.WordRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class testActivity extends AppCompatActivity {

    private String url = "https://alldic.daum.net/search.do?q=";
    String inputFixedString;
    String wordMeaningSt;
    String exampleSentenceSt;
    String exampleSentenceMeaningSt;

    AppDatabase db;
    WordRepository wr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final Bundle bundle = new Bundle();

        db=AppDatabase.getDatabase(getApplicationContext());
        wr=new WordRepository(db.wordDao());
        getWordData("run",1)
                .flatMapCompletable(word->
                {
                    return wr.insert(word);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(e->Log.e("로그insertWord",e.toString()))
                .subscribe();
    }
    Single<Word> getWordData(String wordContent, int dictId) {
        return Single.fromCallable(() -> {

            url += wordContent;
            url += "&dic=eng&search_first=Y";

            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (doc != null) {
                Elements wordMeaningSt = doc.select(".cleanword_type.kuek_type .list_search");
                Log.d("content", wordMeaningSt.text());

                Elements exampleSentenceSt = doc.select(".box_example.box_sound .txt_example .txt_ex");
                Element exampleSentenceStFirst = exampleSentenceSt.first(); // First example sentence
                Log.d("content", exampleSentenceStFirst.text());

                Elements exampleSentenceMeaningSt = doc.select(".box_example.box_sound .mean_example .txt_ex");
                Element exampleSentenceMeaningStFirst = exampleSentenceMeaningSt.first(); // First example sentence meaning
                Log.d("content", exampleSentenceMeaningStFirst.text());

                return new Word(false, wordContent, wordMeaningSt.text(), exampleSentenceStFirst.text(), exampleSentenceMeaningStFirst.text(), dictId);
            }

            // Handle the case when the document is null or an exception occurred
            throw new RuntimeException("Failed to retrieve word data");
        });
    }
}
