package com.example.taskmanager.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;

    @ColumnInfo(name = "due_date")
    private long dueDate;

    @Ignore
    public Task() {}

    public Task(String title, String description, long dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    // Getter and Setter methods
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getDueDate() { return dueDate; }
    public void setDueDate(long dueDate) { this.dueDate = dueDate; }
}
