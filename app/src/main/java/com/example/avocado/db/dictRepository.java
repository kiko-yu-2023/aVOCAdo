package com.example.avocado.db;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class dictRepository {
    private dictDao dictDao;

    public dictRepository(dictDao dictDao) {
        this.dictDao = dictDao;
    }

    public Completable insertDict(dict d) {
        return dictDao.insert(d)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<dict>> getDictsByModified()
    {
        return dictDao.loadOrderByModified()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
