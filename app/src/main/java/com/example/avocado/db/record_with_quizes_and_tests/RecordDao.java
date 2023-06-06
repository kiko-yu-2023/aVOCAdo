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
    Single<Long> insert(Record record);

    @Query("UPDATE Record SET score = "+
            "(SELECT COUNT(*) FROM Quiz WHERE recordID = :recordID AND ISCORRECT=1)"+
            " WHERE recordID = :recordID")
    Completable updateQuizScore(int recordID);

    @Query("UPDATE Record SET score = "+
            "(SELECT COUNT(*)" +
            " FROM Test WHERE recordID = :recordID AND ENGISCORRECT=1 AND KORISCORRECT=1)"+
            " WHERE recordID = :recordID")
    Completable updateTestScore(int recordID);

    @Query("DELETE FROM RECORD WHERE RECORDID=:recordID")
    Completable delete(int recordID);

    @Delete
    Completable delete(Record ... records);

    @Transaction
    @Query("SELECT * FROM RECORD WHERE recordID=:recordID")
    Single<RecordWithTests> getRecordWithTests(int recordID);

    @Transaction
    @Query("SELECT * FROM RECORD WHERE recordID=:recordID")
    Single<RecordWithQuizs> getRecordWithQuizs(int recordID);

    @Query("SELECT * FROM RECORD WHERE RELATEDWITHTEST = 1 ORDER BY TIME DESC")
    Single<List<Record>> getTestRecords();

    @Query("SELECT * FROM RECORD WHERE RELATEDWITHTEST = 0 ORDER BY TIME DESC")
    Single<List<Record>> getQuizRecords();




}
