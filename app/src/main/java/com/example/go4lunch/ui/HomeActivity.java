package com.example.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.go4lunch.R;
import com.example.go4lunch.model.all_searches.geometry.location.LocationModel;
import com.example.go4lunch.ui.list.ListViewFragment;
import com.example.go4lunch.ui.map.MapViewFragment;
import com.example.go4lunch.ui.workmates.WorkmatesFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static String SEARCH_VIEW_QUERY_STATUS    = "NO_QUERY";

    // GOOGLE MAPS - GEOLOCATION
    // Location Services object which store the GPS&Network location
    private FusedLocationProviderClient mFusedLocationProviderClient;

    /** GOOGLE MAPS & PLACES **/
    // Location Permission
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    // Geolocation and PlaceAutocomplete
    //private double mLatitude;
    //private double mLongitude;

    // MVVM - Object declaration
    private HomeViewModel mHomeViewModel;

    // CLASS SCOPE OBJECTS
    //private static final String TAG = HomeActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private MapViewFragment mapViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // GOOGLE PLACE AUTOCOMPLETE - GEOLOCATION - Construct a FusedLocationProviderClient object.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //GOOGLE MAPS - LOCATION PERMISSION
        getLocationPermission();
        if (mLocationPermissionGranted) {
            getDeviceLocation();
        } else {
            getLocationPermission();
        }

        setupViewModel();

        // NAVIGATION DRAWER - Setup DrawerLayout for NavigationDrawer
        mDrawerLayout = findViewById(R.id.home_drawerlayout);

        // NAVIGATION DRAWER + TOOLBAR - Setup toolbar as an ActionBar
        mToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(mToolbar);

        mapViewFragment = new MapViewFragment();

        // NAVIGATION DRAWER + TOOLBAR - Setup Hamburger Icon in the ActionBar and connect to DrawerLayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // NAVIGATION DRAWER - Setup NavigationView
        NavigationView navigationView = findViewById(R.id.home_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // BOTTOM NAVIGATION VIEW - Setup BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        if(savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_mapViewFragment);
        }

        // SEARCH VIEW - Setup SearchView
        SearchView searchView = findViewById(R.id.home_searchview);
        searchView.setOnQueryTextListener(mOnQueryTextListener);
        searchView.setQueryHint("Type here to search");
        searchView.setBackgroundColor(getResources().getColor(R.color.white));
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

    //SEARCH VIEW
    private SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mHomeViewModel.setSearchViewQuery(query);
            //showToast("You clicked ENTER");
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    //BOTTOM NAVIGATION - When an item is selected
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_mapViewFragment:
                    mHomeViewModel.setSearchViewQuery(SEARCH_VIEW_QUERY_STATUS);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, mapViewFragment).commit();

                    /*
                    // GOOGLE MAPS - Display the map
                    SupportMapFragment mapViewFragment = SupportMapFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, mapViewFragment).commit();
                    mapViewFragment.getMapAsync(mOnMapReadyCallback);
                     */

                    return true;

                case R.id.nav_listViewFragment:
                    ListViewFragment listViewFragment = new ListViewFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, listViewFragment).commit();
                    return true;

                case R.id.nav_workmatesFragment:
                    WorkmatesFragment workmatesFragment = new WorkmatesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, workmatesFragment).commit();
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

    // GOOGLE PLACE AUTOCOMPLETE - Request Location Permission
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    // GOOGLE PLACE AUTO COMPLETE - What's happen with the Location Permission result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // GOOGLE PLACE AUTOCOMPLETE - Display the device location on the map
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                LocationModel myLocation = new LocationModel();
                myLocation.setLat(task.getResult().getLatitude());
                myLocation.setLng(task.getResult().getLongitude());
                mHomeViewModel.setMyLocation(myLocation);
            }
        });
    }

    private void setupViewModel() {
        mHomeViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(HomeViewModel.class);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}