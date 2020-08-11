package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.widget.Toast;

import com.example.notepad.db.AppDatabase;
import com.example.notepad.db.NoteDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteDAO noteDAO;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Notepad");

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

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

        FloatingActionButton floatingActionButton = findViewById(R.id.floating_btn);
        floatingActionButton.setOnClickListener(view -> {
            //Toast.makeText(this, "Add new note", Toast.LENGTH_SHORT).show();
            addNewNote();
        });

        loadNotes();
    }

    private void addNewNote() {
        int requestCode = 1;
        //Intent intent = new Intent(this, MakeNotePage.class);
        //startActivityForResult(intent, requestCode);

        Note item = new Note();
        item.setTitle("Testing");
        item.setNote("See if this sql thing works");

        try{
            noteDAO.insert(item);
            setResult(RESULT_OK);
        } catch (SQLiteConstraintException e) {
            Toast.makeText(this, "Some error occurs while inserting data.", Toast.LENGTH_SHORT).show();
        }

        loadNotes();
    }

    private void loadNotes() {
        adapter.updateData(noteDAO.getNotes());
    }
}


