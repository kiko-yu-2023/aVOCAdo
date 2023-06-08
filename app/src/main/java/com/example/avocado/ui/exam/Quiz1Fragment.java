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
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Quiz1Fragment extends QuizDataFragment implements View.OnClickListener {

    private FragmentQuiz1Binding binding;

    TextView textView;
    Button button1, button2, button3, button4;
    private ArrayList<Button> buttons;
    List<String> meanings;

    private String title;
    private int correctAnswer;
    private Word word;

    public Quiz1Fragment(String title, Word word, int correctAnswer) {
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
        container.removeAllViews();

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


        loadData(title);

        return root;
    }


    @Override
    public void onClick(View v) {
        if(v instanceof Button){
            Button clickedButton = (Button) v;
            boolean isCorrectAnswer = clickedButton.getText().toString().equals(word.getMeaning());

            if (isCorrectAnswer) {
                v.setBackgroundResource(R.color.correct_button);
                correctAnswer++;
            } else {
                v.setBackgroundResource(R.color.incorrect_button);
            }
            ExamFragment parentFragment = (ExamFragment) getParentFragment();
            if (parentFragment != null) {
                parentFragment.openNextQuizFragment(correctAnswer); // Pass the updated correctAnswer value
            }
            removeFragment(this);

        }
    }

    protected void handleData(DictWithWords dictWithWords) {
        showWord();
        fillOptions(dictWithWords.words);
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

        if(meanings.size() >= 3){
            int randomIndex = new Random().nextInt(4);
            buttons.get(randomIndex).setText(word.getMeaning());

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