package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RecordWithQuizs {
    @Embedded
    Record record;
    @Relation(parentColumn = "recordID",entityColumn = "recordID",entity = Quiz.class)
    public List<Quiz> quizs;
}
