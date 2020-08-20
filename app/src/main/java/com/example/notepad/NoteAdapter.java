package com.example.notepad;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.db.DatabaseOperations;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {
    private Context mContext;
    private List<Note> noteList;
    private Activity activity;
    private ConstraintLayout mainLayout;
    private FloatingActionButton fab;

    /*public NoteAdapter(Activity activity, Context mContext, List<Note> noteList) {
        this.activity = activity;
        this.mContext = mContext;
        this.noteList = noteList;
    }*/

    public NoteAdapter(Activity activity, Context mContext, List<Note> noteList, ConstraintLayout mainLayout, FloatingActionButton fab) {
        this.activity = activity;
        this.mContext = mContext;
        this.noteList = noteList;
        this.mainLayout = mainLayout;
        this.fab = fab;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (noteList == null) {
            Toast.makeText(mContext, "Still empty. Add a note.", Toast.LENGTH_SHORT).show();
        }else {
            Note currentNote = noteList.get(position);

            holder.title.setText(currentNote.getTitle());
            holder.dateTime.setText(currentNote.getDate() + ", " + currentNote.getTime());
            holder.note.setText(currentNote.getNote());

            holder.deleteBtn.setOnClickListener(v -> {
                deleteNote(currentNote);
            });

            holder.editBtn.setOnClickListener(v -> {
                goEditingMode(currentNote);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (noteList != null) {
            return noteList.size();
        }else return 0;
    }

    public void updateData(List<Note> notes) {
        this.noteList = notes;
        notifyDataSetChanged();
    }

    private void goEditingMode(Note currentNote) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View makeNotePage = inflater.inflate(R.layout.make_note_page, mainLayout, false);
        mainLayout.addView(makeNotePage);
        fab.hide();

        EditText writtenTitle = makeNotePage.findViewById(R.id.title);
        EditText writtenNote = makeNotePage.findViewById(R.id.note);
        ImageButton cancelBtn = makeNotePage.findViewById(R.id.cancel_button);
        ImageButton doneBtn = makeNotePage.findViewById(R.id.button);

        writtenTitle.setText(currentNote.getTitle());
        writtenNote.setText(currentNote.getNote());

        doneBtn.setOnClickListener(v2 -> {
            currentNote.setTitle(writtenTitle.getText().toString());
            currentNote.setNote(writtenNote.getText().toString());

            editNote(currentNote);
            finishWriting(makeNotePage);
        });

        cancelBtn.setOnClickListener(v2 -> {
            finishWriting(makeNotePage);
        });
    }

    private void editNote(Note note) {
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext);
        databaseOperations.updateNote(note);
    }

    private void deleteNote(Note note) {
        DatabaseOperations databaseOperations = new DatabaseOperations(mContext);
        databaseOperations.deleteNote(note);
    }

    private void finishWriting (View makeNotePage) {
        mainLayout.removeView(makeNotePage);
        hideKeyboard();
        fab.show();
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, note, dateTime;
        public ImageButton editBtn, deleteBtn;

        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            dateTime = view.findViewById(R.id.date_time);
            note = view.findViewById(R.id.note);
            editBtn = view.findViewById(R.id.edit_btn);
            deleteBtn = view.findViewById(R.id.delete_btn);
        }
    }

    /*private void loadNotes(NoteDAO noteDAO) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                noteList = noteDAO.getNotes();
                activity.runOnUiThread(new Runnable() { // Only the original thread that created a view hierarchy can touch its views
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        };
        thread.start();
    }*/

}
