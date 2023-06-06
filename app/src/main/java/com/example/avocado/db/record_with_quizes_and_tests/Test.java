package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Test {
    @PrimaryKey(autoGenerate = true)
    private int testID;
    private boolean engIsCorrect;
    private boolean korIsCorrect;
    private String engAnswer;
    private String korAnswer;
    private int wordID;
    private int recordID;



    public boolean isEngIsCorrect() {
        return engIsCorrect;
    }

    public void setEngIsCorrect(boolean engIsCorrect) {
        this.engIsCorrect = engIsCorrect;
    }

    public boolean isKorIsCorrect() {
        return korIsCorrect;
    }

    public void setKorIsCorrect(boolean korIsCorrect) {
        this.korIsCorrect = korIsCorrect;
    }

    public String getEngAnswer() {
        return engAnswer;
    }

    public void setEngAnswer(String engAnswer) {
        this.engAnswer = engAnswer;
    }

    public String getKorAnswer() {
        return korAnswer;
    }

    public void setKorAnswer(String korAnswer) {
        this.korAnswer = korAnswer;
    }

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    @NonNull
    @Override
    public String toString() {
        return testID+" "+engIsCorrect+" "+korIsCorrect+" "+engAnswer+" "+korAnswer+" "+wordID+" "+recordID;
    }
}