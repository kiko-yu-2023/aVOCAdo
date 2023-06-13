package com.example.avocado.ui.exam;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avocado.databinding.FragmentQuiz2Binding;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.record_with_quizes_and_tests.Quiz;

import java.util.ArrayList;

public class Quiz2Fragment extends QuizDataFragment {

    private FragmentQuiz2Binding binding;

    TextView textView;
    EditText inputWordQuiz2;
    ImageView completeQuiz2;
    private String title;//사전 이름
    private int correctAnswer;//정답 개수
    private Word word;
    private ArrayList<Quiz> quiz; //이 퀴즈 저장
    private boolean isCorrect;

    public Quiz2Fragment(String title, Word word, int correctAnswer, ArrayList<Quiz> quiz) {
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

        binding = FragmentQuiz2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textView;
        inputWordQuiz2 = binding.inputWordQuiz2;
        completeQuiz2 = binding.completeQuiz2;

        completeQuiz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCorrect = inputWordQuiz2.getText().toString().toLowerCase().equals(word.getContent());

                if (isCorrect) {
                    correctAnswer ++;
                } else {
                    // Incorrect answer
                    Toast.makeText(getContext(), "Incorrect answer", Toast.LENGTH_SHORT).show();
                }

                updateQuizList();

                ExamFragment parentFragment = (ExamFragment) getParentFragment();

                if (parentFragment != null) {
                    parentFragment.openNextQuizFragment(correctAnswer);
                }
            }
        });

        loadData(title);

        return root;
    }

    protected void handleData(DictWithWords dictWithWords) {
        showExampleWithBlank();
    }

    private ArrayList<Quiz> updateQuizList(){
        String example = word.getExampleSentence();
        String modifiedExample = example.replace(word.getContent(), "_______");
        quiz.add(new Quiz(isCorrect,2,
                modifiedExample,inputWordQuiz2.getText().toString(),word.getWordID(),0));

        return quiz;
    }


    private void showExampleWithBlank() {
        if (word != null) {
            String example = word.getExampleSentence();
            String modifiedExample = example.toLowerCase().replace(word.getContent(), "_______");
            textView.setText(modifiedExample + "\n" + word.getExampleMeaning());
        } else {
            // Example not found
            textView.setText("예문 들어오기 오류");
        }
    }

//    private void isCorrect(String inputWord) {
//        if(inputWord.equals(word.getContent().toLowerCase())){
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