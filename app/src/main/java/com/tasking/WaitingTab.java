package com.tasking;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


public class WaitingTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_waiting_tab, container,
                false);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.waiting_swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.tasks_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter emptyAdapter = new TasksRecyclerAdapter(new ArrayList<Task>(), null, false);
        recyclerView.setAdapter(emptyAdapter);
        final Bundle userParams = getActivity().getIntent().getExtras();
        ArrayList<String> boldNames = null;
        if(userParams.getStringArrayList("taskUids") != null){
            boldNames = userParams.getStringArrayList("taskUids");
        }
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Regular.ttf");
        TextView createTask = (TextView) rootView.findViewById(R.id.waiting_task_text);
        createTask.setText(getResources().getString(R.string.create_waiting_task));
        ImageView arrow = (ImageView) rootView.findViewById(R.id.waiting_img_arrow);
        createTask.setTypeface(typeFace);
        if(!userParams.getBoolean("isManager")) {
            createTask.setText(getResources().getString(R.string.refresh_waiting_task));
            arrow.setScaleX(-1f);
        }
        final ArrayList<Task> tasks = TaskDAO.getInstance(getContext()).getTasks(userParams.getString("uid"), userParams.getBoolean("isManager"));
        if(tasks.size() == 0){
            createTask.setVisibility(View.GONE);
            arrow.setVisibility(View.GONE);
        }
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
            RecyclerView.Adapter adapter = new TasksRecyclerAdapter(tasks, boldNames, userParams.getBoolean("isManager"));
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    if (userParams.getBoolean("isManager")) {
                                        String taskUid = tasks.get(position).getFirebaseId();
                                        userParams.putString("taskUid", taskUid);
                                        Intent intent = new Intent(getContext(), AddTaskActivity.class);
                                        intent.putExtras(userParams);
                                        startActivity(intent);
                                    } else {
                                        String taskUid = tasks.get(position).getFirebaseId();
                                        userParams.putString("taskUid", taskUid);
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
        Bundle userParams = getActivity().getIntent().getExtras();
        FireBaseDB remote = new FireBaseDB(getContext());
        remote.refresh(userParams, new MyCallback<String>() {
                @Override
                public void done(String result, String error, String managerUid, boolean isManager, boolean hasTeam) {
                    if (error != null) {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        swipeLayout.setRefreshing(false);
    }
}
