package com.example.notepad.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notepad.Note;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert
    public void insert(Note... notes);

    @Update
    public void update(Note... notes);

    @Delete
    public void delete(Note... notes);

    @Query("SELECT * FROM Note")
    public List<Note> getNotes();
}
