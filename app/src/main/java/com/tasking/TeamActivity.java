package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TeamActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tasking.R.layout.activity_team);
        Bundle userParams = getIntent().getExtras();
        String uid = userParams.getString("uid");
        TextView title = (TextView)findViewById(R.id.title);
        TextView membersTitle = (TextView)findViewById(R.id.members_title);
        TextView createTeam = (TextView)findViewById(R.id.team_text);
        ImageView arrow = (ImageView) findViewById(R.id.img_arrow);
        Typeface boldTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        Typeface regularTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        title.setTypeface(boldTypeFace);
        membersTitle.setTypeface(regularTypeFace);
        membersTitle.setVisibility(View.GONE);
        createTeam.setTypeface(regularTypeFace);
        ArrayList<Employee> employees = TaskDAO.getInstance(this).getMembers(uid);
        if (employees.size() > 0){
            membersTitle.setVisibility(View.VISIBLE);
            createTeam.setVisibility(View.GONE);
            arrow.setVisibility(View.GONE);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.team_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new TeamRecyclerAdapter(employees, this, userParams);
            recyclerView.setAdapter(adapter);
        }
        FloatingActionButton addMember = (FloatingActionButton) findViewById(R.id.add_btn);
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle userParams = getIntent().getExtras();
                Intent intent = new Intent(TeamActivity.this, AddMemberActivity.class);
                intent.putExtras(userParams);
                startActivity(intent);
            }
        });
    }
}