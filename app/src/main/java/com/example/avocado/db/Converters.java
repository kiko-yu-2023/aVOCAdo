package com.example.avocado.db;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters {
    static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @TypeConverter
    public static Date stringToDate(String timestamp) throws ParseException {
        return timestamp == null ? null : formatter.parse(timestamp);
    }

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        return date == null ? null : formatter.format(date);
    }
}