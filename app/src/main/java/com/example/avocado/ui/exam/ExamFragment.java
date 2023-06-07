package com.example.avocado.ui.exam;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.avocado.R;
import com.example.avocado.databinding.FragmentExamBinding;
import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.ui.home.HomeFragment;

import java.util.Random;

import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class ExamFragment extends Fragment{

    private FragmentExamBinding binding;
    ImageView startQuiz, startTest;
    private AppDatabase db;
    private DictRepository dr;
    private DictWithWords dictWithWords;

    private String title = "hello";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExamBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db=AppDatabase.getDatabase(getContext());
        dr=new DictRepository(db.dictDao(),db.wordDao());

        startQuiz = binding.startQuiz;
        startTest = binding.startTest;

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String title = "hello";
                playQuiz(title);
            }
        });

        return root;
    }

    private void playQuiz(String title) {
        //무결성을 위해 title 이란 이름의 단어장 검색
        dr.getDictByTitle(title).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
            }
            //성공적으로 단어장 검색
            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Dict dict) {
                //단어장과 연결된 단어리스트 찾기
                dr.getWordsBydictID(dict.getDictID())
                        .subscribe(new SingleObserver<DictWithWords>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                            }

                            //성공 단어장-단어리스트 객체 - dictWithWords
                            @Override
                            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull DictWithWords dictWithWords) {
                                Log.d("로그w&s", "words si");
                                Log.d("로그w&s", "words size: " + dictWithWords.words.size());

                                ExamFragment.this.dictWithWords = dictWithWords;

                                openQuizFragment(dictWithWords, title, 0);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e("로그wordsInDict", t.toString());
                            }

                        });
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                Log.e("로그getDictByTitle",e.toString());
            }
        });
    }

    private void openQuizFragment(DictWithWords dictWithWords, String title, int currentIndex) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        int containerId = R.id.test_layout;

        if (currentIndex < dictWithWords.words.size()) {
            Word word = dictWithWords.words.get(currentIndex);
            Fragment selectedFragment;

            if (word.isSentence()) {
                selectedFragment = new Quiz4Fragment(title, word);
            } else {
                int randomFragment = new Random().nextInt(3) + 1;

                switch (randomFragment) {
                    case 1:
                        selectedFragment = new Quiz1Fragment(title, word);
                        break;
                    case 2:
                        selectedFragment = new Quiz2Fragment(title, word);
                        break;
                    case 3:
                        selectedFragment = new Quiz3Fragment(title, word);
                        break;
                    default:
                        selectedFragment = new HomeFragment();
                        break;
                }
            }

            transaction.replace(containerId, selectedFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            currentIndex++;
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void openNextQuizFragment() {
        int currentIndex = getChildFragmentManager().getBackStackEntryCount();

        if (currentIndex < dictWithWords.words.size()) {
            Word word = dictWithWords.words.get(currentIndex);

            Fragment selectedFragment;
            if (word.isSentence()) {
                selectedFragment = new Quiz4Fragment(title, word);
            } else {
                int randomFragment = new Random().nextInt(3) + 1;
                switch (randomFragment) {
                    case 1:
                        selectedFragment = new Quiz1Fragment(title, word);
                        break;
                    case 2:
                        selectedFragment = new Quiz2Fragment(title, word);
                        break;
                    case 3:
                        selectedFragment = new Quiz3Fragment(title, word);
                        break;
                    default:
                        selectedFragment = new HomeFragment();
                        break;
                }
            }

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.test_layout, selectedFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }else{
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.test_layout, new ExamResultFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


}