package com.example.avocado.db;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity(indices = {@Index(value = "title", unique = true)})
public class Dict {
    @PrimaryKey(autoGenerate = true)
    private int dictID;
    private String title;
    private String createdTime;
    private String modifiedTime;

    public Dict(String title)
    {
        this.title=title;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.createdTime=formatter.format(date);
        this.modifiedTime=formatter.format(date);
    }
    public void setDictID(int dictID) {
        this.dictID = dictID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime=modifiedTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime=createdTime;
    }
    public int getDictID() {
        return dictID;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatedTime() {return createdTime;}

    public String getModifiedTime() {
        return this.modifiedTime;
    }

    @NonNull
    @Override
    public String toString() {
        return this.dictID+" "+this.title+" "+this.createdTime+" "+this.modifiedTime;
    }
}

