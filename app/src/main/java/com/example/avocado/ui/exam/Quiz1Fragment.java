package com.example.avocado.ui.exam;

import android.os.Bundle;

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
import com.example.avocado.db.record_with_quizes_and_tests.Quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Quiz1Fragment extends QuizDataFragment implements View.OnClickListener {

    private FragmentQuiz1Binding binding;

    private TextView textView;
    private Button button1, button2, button3, button4;
    private ArrayList<Button> buttons;
    private List<String> meanings;

    private String title;//사전 이름
    private int correctAnswer;//정답 개수
    private Word word;
    private String InputWordQuiz1; //사용자 선택 답
    private ArrayList<Quiz> quiz; //이 퀴즈 저장
    private boolean isCorrect;//정답 여부

    public Quiz1Fragment(String title, Word word, int correctAnswer, ArrayList<Quiz> quiz) {
        this.title = title;
        this.word = word;
        this.correctAnswer = correctAnswer;
        this.quiz = quiz;
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
        InputWordQuiz1 = ((Button) v).getText().toString();
        isCorrect = InputWordQuiz1.equals(word.getMeaning());

        if (isCorrect) {
            v.setBackgroundResource(R.color.correct_button);
            correctAnswer++;
        } else {
            v.setBackgroundResource(R.color.incorrect_button);
        }

        updateQuizList();

        ExamFragment parentFragment = (ExamFragment) getParentFragment();
        if (parentFragment != null) {
            parentFragment.openNextQuizFragment(correctAnswer); // Pass the updated correctAnswer value
        }
    }

    protected void handleData(DictWithWords dictWithWords) {
        showWord();
        fillOptions(dictWithWords.words);
    }

    private ArrayList<Quiz> updateQuizList(){
        if(meanings != null) {
            quiz.add(new Quiz(isCorrect, 1,
                    meanings.toString(), InputWordQuiz1, word.getWordID(), 0));
        }else{
            Toast.makeText(getActivity(), "퀴즈 생성하기 실패", Toast.LENGTH_SHORT).show();
        }

        return quiz;

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
//        FragmentManager fragmentManager = getParentFragmentManager();
//        Fragment currentFragment = fragmentManager.findFragmentById(R.id.test_layout);
//
//        if (currentFragment != null && currentFragment instanceof Quiz4Fragment && currentFragment.isAdded()) {
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.remove(currentFragment).commitAllowingStateLoss();
//        }
//
    }

}