package com.bayin.indicateprogress;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.bayin.indicateprogress.fragment.AlertFragment;
import com.bayin.indicateprogress.fragment.FindFragment;
import com.bayin.indicateprogress.fragment.HomeFragment;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/11/2.
 ****************************************/

public class EnterActivity extends AppCompatActivity {

    private FrameLayout mFgContainer;
    private BottomNavigationView mNavigationBar;
    private HomeFragment mHomeFragment;
    private FindFragment mFindFragment;
    private AlertFragment mAlertFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fg_layout);
        mFgContainer = (FrameLayout) findViewById(R.id.fragment_container);
        mNavigationBar = (BottomNavigationView) findViewById(R.id.navigation);
        initFragments();
        mNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mHomeFragment).commit();
                        return true;
                    case R.id.navigation_dashboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFindFragment).commit();
                        return true;
                    case R.id.navigation_notifications:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mAlertFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void initFragments() {
        mHomeFragment = new HomeFragment();
        mFindFragment = new FindFragment();
        mAlertFragment = new AlertFragment();
    }
}
