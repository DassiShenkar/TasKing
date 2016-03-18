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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class AllTasksTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_tasks_tab, container,
                false);
        final Bundle userParams = getActivity().getIntent().getExtras();
        final ArrayList<Task> tasks = TaskDAO.getInstance(getContext()).getTasks();
        if (tasks.size() > 0) {
            final Spinner spinner = (Spinner) rootView.findViewById(R.id.spn_all_sort_by);
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.tasks_array, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            Collections.sort(tasks, new Comparator<Task>() {
                public int compare(Task task1, Task task2) {
                    return task1.getDate().compareTo(task2.getDate());
                }
            });
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.all_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
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
                    switch (selectedSpinnerItem) {
                        case "STATUS":
                            Collections.sort(tasks, new Comparator<Task>() {
                                @Override
                                public int compare(Task lhs, Task rhs) {
                                    return lhs.getStatus().compareTo(rhs.getStatus());
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
                                    return lhs.getPriority().compareTo(rhs.getPriority());
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
