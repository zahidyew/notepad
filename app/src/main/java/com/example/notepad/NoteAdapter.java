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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.db.DatabaseOperations;
import com.example.notepad.db.NoteDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {
    private Context mContext;
    private List<Note> noteList;
    private Activity activity;
    private ConstraintLayout mainLayout;
    private FloatingActionButton fab;

    public NoteAdapter(Activity activity, Context mContext, List<Note> noteList) {
        this.activity = activity;
        this.mContext = mContext;
        this.noteList = noteList;
    }

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
        Note note = noteList.get(position);

        holder.title.setText(note.getTitle());
        holder.note.setText(note.getNote());

        holder.deleteBtn.setOnClickListener(v -> {
            deleteNote(note);
        });

        holder.editBtn.setOnClickListener(v -> {
            LayoutInflater inflater = activity.getLayoutInflater();
            View makeNotePage = inflater.inflate(R.layout.make_note_page, mainLayout, false);
            mainLayout.addView(makeNotePage);
            fab.hide();

            ImageButton cancelBtn = makeNotePage.findViewById(R.id.cancel_button);
            ImageButton doneBtn = makeNotePage.findViewById(R.id.button);

            EditText writtenTitle = makeNotePage.findViewById(R.id.title);
            EditText writtenNote = makeNotePage.findViewById(R.id.note);
            writtenTitle.setText(note.getTitle());
            writtenNote.setText(note.getNote());

            doneBtn.setOnClickListener(v2 -> {
                note.setTitle(writtenTitle.getText().toString());
                note.setNote(writtenNote.getText().toString());

                editNote(note);

                mainLayout.removeView(makeNotePage);
                hideKeyboard();
                fab.show();
            });

            cancelBtn.setOnClickListener(v2 -> {
                mainLayout.removeView(makeNotePage);
                hideKeyboard();
                fab.show();
            });
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void updateData(List<Note> notes) {
        this.noteList = notes;
        notifyDataSetChanged();
    }

    private void editNote(Note note) {
        DatabaseOperations databaseOperations = new DatabaseOperations(activity, mContext);
        NoteDAO noteDAO = databaseOperations.init();
        databaseOperations.updateNote(note);
        updateData(noteDAO.getNotes());
    }

    private void deleteNote(Note note) {
        DatabaseOperations databaseOperations = new DatabaseOperations(activity, mContext);
        NoteDAO noteDAO = databaseOperations.init();
        databaseOperations.deleteNote(note);
        updateData(noteDAO.getNotes());
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
        public TextView title, note;
        public ImageButton editBtn, deleteBtn;

        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            note = view.findViewById(R.id.note);
            editBtn = view.findViewById(R.id.edit_btn);
            deleteBtn = view.findViewById(R.id.delete_btn);
        }
    }
}
