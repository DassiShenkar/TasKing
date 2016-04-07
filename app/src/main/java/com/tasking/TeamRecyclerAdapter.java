package com.tasking;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class TeamRecyclerAdapter extends RecyclerView.Adapter<TeamRecyclerAdapter
        .ViewHolder> {

    private ArrayList<Employee> employees;
    private Context context;
    private Bundle userParams;

    public  TeamRecyclerAdapter(ArrayList<Employee> employees, Context context, Bundle userParams){
        this.employees = employees;
        this.context = context;
        this.userParams = userParams;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TeamRecyclerAdapter.ViewHolder holder, final int position) {
        Employee employee = employees.get(position);
        holder.userName.setText(employee.getUsername());
        holder.deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
            }
        });
    }

    public void delete(int position) {
        final Employee employee = employees.get(position);
        TaskDAO.getInstance(context).removeMember(employee);
        String password = userParams.getString("password");
        FireBaseDB remote = new FireBaseDB(context);
        remote.removeUser(employee.getUsername(), password, new MyCallback<String>() {
                @Override
                public void done(String result, String error, String managerUid, boolean isManager, boolean hasTeam) {
                    if (error != null) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
