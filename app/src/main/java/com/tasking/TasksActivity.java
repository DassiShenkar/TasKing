package com.tasking;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {

    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        Typeface boldTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("WAITING"));
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
        TextView createTask = (TextView) findViewById(R.id.task_text);
        createTask.setTypeface(typeFace);
        ImageView arrow = (ImageView) findViewById(R.id.task_img_arrow);
        Bundle userParams = getIntent().getExtras();
        tasks = TaskDAO.getInstance(this).getTasks(userParams.getString("userName"));//TODO: change getTasks method
        if(tasks != null){
            createTask.setVisibility(View.GONE);
            arrow.setVisibility(View.GONE);
            //TODO: insert tasks list
        }
    }
    public void addTask(View view){
        Bundle userParams = getIntent().getExtras();
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);
    }

    public void refresh(View view){
        //TODO: after BackEnd support
    }
}
