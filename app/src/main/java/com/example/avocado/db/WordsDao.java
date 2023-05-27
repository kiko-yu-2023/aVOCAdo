package com.example.avocado.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordsDao {
    @Insert
    void insert(Words words);

    @Update
    void update(Words words);

    @Query("DELETE FROM Words")
    void deleteAll();

    @Query("SELECT * FROM Words")
    List<Words> getAll();

    //userInput 단어의 예문 보여주기
    @Query("SELECT * FROM Words WHERE word = :userInput")
    Words findWord(String userInput);

    //index 번째 단어 보여주기
    @Query("SELECT * FROM Words LIMIT 1 OFFSET :index")
    Words getNthWord(int index);

    //index 번째 문장 보여주기
    @Query("SELECT * FROM Words LIMIT 1 OFFSET :index")
    Words getNthExample(int index);

}
