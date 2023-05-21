package com.example.avocado.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Words.class}, version = 1)
public abstract class WordsDatabase extends RoomDatabase {
    public abstract WordsDao getWordsDao();
}
