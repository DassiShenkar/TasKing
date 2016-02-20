package com.tasking;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class TeamActivity extends AppCompatActivity {

    private ArrayList<Employee> listItems;
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
        ArrayList<Employee> team = TaskDAO.getInstance(this).getTeamMembers();
        if (team != null){
            createTeam.setTextColor(Color.parseColor("#ffffff"));
            //TODO: remove greg's arrow
            mAdapter = new MyRecyclerAdapter(team);
            mRecyclerView = (RecyclerView) findViewById(com.tasking.R.id.my_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);//TODO: check relative
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
    public void addMember(View view){
        
    }
}