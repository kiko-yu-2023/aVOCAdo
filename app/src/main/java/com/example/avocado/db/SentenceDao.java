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
public interface SentenceDao {
    @Insert
    Completable insert(Sentence sentence);

    @Update
    Completable update(Sentence sentence);

    @Query("DELETE FROM Sentence")
    Completable deleteAll();

    //모든 단어장에 모든 단어 불러오기
    @Query("SELECT * FROM Sentence")
    Flowable<List<Sentence>> getAll();

    //userInput 단어의 예문 보여주기
    @Query("SELECT * FROM SENTENCE WHERE CONTENT=:content AND DICTID=:dictId")
    Single<Sentence> findSentenceByContentInDict(String content,int dictId);


    /*
    //index 번째 단어 보여주기
    @Query("SELECT * FROM Word LIMIT 1 OFFSET :index")
    Word getNthWord(int index);

    //index 번째 문장 보여주기
    @Query("SELECT * FROM Word LIMIT 1 OFFSET :index")
    Word getNthExample(int index);

    */
}
