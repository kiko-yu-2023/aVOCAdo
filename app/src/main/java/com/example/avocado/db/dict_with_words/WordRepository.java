package com.example.avocado.db.dict_with_words;

import android.util.Log;

import androidx.room.Query;

import java.util.List;

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
    public Single<Word> getWord(int wordID)
    {
        return wordDao.getWord(wordID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Completable delete(Word...words)
    {
        return wordDao.delete(words).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public Completable delete(int wordID)
    {
        return wordDao.delete(wordID).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public Completable update(Word word)
    {
        return wordDao.update(word).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Word> findWordByWordinDict(String word,int dictID){
        return wordDao.findWordByContentInDict(word,dictID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Word>> getOnlyWordsInDict(int dictID)
    {
        return wordDao.getOnlyWordsInDict(dictID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    Single<List<Word>> getOnlySentencesInDict(int dictID)
    {
        return wordDao.getOnlySentencesInDict(dictID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
