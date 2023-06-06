package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.room.Delete;
import androidx.room.Insert;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuizRepository {

    private QuizDao qd;

    public QuizRepository(QuizDao qd)
    {
        this.qd=qd;
    }
    public Completable insert(Quiz ... quizzes)
    {
        return qd.insert(quizzes).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delete(Quiz ... quizzes)
    {
        return qd.delete(quizzes).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Completable delete(int quizID)
    {

        return qd.delete(quizID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
