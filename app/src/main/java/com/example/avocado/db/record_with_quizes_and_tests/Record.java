package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Record {
    @PrimaryKey(autoGenerate = true)
    private int recordId;
    private Date time;
    private int score;
    private boolean relatedWithTest;

    public Record(Date time, boolean relatedWithTest) {
        this.time = time;
        this.relatedWithTest = relatedWithTest;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isRelatedWithTest() {
        return relatedWithTest;
    }

    public void setRelatedWithTest(boolean relatedWithTest) {
        this.relatedWithTest = relatedWithTest;
    }

    @NonNull
    @Override
    public String toString() {
        return this.recordId+" "+this.score+" "+this.time+" "+this.relatedWithTest;
    }
}
