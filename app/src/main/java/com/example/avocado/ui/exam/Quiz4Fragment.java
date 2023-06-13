package com.example.avocado.ui.exam;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avocado.databinding.FragmentQuiz4Binding;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.record_with_quizes_and_tests.Quiz;

import java.util.ArrayList;

public class Quiz4Fragment extends QuizDataFragment {

    private FragmentQuiz4Binding binding;
    TextView exampleMeaning;
    EditText inputSentenceQuiz4;
    ImageView completeQuiz4;

    private String title;
    private int correctAnswer;
    private Word word;
    private ArrayList<Quiz> quiz; //이 퀴즈 저장
    private boolean isCorrect;

    public Quiz4Fragment(String title, Word word, int correctAnswer, ArrayList<Quiz> quiz) {
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

        binding = FragmentQuiz4Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        exampleMeaning = binding.exampleMeaning;
        inputSentenceQuiz4 = binding.inputSentenceQuiz4;
        completeQuiz4 = binding.completeQuiz4;

        loadData(title);

        completeQuiz4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCorrect = inputSentenceQuiz4.getText().toString().equals(word.getContent());

                if (isCorrect) {
                    correctAnswer ++;
                } else {
                    Toast.makeText(getContext(), "Incorrect answer", Toast.LENGTH_SHORT).show();
                }

                updateQuizList();

                ExamFragment parentFragment = (ExamFragment) getParentFragment();
                if (parentFragment != null) {
                    parentFragment.openNextQuizFragment(correctAnswer);
                }
            }
        });

        return root;
    }

    protected void handleData(DictWithWords dictWithWords) {
        showMeaning();
    }

    private ArrayList<Quiz> updateQuizList(){
        quiz.add(new Quiz(isCorrect,4,
                word.getMeaning(),inputSentenceQuiz4.getText().toString(), word.getWordID(),0));

        return quiz;
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

//    private void isCorrect(String inputSentenceQuiz4) {
//        if(inputSentenceQuiz4.equals(word.getContent())){
//            isCorrect = true;
//        }else{
//            isCorrect = false;
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}