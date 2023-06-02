package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.room.Entity;

@Entity
public class Test {
    private int testId;
    private boolean isCorrect;
    private int wordId;
    private int recordId;


}