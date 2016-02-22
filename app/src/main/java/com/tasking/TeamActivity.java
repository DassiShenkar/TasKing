package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

        }
    }
    public void addMember(View view){
        Bundle userParams = getIntent().getExtras();
        Intent intent = new Intent(this, AddMemberActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);
    }
}