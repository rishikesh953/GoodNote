package com.google.goodnote.Models;

public class NewTask {

   private  String task, title, date, time;
   private int priority;

    public NewTask() {
    }

    public NewTask(String title, String task, String date, String time, int priority) {
        this.task = task;
        this.title = title;
        this.date = date;
        this.time = time;
        this.priority = priority;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
