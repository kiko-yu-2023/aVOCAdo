package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RecordWithTests {
    @Embedded
    Record record;
    @Relation(parentColumn = "recordId",entityColumn = "recordId",entity = Quiz.class)
    public List<Test> tests;
}