package com.tasking;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Grisha on 2/22/2016.
 */
public class TeamRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter
        .ViewHolder> {

    @Override
    public TasksRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TasksRecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View parentView) {
            super(parentView);
        }
    }
}
