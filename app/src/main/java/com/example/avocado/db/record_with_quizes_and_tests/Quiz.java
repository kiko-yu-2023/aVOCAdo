package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Quiz {
    @PrimaryKey(autoGenerate = true)
    private int quizId;
    private boolean isCorrect;
    private int type;
    //주관식의 경우 예문, 객관식의 경우 *로 나눠진 선지 4개 입력
    private String question;
    private String userAnswer;
    private int wordId;
    private int recordId;

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
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
        return quizId+" "+isCorrect+" "+type+" "+question+" "+userAnswer+" "+wordId+" "+recordId;
    }
}