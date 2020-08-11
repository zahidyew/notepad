package com.example.notepad;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {
    private Context mContext;
    private List<Note> noteList;
    private Activity activity;
    private String username;
    private boolean justIn = true;

    public NoteAdapter(Activity activity, Context mContext, List<Note> noteList) {
        this.activity = activity;
        this.mContext = mContext;
        this.noteList = noteList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.note.setText(note.getNote());

        holder.done.setOnClickListener(v -> {
            if(justIn) {
                holder.done.setImageResource(R.drawable.ic_add_white_24dp);
                justIn = false;
            }else {
                holder.done.setImageResource(R.drawable.check_icon);
                justIn = true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    void updateData(List<Note> notes) {
        this.noteList = notes;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public EditText title, note;
        public ImageButton done;

        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            note = view.findViewById(R.id.note);
            done = view.findViewById(R.id.done);
        }
    }
}
