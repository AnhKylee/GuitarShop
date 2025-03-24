package com.example.guitarshop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ManagerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Load default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ManageGuitarsFragment())
                .commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_manage_guitars) {
                selectedFragment = new ManageGuitarsFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ManagerProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}

