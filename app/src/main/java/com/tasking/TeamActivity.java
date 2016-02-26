package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TeamActivity extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tasking.R.layout.activity_team);
        Bundle userParams = getIntent().getExtras();
        TextView title = (TextView)findViewById(R.id.title);
        TextView createTeam = (TextView)findViewById(R.id.team_text);
        ImageView arrow = (ImageView) findViewById(R.id.img_arrow);
        Typeface boldTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        Typeface regularTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        title.setTypeface(boldTypeFace);
        createTeam.setTypeface(regularTypeFace);
        String userName = userParams.getString("userName");
        ArrayList<Employee> team = TaskDAO.getInstance(this).getTeamMembers(userName);
        if (team != null){
            createTeam.setVisibility(View.GONE);
            arrow.setVisibility(View.GONE);
            mRecyclerView = (RecyclerView) findViewById(R.id.team_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new TeamRecyclerAdapter(team);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
    public void addMember(View view){
        Bundle userParams = getIntent().getExtras();
        Intent intent = new Intent(this, AddMemberActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);
    }
}