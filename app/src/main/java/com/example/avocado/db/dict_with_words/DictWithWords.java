package com.example.avocado.db.dict_with_words;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DictWithWords{
    @Embedded
    public Dict dict;

    @Relation(parentColumn = "dictID", entityColumn = "dictID", entity = Word.class)
    public List<Word> words;
}
