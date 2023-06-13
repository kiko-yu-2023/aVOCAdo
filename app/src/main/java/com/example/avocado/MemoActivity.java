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
import androidx.fragment.app.FragmentTransaction;
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
import com.example.avocado.ui.home.NewMemoFragment;
import com.example.avocado.ui.home.WordListFragment;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MemoActivity extends AppCompatActivity implements MemoWordAddFragment.ViewPagerInteractionListener {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */

    private AppDatabase db;
    private DictRepository dr;
    private WordRepository wr;
    private ViewPager2 viewPager;
    private ActivityMemoBinding binding;
    private ConstraintLayout toLeft;
    private ConstraintLayout toRight;
    private int currentPage;
    private String dictName;
    private int dictId;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ViewPagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        dictName = intent.getStringExtra("dictName");

        db = AppDatabase.getDatabase(getApplicationContext());
        dr = new DictRepository(db.dictDao(), db.wordDao());
        wr = new WordRepository(db.wordDao());

        pagerAdapter = new ViewPagerAdapter(this);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(dictName);
        getDictWithWordsByTitle(dictName);
        Log.d("getItemCount after", Integer.toString(pagerAdapter.wordList.size()));

        // Instantiate a ViewPager2 and a PagerAdapter.

        //popBackStack 할수있도록

        toLeft = binding.beforeFragmentLayout;
        toRight = binding.nextFragmentLayout;

        toLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentPage - 1);
            }
        });

        toRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentPage + 1);
            }
        });


    }

    @Override
    public void onBackPressed() {
        //WordListFragment 갔다가 돌아올 때 필요한 코드
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    void getDictWithWordsByTitle(String title) {
        //NUM_PAGES와 LIST 받아오기

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
                            //성공 단어장-단어리스트 객체 - dicWithWords
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                pagerAdapter.wordList.add(new Word(false, "empty", null, null, null, dict.getDictID())); //완충
                            }

                            @Override
                            public void onSuccess(@NonNull DictWithWords dictWithWords) {
                                //단어리스트(List) 불러오기 - dictWithWords.words
                                Log.d("로그w&s", "words size: " + dictWithWords.words.size());
                                //내용이 있을 경우
                                if (dictWithWords.words.size() > 0) {
                                    for (Word w : dictWithWords.words) {
                                        Log.d("로그word", w.toString());

                                    }
                                }
                                dictId = dict.getDictID();
                                pagerAdapter.wordList.addAll(dictWithWords.words);
                                Log.d("getItemCount done", Integer.toString(pagerAdapter.wordList.size()));
                                viewPager = binding.viewPager2Container;
                                viewPager.setUserInputEnabled(false);
                                viewPager.setAdapter(pagerAdapter);


                                pagerAdapter.notifyDataSetChanged();

                                //화면 변경시 이벤트
                                viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                    @Override
                                    public void onPageSelected(int position) {
                                        super.onPageSelected(position);

                                        currentPage = position;
                                        Log.d("currentPage", String.valueOf(currentPage));

                                        if (pagerAdapter.getItemCount() == 1) {
                                            toRight.setEnabled(false);
                                            toLeft.setEnabled(false);
                                            toLeft.setVisibility(View.INVISIBLE);
                                            toRight.setVisibility(View.INVISIBLE);
                                        } else if (position == 0) {
                                            toRight.setEnabled(true);
                                            toLeft.setEnabled(false);
                                            toLeft.setVisibility(View.INVISIBLE);
                                        } else if (position == pagerAdapter.getItemCount() - 1) { //마지막 페이지
                                            toRight.setEnabled(false);
                                            toLeft.setEnabled(true);
                                            toLeft.setVisibility(View.VISIBLE);
                                            toRight.setVisibility(View.INVISIBLE);
                                        } else {
                                            toRight.setEnabled(true);
                                            toLeft.setEnabled(true);
                                            toLeft.setVisibility(View.VISIBLE);
                                            toRight.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e("로그word", t.toString());
                                dictId = dict.getDictID();
                                viewPager = binding.viewPager2Container;
                                viewPager.setUserInputEnabled(false);
                                viewPager.setAdapter(pagerAdapter);

                                //pagerAdapter.notifyDataSetChanged();

                                //화면 변경시 이벤트
                                //나중에 transaction으로 변경 필요
                                viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                    @Override
                                    public void onPageSelected(int position) {
                                        super.onPageSelected(position);

                                        currentPage = position;
                                        Log.d("currentPage", String.valueOf(currentPage));

                                        if (pagerAdapter.getItemCount() == 1) {
                                            toRight.setEnabled(false);
                                            toLeft.setEnabled(false);
                                            toLeft.setVisibility(View.INVISIBLE);
                                            toRight.setVisibility(View.INVISIBLE);
                                        } else if (position == 0) {
                                            toRight.setEnabled(true);
                                            toLeft.setEnabled(false);
                                            toLeft.setVisibility(View.INVISIBLE);
                                        } else if (position == pagerAdapter.getItemCount() - 1) { //마지막 페이지
                                            toRight.setEnabled(false);
                                            toLeft.setEnabled(true);
                                            toLeft.setVisibility(View.VISIBLE);
                                            toRight.setVisibility(View.INVISIBLE);
                                        } else {
                                            toRight.setEnabled(true);
                                            toLeft.setEnabled(true);
                                            toLeft.setVisibility(View.VISIBLE);
                                            toRight.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                            }
                        });
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그getDictByTitle", e.toString());
            }
        });
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ViewPagerAdapter extends FragmentStateAdapter {
        private List<Word> wordList = new ArrayList<>();

        public ViewPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        public void setItems(ArrayList<Word> words) {
            wordList = words; // 기존 데이터를 새로운 데이터로 교체
            notifyDataSetChanged(); // 어댑터에 데이터 변경을 알림
        }


        @Override
        public Fragment createFragment(int position) {
            // 마지막 인덱스인 경우 LastFragment 반환
            Log.d("getItemCount", "getItemCount() : " + Integer.toString(getItemCount()) + " position : " + position);
            if (position == getItemCount() - 1) {

                Log.d("getItemCount2", "getItemCount() : " + Integer.toString(getItemCount()) + " position : " + position);
                MemoWordAddFragment memoWordAddFragment = MemoWordAddFragment.newInstance(dictName, dictId, position);
                return memoWordAddFragment;

            } else {
                // 나머지 인덱스인 경우 기존에 사용하던 Fragment 반환
                Log.d("getItemCount3", "getItemCount() : " + Integer.toString(getItemCount()) + " position : " + position);
                MemoWordFragment wordFragment = new MemoWordFragment();
                Bundle bundle = new Bundle();

                String inputFixedString = "";
                String wordMeaningSt = "";
                String exampleSentenceSt = "";
                String exampleSentenceMeaningSt = "";

                Word word = wordList.get(position + 1);

                inputFixedString = word.getContent();
                wordMeaningSt = word.getMeaning();
                exampleSentenceSt = word.getExampleSentence();
                exampleSentenceMeaningSt = word.getExampleMeaning();

                bundle.putString("inputFixedString", inputFixedString);
                bundle.putString("wordMeaningSt", wordMeaningSt);
                bundle.putString("exampleSentenceSt", exampleSentenceSt);
                bundle.putString("exampleSentenceMeaningSt", exampleSentenceMeaningSt);

                wordFragment.setArguments(bundle);
                return wordFragment;
            }
        }

        //        public void replaceFragment(int index, Fragment fragment) {
//            fragments.set(index, fragment);
//            notifyDataSetChanged();
//        }

        @Override
        public int getItemCount() {
            Log.d("getItemCount size", String.valueOf(wordList.size()));
            return wordList.size();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionWordList) {//단어 리스트 fragment로 이동.
            FragmentManager fragmentManager = getSupportFragmentManager();
            WordListFragment fragment = new WordListFragment();
            Bundle bundle = new Bundle();

            bundle.putString("dictName", dictName);
            fragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(R.id.memoLayout, fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void moveToNextPageAndChangeFragment(Word word) {
        int currentPosition = viewPager.getCurrentItem();
        int pageCount = pagerAdapter.getItemCount();

        ArrayList<Word> imsiWords = (ArrayList<Word>) pagerAdapter.wordList;
        imsiWords.add(word);


        pagerAdapter.setItems(imsiWords);

        // ViewPager2의 페이지를 다음 페이지로 설정
        viewPager.setCurrentItem(currentPosition + 1, true);
    }


}

