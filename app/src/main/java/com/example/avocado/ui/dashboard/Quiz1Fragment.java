package com.example.avocado.ui.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

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
    private AppDatabase db;
    private WordRepository wr;
    private DictRepository dr;

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

        db = AppDatabase.getDatabase(getContext());
        wr = new WordRepository(db.wordDao());

        fillWord("abc");


        return root;
    }

    private void fillWord(String title) {
        //무결성을 위해 title 이란 이름의 단어장 검색
        dr.getDictByTitle(title).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            //성공적으로 단어장 검색
            @Override
            public void onSuccess(@NonNull Dict dict) {
                //단어장과 연결된 단어리스트 찾기
                dr.getWordsByDictId(dict.getDictID())
                        .subscribe(new SingleObserver<DictWithWords>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }
                            //성공 단어장-단어리스트 객체 - dictWithWords
                            @Override
                            public void onSuccess(@NonNull DictWithWords dictWithWords) {
                                Log.d("로그w&s","words si");
                                Log.d("로그w&s","words size: "+dictWithWords.words.size());

                                Word word = dictWithWords.words.get(2);
                                if (word != null) {
                                    // Word found, do something with it
                                    textView.setText(word.getContent());
                                } else {
                                    // Word not found
                                    textView.setText("단어 들어오기 오류");
                                }

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

    private void fillOptions(List<Word> words) {
        // Retrieve all Word from the database

        // Extract the meanings from the Word objects
        meanings = new ArrayList<>();
        for (Word word : words) {
            meanings.add(word.getMeaning());
        }

        // Shuffle the meanings list
        Collections.shuffle(meanings);

        if(words.size() >= 4) {
            // Set the meanings to the buttons
            button1.setText(meanings.get(0));
            button2.setText(meanings.get(1));
            button3.setText(meanings.get(2));
            button4.setText(meanings.get(3));
        }else{
            Toast.makeText(getActivity(), "단어 개수 부족", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}