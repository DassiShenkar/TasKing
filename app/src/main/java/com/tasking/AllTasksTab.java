package com.tasking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;


public class AllTasksTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_tasks_tab, container,
                false);
        Bundle userParams = getActivity().getIntent().getExtras();
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spn_all_sort_by);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.tasks_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSpinnerItem = spinner.getSelectedItem().toString();
                switch(selectedSpinnerItem){
                    case "STATUS":
                        break;
                    case "TIME":
                        break;
                    case "PRIORITY":
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayList<Task> tasks = TaskDAO.getInstance(getContext()).getTasks(userParams.getString("userName"));
        ArrayList<String> headers = new ArrayList<>();
        headers.add("TODAY");
        headers.add("TOMORROW");
        headers.add("THIS WEEK");
        //TODO: separate task list to 4 lists by headers and send to the adapter
        if (tasks != null) {
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.all_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new TasksRecyclerAdapter(tasks, headers);
            recyclerView.setAdapter(adapter);
        }
        return rootView;
    }
}
