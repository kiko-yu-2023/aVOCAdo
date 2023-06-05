package com.example.avocado.ui.exam;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();

        binding = FragmentExamBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        startQuiz = binding.startQuiz;
        startTest = binding.startTest;
        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //단어장 하나가 간다 치고!
                openQuiz("example");
            }
        });

        db=AppDatabase.getDatabase(getContext());
        dr=new DictRepository(db.dictDao(),db.wordDao());

        return root;
    }

    private void openQuiz(String title) {
        //무결성을 위해 title 이란 이름의 단어장 검색
        dr.getDictByTitle(title).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
            }
            //성공적으로 단어장 검색
            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Dict dict) {
                //단어장과 연결된 단어리스트 찾기
                dr.getWordsByDictId(dict.getDictID())
                        .subscribe(new SingleObserver<DictWithWords>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                            }
                            //성공 단어장-단어리스트 객체 - dictWithWords
                            @Override
                            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull DictWithWords dictWithWords) {
                                Log.d("로그w&s","words si");
                                Log.d("로그w&s","words size: "+dictWithWords.words.size());

                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();

                                Fragment selectedFragment = null;

                                for(int i=0; i<dictWithWords.words.size(); i++){
                                    Word word = dictWithWords.words.get(i);

                                    if (word.isSentence()){
                                        //문장이 들어온 경우
                                        selectedFragment = new Quiz4Fragment();
                                    }else{
                                        int randomFragment = new Random().nextInt(3) + 1;

                                        switch (randomFragment){
                                            case 1:
                                                selectedFragment = new Quiz1Fragment();
                                                break;
                                            case 2:
                                                selectedFragment = new Quiz2Fragment();
                                                break;
                                            case 3:
                                                selectedFragment = new Quiz3Fragment();
                                                break;
                                        }
                                    }
                                }
                                transaction.replace(R.id.nav_host_fragment_activity_main, selectedFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                            @Override
                            public void onError(Throwable t) {
                                Log.e("로그wordsInDict",t.toString());
                            }

                        });
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                Log.e("로그getDictByTitle",e.toString());
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}