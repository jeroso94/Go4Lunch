package com.example.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.go4lunch.ListViewFragment;
import com.example.go4lunch.MapViewFragment;
import com.example.go4lunch.R;
import com.example.go4lunch.WorkmatesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.mapViewFragment);
    }

    MapViewFragment mMapViewFragment = new MapViewFragment();
    ListViewFragment mListViewFragment = new ListViewFragment();
    WorkmatesFragment mWorkmatesFragment = new WorkmatesFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapViewFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, mMapViewFragment).commit();
                return true;

            case R.id.listViewFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, mListViewFragment).commit();
                return true;

            case R.id.workmatesFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, mWorkmatesFragment).commit();
                return true;
        }
        return false;
    }
}