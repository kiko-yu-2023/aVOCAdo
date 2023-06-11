package com.example.avocado.ui.home;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.avocado.databinding.FragmentMemoWordAddBinding;
import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.dict_with_words.WordRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
    private Boolean isSentence = false;

    private Interpreter tflite;

    private ViewPagerInteractionListener interactionListener;
    private PaintView paintView;
    private EditText inputText;
    private ToggleButton inputChangeButton;
    private ImageView dictSearchButton;
    private TextView wordMeaning;
    private String url = "https://alldic.daum.net/search.do?q=";

    private String dictName;
    private Word word2;
    private int position;
    private int dictId;
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

    public interface ViewPagerInteractionListener {
        void moveToNextPageAndChangeFragment(Word word);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMemoWordAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle args = getArguments();
        if (args != null) {

            position = args.getInt("position", position);
            dictName = args.getString("dictName", "");
            Log.d("args", dictName);
            dictId = args.getInt("dictId", 0);
            Log.d("args", Integer.toString(dictId));
        }

        if (!OpenCVLoader.initDebug()) {
            // OpenCV 초기화 실패
            Log.e("OpenCV", "Failed to initialize OpenCV");
        }


        paintView = binding.handWrittingView;
        inputChangeButton = binding.inputChangeButton;
        inputText = binding.inputText;

        dictSearchButton = binding.dictSearchButton;

        inputChangeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) { //toggle is enabled
                    inputText.setVisibility(View.VISIBLE);
                    paintView.setVisibility(View.GONE);
                    paintView.reset();
                } else { //toggle is disabled
                    inputText.setVisibility(View.GONE);
                    paintView.setVisibility(View.VISIBLE);
                    paintView.reset();
                }
            }
        });

        //DB에 데이터 넣고 MemoWordFragment로 이동하기.
        dictSearchButton.setOnClickListener(new View.OnClickListener() {
            //문장이나 단어를 입력했을 때의 이벤트
            @Override
            public void onClick(View view) {
                final String[] inputFixedString = new String[1];

                //1) 입력이 없는데 등록 버튼을 눌렀을 경우
                //1-1) 현재 텍스트가 활성화 되어 있는 경우
                //1-2) 현재 캔버스가 활성화 되어 있는 경우
                boolean isCanvasEmpty = paintView.isCanvasEmpty();
                if (isCanvasEmpty && inputText.getText().length() == 0) {
                    Toast.makeText(getContext(), "입력이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {

                    inputText.setVisibility(View.GONE);
                    paintView.setVisibility(View.GONE);
                    inputChangeButton.setVisibility(View.GONE);
                    dictSearchButton.setVisibility(View.GONE);


                    //단어 뜻 등의 정보 사전 API로 얻어오기
                    //검색할 단어를 검색하는 url 완성

                    AppDatabase db = AppDatabase.getDatabase(getContext());
                    DictRepository dr = new DictRepository(db.dictDao(), db.wordDao());
                    WordRepository wr = new WordRepository(db.wordDao());

                    getInputFixedString(isCanvasEmpty,inputFixedString)
                            //웹크롤링을 수행하는 Thread
                            .flatMap(fixedString->{
                        inputFixedString[0]=fixedString;
                        //[0] 배열 형태인 이유는 뭘까
                        Log.d("word", "웹크롤링 수행전 : " + inputFixedString[0]);
                        return getWordData(inputFixedString[0],dictId);
                    }).flatMapCompletable(word ->
                    {
                        if(word==null)
                        {
                            Toast.makeText(getContext(),"존재하지 않는 단어입니다",Toast.LENGTH_SHORT);
                            return Completable.error(new Throwable("존재하지 않는 단어 검색"));
                        }
                        Log.d("로그 getandinsert word data from dict", word.toString());

                        word2 = word;
                        return wr.insert(word);})
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                }
                                @Override
                                public void onComplete() {
                                    if (interactionListener != null) {
                                        Log.d("로그 getandinsert word data from dict", "success");
                                        //super.getDict
                                        interactionListener.moveToNextPageAndChangeFragment(word2);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("로그 getandinsert word data from dict", e.toString());
                                }
                            });
                }
            }
        });
        return root;
    }
    Single<String> getInputFixedString(boolean isCanvasEmpty,String[] inputFixedString)
    {
        if (isCanvasEmpty) //2) 텍스트로 입력했을 때
        {
            return Single.just(String.valueOf(inputText.getText()));
        } else //3) 손글씨로 입력했을 때
        {
            //tensorflow lite 모델 가져오기
            tflite = getTfliteInterpreter("model.tflite");

            Mat mat = new Mat();
            Utils.bitmapToMat(paintView.convertToPNG(),mat);

            ImageProcessing ip = new ImageProcessing();
            String res=ip.processImage(mat,tflite);
            Log.d("로그 img 인식",res);
            //얘를 트렌젝션으로 감싸주세요
            return Single.just(res);
        }
    }
    Single<Word> getWordData(String wordContent, int dictId) {
        return Single.fromCallable(() -> {

            url += wordContent;
            url += "&dic=eng&search_first=Y";


            Log.d("word", url);
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                Log.e("로그 word add", e.toString());
                e.printStackTrace();
            }

            Log.d("doc", doc.text());

            if (doc != null) {

                //문장일 경우 해석이 나옴
                Elements sentenceMeaningEle = doc.select(".cont_speller");
                Elements wordMeaningEle = null;
                Element exampleSentenceStFirst = null;
                Element exampleSentenceMeaningStFirst = null;

                Log.d("sentence", String.valueOf(sentenceMeaningEle.text().length()));

                if (sentenceMeaningEle.text().length() == 0) //단어인 경우
                {
                    isSentence = false;
                } else {
                    isSentence = true;
                }

                Log.d("word", String.valueOf(isSentence));

                wordMeaningEle = doc.select(".cleanword_type.kuek_type .list_search");
                Log.d("word", wordMeaningEle.text());

                Elements exampleSentenceEle = doc.select(".box_example.box_sound .txt_example .txt_ex");
                exampleSentenceStFirst = exampleSentenceEle.first(); //첫 예문
                Log.d("word", exampleSentenceStFirst.text());

                Elements exampleSentenceMeaningEle = doc.select(".box_example.box_sound .mean_example .txt_ex");
                exampleSentenceMeaningStFirst = exampleSentenceMeaningEle.first(); //첫 예문
                Log.d("word", exampleSentenceMeaningStFirst.text());

                String wordMeaningSt;
                String exampleSentenceSt = null;
                String exampleSentenceMeaningSt = null;

                if (isSentence == false) //단어일 경우
                {
                    wordMeaningSt = wordMeaningEle.text();
                    exampleSentenceSt = exampleSentenceStFirst.text();
                    exampleSentenceMeaningSt = exampleSentenceMeaningStFirst.text();
                } else { //문장일 경우
                    wordMeaningSt = sentenceMeaningEle.text();
                }

                return new Word(isSentence, wordContent, wordMeaningSt, exampleSentenceSt, exampleSentenceMeaningSt, dictId);
            }

            // Handle the case when the document is null or an exception occurred
            throw new RuntimeException("Failed to retrieve word data");
        });
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ViewPagerInteractionListener) {
            interactionListener = (ViewPagerInteractionListener) context;
        } else {
            throw new IllegalStateException("The hosting Activity must implement ViewPagerInteractionListener");
        }
    }

    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            Log.d("model","가져옴");
            return new Interpreter(loadModelFile(modelPath));
        }
        catch (Exception e) {
            Log.d("model","실패");
        }
        return null;
    }

    private MappedByteBuffer loadModelFile(String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = getContext().getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}