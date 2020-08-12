package com.example.notepad.db;

import androidx.lifecycle.LiveData;
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
    void insert(Note... notes);

    @Update
    void update(Note... notes);

    @Delete
    void delete(Note... notes);

    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getAllNotes();
}
