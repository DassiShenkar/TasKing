package com.tasking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class WaitingTab extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_waiting_tab, container,
                false);
        final Bundle userParams = getActivity().getIntent().getExtras();
        final ArrayList<Task> tasks = TaskDAO.getInstance(getContext()).getTasks();
        if (tasks.size() > 0) {
            Collections.sort(tasks, new Comparator<Task>() {
                public int compare(Task task1, Task task2) {
                    return task1.getDate().compareTo(task2.getDate());
                }
            });
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.tasks_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new TasksRecyclerAdapter(tasks);
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    //TODO: get isManager from Bundle DASSI
                                    //Employee employee = TaskDAO.getInstance(getContext()).getTeamMember(userParams.getString("userName"));
                                    if (userParams.getBoolean("isManager")) {
                                        int taskId = tasks.get(position).getId();
                                        userParams.putInt("taskId", taskId);
                                        Intent intent = new Intent(getContext(), AddTaskActivity.class);
                                        intent.putExtras(userParams);
                                        startActivity(intent);
                                    } else {
                                        int taskId = tasks.get(position).getId();
                                        userParams.putInt("taskId", taskId);
                                        Intent intent = new Intent(getContext(), ViewTaskActivity.class);
                                        intent.putExtras(userParams);
                                        startActivity(intent);
                                    }
                                }
                            })
            );
        }
        return rootView;
    }
}
