package com.example.avocado.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface WordDao {
    @Insert
    Completable insert(Word word);

    @Update
    Completable update(Word word);

    @Query("DELETE FROM Word")
    Completable deleteAll();

    //모든 단어장에 모든 단어 불러오기
    @Query("SELECT * FROM Word")
    Flowable<List<Word>> getAll();

    //userInput 단어의 예문 보여주기
    /* deprcated
    * @Query("SELECT * FROM Word WHERE WORD = :userInput")
    * Word findWord(String userInput);
    * */
    @Query("SELECT * FROM WORD WHERE CONTENT=:content AND DICTID=:dictId")
    Single<Word> findWordByContentInDict(String content,int dictId);
}
