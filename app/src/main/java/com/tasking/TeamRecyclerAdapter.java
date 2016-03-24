package com.tasking;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class TeamRecyclerAdapter extends RecyclerView.Adapter<TeamRecyclerAdapter
        .ViewHolder> {

    private ArrayList<String> employees;
    private Context context;

    public  TeamRecyclerAdapter(ArrayList<String> employees, Context context){
        this.employees = employees;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TeamRecyclerAdapter.ViewHolder holder, final int position) {
        String employee = employees.get(position);
        holder.userName.setText(employee);
        holder.deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
            }
        });
    }

    public void delete(int position) {
        String employeeStr = employees.get(position);
        Employee employee = TaskDAO.getInstance(context).getMember(employeeStr);
        TaskDAO.getInstance(context).removeMember(employee);
        //TODO: remove in firebase
        employees.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private ImageView deleteUser;
        public ViewHolder(View parentView) {
            super(parentView);
            userName = (TextView) parentView.findViewById(R.id.txt_team_list_name);
            deleteUser = (ImageView) parentView.findViewById(R.id.btn_team_delete);
            Typeface regularTypeFace = Typeface.createFromAsset(parentView.getContext().getAssets(), "fonts/Raleway-Regular.ttf");
            userName.setTypeface(regularTypeFace);
        }
    }
}
