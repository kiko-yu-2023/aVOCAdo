package com.example.avocado.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.avocado.db.dict_with_words.Dict;
import com.example.avocado.db.dict_with_words.DictDao;
import com.example.avocado.db.dict_with_words.Word;
import com.example.avocado.db.dict_with_words.WordDao;

@Database(entities = {Dict.class, Word.class}, version = 1,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract DictDao dictDao();
    public abstract WordDao wordDao();
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "avocado_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
