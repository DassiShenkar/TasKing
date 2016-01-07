package com.tasking;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;

public class TeamActivity extends AppCompatActivity {

    private ArrayList<String> listItems;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tasking.R.layout.activity_team);
        Toolbar myToolbar = (Toolbar) findViewById(com.tasking.R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        TextView title = (TextView)findViewById(com.tasking.R.id.title);
        TextView createTeam = (TextView)findViewById(com.tasking.R.id.team_text);
        Typeface boldTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        Typeface regularTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        title.setTypeface(boldTypeFace);
        createTeam.setTypeface(regularTypeFace);
        listItems = new ArrayList<>();
        mAdapter = new MyRecyclerAdapter(listItems);
        mRecyclerView = (RecyclerView) findViewById(com.tasking.R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        //todo: ArrayList<Task> userTasks = TaskDAO.getInstance(this).getTasks();
        //todo: if (userTasks != null){
        //todo:    createTeam.setVisibility(TextView.GONE);
        //todo: }
    }
}