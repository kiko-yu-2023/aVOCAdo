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

public class Quiz2Fragment extends QuizDataFragment {

    private FragmentQuiz2Binding binding;

    TextView textView;
    EditText inputWordQuiz2;
    ImageView completeQuiz2;
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

        container.removeAllViews();

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

        loadData(title);

        return root;
    }

    protected void handleData(DictWithWords dictWithWords) {
        showExampleWithBlank();
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