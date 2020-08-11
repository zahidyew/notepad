package com.example.notepad.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.notepad.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDAO getNoteDAO();
}

