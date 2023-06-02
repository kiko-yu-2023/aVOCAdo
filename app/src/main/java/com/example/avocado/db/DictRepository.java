package com.example.avocado.db;

import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlinx.coroutines.flow.Flow;

public class DictRepository {
    private DictDao dictDao;

    public DictRepository(DictDao dictDao) {
        this.dictDao = dictDao;
    }

    public Completable insertDict(Dict d) {
        return dictDao.insert(d)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<Dict>> getDictsByModified()
    {
        return dictDao.loadOrderByModified()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Dict> getDictByTitle(String title)
    {
        return dictDao.getDictByTitle(title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Flowable<DictWithWords> getWordsByDictId(int dictId)
    {
        return dictDao.getDictWithWordsById(dictId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateModifiedTime(int dictId,Date date)
    {
        return dictDao.updateModifiedTime(dictId,date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
