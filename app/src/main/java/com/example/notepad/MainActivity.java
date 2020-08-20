package com.example.notepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.notepad.db.DatabaseOperations;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout mainLayout;
    private NoteAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private DatabaseOperations databaseOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Notepad");
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        databaseOperations = new DatabaseOperations(this);

        mainLayout = findViewById(R.id.main_activity);
        floatingActionButton = findViewById(R.id.floating_btn);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new NoteAdapter(this, this, new ArrayList<Note>(), mainLayout, floatingActionButton);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(view -> {
            //Toast.makeText(this, "Add new note", Toast.LENGTH_SHORT).show();
            addNewNote();
        });

        observeChangesToDB();
    }

    private void observeChangesToDB() {
        // The onChanged() method fires when the observed data changes and the activity is in the foreground.
        databaseOperations.getAllMyNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable final List<Note> notes) {
                // Update the cached copy of the data in the adapter.
                adapter.updateData(notes);
            }
        });
    }

    private void addNewNote() {
        floatingActionButton.hide();

        LayoutInflater inflater = getLayoutInflater();
        View makeNotePage = inflater.inflate(R.layout.make_note_page, mainLayout, false);
        mainLayout.addView(makeNotePage);

        ImageButton cancelBtn = makeNotePage.findViewById(R.id.cancel_button);
        ImageButton doneBtn = makeNotePage.findViewById(R.id.button);
        EditText writtenTitle = makeNotePage.findViewById(R.id.title);
        EditText writtenNote = makeNotePage.findViewById(R.id.note);

        // the return String is "E, dd MMM yy/hh:mm a"
        // eg: Fri, 21 Aug 20/10:54 am
        String[] dateAndTime = getDateAndTime().split("/");
        String date = dateAndTime[0];
        String time = dateAndTime[1];

        doneBtn.setOnClickListener(v -> {
            Note item = new Note();
            item.setTitle(writtenTitle.getText().toString());
            item.setNote(writtenNote.getText().toString());
            item.setDate(date);
            item.setTime(time);

            //Toast.makeText(this, "Date is: " + item.getDate() + "\nTime is: " + item.getTime(), Toast.LENGTH_LONG).show();

            databaseOperations.insertNewNote(item);
            finishWriting(makeNotePage);
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        });

        cancelBtn.setOnClickListener(v -> {
            finishWriting(makeNotePage);
        });
    }

    private void finishWriting (View makeNotePage) {
        mainLayout.removeView(makeNotePage);
        hideKeyboard();
        floatingActionButton.show();
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private String getDateAndTime() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("E, dd MMM yy/hh:mm a"));
        // the return String is "E, dd MMM yy/hh:mm a", so we need to split it later
    }

    /*private void loadNotes() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                adapter.updateData(noteDAO.getNotes());
            }
        };
        thread.start();
    }*/
}


