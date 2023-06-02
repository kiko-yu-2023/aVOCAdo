package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.room.Entity;

@Entity
public class Quiz {
    private int quizId;
    private boolean isCorrect;
    private int wordId;
    private int recordId;

}