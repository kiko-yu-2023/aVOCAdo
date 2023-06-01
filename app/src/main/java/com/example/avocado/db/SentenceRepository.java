package com.example.avocado.db;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SentenceRepository {
    private SentenceDao sentenceDao;

    public SentenceRepository(SentenceDao sentenceDao)
    {
        this.sentenceDao=sentenceDao;
    }

    public Completable insert(Sentence sentence)
    {
        return sentenceDao.insert(sentence).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable update(Sentence sentence)
    {
        return sentenceDao.update(sentence).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Completable deleteAll()
    {
        return sentenceDao.deleteAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Sentence> findSentenceByContentInDict(String content,int dictId){
        return sentenceDao.findSentenceByContentInDict(content,dictId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
