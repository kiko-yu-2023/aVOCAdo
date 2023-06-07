package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface TestDao {
    @Insert
    Completable insert(Test ... tests);

    @Delete
    Completable delete(Test ... tests);

    @Query("DELETE FROM TEST WHERE TESTID=:testID")
    Completable delete(int testID);


    @Query("SELECT EXISTS (SELECT * FROM TEST WHERE RECORDID = :recordID)")
    Single<Boolean> RecordHasTest(int recordID);


}
