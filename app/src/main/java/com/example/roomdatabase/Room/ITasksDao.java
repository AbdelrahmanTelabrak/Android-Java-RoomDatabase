package com.example.roomdatabase.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ITasksDao {

    @Query("SELECT * FROM tasks WHERE status = 'Active' ")
    List<Tasks> getActiveTasks();

    @Query("SELECT * FROM tasks WHERE status = 'Done' ")
    List<Tasks> getDoneTasks();

    @Query("SELECT * FROM tasks WHERE status = 'Archive' ")
    List<Tasks> getArchiveTasks();

    @Query("DELETE FROM tasks")
    void deleteAll();

    @Insert
    void insertTask(Tasks task);

    @Delete
    void deleteTask(Tasks task);

    @Update
    void updateTask(Tasks task);
}
