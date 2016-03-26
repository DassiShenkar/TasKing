package com.tasking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


public class WaitingTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_waiting_tab, container,
                false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.tasks_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter emptyAdapter = new TasksRecyclerAdapter(new ArrayList<Task>(), false);
        recyclerView.setAdapter(emptyAdapter);
        final Bundle userParams = getActivity().getIntent().getExtras();
        TextView createTask = (TextView) rootView.findViewById(R.id.waiting_task_text);
        createTask.setText(getResources().getString(R.string.create_waiting_task));
        ImageView arrow = (ImageView) rootView.findViewById(R.id.waiting_img_arrow);
        if(!userParams.getBoolean("isManager")) {
            createTask.setText(getResources().getString(R.string.refresh_waiting_task));
            arrow.setScaleX(-1f);
        }
        final ArrayList<Task> tasks = TaskDAO.getInstance(getContext()).getTasks();
        for(Iterator<Task> it = tasks.iterator(); it.hasNext();){
            Task task = it.next();
            if(task.getStatus().equals(getResources().getString(R.string.status_done)) || task.getStatus().equals(getResources().getString(R.string.in_process))){
                it.remove();
            }
        }
        if (tasks.size() > 0) {
            createTask.setVisibility(View.GONE);
            arrow.setVisibility(View.GONE);
            Collections.sort(tasks, new Comparator<Task>() {
                public int compare(Task task1, Task task2) {
                    return task1.getDate().compareTo(task2.getDate());
                }
            });
            RecyclerView.Adapter adapter = new TasksRecyclerAdapter(tasks, userParams.getBoolean("isManager"));
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
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
    @Override
    public void onRefresh() {
        //TODO: implement refresh logic
        //TODO: NEW TASK NOTIFICATION  (Via GCM - OPTIONAL - Extra Credit)
        //TODO: When a New task is sent to TeamMember, they see a GCM Floating Notification.
//TODO:        When the client checks for a new task, and finds one:
//TODO:        NOTIFICATION: “You have received a new Task”. [View Task],[Cancel]
//TODO:        If more than 1 new task:
//TODO:        NOTIFICATION: “You have received some new Tasks”. [View],[Cancel]
//TODO:        On [View] Open WAITING Tab in Main View.
//TODO:        HIGHLIGHT the NEW TASKS (options: Bold Text, Background Color)
//TODO:        VIEW TASK
//TODO:        Open REPORT TASK STATUS View for this Task

    }
}
