package com.example.avocado.db.dict_with_words;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;


@Dao
public interface DictDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    Completable insert(Dict ... dicts);

    @Query("SELECT * FROM DICT WHERE title = :title")
    Single<Dict> getDictByTitle(String title);


    @Delete
    Completable delete(Dict dict);

    //수정일자 최신순 전체조회
    @Query("SELECT * FROM DICT ORDER BY modifiedTime")
    Single<List<Dict>> loadOrderByModified();
    @Transaction
    @Query("SELECT * FROM dict WHERE dictID = :dictId")
    Single<DictWithWords> getDictWithWordsById(int dictId);

    @Query("UPDATE DICT SET MODIFIEDTIME=:modifiedTime WHERE DICTID=:dictId")
    Completable updateModifiedTime(int dictId,Date modifiedTime);


    //mergeDict : 단어장 아이디를 받아 dict1에 dict2를 합친다.

    //separateDict : 새로운 단어장을 생성하여 기존의 단어장에 있는 선택된 단어들을 옮긴다.

    //migrateDict : dict1에서 선택된 wordlist 단어들을 dict2로 옮긴다.

}
