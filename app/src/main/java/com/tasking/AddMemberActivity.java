package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Map;

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

    public void editMember(View view) {
        RelativeLayout wrapper = (RelativeLayout) findViewById(R.id.team_members_wrapper);
        RelativeLayout editWrapper = (RelativeLayout) findViewById(R.id.edit_members_wrapper);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.team_recycler_view);
        recyclerView.setVisibility(View.GONE);
        wrapper.setVisibility(View.GONE);
        editWrapper.setVisibility(View.VISIBLE);
    }

    public void addMember(View view){
        teamMembers = new ArrayList<>();
        EditText name = (EditText) findViewById(R.id.edit_team_name);
        teamName = name.getText().toString();
        if(!teamName.equals("")) {
            TextView add = (TextView) findViewById(R.id.add_member_btn);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText email = (EditText) findViewById(R.id.edit_member_email);
                    EditText phone = (EditText) findViewById(R.id.edit_member_phone);
                    String emailStr = email.getText().toString();
                    String phoneStr = phone.getText().toString();
                    Bundle userParams = getIntent().getExtras();
                    if (!emailStr.equals("") || !phoneStr.equals("")){
                        Employee employee;
                        ArrayList<Employee> employees = TaskDAO.getInstance(getApplicationContext()).getMembers(userParams.getString("uid"));
                        email.setText("");
                        phone.setText("");
                        employee = new TeamMember(emailStr, phoneStr);
                        teamMembers.add(employee);
                        final Employee employeeAdd = employee;
                        final Bundle managerParams = getIntent().getExtras();
                        final Firebase firebase = new Firebase("https://tasking-android.firebaseio.com/");
                        firebase.createUser(employee.getUserName(), employee.getPassword(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                            @Override
                            public void onSuccess(Map<String, Object> result) {
                                String uid = result.get("uid").toString();
                                String managerUid = managerParams.getString("uid");
                                if (managerUid != null) {
                                    firebase.child("managers").child(managerUid).child("team").child("teamName").setValue(teamName);
                                    firebase.child("managers").child(managerUid).child("team").child(uid).child("username").setValue(employeeAdd.getUserName());
                                }
                                firebase.child("member-manager").child(uid).setValue(managerUid);
                                employeeAdd.setUid(uid);
                                employeeAdd.setManagerId(managerUid);
                                TaskDAO.getInstance(getApplicationContext()).addMember(employeeAdd);
                                Toast.makeText(getApplicationContext(), "Member added", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        if (employees.size() > 0){
                            RelativeLayout wrapperEdit = (RelativeLayout) findViewById(R.id.edit_members_wrapper);
                            RelativeLayout addMember = (RelativeLayout) findViewById(R.id.team_members_wrapper);
                            wrapperEdit.setVisibility(View.GONE);
                            addMember.setVisibility(View.VISIBLE);
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.team_recycler_view);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            RecyclerView.Adapter adapter = new TeamRecyclerAdapter(employees, getApplicationContext());
                            recyclerView.setAdapter(adapter);
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

    public void sendInvites(View view){
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
                //TODO: put google play link in mail body
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
