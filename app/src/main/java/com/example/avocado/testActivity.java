package com.example.avocado;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class testActivity extends AppCompatActivity {

    private String url = "https://alldic.daum.net/search.do?q=";
    String inputFixedString;
    String wordMeaningSt;
    String exampleSentenceSt;
    String exampleSentenceMeaningSt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final Bundle bundle = new Bundle();

        url += "potato";
        url += "&dic=eng&search_first=Y";
        new Thread(){
            @Override

            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get();
                    //Log.d("doc",doc.text());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Elements wordMeaningSt = doc.select(".cleanword_type.kuek_type .list_search");
                Log.d("content",wordMeaningSt.text());

                Elements exampleSentenceSt = doc.select(".box_example.box_sound .txt_example .txt_ex");
                Element exampleSentenceStFirst = exampleSentenceSt.first(); //첫 예문
                Log.d("content",exampleSentenceStFirst.text());

                Elements exampleSentenceMeaningSt = doc.select(".box_example.box_sound .mean_example .txt_ex");
                Element exampleSentenceMeaningStFirst = exampleSentenceMeaningSt.first(); //첫 예문
                Log.d("content",exampleSentenceMeaningStFirst.text());


                Elements sentenceMeaningEle = doc.select(".cont_speller");
                Log.d("content",sentenceMeaningEle.text());
            }
        }.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Log.d("text",bundle.getString("numbers"));                  //이런식으로 View를 메인 쓰레드에서 뿌려줘야한다.
        }
    };
}
