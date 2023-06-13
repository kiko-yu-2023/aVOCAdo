package com.example.avocado.ui.mypage;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avocado.databinding.FragmentQuizScoreBinding;
import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictDao;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.record_with_quizes_and_tests.Record;
import com.example.avocado.db.record_with_quizes_and_tests.RecordRepository;
import com.example.avocado.ui.exam.ExamFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.model.GradientColor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class QuizScoreFragment extends Fragment {
    private FragmentQuizScoreBinding binding;
    private String title;
    //선 그래프
    private LineChart lineChart;
    private TextView quizHistoryTime, quizHistoryScore;

    public QuizScoreFragment(String title) {
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();

        binding = FragmentQuizScoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        quizHistoryTime = binding.quizHistoryTime;
        quizHistoryScore = binding.quizHistoryScore;

        AppDatabase db = AppDatabase.getDatabase(getContext());
        DictRepository dr = new DictRepository(db.dictDao(), db.wordDao());;

        dr.getDictByTitle(title).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
            }
            //성공적으로 단어장 검색
            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Dict dict) {
                //단어장과 연결된 단어리스트 찾기
                dr.getWordsByDictID(dict.getDictID())
                        .subscribe(new SingleObserver<DictWithWords>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                            }

                            //성공 단어장-단어리스트 객체 - dictWithWords
                            @Override
                            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull DictWithWords dictWithWords) {
                                Log.d("로그w&s", "words si");
                                Log.d("로그w&s", "words size: " + dictWithWords.words.size());

                                int dictID = dict.getDictID();
                                getRecordsByDictID(dictID);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e("로그wordsInDict", t.toString());
                            }

                        });
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                Log.e("로그getDictByTitle",e.toString());
            }
        });
        //showLineChart();

        // Inflate the layout for this fragment
        return root;
    }

//    private void showLineChart() {
//        ArrayList<Entry> entry_chart1 = new ArrayList<>(); // 데이터를 담을 Arraylist
//
//        lineChart = binding.chart;
//
//        LineData chartData = new LineData(); // 차트에 담길 데이터
//
//        entry_chart1.add(new Entry(0, 100)); //entry_chart1에 좌표 데이터를 담는다.
//
//
//        LineDataSet lineDataSet1 = new LineDataSet(entry_chart1, "LineGraph1"); // 데이터가 담긴 Arraylist 를 LineDataSet 으로 변환한다.
//
//        lineDataSet1.setColor(Color.RED); // 해당 LineDataSet의 색 설정 :: 각 Line 과 관련된 세팅은 여기서 설정한다.
//
//        chartData.addDataSet(lineDataSet1); // 해당 LineDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.
//
//        lineChart.setData(chartData); // 차트에 위의 DataSet을 넣는다.
//
//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTextColor(Color.BLACK);
//        //xAxis.enableGridDashedLine(20, 20, 0);
//
//        YAxis yLAxis = lineChart.getAxisLeft();
//        yLAxis.setTextColor(Color.BLACK);
//
//        YAxis yRAxis = lineChart.getAxisRight();
//        yRAxis.setDrawLabels(false);
//        yRAxis.setDrawAxisLine(false);
//        yRAxis.setDrawGridLines(false);
//        //yRAxis.enableGridDashedLine(6, 20, 0);
//
//        lineChart.invalidate(); // 차트 업데이트
//        lineChart.setTouchEnabled(false); // 차트 터치 disable
//
//    }

    private void getRecordsByDictID(int dictID)
    {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        RecordRepository rr = new RecordRepository(db.recordDao(), db.quizDao(), db.testDao());
        //종류 상관없이면 getAllRecords, 아니면 getQuizRecords 혹은 getTestRecords
        //함수명 제외 코드는 다 같음
        rr.getAllRecordsByDictID(dictID).subscribe(new SingleObserver<List<Record>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull List<Record> records) {
                Log.d("로그 record get"," "+records.size());
                int index = dictID - 1;
                quizHistoryScore.setText(records.get(index).getScore());
                quizHistoryTime.setText(records.get(index).getTime()+"");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("로그 record get",e.toString());
            }
        });
    }
}