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

public class Quiz4Fragment extends QuizDataFragment {

    private FragmentQuiz4Binding binding;
    TextView exampleMeaning;
    EditText inputSentenceQuiz4;
    ImageView completeQuiz4;

    private String title;
    private int correctAnswer;
    private Word word;

    public Quiz4Fragment(String title, Word word, int correctAnswer) {
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

        binding = FragmentQuiz4Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        exampleMeaning = binding.exampleMeaning;
        inputSentenceQuiz4 = binding.inputSentenceQuiz4;
        completeQuiz4 = binding.completeQuiz4;

        loadData(title);

        completeQuiz4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCorrect(inputSentenceQuiz4.getText().toString())) {
                    // Correct answer
                    correctAnswer ++;
                } else {
                    Toast.makeText(getContext(), "Incorrect answer", Toast.LENGTH_SHORT).show();
                }
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