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
            RecyclerView.Adapter adapter = new TeamRecyclerAdapter(employees, this);
            recyclerView.setAdapter(adapter);
        }
    }

    public void addMember(View view){
        Bundle userParams = getIntent().getExtras();
        Intent intent = new Intent(this, AddMemberActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);
    }
}