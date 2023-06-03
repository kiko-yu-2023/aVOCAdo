package com.example.avocado.db.dict_with_words;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int wordID;
    @NonNull
    private boolean isSentence;
    @NonNull
    private String content;
    @NonNull
    private String meaning;

    private String exampleSentence;

    private String exampleMeaning;
    @NonNull
    private boolean isMemorized=false;
    @ColumnInfo(name = "dictID")
    private int dictID;
    public Word(boolean isSentence, String content, String meaning, String exampleSentence,String exampleMeaning, int dictID) {
        this.isSentence=isSentence;
        this.content=content;
        this.meaning=meaning;
        this.exampleSentence=exampleSentence;
        this.exampleMeaning=exampleMeaning;
        this.dictID=dictID;
    }

    public String getExampleMeaning() {
        return exampleMeaning;
    }

    public void setExampleMeaning(String exampleMeaning) {
        this.exampleMeaning = exampleMeaning;
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

    public boolean isSentence() {
        return isSentence;
    }

    public void setSentence(boolean sentence) {
        isSentence = sentence;
    }

    public void setDictID(int dictID) {
        this.dictID = dictID;
    }

    @NonNull
    @Override
    public String toString() {
        return this.wordID+" "+this.content+" "+this.meaning+" "
                +this.exampleSentence+" "+this.isMemorized+" "+this.dictID+" "+this.isSentence;
    }
}
