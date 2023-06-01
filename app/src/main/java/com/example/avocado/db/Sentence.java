package com.example.avocado.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Sentence {
    @PrimaryKey(autoGenerate = true)
    private int sentenceID;
    private String content;
    private String meaning;
    private boolean isMemorized=false;
    @ColumnInfo(name="dictID")
    private int dictID;

    public Sentence(String content,String meaning,int dictID){
        this.content=content;
        this.meaning=meaning;
        this.dictID=dictID;
    }

    public int getSentenceID() {
        return sentenceID;
    }

    public boolean isMemorized() {
        return isMemorized;
    }

    public void setSentenceID(int sentenceID) {
        this.sentenceID = sentenceID;
    }

    public String getContent() {
        return content;
    }

    public void setSentence(String sentence) {
        this.content = content;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
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
        return this.sentenceID+" "+this.content+" "+this.meaning+" "+this.isMemorized+" "+this.dictID;
    }
}
