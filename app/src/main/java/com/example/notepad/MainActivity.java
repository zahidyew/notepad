package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notepad.db.AppDatabase;
import com.example.notepad.db.NoteDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout mainLayout;
    private NoteDAO noteDAO;
    private NoteAdapter adapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Notepad");
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        mainLayout = findViewById(R.id.main_activity);

        noteDAO = Room.databaseBuilder(this, AppDatabase.class, "db-notes")
                .allowMainThreadQueries()
                .build()
                .getNoteDAO();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        //List<Note> noteList = new ArrayList<>();
        adapter = new NoteAdapter(this, this, new ArrayList<Note>());

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        floatingActionButton = findViewById(R.id.floating_btn);
        floatingActionButton.setOnClickListener(view -> {
            //Toast.makeText(this, "Add new note", Toast.LENGTH_SHORT).show();
            addNewNote();
        });

        loadNotes();
    }

    private void addNewNote() {
        floatingActionButton.hide();

        LayoutInflater inflater = getLayoutInflater();
        View makeNotePage = inflater.inflate(R.layout.make_note_page, mainLayout, false);
        mainLayout.addView(makeNotePage);

        Button cancelBtn = makeNotePage.findViewById(R.id.cancel_button);
        Button doneBtn = makeNotePage.findViewById(R.id.button);
        EditText writtenTitle = makeNotePage.findViewById(R.id.title);
        EditText writtenNote = makeNotePage.findViewById(R.id.note);

        doneBtn.setOnClickListener(v -> {
            mainLayout.removeView(makeNotePage);

            Note item = new Note();
            item.setTitle(writtenTitle.getText().toString());
            item.setNote(writtenNote.getText().toString());

            try{
                noteDAO.insert(item);
                setResult(RESULT_OK);
            } catch (SQLiteConstraintException e) {
                Toast.makeText(this, "Some error occurs while inserting data.", Toast.LENGTH_SHORT).show();
            }

            floatingActionButton.show();
            loadNotes();
        });

        cancelBtn.setOnClickListener(v -> {
            mainLayout.removeView(makeNotePage);
            floatingActionButton.show();
        });
        /*Note item = new Note();
        item.setTitle("Testing");
        item.setNote("See if this sql thing works");

        try{
            noteDAO.insert(item);
            setResult(RESULT_OK);
        } catch (SQLiteConstraintException e) {
            Toast.makeText(this, "Some error occurs while inserting data.", Toast.LENGTH_SHORT).show();
        }*/

        //loadNotes();
    }

    private void loadNotes() {
        adapter.updateData(noteDAO.getNotes());
    }
}


