package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
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
                    EditText name = (EditText) findViewById(R.id.edit_member_name);
                    EditText email = (EditText) findViewById(R.id.edit_member_email);
                    EditText phone = (EditText) findViewById(R.id.edit_member_phone);
                    String nameStr = name.getText().toString();
                    String emailStr = email.getText().toString();
                    String phoneStr = phone.getText().toString();
                    if (!nameStr.equals("") || !emailStr.equals("") || !phoneStr.equals("")){
                        Bundle userParams = getIntent().getExtras();
                        Employee employee = TaskDAO.getInstance(getApplicationContext()).getTeamMember(nameStr, userParams.getString("userName"));
                        if(employee == null) {
                            employee = new TeamMember(emailStr, phoneStr, 0);
                            TaskDAO.getInstance(getApplicationContext()).addTeamMember(employee);
                            TaskDAO.getInstance(getApplicationContext()).addMemberManager(employee.getUserName(), userParams.getString("userName"));
                            Toast.makeText(getApplicationContext(), "Member added", Toast.LENGTH_SHORT).show();
                            name.setText("");
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
        for (Employee member : teamMembers) {
            if(member.getUserName().equals("")){
                teamMembers.remove(member);
            }
        }
        if(teamMembers.size() > 0) {
            Bundle userParams = getIntent().getExtras();
            Intent sendMail = new Intent(Intent.ACTION_SEND);
            String[] to = new String[teamMembers.size()];
            Employee manager = TaskDAO.getInstance(this).getTeamMember(userParams.getString("userName"));
            int i = 0;
            for (Employee member : teamMembers) {
                to[i++] = member.getUserName();
            }
            teamMembers = null;
            String subject = "Let's go TasKing Together";
            String body = "Hello\n" + manager.getUserName() + " welcomes you to download the TasKing app\nand join the " +
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
            Toast.makeText(this, "No new members were added", Toast.LENGTH_SHORT).show();
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
