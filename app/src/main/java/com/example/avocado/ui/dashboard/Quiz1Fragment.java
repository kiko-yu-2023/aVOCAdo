package com.example.avocado.ui.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.avocado.databinding.FragmentQuiz1Binding;
import com.example.avocado.db.Words;
import com.example.avocado.db.WordsDao;
import com.example.avocado.db.WordsDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Quiz1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Quiz1Fragment extends Fragment {

    private FragmentQuiz1Binding binding;

    TextView textView;
    Button button1, button2, button3, button4;

    List<String> meanings;
    private WordsDatabase db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Quiz1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Quiz1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Quiz1Fragment newInstance(String param1, String param2) {
        Quiz1Fragment fragment = new Quiz1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();

        binding = FragmentQuiz1Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textView;
        button1 = binding.button1;
        button2 = binding.button2;
        button3 = binding.button3;
        button4 = binding.button4;

        db = Room.databaseBuilder(requireContext(), WordsDatabase.class, "words")
                .allowMainThreadQueries()
                .build();

        fillWord();
        fillOptions();

        return root;
    }

    private void fillWord() {
        Words word = db.getWordsDao().getNthWord(2);
        if (word != null) {
            // Word found, do something with it
            textView.setText(word.word);
        } else {
            // Word not found
            textView.setText("단어 들어오기 오류");
        }
    }

    private void fillOptions() {
        // Retrieve all Words from the database
        List<Words> allWords =db.getWordsDao().getAll();

        // Extract the meanings from the Words objects
        meanings = new ArrayList<>();
        for (Words word : allWords) {
            meanings.add(word.meaning);
        }

        // Shuffle the meanings list
        Collections.shuffle(meanings);

        // Set the meanings to the buttons
        button1.setText(meanings.get(0));
        button2.setText(meanings.get(1));
        button3.setText(meanings.get(2));
        button4.setText(meanings.get(3));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}