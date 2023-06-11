package com.example.avocado.ui.exam;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.avocado.db.AppDatabase;
import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictRepository;
import com.example.avocado.db.dict_with_words.DictWithWords;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class QuizDataFragment extends Fragment {
    private AppDatabase db = AppDatabase.getDatabase(getContext());
    private DictRepository dr = new DictRepository(db.dictDao(), db.wordDao());;

    protected void loadData(String title){
        //무결성을 위해 title 이란 이름의 단어장 검색
        dr.getDictByTitle(title).subscribe(new SingleObserver<Dict>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            //성공적으로 단어장 검색
            @Override
            public void onSuccess(@NonNull Dict dict) {
                //단어장과 연결된 단어리스트 찾기
                dr.getWordsByDictID(dict.getDictID())
                        .subscribe(new SingleObserver<DictWithWords>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }
                            //성공 단어장-단어리스트 객체 - dictWithWords
                            @Override
                            public void onSuccess(@NonNull DictWithWords dictWithWords) {
                                Log.d("로그w&s","words size: "+dictWithWords.words.size());

                                handleData(dictWithWords);
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

    protected abstract void handleData(DictWithWords dictWithWords);

}
