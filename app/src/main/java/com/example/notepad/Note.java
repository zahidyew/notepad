package com.example.notepad;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Note")
public class Note {

    @PrimaryKey(autoGenerate = true) // set noteId as Primary key
    private int noteId;
    private String title, note;
    private Date date;

    public int getNoteId() {
        return noteId;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public Date getDate() {
        return date;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
