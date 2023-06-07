package com.example.avocado.ui.exam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.avocado.R;
import com.example.avocado.databinding.FragmentExamResultBinding;
import com.example.avocado.db.dict_with_words.DictWithWords;

import java.text.DecimalFormat;

public class ExamResultFragment extends Fragment {
    private FragmentExamResultBinding binding;
    private int correctAnswer;
    private DictWithWords dictWithWords;

    TextView correctAnswerCount, correctAnswerPercentage;

    public ExamResultFragment(int correctAnswer, DictWithWords dictWithWords) {
        // Required empty public constructor
        this.correctAnswer = correctAnswer;
        this.dictWithWords = dictWithWords;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExamResultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        correctAnswerCount = binding.correctAnswerCount;
        correctAnswerPercentage = binding.correctAnswerPercentage;

        correctAnswerCount.setText(correctAnswer+" / "+dictWithWords.words.size());
        String string = calculateCorrectAnswerPercentage(dictWithWords.words.size(), correctAnswer);
        correctAnswerPercentage.setText(string);

        return root;
    }

    public static String calculateCorrectAnswerPercentage(int totalWordsCount, int correctAnswerCount) {
        double percentage = (double) correctAnswerCount / totalWordsCount * 100;
        DecimalFormat decimalFormat = new DecimalFormat("#");
        decimalFormat.setMaximumFractionDigits(0);
        return decimalFormat.format(percentage) + "%";
    }
}