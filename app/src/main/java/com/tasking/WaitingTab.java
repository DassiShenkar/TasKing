package com.tasking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class WaitingTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_waiting_tab, container,
                false);
        Bundle userParams = getActivity().getIntent().getExtras();
        ArrayList<String> headers = new ArrayList<>();
        headers.add("TODAY");
        headers.add("TOMORROW");
        headers.add("THIS WEEK");
        //TODO: separate task list to 4 lists by headers and send to the adapter
        ArrayList<Task> tasks = TaskDAO.getInstance(getContext()).getTasks(userParams.getString("userName"));
        if (tasks != null) {
            //TODO: compare two dates for sorting
            /*Collections.sort(tasks, new Comparator<Task>() {
                public int compare(Task task1, Task task2)  {
                    SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
                    Date date1 = format.parse(task1.getDueDate());
                    Date date2 = format.parse(task2.getDueDate());
                    if (date1.getTime() == date2.getTime())
                        return 0;
                    return date1.getTime() < date2.getTime() ? -1 : 1;
                }
            });*/
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.tasks_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new TasksRecyclerAdapter(tasks, headers);
            recyclerView.setAdapter(adapter);
        }
        return rootView;
    }

}
