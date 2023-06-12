package com.example.avocado.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avocado.R;
import com.example.avocado.databinding.FragmentWordListListBinding;
import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.dict_with_words.WordRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
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
    private TextView noMemoText;
    private int dictId;

    private Bundle receivedBundle;
    private String dictName;

    public WordListAdapter adapter_word;
    private BottomNavigationView bottomNavView;
    private ArrayList<Word> seletList = new ArrayList<>();
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
            receivedBundle = getArguments();
            dictName = getArguments().getString("dictName", "");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentWordListListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //BottomNavigationView 초기화
        bottomNavView = binding.navView;
        bottomNavView.setVisibility(View.GONE);

        //리사이클려뷰 초기화
        rc_list = binding.recyclerviewWord;
        rc_list.setHasFixedSize(true);

        noMemoText = binding.noTextView;

        adapter_word = new WordListAdapter(total_items);
        //adapter_dict.setOnItemClickListener(this);
        rc_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rc_list.setAdapter(adapter_word);


        AppDatabase db = AppDatabase.getDatabase(getContext());
        //dictRepo를 private으로 클래스 oncreate 밖에 정의하는 걸 추천
        DictRepository dr = new DictRepository(db.dictDao(), db.wordDao());
        WordRepository wr = new WordRepository(db.wordDao());

        dr.getDictByTitle(dictName).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            //성공적으로 단어장 검색
            @Override
            public void onSuccess(@NonNull Dict dict) {
                //단어장과 연결된 단어리스트 찾기
                dr.getWordsByDictID(dict.getDictID())
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
                                    noMemoText.setText("");
                                    for (Word w : dictWithWords.words) {
                                        Log.d("wordList", w.toString());

                                    }
                                }
                                dictId = dict.getDictID();
                                total_items = (ArrayList<Word>) dictWithWords.words;

                                Log.d("set", "설정함");
                                adapter_word.setWordList(total_items);
                                adapter_word.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e("wordList", t.toString());
                                dictId = dict.getDictID();
                                adapter_word.notifyDataSetChanged();
                            }
                        });
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그getDictByTitle", e.toString());
            }
        });

        adapter_word.setOnItemClickListener(new WordListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                // 아이템 클릭 시 bottom navigation menu를 표시

                Log.d("itemClicked", total_items.get(position).getContent());
                //반대값 넣기
                if (adapter_word.isSelected.get(position) == true) //true인 경우
                {
                    seletList.remove(total_items.get(position));
                    adapter_word.isSelected.set(position, false);
                } else //fasle인 경우
                {
                    Log.d("add", total_items.get(position).getContent());
                    seletList.add(total_items.get(position));
                    adapter_word.isSelected.set(position, true);
                }

                if (seletList.size() == 0) {
                    bottomNavView.setVisibility(View.GONE);
                    seletList.clear();
                } else
                    bottomNavView.setVisibility(View.VISIBLE);
                adapter_word.notifyDataSetChanged();
            }
        });

        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            AppDatabase db = AppDatabase.getDatabase(getContext());
            //dictRepo를 private으로 클래스 oncreate 밖에 정의하는 걸 추천
            DictRepository dr = new DictRepository(db.dictDao(), db.wordDao());
            WordRepository wr = new WordRepository(db.wordDao());
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 클릭된 메뉴 아이템을 기반으로 처리 로직을 구현합니다.
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_trash) {// 휴지통 클릭했을 때의 로직
                    //Log.d("pressed", "navigation_trash pressed");

                    //그냥 배열로 변경.
                    for (int i = 0; i < seletList.size(); i++) {
                        wr.delete(seletList.get(i)).subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                Log.d("deleted", "deleted");
                                Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                adapter_word.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e("deleted", "deleted failed");

                            }
                        });
                    }
                }
                //
                else if (itemId == R.id.navigation_move) {
                    //단어장 선택 화면 보여주기
                    boolean hasAddMemo = false;
                    HomeFragment homeFragment = (HomeFragment) HomeFragment.newInstance(hasAddMemo);
                    getChildFragmentManager().findFragmentById(R.id.wordListLayout);
                    homeFragment.setOnItemClickListener(new HomeFragment.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position, String seletedDictName) {

                            //선택된 dict로 단어들 이동.
                            dr.getDictByTitle(seletedDictName).subscribe(new SingleObserver<Dict>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onSuccess(@NonNull Dict dict) {
                                    for(Word i:seletList)
                                    {
                                        i.setDictID(dict.getDictID());
                                    }
                                    wr.update(seletList.toArray(new Word[seletList.size()])).subscribe(new CompletableObserver() {
                                        @Override
                                        public void onSubscribe(@NonNull Disposable d) {

                                        }

                                        @Override
                                        public void onComplete() {
                                            Log.d("word move","success");
                                            Toast.makeText(getContext(),"단어가 "+dictName+"로부터 "+seletedDictName+"으로 이동되었습니다.",Toast.LENGTH_LONG).show();

                                            //이전 fragment로 돌아가기
                                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                            fragmentManager.popBackStack();
                                        }

                                        @Override
                                        public void onError(@NonNull Throwable e) {
                                            Log.d("word move","success");
                                        }
                                    });
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {

                                }
                            });

                        }
                    });

                    FragmentManager fragmentManager = getChildFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.replace(R.id.wordListLayout, homeFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                return false;
            }
        });

        return root;
    }
    //HomeFragment가 화면에 보일 때마다 실행.
    public void onResume(){
        super.onResume();

        // 이후 데이터 가져오기 함수화 필요.
        // 삭제 연산시 데이터 0일경우 추가 필요.

        AppDatabase db=AppDatabase.getDatabase(getContext());
        //dictRepo를 private으로 클래스 oncreate 밖에 정의하는 걸 추천
        DictRepository dr = new DictRepository(db.dictDao(),db.wordDao());
        dr.getDictByTitle(dictName).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            //성공적으로 단어장 검색
            @Override
            public void onSuccess(@NonNull Dict dict) {
                //단어장과 연결된 단어리스트 찾기
                dr.getWordsByDictID(dict.getDictID())
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
                                    noMemoText.setText("");
                                    for (Word w : dictWithWords.words) {
                                        Log.d("wordList", w.toString());

                                    }
                                }
                                dictId = dict.getDictID();
                                total_items = (ArrayList<Word>) dictWithWords.words;

                                Log.d("set", "설정함");
                                adapter_word.setWordList(total_items);
                                adapter_word.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e("wordList", t.toString());
                                dictId = dict.getDictID();
                                adapter_word.notifyDataSetChanged();
                            }
                        });
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그getDictByTitle", e.toString());
            }
        });

    }

}