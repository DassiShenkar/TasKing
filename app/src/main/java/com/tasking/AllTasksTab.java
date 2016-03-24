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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class AllTasksTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

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
        RecyclerView.Adapter emptyAdapter = new TasksRecyclerAdapter(new ArrayList<Task>());
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
            final RecyclerView.Adapter adapter = new TasksRecyclerAdapter(tasks);
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

                    final Map<String, String> statusMap = new HashMap<>();
                    final Map<String, String> priorityMap = new HashMap<>();
                    statusMap.put("Waiting", "2");
                    statusMap.put("In Progress", "1");
                    statusMap.put("Done", "0");
                    priorityMap.put("Urgent", "2");
                    priorityMap.put("Normal", "1");
                    priorityMap.put("Low", "0");


                    switch (selectedSpinnerItem) {
                        case "STATUS":
                            Collections.sort(tasks, new Comparator<Task>() {
                                @Override
                                public int compare(Task lhs, Task rhs) {
                                    return statusMap.get(lhs.getStatus()).compareTo(statusMap.get(rhs.getStatus()));
                                }
                            });
                            break;
                        case "TIME":
                            Collections.sort(tasks, new Comparator<Task>() {
                                @Override
                                public int compare(Task lhs, Task rhs) {
                                    return lhs.getDate().compareTo(rhs.getDate());
                                }
                            });
                            break;
                        case "PRIORITY":
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
        //TODO: implement refresh logic
    }
}
