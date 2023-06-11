package com.example.avocado;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.avocado.db.AppDatabase;

import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.dict_with_words.WordRepository;
import com.example.avocado.db.record_with_quizes_and_tests.Quiz;
import com.example.avocado.db.record_with_quizes_and_tests.QuizRepository;
import com.example.avocado.db.record_with_quizes_and_tests.Record;
import com.example.avocado.db.record_with_quizes_and_tests.RecordRepository;
import com.example.avocado.db.record_with_quizes_and_tests.Test;
import com.example.avocado.db.record_with_quizes_and_tests.TestRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private int NUM_PAGES= -1;
    static AppDatabase db;
    static DictRepository dr;
    static WordRepository wr;

    static RecordRepository rr;
    static QuizRepository qr;
    static TestRepository tr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Memo 몇개 만들지 pageIndicator 받아오기?

        //배경화면 보이도록 패딩넣기
        ConstraintLayout mainLayout = binding.container;
        int padding = getResources().getDimensionPixelSize(R.dimen.fragment_padding);
        mainLayout.setPadding(padding, 0, padding, padding);


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

        db = AppDatabase.getDatabase(getApplicationContext());
        dr = new DictRepository(db.dictDao(), db.wordDao());
        wr = new WordRepository(db.wordDao());
        rr = new RecordRepository(db.recordDao(), db.quizDao(), db.testDao());
        qr = new QuizRepository(db.quizDao());
        tr = new TestRepository(db.testDao());


        //dictInsert("abc");
        //putWord("abc");
        //putSentence("abc");
        //deleteWord(2);
        //insertRecordAndQuiz(1);


        //putSentence("Test");
        //putWord("Test");

        //insertRecordAndTest(4);
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment).commit();
    }

    //키보드 나왔을 때 다른 곳을 터치하면 키보드 내리기
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        if (view != null && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) &&
                view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int[] sourceCoordinates = new int[2];
            view.getLocationOnScreen(sourceCoordinates);
            float x = event.getRawX() + view.getLeft() - sourceCoordinates[0];
            float y = event.getRawY() + view.getTop() - sourceCoordinates[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(event);
    }
    //wordID로 word 객체 찾아오기
    static void getWord(int wordID)
    {
        wr.getWord(wordID).subscribe(new SingleObserver<Word>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onSuccess(@NonNull Word word) {
                //작동 코드ㅡ
            }
            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그 getWord",e.toString());
            }
        });
    }
    //1. dictId있는 특정 dict에 대한 모든 record를 가져오는 기능
    static void getRecordsByDictID(int dictID)
    {
        //종류 상관없이면 getAllRecords, 아니면 getQuizRecords 혹은 getTestRecords
        //함수명 제외 코드는 다 같음
        rr.getAllRecordsByDictID(dictID).subscribe(new SingleObserver<List<Record>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull List<Record> records) {
                Log.d("로그 record get"," "+records.size());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그 record get",e.toString());
            }
        });
    }


    //deleteWord or Sentence
    static void deleteWord(int wordID)
    {
        wr.delete(wordID).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.e("로그 word delete","success");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그 word delete",e.toString());
            }
        });
    }
    static void deleteDict(int dictID)
    {
        dr.delete(1).doOnError(e->Log.e("로그 dict 삭제",e.toString())).subscribe();
    }
    //단어장에서 단어만 가져오기
    static void getOnlyWords(int dictID)
    {
        wr.getOnlyWordsInDict(dictID).subscribe(new SingleObserver<List<Word>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull List<Word> words) {
                Log.d("로그 words만",words.size()+" ");
                for(Word word:words)
                {
                    Log.d("로그 words만",word.toString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그 words만","~");
            }
        });
    }
    //단어장 생성
    static void dictInsert(String title)
    {
        Dict dict1= new Dict(title);
        dr.insertDict(dict1).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                //생성이 성공적으로 된 경우 할일
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //단어장 이름이 같아서 생성 안된 경우 할 일
                Log.e("로그 insert Dict","same title");
            }
        });
    }
    //title이란 이름의  단어장에 문장 삽입
    static void putSentence(String title)
    {
        //무결성을 위해 title 이란 이름의 단어장 검색
        dr.getDictByTitle(title).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            //성공적으로 단어장 검색.=> 목표: 해당 단어장 id에 sentence 넣기
            @Override
            public void onSuccess(@NonNull Dict dict) {
                //단어 db에 문장 insert - 문장이므로 첫 인자 isSentence=true,예시문장 인자 null
                wr.insert(new Word(true,"I am Sam","나는 샘이다",null,null,dict.getDictID()))
                        .subscribe();
                wr.insert(new Word(true,"Do you like coffee?","커피 좋아하니?",null,null,dict.getDictID()))
                        .subscribe();
                wr.insert(new Word(true,"Where are you from?","넌 어디서 왔니?",null,null,dict.getDictID()))
                        .subscribe();
                //단어장이 수정되었으니 수정시간 업데이트
                dr.updateModifiedTime(dict.getDictID(),new Date()).subscribe();
            }
            //해당 단어장 검색 싪패
            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그Sentence",e.toString());
            }
        });
    }
    //title이란 이름의  단어장에 단어 삽입
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
                wr.insert(new Word(false,"cat","고양이","Cat is my favorite animal","고양이는 내가 좋아하는 동물이야.",dict.getDictID()))
                        .subscribe();
                wr.insert(new Word(false,"piano","피아노","Are you good at playing the piano?","너 피아노 잘쳐?",dict.getDictID()))
                        .subscribe();
                wr.insert(new Word(false,"apple","사과","I love apple.","난 사과를 좋아해.",dict.getDictID()))
                        .subscribe();
                wr.insert(new Word(false,"coffee","커피","I need coffee.","나 커피가 필요해.",dict.getDictID()))
                        .subscribe();
                wr.insert(new Word(false,"phone","폰","I need to get a new phone.","새 휴대폰을 사야겠어.",dict.getDictID()))
                        .subscribe();

                //단어장이 수정되었으니 수정시간 업데이트
                dr.updateModifiedTime(dict.getDictID(),new Date()).subscribe();
            }
            //해당 단어장 검색 싪패
            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그Sentence",e.toString());
            }
        });
    }
    //특정 단어장 속 단어들 list 얻기
    static void findWordsInDict(String title)
    {
        //무결성을 위해 title 이란 이름의 단어장 검색
        dr.getDictByTitle(title).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            //성공적으로 단어장 검색
            @Override
            public void onSuccess(@NonNull Dict dict) {
                //단어장과 연결된 단어리스트 찾기
                dr.getWordsByDictID(dict.getDictID())
                        .subscribe(new SingleObserver<DictWithWords>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }
                            //성공 단어장-단어리스트 객체 - dictWithWords
                            @Override
                            public void onSuccess(@NonNull DictWithWords dictWithWords) {

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
                                Log.e("로그wordsInDict",t.toString());
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
    //단어 이름으로 특정 사전(id로 구분) 속 단어 객체 찾기
    static void findWordByContent()
    {
        //예시 : hello라는 이름의 1번 단어장에 들어간 객체 찾기
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