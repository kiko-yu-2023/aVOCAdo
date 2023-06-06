package com.example.avocado.db.dict_with_words;

import androidx.room.Dao;
import androidx.room.Delete;
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

    @Delete
    Completable delete(Word ... words);
    @Query("DELETE FROM WORD WHERE WORDID=:wordID")
    Completable delete(int wordID);

    @Query("SELECT EXISTS (SELECT * FROM WORD WHERE dictID = :dictID)")
    Single<Boolean> dictHasWord(int dictID);


    //userInput 단어의 예문 보여주기
    /* deprcated
    * @Query("SELECT * FROM Word WHERE WORD = :userInput")
    * Word findWord(String userInput);
    * */
    @Query("SELECT * FROM WORD WHERE CONTENT=:content AND dictID=:dictID")
    Single<Word> findWordByContentInDict(String content,int dictID);

    @Query("SELECT * FROM WORD WHERE dictID=:dictID AND NOT ISSENTENCE")
    Single<List<Word>> getOnlyWordsInDict(int dictID);

    @Query("SELECT * FROM WORD WHERE dictID=:dictID AND ISSENTENCE")
    Single<List<Word>> getOnlySentencesInDict(int dictID);
}
