package com.example.avocado.db.dict_with_words;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.example.avocado.db.Converters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity(indices = {@Index(value = "title", unique = true)})
public class Dict {
    @PrimaryKey(autoGenerate = true)
    private int dictID;
    @NonNull
    private String title;
    @NonNull
    private Date createdTime;
    @NonNull
    private Date modifiedTime;

    public Dict(String title)
    {
        if(title==null||title.isEmpty())
        {
            this.title= Converters.dateToTimestamp(new Date());
        }
        else {
            this.title = title;
        }
        this.createdTime=new Date();
        this.modifiedTime=new Date();
    }
    public int getDictID() {
        return dictID;
    }

    public void setDictID(int dictID) {
        this.dictID = dictID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime=modifiedTime;
    }
    public void setCreatedTime(Date createdTime) {
        this.createdTime=createdTime;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreatedTime() {return createdTime;}

    public Date getModifiedTime() {
        return this.modifiedTime;
    }

    @NonNull
    @Override
    public String toString() {
        return this.dictID+" "+this.title+" "+this.createdTime+" "+this.modifiedTime;
    }
}

