package com.example.roomdatabase.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Tasks implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Title")
    private String title;

    @ColumnInfo(name = "Date")
    private String date;

    @ColumnInfo(name = "Time")
    private String time;

    @ColumnInfo(name = "Status")
    private String status = "Active";

    public Tasks(String title, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatusDone(){
        this.status = "Done";
    }

    public void setStatusArchive(){
        this.status = "Archive";
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
