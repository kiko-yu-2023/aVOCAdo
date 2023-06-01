package com.example.avocado.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
@Entity
public class Word {

    @PrimaryKey(autoGenerate = true)
    private int wordID;

    private String content;
    private String meaning;
    private String exampleSentence;
    private boolean isMemorized=false;
    @ColumnInfo(name = "dictID")
    private int dictID;
    public Word(String content,String meaning,String exampleSentence,int dictID)
    {
        this.content=content;
        this.meaning=meaning;
        this.exampleSentence=exampleSentence;
        //dictID가 있는지 확인하는 부분이 필요함
        this.dictID=dictID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    public int getWordID() {
        return wordID;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String word) {
        this.content = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }

    public void setExampleSentence(String exampleSentence) {
        this.exampleSentence = exampleSentence;
    }

    public boolean isMemorized() {
        return isMemorized;
    }

    public void setMemorized(boolean memorized) {
        isMemorized = memorized;
    }

    public int getDictID() {
        return dictID;
    }

    public void setDictID(int dictID) {
        this.dictID = dictID;
    }

    @NonNull
    @Override
    public String toString() {
        return this.wordID+" "+this.content+" "+this.meaning+" "+this.exampleSentence+" "+this.isMemorized+" "+this.dictID;
    }
}
