package com.example.avocado.db;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
}
