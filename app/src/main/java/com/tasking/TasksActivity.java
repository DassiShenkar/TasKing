package com.tasking;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Bundle userParams = getIntent().getExtras();
        ArrayList<Task> tasks = TaskDAO.getInstance(this).getTasks(userParams.getString("uid"), userParams.getBoolean("isManager"));
        if(userParams.getString("taskUid") != null) {
            userParams.remove("taskUid");
        }
        TextView createTask = (TextView) findViewById(R.id.task_text);
        ImageView arrow = (ImageView) findViewById(R.id.task_img_arrow);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(!userParams.getBoolean("isManager")){
            createTask.setText(getResources().getString(R.string.refresh_task));
            arrow.setScaleX(-1f);
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_member_main_drawer);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        int numOfTasks = tasks.size();
        for(Task task: tasks){
            if(!task.getStatus().equals("Waiting")){
                numOfTasks--;
            }
        }
        String waitingTab = "WAITING     " + String.valueOf(numOfTasks);
        Typeface boldTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(waitingTab));
        tabLayout.addTab(tabLayout.newTab().setText("ALL TASKS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(boldTypeFace);
                    ((TextView) tabViewChild).setTextColor(Color.parseColor("#111a1e"));
                    ((TextView) tabViewChild).setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                }
            }
        }
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        createTask.setTypeface(typeFace);
        if (tasks.size() > 0) {
            createTask.setVisibility(View.GONE);
            arrow.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle userParams = getIntent().getExtras();
        if(userParams.getBoolean("isManager")) {
            if (id == R.id.nav_members_list) {
                Intent intent = new Intent(this, TeamActivity.class);
                intent.putExtras(userParams);
                startActivity(intent);

            } else if (id == R.id.nav_add_member) {
                Intent intent = new Intent(this, AddMemberActivity.class);
                intent.putExtras(userParams);
                startActivity(intent);

            } else if (id == R.id.nav_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtras(userParams);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {
                Bundle activityCheck = new Bundle();
                activityCheck.putBoolean("isLoginActivity", true);
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtras(activityCheck);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if (id == R.id.nav_about) {
            }
        }
        else{
            if (id == R.id.nav_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtras(userParams);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {
                Bundle activityCheck = new Bundle();
                activityCheck.putBoolean("isLoginActivity", true);
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtras(activityCheck);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if (id == R.id.nav_about) {
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void addTask(View view){
        Bundle userParams = getIntent().getExtras();
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);
    }

    public void refresh(View view){
        Bundle userParams = getIntent().getExtras();
        FireBaseDB.getInstance(this).refresh(userParams, new MyCallback<String>() {
            @Override
            public void done(String result, String error, String managerUid, boolean isManager, boolean hasTeam) {
                if(error != null){
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
