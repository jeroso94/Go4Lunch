package com.example.go4lunch.ui;

import static android.content.ContentValues.TAG;

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
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.go4lunch.R;
import com.example.go4lunch.model.all_searches.geometry.location.LocationModel;
import com.example.go4lunch.ui.list.ListViewFragment;
import com.example.go4lunch.ui.map.MapViewFragment;
import com.example.go4lunch.ui.place_details.PlaceDetailsActivity;
import com.example.go4lunch.ui.workmates.WorkmatesFragment;
import com.example.go4lunch.worker.NotifyWorker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // GOOGLE MAPS - GEOLOCATION
    // Location Services object which store the GPS&Network location
    private FusedLocationProviderClient mFusedLocationProviderClient;

    /** GOOGLE MAPS & PLACES **/
    // Location Permission
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // MVVM - Object declaration
    private HomeViewModel mHomeViewModel;

    // CLASS SCOPE OBJECTS
    //private static final String TAG = HomeActivity.class.getSimpleName();
    private final static String SEARCH_VIEW_QUERY_STATUS    = "NO_QUERY";
    final String[] SETTINGS_ITEM_LIST = new String[]{"Notification"};

    private DrawerLayout mDrawerLayout;
    private MapViewFragment mapViewFragment;

    final boolean[] mCheckedItems = new boolean[SETTINGS_ITEM_LIST.length];
    final List<String> mSelectedItems = Arrays.asList(SETTINGS_ITEM_LIST);

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

        loadSettingsFromDevice();

        // NAVIGATION DRAWER - Setup DrawerLayout for NavigationDrawer
        mDrawerLayout = findViewById(R.id.home_drawerlayout);

        // NAVIGATION DRAWER + TOOLBAR - Setup toolbar as an ActionBar
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        mapViewFragment = new MapViewFragment();

        // NAVIGATION DRAWER + TOOLBAR - Setup Hamburger Icon in the ActionBar and connect to DrawerLayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
    private final SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
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
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_mapViewFragment:
                    mHomeViewModel.setSearchViewQuery(SEARCH_VIEW_QUERY_STATUS);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, mapViewFragment).commit();
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
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_your_lunch:
                mHomeViewModel.loadUserDetails().observe(this, userDetailsViewState ->{
                    if (userDetailsViewState.getPlaceId() != null){
                        Intent placeDetailsIntent = new Intent(this, PlaceDetailsActivity.class);
                        placeDetailsIntent.putExtra("PLACE_ID", userDetailsViewState.getPlaceId());
                        startActivity(placeDetailsIntent);
                    } else {
                        showToast(getString(R.string.nav_drawer_item1_noresult));
                    }
                });
                break;

            case R.id.nav_settings:
                showSettingsAlertDialog();
                break;

            case R.id.nav_logout:
                mHomeViewModel.signOut(this).addOnSuccessListener(unused -> {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                });
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // SETTINGS
    private void loadSettingsFromDevice() {

        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        SharedPreferences sharedPreferences = getSharedPreferences("Go4LunchSharedPref", MODE_PRIVATE);
        for (int i = 0; i < SETTINGS_ITEM_LIST.length  ; i++){
            mCheckedItems[i] = Boolean.parseBoolean(sharedPreferences.getString(SETTINGS_ITEM_LIST[i],"false"));
            Log.d(TAG, "loadSettingsFromDevice: " + SETTINGS_ITEM_LIST[i] + "-" + mCheckedItems[i]);
            if (SETTINGS_ITEM_LIST[i].equals("Notification") && mCheckedItems[i]){
                enableNotifyWorker();
            }
        }
    }

    private void enableNotifyWorker() {
        //we set a tag to be able to cancel all work of this type if needed
        final String WORK_TAG = "notificationWork";

        mHomeViewModel.loadUserDetails().observe(this, userDetailsViewState ->{
            if (userDetailsViewState.getPlaceId() != null){
                PeriodicWorkRequest notificationWork =
                        new PeriodicWorkRequest.Builder(NotifyWorker.class, 15, TimeUnit.MINUTES)
                                .addTag(WORK_TAG)
                                .build();

                WorkManager.getInstance(this).cancelAllWorkByTag(WORK_TAG);
                WorkManager.getInstance(this).enqueue(notificationWork);
            } else {
                showToast(getString(R.string.notification_enabled_without_placeId_stored));
            }
        });

    }

    private void showSettingsAlertDialog() {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

        builder.setTitle(R.string.alert_dialog_settings_title);
        builder.setMultiChoiceItems(SETTINGS_ITEM_LIST, mCheckedItems, (dialogInterface, i, b) -> mCheckedItems[i] = b);

        builder.setPositiveButton(R.string.alert_dialog_settings_save_button_label, (dialogInterface, i) -> saveSettingsOnDevice());

        builder.setNegativeButton(R.string.alert_dialog_settings_cancel_button_label, (dialogInterface, i) -> {

        });

        builder.create().show();
    }

    private void saveSettingsOnDevice() {
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Go4LunchSharedPref",MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        for (int i = 0; i < SETTINGS_ITEM_LIST.length ; i++){
            myEdit.putString(mSelectedItems.get(i), String.valueOf(mCheckedItems[i]));
            if (mSelectedItems.get(i).equals("Notification") && mCheckedItems[i]){
                enableNotifyWorker();
            }
        }

        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.apply();
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
        locationResult.addOnCompleteListener(this, task -> {
            LocationModel myLocation = new LocationModel();
            myLocation.setLat(task.getResult().getLatitude());
            myLocation.setLng(task.getResult().getLongitude());
            mHomeViewModel.setMyLocation(myLocation);
        });
    }

    private void setupViewModel() {
        mHomeViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(HomeViewModel.class);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}