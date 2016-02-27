package com.tasking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class WaitingTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getView() != null) {//TODO: fix getView() ---> returns null
            ImageView arrow = (ImageView) getView().findViewById(R.id.task_img_arrow);
            TextView createTask = (TextView) getView().findViewById(R.id.task_text);
            Bundle userParams = getActivity().getIntent().getExtras();
            ArrayList<Task> tasks = TaskDAO.getInstance(getContext()).getTasks(userParams.getString("userName"));
            if (tasks != null) {
                createTask.setVisibility(View.GONE);
                arrow.setVisibility(View.GONE);
                RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.tasks_recycler_view);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                RecyclerView.Adapter adapter = new TasksRecyclerAdapter(tasks);
                recyclerView.setAdapter(adapter);
                return recyclerView;
            }
        }
        return inflater.inflate(R.layout.fragment_waiting_tab, container,
                false);
    }

}
