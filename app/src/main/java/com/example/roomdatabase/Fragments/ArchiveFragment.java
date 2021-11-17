package com.example.roomdatabase.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roomdatabase.R;
import com.example.roomdatabase.Room.ITasksDao;
import com.example.roomdatabase.Room.Tasks;
import com.example.roomdatabase.Room.TasksDatabase;
import com.example.roomdatabase.TasksAdapter;

import java.util.List;


public class ArchiveFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_archive, container, false);
    }

    RecyclerView recyclerViewArchive;
    ITasksDao iTasksDao;
    List<Tasks> tasksListArchive;
    TasksAdapter tasksAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iTasksDao = TasksDatabase.getInstance(getContext()).tasksDao();
        tasksListArchive = iTasksDao.getArchiveTasks();
        recyclerViewArchive = view.findViewById(R.id.archive_rv_tasks);
        tasksAdapter = new TasksAdapter(tasksListArchive);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerViewArchive);
        recyclerViewArchive.setAdapter(tasksAdapter);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull  RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    iTasksDao.deleteTask(tasksListArchive.get(viewHolder.getAdapterPosition()));
                    tasksListArchive.remove(viewHolder.getAdapterPosition());
                    tasksAdapter.notifyDataSetChanged();

                }
            };

}