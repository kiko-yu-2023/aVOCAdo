package com.example.avocado.db.dict_with_words;

import android.util.Log;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.internal.operators.completable.CompletableError;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WordRepository {
    private WordDao wordDao;

    public WordRepository(WordDao wordDao)
    {
        this.wordDao=wordDao;
    }

    public Completable insert(Word word)
    {
        if ((word.isSentence() && word.getExampleSentence() !=null&&word.getExampleMeaning()!=null)||
                (!word.isSentence()&&word.getExampleSentence()==null&&word.getExampleMeaning()==null))
        {
            return new CompletableError(new Throwable("sentence/word insert 타입에러"));
        }

        return wordDao.insert(word).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delete(Word word)
    {
        return wordDao.delete(word).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public Completable update(Word word)
    {
        return wordDao.update(word).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Word> findWordByWordinDict(String word,int dictId){
        return wordDao.findWordByContentInDict(word,dictId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
