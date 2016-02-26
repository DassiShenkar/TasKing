package com.tasking;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class TeamRecyclerAdapter extends RecyclerView.Adapter<TeamRecyclerAdapter
        .ViewHolder> {

    private ArrayList<Employee> employees;

    public  TeamRecyclerAdapter(ArrayList<Employee> employees){
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.userName.setText(employee.getUserName());
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView userName;
        private Button delete;
        public ViewHolder(View parentView) {
            super(parentView);
            title = (TextView) parentView.findViewById(R.id.txt_team_list_title);
            userName = (TextView) parentView.findViewById(R.id.txt_team_list_name);
            delete = (Button) parentView.findViewById(R.id.btn_team_delete);
        }
    }
}
