package com.example.avocado.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {dict.class}, version = 1,exportSchema = false)
public abstract class appDatabase extends RoomDatabase {
    public abstract dictDao dictDao();
    private static volatile appDatabase INSTANCE;

    public static appDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (appDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    appDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
