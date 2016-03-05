package com.tasking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class AllTasksTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_tasks_tab, container,
                false);
        Bundle userParams = getActivity().getIntent().getExtras();
        ArrayList<Task> tasks = TaskDAO.getInstance(getContext()).getTasks(userParams.getString("userName"));
        if (tasks != null) {
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.all_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new TasksRecyclerAdapter(tasks, "TODAY");
            recyclerView.setAdapter(adapter);
            return recyclerView;
        }
        return rootView;
    }
}
