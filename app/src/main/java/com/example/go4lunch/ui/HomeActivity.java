package com.example.go4lunch.ui;

import static com.example.go4lunch.BuildConfig.PLACES_API_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    // NAVIGATION DRAWER - GLOBAL OBJECT
    private DrawerLayout mDrawerLayout;

    // GOOGLE MAPS - GEOLOCATION
    // Location Services object which store the GPS&Network location
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // Camera settings
    private static final int DEFAULT_ZOOM = 15;

    // GOOGLE PLACES -
    private PlacesClient mPlacesClient;

    // GOOGLE MAPS & PLACES - Location Permission
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //GOOGLE MAPS & PLACES - LOCATION PERMISSION
        getLocationPermission();

        // GOOGLE MAPS - GEOLOCATION - Construct a FusedLocationProviderClient object.
        mFusedLocationProviderClient =LocationServices.getFusedLocationProviderClient(this);

        // GOOGLE PLACES - Initialize the SDK and create a new PlacesClient instance
        Places.initialize(getApplicationContext(), PLACES_API_KEY);
        mPlacesClient = Places.createClient(this);

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

    // GOOGLE MAPS - Request Location Permission
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    // GOOGLE MAPS - What's happen with the Location Permission result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // GOOGLE MAPS - When the map is loaded in memory
    private final OnMapReadyCallback mOnMapReadyCallback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            //getSydneyLocation();
            if (locationPermissionGranted) {
                getDeviceLocation(googleMap);
                showCurrentPlace(googleMap);
            } else {
                getLocationPermission();
            }
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
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation,DEFAULT_ZOOM));
            }
        });
    }

    // GOOGLE PLACES - Show places around the device location
    @SuppressWarnings("MissingPermission")
    private void showCurrentPlace(GoogleMap googleMap) {

        // Setup place's attributes to request
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.LAT_LNG, Place.Field.ICON_URL);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(task -> {
            FindCurrentPlaceResponse response = task.getResult();
            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                /*
                Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                        placeLikelihood.getPlace().getName(),
                        placeLikelihood.getLikelihood()));
                 */
                googleMap.addMarker(new MarkerOptions()
                        .position(Objects.requireNonNull(placeLikelihood.getPlace().getLatLng()))
                        .title(placeLikelihood.getPlace().getName())
                        .snippet(placeLikelihood.getPlace().getAddress())
                        /*
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(Objects.requireNonNull(BitmapFactory
                                        .decodeFile(placeLikelihood.getPlace().getIconUrl()))))
                         */
                );
            }
        });
    }
}