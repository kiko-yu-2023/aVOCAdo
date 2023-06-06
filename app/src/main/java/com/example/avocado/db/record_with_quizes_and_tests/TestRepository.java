package com.example.avocado.db.record_with_quizes_and_tests;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TestRepository {
    private TestDao td;

    public TestRepository(TestDao td)
    {
        this.td=td;
    }

    public Completable insert(Test ... tests)
    {
        return td.insert(tests).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delete(Test ... tests)
    {
        return td.delete(tests).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Completable delete(int testID)
    {

        return td.delete(testID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
