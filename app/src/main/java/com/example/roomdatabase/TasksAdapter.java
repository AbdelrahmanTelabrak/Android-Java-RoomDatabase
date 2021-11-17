package com.example.roomdatabase;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdatabase.Room.ITasksDao;
import com.example.roomdatabase.Room.Tasks;
import com.example.roomdatabase.Room.TasksDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.Holder> {
    private final List<Tasks> itemTask;

    public TasksAdapter(List<Tasks> tasks) {
        this.itemTask = tasks;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.Holder holder, int position) {

        Tasks tasks = itemTask.get(position);

        holder.textViewTitle.setText(tasks.getTitle());
        holder.textViewDate.setText(tasks.getDate());
        holder.textViewTime.setText(tasks.getTime());

        holder.imageViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemTask.get(position).setStatusDone();
                TasksDatabase.getInstance(v.getContext()).tasksDao().updateTask(itemTask.get(position));
                itemTask.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.imageViewArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemTask.get(position).setStatusArchive();
                TasksDatabase.getInstance(v.getContext()).tasksDao().updateTask(itemTask.get(position));
                itemTask.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemTask.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        TextView textViewDate;
        TextView textViewTime;
        ImageView imageViewDone;
        ImageView imageViewArchive;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.item_taskTitle);
            textViewDate = itemView.findViewById(R.id.item_date);
            textViewTime = itemView.findViewById(R.id.item_time);
            imageViewDone = itemView.findViewById(R.id.item_iv_taskDone);
            imageViewArchive = itemView.findViewById(R.id.item_iv_taskArchive);
        }
    }
}
