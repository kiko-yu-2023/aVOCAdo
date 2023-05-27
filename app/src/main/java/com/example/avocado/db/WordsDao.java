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

    @Query("SELECT * FROM Words WHERE word = :userInput")
    Words findWord(String userInput);
}
