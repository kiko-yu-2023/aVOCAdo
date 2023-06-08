package com.example.avocado.ui.exam;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.avocado.R;
import com.example.avocado.databinding.FragmentExamResultBinding;
import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.DictWithWords;
import com.example.avocado.db.record_with_quizes_and_tests.Quiz;
import com.example.avocado.db.record_with_quizes_and_tests.QuizRepository;
import com.example.avocado.db.record_with_quizes_and_tests.Record;
import com.example.avocado.db.record_with_quizes_and_tests.RecordRepository;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

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

        insertRecordAndQuiz(dictWithWords.dict.getDictID());
                
        return root;
    }

    private void insertRecordAndQuiz(int dictID) {
        AppDatabase db = AppDatabase.getDatabase(getActivity().getApplicationContext());
        RecordRepository rr = new RecordRepository(db.recordDao(),db.quizDao(),db.testDao());;
        QuizRepository qr = new QuizRepository(db.quizDao());

        //레코드 객체 생성(점수와 recordID는 아직 알수 없음)
        Record rec=new Record(true,dictID);
        //객체 db에 입력
        rr.insert(rec).subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }
            //인풋으로 db에서 배정받은 recordID를 얻음
            @Override
            public void onSuccess(@NonNull Integer recordID) {
                //그리고 퀴즈나 테스트 객체들 생성하여 recordID와 연결
                Quiz q1=new Quiz(true,1,"random","abdf",1,recordID);
                Quiz q2=new Quiz(true,1,"random","abdf",2,recordID);
                //insert(q1,q2,q3..)식으로 해도 되고 insert(quiz 배열)로도 가능
                qr.insert(q1,q2).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }
                    @Override
                    public void onComplete() {
                        //입력 완료하면 DB에서 레코드의 점수를 자동으로 계산해줌
                        rr.updateScore(recordID).doOnComplete(()-> Log.e("로그 update score","success")).doOnError(e->{Log.e("로그 record score update",e.toString());}).subscribe();
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("로그 quiz insert mainActivity",e.toString());
                    }
                });
            }
            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    public static String calculateCorrectAnswerPercentage(int totalWordsCount, int correctAnswerCount) {
        double percentage = (double) correctAnswerCount / totalWordsCount * 100;
        DecimalFormat decimalFormat = new DecimalFormat("#");
        decimalFormat.setMaximumFractionDigits(0);
        return decimalFormat.format(percentage) + "%";
    }
}