package il.ac.shenkar.tasking;

import android.app.Activity;
import android.app.ProgressDialog;
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

import java.util.ArrayList;

public class AddMemberActivity extends Activity {

    private ArrayList<Employee> teamMembers;
    private String teamName;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        RelativeLayout wrapper = (RelativeLayout) findViewById(R.id.edit_members_wrapper);
        wrapper.setVisibility(View.GONE);
        EditText tName = (EditText) findViewById(R.id.edit_team_name);
        String name = SaveSharedPreference.getTeamName(AddMemberActivity.this);
        if (!name.equals("")) {
            tName.setText(name);
            tName.setEnabled(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        TextView title = (TextView) findViewById(R.id.title);
        if (!name.equals("")) {
            title.setText(name);
        }
        Typeface boldTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        title.setTypeface(boldTypeFace);
        progress = new ProgressDialog(this, R.style.ProgressCustomTheme);
        progress.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
    }

    public void editMember(View view) {
        RelativeLayout wrapper = (RelativeLayout) findViewById(R.id.team_members_wrapper);
        RelativeLayout editWrapper = (RelativeLayout) findViewById(R.id.edit_members_wrapper);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.team_recycler_view);
        recyclerView.setVisibility(View.GONE);
        wrapper.setVisibility(View.GONE);
        editWrapper.setVisibility(View.VISIBLE);
    }

    public void addMember(View view) {
        EditText name = (EditText) findViewById(R.id.edit_team_name);
        teamName = name.getText().toString();
        if (!teamName.equals("")) {
            TextView add = (TextView) findViewById(R.id.add_member_btn);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText email = (EditText) findViewById(R.id.edit_member_email);
                    EditText phone = (EditText) findViewById(R.id.edit_member_phone);
                    String emailStr = email.getText().toString();
                    String phoneStr = phone.getText().toString();
                    if (!emailStr.equals("") || !phoneStr.equals("")) {
                        Employee employee;
                        email.setText("");
                        phone.setText("");
                        employee = new Employee(emailStr, phoneStr);
                        final Employee employeeAdd = employee;
                        final Bundle managerParams = getIntent().getExtras();
                        String managerUid = managerParams.getString("uid");
                        progress.show();
                        FireBaseDB remote = new FireBaseDB(AddMemberActivity.this);
                        remote.createMember(employee.getUsername(), employee.getPassword(), managerUid, teamName, new MyCallback<String>() {
                            @Override
                            public void done(String result, String error, String managerUid, boolean isManager, boolean hasTeam) {
                                if (error != null) {
                                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                } else {
                                    TaskDAO.getInstance(getApplicationContext()).addMember(employeeAdd, result, managerUid);
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Member added", Toast.LENGTH_SHORT).show();
                                    teamMembers = TaskDAO.getInstance(getApplicationContext()).getMembers(managerUid);
                                    if (teamMembers.size() > 0) {
                                        RelativeLayout wrapperEdit = (RelativeLayout) findViewById(R.id.edit_members_wrapper);
                                        RelativeLayout addMember = (RelativeLayout) findViewById(R.id.team_members_wrapper);
                                        wrapperEdit.setVisibility(View.GONE);
                                        addMember.setVisibility(View.VISIBLE);
                                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.team_recycler_view);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        recyclerView.setHasFixedSize(true);
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                        recyclerView.setLayoutManager(layoutManager);
                                        RecyclerView.Adapter adapter = new TeamRecyclerAdapter(teamMembers, getApplicationContext(), managerParams);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please enter Team name", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendInvites(View view) {
        if (teamMembers != null) {
            for (Employee member : teamMembers) {
                if (member.getUsername().equals("")) {
                    teamMembers.remove(member);
                }
            }
            if (teamMembers.size() > 0) {
                Bundle userParams = getIntent().getExtras();
                Intent sendMail = new Intent(Intent.ACTION_SEND);
                String[] to = new String[teamMembers.size()];
                int i = 0;
                for (Employee member : teamMembers) {
                    to[i++] = member.getUsername();
                }
                String subject = "Let's go TasKing Together";
                String linkToApp = "http://play.google.com/store/apps/details?id=il.ac.shenkar.tasking";
                String body = "Hello\n" + userParams.getString("userName") + " welcomes you to download the TasKing app\nand join the " +
                        teamName + " team\nYou can get the app at " + linkToApp;
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
            } else {
                Toast.makeText(this, "There are no email clients installed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please add memebers or press BACK to return", Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            Intent intent = new Intent(this, TasksActivity.class);
            Bundle userParams = getIntent().getExtras();
            if (teamName != null) {
                SaveSharedPreference.setTeamName(AddMemberActivity.this, teamName);
            }
            intent.putExtras(userParams);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
