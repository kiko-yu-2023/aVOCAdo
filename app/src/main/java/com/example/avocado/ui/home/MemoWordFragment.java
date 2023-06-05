package com.example.avocado.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.avocado.databinding.FragmentMemoWordBinding;
import com.example.avocado.db.AppDatabase;

import java.util.Date;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemoWordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MemoWordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "para1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView inputFixed;
    private TextView wordMeaning;
    private TextView exampleSentence;
    private ImageView eaxmpleSentenceSpeaker;
    private TextView exampleSentenceMeaning;
    private FragmentMemoWordBinding binding;

    public MemoWordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Memo.
     */
    // TODO: Rename and change types and number of parameters
    public static MemoWordFragment newInstance(String param1, String param2) {
        MemoWordFragment fragment = new MemoWordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMemoWordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String inputFixedString = "";
        String wordMeaningSt = "";
        String exampleSentenceSt= "";
        String exampleSentenceMeaningSt= "";

        Bundle args = getArguments();
        if (args != null) {
            inputFixedString = args.getString("inputFixedString");
            wordMeaningSt = args.getString("wordMeaningSt");
            exampleSentenceSt = args.getString("exampleSentenceSt");
            exampleSentenceMeaningSt = args.getString("exampleSentenceMeaningSt");
        }

        inputFixed = binding.inputFixed;

        wordMeaning = binding.wordMeaning;
        exampleSentence = binding.exampleSentence;
        eaxmpleSentenceSpeaker = binding.eaxmpleSentenceSpeaker;
        exampleSentenceMeaning = binding.exampleSentenceMeaning;


        //뜻이랑 단어 TextView들에 업데이트.
        inputFixed.setText(inputFixedString);
        wordMeaning.setText(wordMeaningSt);
        exampleSentence.setText(exampleSentenceSt);
        eaxmpleSentenceSpeaker.setVisibility(View.VISIBLE);
        exampleSentenceMeaning.setText(exampleSentenceMeaningSt);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}