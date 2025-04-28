package com.example.merchapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.merchapp.model.Task;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks = new ArrayList<>();
    private OnTaskClickListener clickListener;
    private OnTaskLongClickListener longClickListener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    public interface OnTaskLongClickListener {
        void onTaskLongClick(Task task, View view, int position);
    }

    public TaskAdapter(OnTaskClickListener clickListener, OnTaskLongClickListener longClickListener) {
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.storeNameTextView.setText(task.getStore().getName());
        holder.taskDetailsTextView.setText("Photos: " +
                (task.isRequiresCashRegisterPhoto() ? "Cash" : "") +
                (task.isRequiresMainZonePhoto() ? " Zone" : ""));
        holder.completeButton.setEnabled(!task.isCompleted());
        holder.completeButton.setOnClickListener(v -> clickListener.onTaskClick(task));
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onTaskLongClick(task, v, holder.getAdapterPosition());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView storeNameTextView, taskDetailsTextView;
        Button completeButton;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            storeNameTextView = itemView.findViewById(R.id.storeNameTextView);
            taskDetailsTextView = itemView.findViewById(R.id.taskDetailsTextView);
            completeButton = itemView.findViewById(R.id.completeButton);
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }
}