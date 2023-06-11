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
    private QuizDao qd;
    private TestDao td;
    public RecordRepository(RecordDao rd,QuizDao qd,TestDao td)
    {
        this.rd=rd; this.qd=qd;
        this.td=td;
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

    public Single<RecordWithQuizs> getRecordWithQuizs(int recordID)
    {
        return rd.getRecord(recordID).flatMap(record -> {
            if(record.isRelatedWithTest()) {
                return Single.error(new Throwable("Quiz Record가 아닙니다"));
            }
            else {
                return qd.RecordHasQuiz(recordID);
            }
        }).flatMap(hasQuiz->{
            if(hasQuiz)
            {
                return rd.getRecordWithQuizs(recordID);
            }
            else {
                return Single.error(new Throwable("해당하는 퀴즈가 없습니다"));
            }}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public Single<RecordWithTests> getRecordWithTests(int recordID)
    {
        return rd.getRecord(recordID).flatMap(record -> {
            if(!record.isRelatedWithTest())
            {
                return Single.error(new Throwable("Test Record가 아닙니다"));
            }
            else {
                return td.RecordHasTest(recordID);
            }
        }).flatMap(hasTest->{
            if(hasTest)
            {
                return rd.getRecordWithTests(recordID);
            }
            else {
                return Single.error(new Throwable("해당하는 테스트가 없습니다"));
            }}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Record>> getAllRecordsByDictID(int dictID)
    {
        return rd.getAllRecordsByDictID(dictID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Single<List<Record>> getTestRecordsByDictID(int dictID)
    {
        return rd.getTestRecordsByDictID(dictID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Record>> getQuizRecordsByDictID(int dictID)
    {
        return rd.getQuizRecordsByDictID(dictID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
