package com.example.avocado;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.avocado.databinding.ActivityMemoBinding;
import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.dict_with_words.WordRepository;
import com.example.avocado.ui.home.MemoWordAddFragment;
import com.example.avocado.ui.home.MemoWordFragment;
import com.example.avocado.ui.home.WordListFragment;


import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MemoActivity extends AppCompatActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static int NUM_PAGES = 0;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;
    private ActivityMemoBinding binding;
    private ConstraintLayout toLeft;
    private ConstraintLayout toRight;
    private int currentPage;
    private String dictName;

    private List<Word> wordList;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        dictName = intent.getStringExtra("dictName");

        ActionBar ab = getSupportActionBar();
        ab.setTitle(dictName);
        getDictWithWordsByTitle(dictName);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = binding.viewPager2Container;

        toLeft = binding.beforeFragmentLayout;
        toRight = binding.nextFragmentLayout;



        viewPager.setUserInputEnabled(false);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        toLeft.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentPage-1);
            }
        });

        toRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentPage+1);
            }
        });

        //화면 변경시 이벤트
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                currentPage = position;
                
                if(position ==0)
                {
                    toRight.setEnabled(true);
                    toLeft.setEnabled(false);
                    toLeft.setVisibility(View.INVISIBLE);
                } else if (position == NUM_PAGES-1) { //마지막 페이지
                    toRight.setEnabled(false);
                    toLeft.setEnabled(true);
                    toLeft.setVisibility(View.VISIBLE);
                    toRight.setVisibility(View.INVISIBLE);
                }
                else
                {
                    toRight.setEnabled(true);
                    toLeft.setEnabled(true);
                    toLeft.setVisibility(View.VISIBLE);
                    toRight.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }
    void getDictWithWordsByTitle(String title)
    {
        //NUM_PAGES와 LIST 받아오기

        AppDatabase db= AppDatabase.getDatabase(getApplicationContext());
        DictRepository dr = new DictRepository(db.dictDao(),db.wordDao());
        WordRepository wr=new WordRepository(db.wordDao());

        //무결성을 위해 title 이란 이름의 단어장 검색
        dr.getDictByTitle(title).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            //성공적으로 단어장 검색
            @Override
            public void onSuccess(@NonNull Dict dict) {
                //단어장과 연결된 단어리스트 찾기
                dr.getWordsBydictID(dict.getDictID())
                        .subscribe(new SingleObserver<DictWithWords>() {
                            //성공 단어장-단어리스트 객체 - dicWithWords
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@NonNull DictWithWords dictWithWords) {
                                //단어리스트(List) 불러오기 - dictWithWords.words
                                Log.d("로그w&s","words size: "+dictWithWords.words.size());
                                //내용이 있을 경우
                                if(dictWithWords.words.size()>0)
                                {
                                    for(Word w:dictWithWords.words)
                                    {
                                        Log.d("로그word",w.toString());

                                    }
                                }
                                NUM_PAGES = dictWithWords.words.size();
                                wordList=dictWithWords.words;
                                pagerAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e("로그word",t.toString());
                                NUM_PAGES = 0;
                                pagerAdapter.notifyDataSetChanged();
                            }
                        });
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그getDictByTitle",e.toString());
            }
        });
    }
    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            // 마지막 인덱스인 경우 LastFragment 반환
            if (position == getItemCount() - 1) {
                return new MemoWordAddFragment();
            } else {
                // 나머지 인덱스인 경우 기존에 사용하던 Fragment 반환
                MemoWordFragment wordFragment = new MemoWordFragment();
                Bundle bundle = new Bundle();

                String inputFixedString="";
                String wordMeaningSt="";
                String exampleSentenceSt="";
                String exampleSentenceMeaningSt="";

                Word word=wordList.get(position);
                if(word.isSentence()) {
                    inputFixedString = word.getContent();
                    wordMeaningSt = word.getMeaning();
                }
                else {
                    inputFixedString = word.getContent();
                    wordMeaningSt = word.getMeaning();
                    exampleSentenceSt = word.getExampleSentence();
                    exampleSentenceMeaningSt = word.getExampleMeaning();
                }
                bundle.putString("inputFixedString",inputFixedString);
                bundle.putString("wordMeaningSt",wordMeaningSt);
                bundle.putString("exampleSentenceSt",exampleSentenceSt);
                bundle.putString("exampleSentenceMeaningSt",exampleSentenceMeaningSt);

                wordFragment.setArguments(bundle);
                return wordFragment;
        }}

        @Override
        public int getItemCount() {
            return NUM_PAGES+1;
        }
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

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.actionbar_actions,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.actionWordList) {//단어 리스트 fragment로 이동.
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.memoLayout, WordListFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
    