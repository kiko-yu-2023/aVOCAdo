package com.example.avocado.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


@Dao
public interface DictDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    Completable insert(Dict d);

    @Query("DELETE FROM DICT")
    Completable deleteAll();

    //수정일자 최신순 전체조회
    @Query("SELECT * FROM DICT ORDER BY modifiedTime")
    Flowable<List<Dict>> loadOrderByModified();

    //updateUpdatedTime

    //mergeDict : 단어장 아이디를 받아 dict1에 dict2를 합친다.

    //separateDict : 새로운 단어장을 생성하여 기존의 단어장에 있는 선택된 단어들을 옮긴다.

    //migrateDict : dict1에서 선택된 wordlist 단어들을 dict2로 옮긴다.
}
