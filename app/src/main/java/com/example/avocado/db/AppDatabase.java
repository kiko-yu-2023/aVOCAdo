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
import com.example.avocado.db.record_with_quizes_and_tests.Quiz;
import com.example.avocado.db.record_with_quizes_and_tests.QuizDao;
import com.example.avocado.db.record_with_quizes_and_tests.Record;
import com.example.avocado.db.record_with_quizes_and_tests.RecordDao;
import com.example.avocado.db.record_with_quizes_and_tests.Test;
import com.example.avocado.db.record_with_quizes_and_tests.TestDao;

@Database(entities = {Dict.class, Word.class, Record.class, Quiz.class, Test.class}, version = 1,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecordDao recordDao();

    public abstract QuizDao quizDao();

    public abstract TestDao testDao();
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
