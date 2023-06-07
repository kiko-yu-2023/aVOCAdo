package com.example.avocado.ui.exam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avocado.R;
import com.example.avocado.databinding.FragmentQuiz1Binding;
import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.dict_with_words.WordRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class Quiz1Fragment extends Fragment implements View.OnClickListener {

    private FragmentQuiz1Binding binding;

    TextView textView;
    Button button1, button2, button3, button4;
    private ArrayList<Button> buttons;
    List<String> meanings;
    private AppDatabase db;
    private WordRepository wr;
    private DictRepository dr;

    private String title;
    private Word word;

    public Quiz1Fragment(String title, Word word) {
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
        //container.removeAllViews();

        binding = FragmentQuiz1Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textView;
        button1 = binding.button1;
        button2 = binding.button2;
        button3 = binding.button3;
        button4 = binding.button4;

        buttons = new ArrayList<>();
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        db = AppDatabase.getDatabase(getContext());
        wr = new WordRepository(db.wordDao());
        dr = new DictRepository(db.dictDao(), db.wordDao());

        wordAccess(title);

        return root;
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button){
            Button clickedButton = (Button) v;
            boolean isCorrectAnswer = clickedButton.getText().toString().equals(word.getMeaning());

            if (isCorrectAnswer) {
                v.setBackgroundResource(R.color.correct_button);
                removeFragment(this);
                ((ExamFragment) getParentFragment()).openNextQuizFragment(); // Call the method in ExamFragment to open the next quiz fragment
            } else {
                v.setBackgroundResource(R.color.incorrect_button);
                removeFragment(this);
            }
        }
    }

    private void wordAccess(String title) {
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

                                showWord();

                                fillOptions(dictWithWords.words);
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

    private void showWord() {
        if (word != null) {
            textView.setText(word.getContent());
        } else {
            textView.setText("단어 들어오기 오류");
        }
    }

    private void fillOptions(List<Word> words) {
        meanings = new ArrayList<>();

        for (Word sample : words) {
            if(!sample.isSentence() && !sample.getMeaning().equals(word.getMeaning()))
                    meanings.add(sample.getMeaning());
        }

        int randomIndex = new Random().nextInt(4);
        buttons.get(randomIndex).setText(word.getMeaning());

        if(meanings.size() >= 3){
            Collections.shuffle(meanings);

            for(int i = 0; i < 4; i++){
                if(i != randomIndex){
                    buttons.get(i).setText(meanings.get(i));
                }
            }
        }else{
            Toast.makeText(getActivity(), "단어 개수 부족", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFragment(Quiz1Fragment quiz1Fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.test_layout);

        if (currentFragment != null && currentFragment instanceof Quiz4Fragment && currentFragment.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(currentFragment).commitAllowingStateLoss();
        }
    }

}