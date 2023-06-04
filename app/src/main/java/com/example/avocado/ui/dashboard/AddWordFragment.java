//package com.example.avocado.ui.dashboard;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//import androidx.room.Room;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.example.avocado.R;
//import com.example.avocado.databinding.FragmentAddWordBinding;
//import com.example.avocado.databinding.FragmentQuiz1Binding;
//import com.example.avocado.db.Words;
//import com.example.avocado.db.AppDatabase;
//import com.example.avocado.db.dict_with_words.Word;
//
//import java.util.List;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link AddWordFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class AddWordFragment extends Fragment {
//
//    private FragmentAddWordBinding binding;
//
//    TextView word, meaning, example, savedWord;
//    Button addButton, deleteButton, showButton, showExampleButton, searchExampleButton;
//
//    EditText searchWord;
//
//    private AppDatabase db;
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public AddWordFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment AddWordFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static AddWordFragment newInstance(String param1, String param2) {
//        AddWordFragment fragment = new AddWordFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        container.removeAllViews();
//
//        binding = FragmentAddWordBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        word = binding.word;
//        meaning = binding.meaning;
//        example = binding.example;
//
//        addButton = binding.addButton;
//        deleteButton = binding.deleteButton;
//        showButton = binding.showButton;
//        showExampleButton = binding.showExamplesButton;
//        searchExampleButton = binding.searchExampleButton;
//
//        searchWord = binding.searchWord;
//
//        savedWord = binding.savedWord;
//
//        db = Room.databaseBuilder(requireContext(), AppDatabase.class, "words")
//                .allowMainThreadQueries()
//                .build();
//
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Word word = new Word();
//
//                word.setContent(); = word.getText().toString();
//                word.meaning = meaning.getText().toString();
//                word.example = example.getText().toString();
//
//                db.getWordsDao().insert(words);
//                fetchWordList();
//            }
//        });
//
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                db.getWordsDao().deleteAll();
//                fetchWordList();
//            }
//        });
//
//        showButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fetchWordList();
//            }
//        });
//
//        showExampleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showExamples();
//            }
//        });
//
//        searchExampleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchExample();
//            }
//        });
//
//        return root;
//    }
//
//    private void searchExample() {
//        String userInput = binding.searchWord.getText().toString();
//
//        // Search for the word in the database
//        Words word = db.getWordsDao().findWord(userInput);
//
//        if (word != null) {
//            // Word found, display the example
//            savedWord.setText(word.example);
//        } else {
//            // Word not found
//            savedWord.setText("Example not found");
//        }
//    }
//
//    private void showExamples() {
//        List<Words> exampleList = db.getWordsDao().getAll();
//        String exampleListText = "";
//        for(Words words : exampleList){
//            exampleListText += words.example;
//            savedWord.setText(exampleListText);
//        }
//    }
//
//    private void fetchWordList(){
//        List<Words> wordList = db.getWordsDao().getAll();
//        String wordListText = "단어 목록";
//        for(Words words : wordList){
//            wordListText += "\n"+words.word+" "+words.meaning+" "+words.example;
//            savedWord.setText(wordListText);
//        }
//    }
//
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}