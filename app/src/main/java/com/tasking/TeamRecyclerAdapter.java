package com.tasking;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class TeamRecyclerAdapter extends RecyclerView.Adapter<TeamRecyclerAdapter
        .ViewHolder> {

    private ArrayList<String> employees;

    public  TeamRecyclerAdapter(ArrayList<String> employees){
        this.employees = employees;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TeamRecyclerAdapter.ViewHolder holder, int position) {
        String employee = employees.get(position);
        holder.userName.setText(employee);
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        public ViewHolder(View parentView) {
            super(parentView);
            userName = (TextView) parentView.findViewById(R.id.txt_team_list_name);
            Typeface regularTypeFace = Typeface.createFromAsset(parentView.getContext().getAssets(), "fonts/Raleway-Regular.ttf");
            userName.setTypeface(regularTypeFace);
        }
    }
}
