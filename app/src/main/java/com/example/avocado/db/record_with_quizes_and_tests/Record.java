package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.room.Entity;

import java.util.Date;

@Entity
public class Record {
    private int recordId;
    private Date time;
    private int score;
    private boolean relatedWithTest;
}
