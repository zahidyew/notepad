package com.example.notepad.db;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.notepad.Note;

import java.util.List;

public class DatabaseOperations {
    private NoteDAO noteDAO;
    private LiveData<List<Note>> allNotes;

    public DatabaseOperations(Context context) {
        noteDAO = AppDatabase.getDatabase(context).getNoteDAO();
        allNotes = noteDAO.getAllNotes();
    }

    public LiveData<List<Note>> getAllMyNotes() {
        return allNotes;
    }

    public void insertNewNote(Note item) {
        AppDatabase.databaseWriteExecutor.execute(()-> {
            noteDAO.insert(item);
        });
    }

    public void updateNote(Note item) {
        AppDatabase.databaseWriteExecutor.execute(()-> {
            noteDAO.update(item);
        });
    }

    public void deleteNote(Note item) {
        AppDatabase.databaseWriteExecutor.execute(()-> {
            noteDAO.delete(item);
        });
    }
}
