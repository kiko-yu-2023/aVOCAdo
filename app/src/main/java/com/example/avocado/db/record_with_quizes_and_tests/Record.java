package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Record {
    @PrimaryKey(autoGenerate = true)
    private int recordID;
    private Date time;
    private String score;
    private boolean relatedWithTest;
    private int dictID;

    public Record(boolean relatedWithTest,int dictID) {
        this.time = new Date();
        this.relatedWithTest = relatedWithTest;
        this.dictID=dictID;
    }

    public int getDictID() {
        return dictID;
    }

    public void setDictID(int dictID) {
        this.dictID = dictID;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
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
        return this.recordID+" "+this.score+" "+this.time+" "+this.relatedWithTest;
    }
}
