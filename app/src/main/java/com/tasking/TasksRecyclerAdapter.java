package com.tasking;


import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TasksRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Task> tasks;
    ArrayList<String> headers = new ArrayList<>();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public TasksRecyclerAdapter(ArrayList<Task> tasks, ArrayList<String> headers){
        this.tasks = tasks;
        this.headers = headers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tasks_list_header, parent, false);
            return new HeaderViewHolder(v);
        }
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tasks_list_item, parent, false);
            return new ItemViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position){
        if(position < headers.size()){
            return TYPE_HEADER;
        }
        else{
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //TODO: parse date and check it
        //TODO: check which fragment is it
        if(holder instanceof ItemViewHolder) {
            Task task = tasks.get(position);
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            itemHolder.description.setTag(task.getId());
            itemHolder.description.setText(task.getName());
            itemHolder.category.setText(task.getCategory());
            itemHolder.date.setText(task.getDueDate());
        }
        else{
            String header = headers.get(position);
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.title.setText(header);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size() + headers.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView description;
        private TextView category;
        private TextView date;

        public ItemViewHolder(View parentView) {
            super(parentView);
            description = (TextView) parentView.findViewById(R.id.list_item_description);
            category = (TextView) parentView.findViewById(R.id.list_item_category);
            date = (TextView) parentView.findViewById(R.id.list_item_dueDate);
            Typeface regularTypeFace = Typeface.createFromAsset(parentView.getContext().getAssets(), "fonts/Raleway-Regular.ttf");
            description.setTypeface(regularTypeFace);
            category.setTypeface(regularTypeFace);
            date.setTypeface(regularTypeFace);

        }
    }
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public HeaderViewHolder(View parentView) {
            super(parentView);
            title = (TextView) parentView.findViewById(R.id.tasks_title);
            Typeface regularTypeFace = Typeface.createFromAsset(parentView.getContext().getAssets(), "fonts/Raleway-Regular.ttf");
            title.setTypeface(regularTypeFace);
        }
    }
}
