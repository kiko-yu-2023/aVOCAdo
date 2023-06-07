package com.example.avocado.ui.exam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avocado.databinding.FragmentQuiz4Binding;
import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.dict_with_words.WordRepository;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class Quiz4Fragment extends Fragment {

    private FragmentQuiz4Binding binding;
    TextView exampleMeaning;
    EditText inputSentenceQuiz4;
    Button completeQuiz4;

    private AppDatabase db;
    private DictRepository dr;
    private WordRepository wr;
    private String title;
    private Word word;

    public Quiz4Fragment(String title, Word word) {
        this.title = title;
        this.word = word;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();

        binding = FragmentQuiz4Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        exampleMeaning = binding.exampleMeaning;
        inputSentenceQuiz4 = binding.inputSentenceQuiz4;
        completeQuiz4 = binding.completeQuiz4;

        db=AppDatabase.getDatabase(getContext());
        wr=new WordRepository(db.wordDao());
        dr=new DictRepository(db.dictDao(),db.wordDao());

        showExampleMeaning();

        completeQuiz4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCorrect(inputSentenceQuiz4.getText().toString())) {
                    // Correct answer
                    ExamFragment parentFragment = (ExamFragment) getParentFragment();
                    if (parentFragment != null) {
                        parentFragment.openNextQuizFragment();
                    }
                } else {
                    Toast.makeText(getContext(), "Incorrect answer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private void showExampleMeaning() {
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
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }
                            //성공 단어장-단어리스트 객체 - dictWithWords
                            @Override
                            public void onSuccess(@NonNull DictWithWords dictWithWords) {
                                Log.d("로그w&s","words si");
                                Log.d("로그w&s","words size: "+dictWithWords.words.size());

                                showMeaning();

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


    private void showMeaning() {
        if (word != null) {
            String example = word.getMeaning();
            exampleMeaning.setText(example);
        } else {
            // Example not found
            exampleMeaning.setText("예문 해석 가져오기 실패");
        }
    }


    private boolean isCorrect(String inputWord) {
        if(inputWord.equals(word.getContent())){
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