package com.example.notepad.db;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.notepad.Note;

import java.util.List;

public class DatabaseOperations {
    private Context context;
    private Activity activity;
    private NoteDAO noteDAO;
    private LiveData<List<Note>> allNotes;

    public DatabaseOperations(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public NoteDAO init() {
        /*noteDAO = Room.databaseBuilder(context, AppDatabase.class, "db-notes")
                .allowMainThreadQueries()
                .build()
                .getNoteDAO();*/

        noteDAO = AppDatabase.getDatabase(context).getNoteDAO();

        // gotta need to make the getNotes func returns LiveData
        //allNotes = noteDAO.getNotes();
        //mAllWords = mWordDao.getAlphabetizedWords();

        return noteDAO;
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public void insertNewNote(Note item) {
        AppDatabase.databaseWriteExecutor.execute(()-> {
            noteDAO.insert(item);
        });

        /*try{
            noteDAO.insert(item);
            activity.setResult(RESULT_OK);
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "Some error occurs while inserting data.", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void updateNote(Note item) {
        AppDatabase.databaseWriteExecutor.execute(()-> {
            noteDAO.update(item);
        });

        /*try{
            noteDAO.update(item);
            activity.setResult(RESULT_OK);
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "Some error occurs while updating data.", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void deleteNote(Note item) {
        AppDatabase.databaseWriteExecutor.execute(()-> {
            noteDAO.delete(item);
        });

        /*try{
            noteDAO.delete(item);
            activity.setResult(RESULT_OK);
        }catch (SQLiteConstraintException e) {
            Toast.makeText(context, "Some error occurs while deleting data.", Toast.LENGTH_SHORT).show();
        }*/
    }
}
