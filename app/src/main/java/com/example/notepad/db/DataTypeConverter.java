package com.example.notepad.db;

import androidx.room.TypeConverter;

import java.util.Date;

public class DataTypeConverter {
    @TypeConverter
    public long convertDateToLong(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public Date convertLongToDate(Long time) {
        return new Date(time);
    }
}
