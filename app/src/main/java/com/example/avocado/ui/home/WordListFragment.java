package com.example.avocado.ui.home;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.avocado.R;
import com.example.avocado.databinding.FragmentHomeBinding;
import com.example.avocado.databinding.FragmentWordListBinding;
import com.example.avocado.databinding.FragmentWordListListBinding;
import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.dict_with_words.WordRepository;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * A fragment representing a list of Items.
 */
public class WordListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters

    private FragmentWordListListBinding binding;
    private RecyclerView rc_list;
    private int dictId;

    private Bundle receivedBundle;
    ArrayList<Word> wordList;
    private String dictName;

    public MyItemRecyclerViewAdapter adapter_word;
    //원본 데이터 리스트
    public ArrayList<Word> total_items = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WordListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static WordListFragment newInstance(int columnCount) {
        WordListFragment fragment = new WordListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            receivedBundle= getArguments();
            dictName = getArguments().getString("dictName","");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentWordListListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //리사이클려뷰 초기화
        rc_list = binding.recyclerviewWord;
        rc_list.setHasFixedSize(true);

        //TextView noMemoText = binding.noMemoText;

        adapter_word = new MyItemRecyclerViewAdapter(total_items);
        //adapter_dict.setOnItemClickListener(this);
        rc_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rc_list.setAdapter(adapter_word);

        AppDatabase db=AppDatabase.getDatabase(getContext());
        //dictRepo를 private으로 클래스 oncreate 밖에 정의하는 걸 추천
        DictRepository dr = new DictRepository(db.dictDao(),db.wordDao());
        WordRepository wr = new WordRepository(db.wordDao());


        dr.getDictByTitle(dictName).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            //성공적으로 단어장 검색
            @Override
            public void onSuccess(@NonNull Dict dict) {
                //단어장과 연결된 단어리스트 찾기
                dr.getWordsBydictID(dict.getDictID())
                        .subscribe(new SingleObserver<DictWithWords>() {
                            //성공 단어장-단어리스트 객체 - dicWithWords
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@NonNull DictWithWords dictWithWords) {
                                //단어리스트(List) 불러오기 - dictWithWords.words
                                Log.d("wordList", "words size: " + dictWithWords.words.size());
                                //내용이 있을 경우
                                if (dictWithWords.words.size() > 0) {
                                    for (Word w : dictWithWords.words) {
                                        Log.d("wordList", w.toString());

                                    }
                                }
                                dictId = dict.getDictID();
                                total_items = (ArrayList<Word>) dictWithWords.words;

                                Log.d("set","설정함");
                                adapter_word.setWordList(total_items);
                                adapter_word.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e("wordList", t.toString());
                                dictId=dict.getDictID();
                                adapter_word.notifyDataSetChanged();
                            }
                        });
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그getDictByTitle", e.toString());
            }
       });

        return root;
    }
}