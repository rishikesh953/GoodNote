package com.google.goodnote.Models;

public class NoteClass {

    private String note, title;

    public NoteClass() {
    }

    public NoteClass(String note, String title) {
        this.note = note;
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
