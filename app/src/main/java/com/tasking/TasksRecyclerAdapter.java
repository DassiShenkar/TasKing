package com.tasking;


import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.ViewHolder> {

    private ArrayList<Task> tasks;
    private boolean isManager;

    public TasksRecyclerAdapter(ArrayList<Task> tasks, boolean isManager){
        this.isManager = isManager;
        this.tasks = tasks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TasksRecyclerAdapter.ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.description.setText(task.getName());
        holder.category.setText(task.getCategory());
        String date = (task.convertDateString() + " " + task.convertTimeString());
        holder.date.setText(date);
        if(isManager){
            holder.icon.setImageResource(R.drawable.ic_action_edit);
        }
        else{
            holder.icon.setImageResource(R.drawable.ic_visibility_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView description;
        private TextView category;
        private TextView date;

        public ViewHolder(View parentView) {
            super(parentView);
            description = (TextView) parentView.findViewById(R.id.list_item_description);
            category = (TextView) parentView.findViewById(R.id.list_item_category);
            date = (TextView) parentView.findViewById(R.id.list_item_dueDate);
            icon = (ImageView) parentView.findViewById(R.id.edit_task_icon);
            Typeface regularTypeFace = Typeface.createFromAsset(parentView.getContext().getAssets(), "fonts/Raleway-Regular.ttf");
            description.setTypeface(regularTypeFace);
            category.setTypeface(regularTypeFace);
            date.setTypeface(regularTypeFace);
        }
    }
}
