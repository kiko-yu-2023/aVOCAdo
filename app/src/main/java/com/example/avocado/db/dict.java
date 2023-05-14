package com.example.avocado.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "dict_table")
public class dict {
    @PrimaryKey(autoGenerate = true)
    private int dictID;
    private String title;
    private final String date;

    public dict(String title)
    {
        this.title=title;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.date=formatter.format(date);
    }

    public int getDictID() {
        return dictID;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}
