package com.example.avocado;

import android.os.Bundle;
import android.util.Log;

import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.dict_with_words.WordRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.avocado.databinding.ActivityMainBinding;

import org.reactivestreams.Subscription;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    static AppDatabase db;
    static DictRepository dr;
    static WordRepository wr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        db= AppDatabase.getDatabase(getApplicationContext());
        dr=new DictRepository(db.dictDao());
        wr=new WordRepository(db.wordDao());

        dictInsert("bcd");

        getAllDicts();

        putWord("bcd");

        putSentence("abc");

        findWordsInDict();

        findWordByContent();


    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment).commit();
    }
    static void dictInsert(String title)
    {
        Dict dict1= new Dict(title);
        dr.insertDict(dict1).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그 insert Dict","same title");
            }
        });
    }
    static void putSentence(String title)
    {
        dr.getDictByTitle(title).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onSuccess(@NonNull Dict dict) {
                wr.insert(new Word(true,"I am sam","나는 샘이다",null,dict.getDictID()))
                            .subscribe();
                dr.updateModifiedTime(dict.getDictID(),new Date());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그Sentence",e.toString());
            }
        });
    }
    static void putWord(String title)
    {
        //무결성을 위해 title 이란 이름의 단어장 검색
        dr.getDictByTitle(title).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            //성공적으로 단어장 검색.=> 목표: 해당 단어장 id에 word 넣기
            @Override
            public void onSuccess(@NonNull Dict dict) {
                //단어 db에 단어 insert - 단어이므로 첫 인자 isSentence=false, 예시문장 인자 not null
                wr.insert(new Word(true,"I am sam","나는 샘이다",null,dict.getDictID()))
                        .subscribe();
                //단어장이 수정되었으니 수정시간 업데이트
                dr.updateModifiedTime(dict.getDictID(),new Date());
            }
            //해당 단어장 검색 싪패
            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그Sentence",e.toString());
            }
        });
    }
    static void findWordsInDict()
    {
        dr.getDictByTitle("abc").subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onSuccess(@NonNull Dict dict) {
                dr.getWordsByDictId(dict.getDictID())
                        .subscribe(new FlowableSubscriber<DictWithWords>() {
                            @Override
                            public void onSubscribe(@NonNull Subscription s) {
                            }

                            @Override
                            public void onNext(DictWithWords dictWithWords) {

                                Log.d("로그w&s","words size: "+dictWithWords.words.size());
                                if(dictWithWords.words.size()>0)
                                {
                                    for(Word w:dictWithWords.words)
                                    {
                                        Log.d("로그word",w.toString());
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그getDictByTitle",e.toString());
            }
        });
    }
    static void getAllDicts()
    {
        //모든 시간 순 dict 불러오기
        dr.getDictsByModified().subscribe(new Consumer<List<Dict>>() {
            @Override
            public void accept(List<Dict> dicts) throws Throwable {
                Log.d("로그","dicts : "+dicts.get(0));
            }
        });
    }
    static void findWordByContent()
    {
        wr.findWordByWordinDict("hello",1)
                .subscribe(new SingleObserver<Word>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Word word) {

                    }
                    @Override
                    public void onError(Throwable t) {
                        Log.e("로그findword","에러 하나는 정상 "+t.toString());
                    }
                });
    }
}