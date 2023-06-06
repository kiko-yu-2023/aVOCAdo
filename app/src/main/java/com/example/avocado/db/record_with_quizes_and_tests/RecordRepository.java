package com.example.avocado.db.record_with_quizes_and_tests;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.operators.flowable.FlowableError;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecordRepository {
    private RecordDao rd;
    public RecordRepository(RecordDao rd)
    {
        this.rd=rd;
    }
    public Single<Integer> insert(Record record)
    {
        return rd.insert(record).map(new Function<Long, Integer>() {
            @Override
            public Integer apply(Long insertedId) throws Throwable {
                return insertedId.intValue();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateScore(int recordID)
    {
        return rd.updateQuizScore(recordID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delete(int recordID)
    {
        return rd.delete(recordID).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delete(Record ... records)
    {
        return rd.delete(records).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<RecordWithQuizs> getRecordWithQuizs(Record record)
    {
        if(record.isRelatedWithTest())
        {
            return Single.error(new Throwable("Quiz Record가 아닙니다"));
        }
        return rd.getRecordWithQuizs(record.getRecordID()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<RecordWithTests> getRecordWithTests(Record record)
    {
        if(!record.isRelatedWithTest())
        {
            return Single.error(new Throwable("Test Record가 아닙니다"));
        }
        return rd.getRecordWithTests(record.getRecordID()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Record>> getTestRecords()
    {
        return rd.getTestRecords().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Record>> getQuizRecords()
    {
        return rd.getQuizRecords().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
