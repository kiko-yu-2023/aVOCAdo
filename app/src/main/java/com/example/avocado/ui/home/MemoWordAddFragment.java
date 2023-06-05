package com.example.avocado.ui.home;

import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.avocado.databinding.FragmentMemoWordAddBinding;
import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.dict_with_words.WordRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemoWordAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MemoWordAddFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "para1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PaintView paintView;
    private EditText inputText;
    private ToggleButton inputChangeButton;
    private Button dictSearchButton;
    private TextView wordMeaning;
    private String url = "https://alldic.daum.net/search.do?q=";
    private TextView exampleSentence;
    private ImageView eaxmpleSentenceSpeaker;
    private TextView exampleSentenceMeaning;
    private String dictName;
    private FragmentMemoWordAddBinding binding;

    public MemoWordAddFragment() {
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
    public static MemoWordAddFragment newInstance(String param1, String param2) {
        MemoWordAddFragment fragment = new MemoWordAddFragment();
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
        binding = FragmentMemoWordAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle args = getArguments();
        if(args!=null)
        {
            dictName = args.getString("dictName","");
            Log.d("dictName",dictName);
        }



        paintView = binding.handWrittingView;
        inputChangeButton = binding.inputChangeButton;
        inputText = binding.inputText;

        dictSearchButton = binding.dictSearchButton;

        inputChangeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){ //toggle is enabled
                    inputText.setVisibility(View.VISIBLE);
                    paintView.setVisibility(View.GONE);
                    paintView.reset();
                }
                else { //toggle is disabled
                    inputText.setVisibility(View.GONE);
                    paintView.setVisibility(View.VISIBLE);
                    paintView.reset();
                }
            }
        });

        //DB에 데이터 넣고 MemoWordFragment로 이동하기.
        dictSearchButton.setOnClickListener(new View.OnClickListener(){
            //문장이나 단어를 입력했을 때의 이벤트
            @Override
            public void onClick(View view) {
                inputText.setVisibility(View.GONE);
                paintView.setVisibility(View.GONE);
                inputChangeButton.setVisibility(View.GONE);
                dictSearchButton.setVisibility(View.GONE);

                String inputFixedString;
                final String[] wordMeaningSt = new String[1];
                final String[] exampleSentenceSt = new String[1];
                final String[] exampleSentenceMeaningSt = new String[1];

                //1) 입력이 없는데 등록 버튼을 눌렀을 경우
                 //1-1) 현재 텍스트가 활성화 되어 있는 경우
                 //1-2) 현재 캔버스가 활성화 되어 있는 경우
                boolean isCanvasEmpty = paintView.isCanvasEmpty();
                if (isCanvasEmpty && inputText.getText().length()==0) {
                    Toast.makeText(getContext(),"입력이 없습니다.",Toast.LENGTH_SHORT).show();
                } else {

                    if(true) //2) 텍스트로 입력했을 때
                    {
                        inputFixedString = String.valueOf(inputText.getText());
                    }
                    else //3) 손글씨로 입력했을 때
                    {

                    }


                    //단어 뜻 등의 정보 사전 API로 얻어오기
                    //검색할 단어를 검색하는 url 완성
                    url += inputFixedString;
                    url += "&dic=eng&search_first=Y";


                    //웹크롤링을 수행하는 Thread
                    Thread thread = new Thread(){
                        @Override

                        public void run() {
                            Document doc = null;
                            try {
                                doc = Jsoup.connect(url).get();
                                //Log.d("doc",doc.text());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Elements wordMeaningEle = doc.select(".cleanword_type.kuek_type .list_search");
                            Log.d("content",wordMeaningEle.text());

                            Elements exampleSentenceEle = doc.select(".box_example.box_sound .txt_example .txt_ex");
                            Element exampleSentenceStFirst = exampleSentenceEle.first(); //첫 예문
                            Log.d("content",exampleSentenceStFirst.text());

                            Elements exampleSentenceMeaningEle = doc.select(".box_example.box_sound .mean_example .txt_ex");
                            Element exampleSentenceMeaningStFirst = exampleSentenceMeaningEle.first(); //첫 예문
                            Log.d("content",exampleSentenceMeaningStFirst.text());


                            wordMeaningSt[0] = wordMeaningEle.text();
                            exampleSentenceSt[0] = exampleSentenceStFirst.text();
                            exampleSentenceMeaningSt[0] = exampleSentenceMeaningStFirst.text();

                        }
                    };

                    thread.start(); //웹크롤링 시작

                    //웹크롤링 종료 기다리기
                    try{
                        Log.d("wait","기다리기 돌입");
                        thread.join();
                    }catch (InterruptedException e){
                        Log.e("join","못 기다림");
                    }


                    Log.d("wait","기다리기 끝, 단어장 넣기");
                    //단어장에 넣기.
                    AppDatabase db= AppDatabase.getDatabase(getContext());
                    DictRepository dr = new DictRepository(db.dictDao(),db.wordDao());
                    WordRepository wr=new WordRepository(db.wordDao());

                    if(true) //입력이 단어인가?
                    {
                        Log.d("word",inputFixedString);
                        //case 1) 입력이 단어일 경우
                        //무결성을 위해 title 이란 이름의 단어장 검색
                        dr.getDictByTitle(dictName).subscribe(new SingleObserver<Dict>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                            }

                            //성공적으로 단어장 검색.=> 목표: 해당 단어장 id에 word 넣기
                            @Override
                            public void onSuccess(@NonNull Dict dict) {
                                //단어 db에 단어 insert - 단어이므로 첫 인자 isSentence=false, 예시문장 인자 not null
                                wr.insert(new Word(false,inputFixedString, wordMeaningSt[0], exampleSentenceSt[0], exampleSentenceMeaningSt[0],dict.getDictID()))
                                        .subscribe(new CompletableObserver() {
                                            @Override
                                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                            }

                                            @Override
                                            public void onComplete() {
                                                Log.d("word Insert",inputFixedString);
                                            }

                                            @Override
                                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                                Log.e("word Insert",inputFixedString);
                                            }
                                        });
                                //단어장이 수정되었으니 수정시간 업데이트
                                dr.updateModifiedTime(dict.getDictID(),new Date()).subscribe();

                                //뜻이랑 단어 TextView들에 업데이트.
                                wordMeaning.setText(wordMeaningSt[0]);
                                exampleSentence.setText(exampleSentenceSt[0]);
                                eaxmpleSentenceSpeaker.setVisibility(View.VISIBLE);
                                exampleSentenceMeaning.setText(exampleSentenceMeaningSt[0]);
                            }
                            //해당 단어장 검색 실패
                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e("로그Sentence",e.toString());
                            }
                        });
                    }
                    else {
                        //case 2) 입력이 문장일 경우
                        //무결성을 위해 title 이란 이름의 단어장 검색
                        dr.getDictByTitle(dictName).subscribe(new SingleObserver<Dict>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                            }

                            //성공적으로 단어장 검색.=> 목표: 해당 단어장 id에 sentence 넣기
                            @Override
                            public void onSuccess(@NonNull Dict dict) {
                                //단어 db에 문장 insert - 문장이므로 첫 인자 isSentence=true,예시문장 인자 null
                                wr.insert(new Word(true,inputFixedString, wordMeaningSt[0],null,null,dict.getDictID()))
                                        .subscribe();
                                //단어장이 수정되었으니 수정시간 업데이트
                                dr.updateModifiedTime(dict.getDictID(),new Date());
                            }
                            //해당 단어장 검색 싪패
                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e("로그Sentence",e.toString());
                            }
                        });
                    }


                }




            }
        });

        return root;
    }

    @Override
    public void onResume() {
        paintView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        paintView.onPause();
        super.onPause();
    }
}