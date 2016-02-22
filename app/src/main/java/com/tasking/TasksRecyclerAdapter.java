package com.tasking;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter
        .ViewHolder> {

    ArrayList<Task> tasks;

    public TasksRecyclerAdapter(ArrayList<Task> tasks){
        this.tasks = tasks;
    }

    @Override
    public TasksRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TasksRecyclerAdapter.ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.description.setText(task.getName());
        holder.category.setText(task.getCategory());
        holder.date.setText(task.getDueDate());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView description;
        private TextView category;
        private TextView date;
        private Button viewEdit;

        public ViewHolder(View parentView) {
            super(parentView);
            description = (TextView) parentView.findViewById(com.tasking.R.id.list_item_description);
            category = (TextView) parentView.findViewById(com.tasking.R.id.list_item_category);
            date = (TextView) parentView.findViewById(com.tasking.R.id.list_item_dueDate);
            viewEdit = (Button) parentView.findViewById(com.tasking.R.id.list_item_view_edit);
        }
    }
}
