package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import kotlinx.coroutines.flow.Flow;

@Dao
public interface RecordDao {
    @Insert
    Completable insert(Record ... record);

    @Query("UPDATE Record SET score = "+
            "(SELECT SUM(isCorrect) FROM Quiz WHERE recordId = :recordId)"+
            " WHERE recordId = :recordId")
    Completable updateScore(int recordId);

    @Delete
    Completable delete(int ... recordId);

    @Delete
    Completable delete(Record ... records);

    @Transaction
    @Query("SELECT * FROM RECORD WHERE RECORDID=:recordId")
    Single<RecordWithTests> getRecordWithTests(int recordId);

    @Transaction
    @Query("SELECT * FROM RECORD WHERE RECORDID=:recordId")
    Single<RecordWithQuizs> getRecordWithQuizs(int recordId);

    @Query("SELECT * FROM RECORD WHERE RELATEDWITHTEST = 1 ORDER BY TIME DESC")
    Single<List<Record>> getTestRecords();

    @Query("SELECT * FROM RECORD WHERE RELATEDWITHTEST = 0 ORDER BY TIME DESC")
    Single<List<Record>> getQuizRecords();




}
