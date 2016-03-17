package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddMemberActivity extends Activity {

    private ArrayList<Employee> teamMembers;
    private String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        RelativeLayout wrapper = (RelativeLayout) findViewById(R.id.edit_members_wrapper);
        wrapper.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        TextView title = (TextView)findViewById(R.id.title);
        Typeface boldTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        title.setTypeface(boldTypeFace);
    }

    public void addMember(View view){
        teamMembers = new ArrayList<>();
        EditText name = (EditText) findViewById(R.id.edit_team_name);
        teamName = name.getText().toString();
        if(!teamName.equals("")) {
            RelativeLayout wrapper = (RelativeLayout) findViewById(R.id.edit_members_wrapper);
            wrapper.setVisibility(View.VISIBLE);
            Button add = (Button) findViewById(R.id.add_member_btn);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText email = (EditText) findViewById(R.id.edit_member_email);
                    EditText phone = (EditText) findViewById(R.id.edit_member_phone);
                    String emailStr = email.getText().toString();
                    String phoneStr = phone.getText().toString();
                    if (!emailStr.equals("") || !phoneStr.equals("")){
                        Employee employee;
                        ArrayList<Employee> employees = TaskDAO.getInstance(getApplicationContext()).getMembers();
                        employee = new TeamMember(emailStr, phoneStr, 0);
                        TaskDAO.getInstance(getApplicationContext()).addMember(employee);
                        //TODO: send member details to firebaseDB
                        boolean exists = false;
                        for(Employee employeeName: employees){
                            if(employeeName.getUserName().equals(emailStr)){
                                exists = true;
                                break;
                            }
                        }
                        if(!exists) {
                            Toast.makeText(getApplicationContext(), "Member added", Toast.LENGTH_SHORT).show();
                            email.setText("");
                            phone.setText("");
                            teamMembers.add(employee);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Member already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Please enter Team name", Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View view){
        Intent intent = new Intent(this, TeamActivity.class);
        Bundle userParams = getIntent().getExtras();
        intent.putExtras(userParams);
        startActivity(intent);
    }

    public void done(View view){
        if(teamMembers != null){
            for (Employee member : teamMembers) {
                if(member.getUserName().equals("")){
                    teamMembers.remove(member);
                }
            }
            if(teamMembers.size() > 0) {
                Bundle userParams = getIntent().getExtras();
                Intent sendMail = new Intent(Intent.ACTION_SEND);
                String[] to = new String[teamMembers.size()];
                int i = 0;
                for (Employee member : teamMembers) {
                    to[i++] = member.getUserName();
                }
                teamMembers = null;
                String subject = "Let's go TasKing Together";
                String body = "Hello\n" + userParams.getString("userName") + " welcomes you to download the TasKing app\nand join the " +
                        teamName + " team\nYou can get the app at http://someplace.com";
                sendMail.setType("message/rfc822");
                sendMail.putExtra(Intent.EXTRA_EMAIL, to);
                sendMail.putExtra(Intent.EXTRA_SUBJECT, subject);
                sendMail.putExtra(Intent.EXTRA_TEXT, body);
                try {
                    startActivityForResult(Intent.createChooser(sendMail, "Send mail..."), 1);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There are no email clients installed", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "There are no email clients installed", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Please add memebers or press BACK to return", Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==1)
        {
            Intent intent = new Intent(this, TasksActivity.class);
            Bundle userParams = getIntent().getExtras();
            intent.putExtras(userParams);
            startActivity(intent);
        }
    }
}
