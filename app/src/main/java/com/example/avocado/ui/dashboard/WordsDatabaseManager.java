package com.example.avocado.ui.dashboard;

import android.content.Context;

import androidx.room.Room;

import com.example.avocado.db.WordsDatabase;
import com.example.avocado.db.Words;

public class WordsDatabaseManager {
    private WordsDatabase db;

    public WordsDatabaseManager(Context context) {
        db = Room.databaseBuilder(context, WordsDatabase.class, "words")
                .allowMainThreadQueries()
                .build();
    }

    public Words getNthExample(int n) {
        return db.getWordsDao().getNthExample(n);
    }
}

