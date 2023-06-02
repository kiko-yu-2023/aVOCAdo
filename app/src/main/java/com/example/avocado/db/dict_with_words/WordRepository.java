package com.example.avocado.db.dict_with_words;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WordRepository {
    private WordDao wordDao;

    public WordRepository(WordDao wordDao)
    {
        this.wordDao=wordDao;
    }

    public Completable insert(Word word)
    {
        return wordDao.insert(word).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable update(Word word)
    {
        return wordDao.update(word).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Completable deleteAll()
    {
        return wordDao.deleteAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Word> findWordByWordinDict(String word,int dictId){
        return wordDao.findWordByContentInDict(word,dictId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
