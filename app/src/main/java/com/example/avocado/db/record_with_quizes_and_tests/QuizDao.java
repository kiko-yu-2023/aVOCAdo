package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface QuizDao {
    @Insert
    Completable insert(Quiz ... quiz);

    @Delete
    Completable delete(Quiz ... quizzes);

    @Query("DELETE FROM QUIZ WHERE QUIZID=:quizID")
    Completable delete(int quizID);
}
