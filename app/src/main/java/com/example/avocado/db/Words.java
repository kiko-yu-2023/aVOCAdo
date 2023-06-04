package com.example.avocado.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Words {
    @PrimaryKey
    @NonNull
    public String word;
    public String meaning;
    public String example;
}
