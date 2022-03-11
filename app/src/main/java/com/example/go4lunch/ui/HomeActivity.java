package com.example.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.go4lunch.R;
import com.example.go4lunch.ui.list.ListViewFragment;
import com.example.go4lunch.ui.map.MapViewFragment;
import com.example.go4lunch.ui.workmates.WorkmatesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    // NAVIGATION DRAWER - GLOBAL OBJECT
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // NAVIGATION DRAWER - Setup DrawerLayout for NavigationDrawer
        mDrawerLayout = findViewById(R.id.home_drawerlayout);

        // NAVIGATION DRAWER + TOOLBAR - Setup toolbar as an ActionBar
        Toolbar mToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(mToolbar);

        // NAVIGATION DRAWER + TOOLBAR - Setup Hamburger Icon in the ActionBar and connect to DrawerLayout
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        // NAVIGATION DRAWER - Setup NavigationView
        NavigationView mNavigationView = findViewById(R.id.home_nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // BOTTOM NAVIGATION VIEW - Setup BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        if(savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_mapViewFragment);
        }
    }

    // NAVIGATION DRAWER & ANDROID UI - When BACK is pressed
    @Override
    public void onBackPressed() {
        // When press BACK DrawerLayout is closed
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //BOTTOM NAVIGATION - When an item is selected
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_mapViewFragment:
                    MapViewFragment mMapViewFragment = new MapViewFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, mMapViewFragment).commit();

                    /*
                    // GOOGLE MAPS - Display the map
                    SupportMapFragment mMapViewFragment = SupportMapFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, mMapViewFragment).commit();
                    mMapViewFragment.getMapAsync(mOnMapReadyCallback);
                     */

                    return true;

                case R.id.nav_listViewFragment:
                    ListViewFragment mListViewFragment = new ListViewFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, mListViewFragment).commit();
                    return true;

                case R.id.nav_workmatesFragment:
                    WorkmatesFragment mWorkmatesFragment = new WorkmatesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, mWorkmatesFragment).commit();
                    return true;
            }
            return false;
        }
    };

    // NAVIGATION DRAWER - When an item is selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_your_lunch:
                showToast("Go to your lunch");
                break;

            case R.id.nav_settings:
                showToast("Go to your settings");
                break;

            case R.id.nav_logout:
                showToast("SignOut called");
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}