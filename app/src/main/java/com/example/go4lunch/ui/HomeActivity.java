package com.example.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.go4lunch.ListViewFragment;
import com.example.go4lunch.MapViewFragment;
import com.example.go4lunch.R;
import com.example.go4lunch.WorkmatesFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    // NAVIGATION DRAWER - GLOBAL OBJECT
    private DrawerLayout mDrawerLayout;

    // GOOGLE MAPS - GEOLOCATION - Location Services object which store the GPS&Network location
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // GOOGLE MAPS - GEOLOCATION - Construct a FusedLocationProviderClient object.
        mFusedLocationProviderClient =LocationServices.getFusedLocationProviderClient(this);

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
                    //MapViewFragment mMapViewFragment = new MapViewFragment();
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, mMapViewFragment).commit();

                    // GOOGLE MAPS - Display the map
                    SupportMapFragment mMapViewFragment = SupportMapFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, mMapViewFragment).commit();
                    mMapViewFragment.getMapAsync(mOnMapReadyCallback);                    
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

    // GOOGLE MAPS - When the map is loaded in memory
    private final OnMapReadyCallback mOnMapReadyCallback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            //getSydneyLocation();
            getDeviceLocation(googleMap);
        }

        // getSydneyLocation() method
        /*
        private void getSydneyLocation() {
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
         */
    };

    // GOOGLE MAPS - Display the device location on the map
    @SuppressLint("MissingPermission")
    private void getDeviceLocation(GoogleMap googleMap) {
        // Enable "MyLocation" layer and "MyLocation" button
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                LatLng deviceLocation= new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
                googleMap.addMarker(new MarkerOptions()
                        .position(deviceLocation)
                        .title("I'm here"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(deviceLocation));
            }
        });
    }




}