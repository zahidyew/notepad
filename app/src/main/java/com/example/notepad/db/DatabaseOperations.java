package com.example.notepad.db;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.widget.Toast;

import androidx.room.Room;

import com.example.notepad.Note;

import static android.app.Activity.RESULT_OK;

public class DatabaseOperations {
    private Context context;
    private Activity activity;
    private NoteDAO noteDAO;

    public DatabaseOperations(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public NoteDAO init() {
        noteDAO = Room.databaseBuilder(context, AppDatabase.class, "db-notes")
                .allowMainThreadQueries()
                .build()
                .getNoteDAO();

        return noteDAO;
    }

    public void insertNewNote(Note item) {
        try{
            noteDAO.insert(item);
            activity.setResult(RESULT_OK);
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "Some error occurs while inserting data.", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateNote(Note item) {
        try{
            noteDAO.update(item);
            activity.setResult(RESULT_OK);
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "Some error occurs while updating data.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteNote(Note item) {
        try{
            noteDAO.delete(item);
            activity.setResult(RESULT_OK);
        }catch (SQLiteConstraintException e) {
            Toast.makeText(context, "Some error occurs while deleting data.", Toast.LENGTH_SHORT).show();
        }
    }
}
