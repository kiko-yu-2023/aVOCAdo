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

import com.example.avocado.databinding.FragmentQuiz2Binding;
import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.dict_with_words.WordRepository;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class Quiz2Fragment extends Fragment {

    private FragmentQuiz2Binding binding;

    TextView textView;
    EditText inputWordQuiz2;
    Button completeQuiz2;

    private AppDatabase db;
    private DictRepository dr;
    private WordRepository wr;
    private String title;
    private int correctAnswer;
    private Word word;

    public Quiz2Fragment(String title, Word word, int correctAnswer) {
        this.title = title;
        this.word = word;
        this.correctAnswer = correctAnswer;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentQuiz2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textView;
        inputWordQuiz2 = binding.inputWordQuiz2;
        completeQuiz2 = binding.completeQuiz2;
        completeQuiz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCorrect(inputWordQuiz2.getText().toString())) {
                    // Correct answer
                    correctAnswer ++;
                } else {
                    // Incorrect answer
                    Toast.makeText(getContext(), "Incorrect answer", Toast.LENGTH_SHORT).show();
                }
                ExamFragment parentFragment = (ExamFragment) getParentFragment();
                if (parentFragment != null) {
                    parentFragment.openNextQuizFragment(correctAnswer);
                }
            }
        });

        db=AppDatabase.getDatabase(getContext());
        wr=new WordRepository(db.wordDao());
        dr=new DictRepository(db.dictDao(),db.wordDao());

        fillSentence(title);

        return root;
    }

    private void fillSentence(String title) {
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

                                showExampleWithBlank();
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

    private void showExampleWithBlank() {
        if (word != null) {
            String example = word.getExampleSentence();
            String modifiedExample = example.replace(word.getContent(), "_______");
            textView.setText(modifiedExample + "\n" + word.getExampleMeaning());
        } else {
            // Example not found
            textView.setText("예문 들어오기 오류");
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