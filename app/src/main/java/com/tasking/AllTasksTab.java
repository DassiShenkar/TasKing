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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class AllTasksTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<Task> newTasks;
    private ArrayList<Employee> newEmployees;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_tasks_tab, container,
                false);
        final Bundle userParams = getActivity().getIntent().getExtras();
        final ArrayList<Task> tasks = TaskDAO.getInstance(getContext()).getTasks();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.all_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter emptyAdapter = new TasksRecyclerAdapter(new ArrayList<Task>(), userParams.getBoolean("isManager"));
        recyclerView.setAdapter(emptyAdapter);
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spn_all_sort_by);
        TextView spinnerText = (TextView) rootView.findViewById(R.id.txt_all_sort_by);
        spinner.setVisibility(View.GONE);
        spinnerText.setVisibility(View.GONE);
        if (tasks.size() > 0) {
            spinner.setVisibility(View.VISIBLE);
            spinnerText.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.tasks_array, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            Collections.sort(tasks, new Comparator<Task>() {
                public int compare(Task task1, Task task2) {
                    return task1.getDate().compareTo(task2.getDate());
                }
            });
            final RecyclerView.Adapter adapter = new TasksRecyclerAdapter(tasks, userParams.getBoolean("isManager"));
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
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedSpinnerItem = spinner.getSelectedItem().toString();

                    final Map<String, Integer> statusMap = new HashMap<>();
                    final Map<String, Integer> priorityMap = new HashMap<>();
                    statusMap.put("Waiting", 0);
                    statusMap.put("In Progress", 1);
                    statusMap.put("Done", 2);
                    priorityMap.put("Urgent", 0);
                    priorityMap.put("Normal", 1);
                    priorityMap.put("Low", 2);


                    switch (selectedSpinnerItem) {
                        case "Status":
                            Collections.sort(tasks, new Comparator<Task>() {
                                @Override
                                public int compare(Task lhs, Task rhs) {
                                    return statusMap.get(lhs.getStatus()).compareTo(statusMap.get(rhs.getStatus()));
                                }
                            });
                            break;
                        case "Time":
                            Collections.sort(tasks, new Comparator<Task>() {
                                @Override
                                public int compare(Task lhs, Task rhs) {
                                    return lhs.getDate().compareTo(rhs.getDate());
                                }
                            });
                            break;
                        case "Priority":
                            Collections.sort(tasks, new Comparator<Task>() {
                                @Override
                                public int compare(Task lhs, Task rhs) {
                                    return priorityMap.get(lhs.getPriority()).compareTo(priorityMap.get(rhs.getPriority()));
                                }
                            });
                            break;
                        default:
                            break;
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        return rootView;
    }

    @Override
    public void onRefresh() {
        Firebase firebase = new Firebase("https://tasking-android.firebaseio.com/");
        firebase.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.child("Managers").child("Tasks").getChildren()){
                    Task task = snapshot.getValue(Task.class);
                    addToTasks(task);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addToTasks(Task task){
        Bundle userParams = getActivity().getIntent().getExtras();
        if(userParams.getBoolean("isManager")) {
            //TODO: get employees
        }
        if (newTasks == null) {
            newTasks = new ArrayList<>();
        } else {
            newTasks.clear();
        }
        Task compareTask = TaskDAO.getInstance(getContext()).getTask(task.getId());
        if (compareTask == null) {
            //TODO: add task
        } else if (task.getTimeStamp().equals(compareTask.getTimeStamp())) {//TODO: compare dates
            //TODO: update task
        }
    }
}
