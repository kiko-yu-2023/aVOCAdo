package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Test {
    @PrimaryKey(autoGenerate = true)
    private int testId;
    private boolean engIsCorrect;
    private boolean korIsCorrect;
    private String engAnswer;
    private String korAnswer;
    private int wordId;
    private int recordId;

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

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

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    @NonNull
    @Override
    public String toString() {
        return testId+" "+engIsCorrect+" "+korIsCorrect+" "+engAnswer+" "+korAnswer+" "+wordId+" "+recordId;
    }
}