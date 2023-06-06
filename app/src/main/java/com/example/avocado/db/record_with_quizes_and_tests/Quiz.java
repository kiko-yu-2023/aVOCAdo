package com.example.avocado.db.record_with_quizes_and_tests;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Quiz {
    @PrimaryKey(autoGenerate = true)
    private int quizID;
    private boolean isCorrect;
    private int type;
    //주관식의 경우 예문, 객관식의 경우 *로 나눠진 선지 4개 입력
    private String question;
    private String userAnswer;
    private int wordID;
    private int recordID;

    public Quiz (boolean isCorrect,int type,String question,String userAnswer,int wordID,int recordID)
    {
        this.isCorrect=isCorrect;
        this.type=type;
        this.question=question;
        this.userAnswer=userAnswer;
        this.wordID=wordID;
        this.recordID=recordID;
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

    public int getQuizID() {
        return quizID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
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
        return quizID+" "+isCorrect+" "+type+" "+question+" "+userAnswer+" "+wordID+" "+recordID;
    }
}