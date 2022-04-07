package com.example.go4lunch.ui.map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.go4lunch.R;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;
import com.example.go4lunch.ui.ViewModelFactory;
import com.example.go4lunch.ui.place_details.PlaceDetailsActivity;
import com.example.go4lunch.ui.workmates.WorkmatesFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MapViewFragment extends Fragment {

    // GOOGLE MAPS - GEOLOCATION
    // Location Services object which store the GPS&Network location
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // Camera settings
    private static final int DEFAULT_ZOOM = 15;

    /** GOOGLE PLACES with SDK - Object declaration
    private PlacesClient mPlacesClient;
     */

    /** GOOGLE MAPS & PLACES **/
    // Location Permission
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    // Geolocation and Nearby Places
    private double mLatitude;
    private double mLongitude;
    private final int RADIUS = 2000;

    // MVVM - Object declaration
    private MapViewModel mapViewModel;

    // GOOGLE PLACES with API and MVVM - Instance the ViewModel object (mMapViewModel)
    // based on the Factory ViewModelFactory (ViewModelFactory.getInstance())
    private void setupViewModel() {
        mapViewModel = new ViewModelProvider(getActivity(), ViewModelFactory.getInstance()).get(MapViewModel.class);
    }

    public MapViewFragment() {
        // Required public constructor (empty)
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //GOOGLE MAPS & PLACES - LOCATION PERMISSION
        getLocationPermission();

        // GOOGLE MAPS - GEOLOCATION - Construct a FusedLocationProviderClient object.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        /** GOOGLE PLACES with SDK - onCreateView() snippet
        // Initialize the SDK and create a new PlacesClient instance
        Places.initialize(requireContext(), PLACES_API_KEY);
        mPlacesClient = Places.createClient(requireActivity());
          */

        /** GOOGLE PLACES with API and MVVM - setupViewModel()
         *
         */
        setupViewModel();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frameLayout);
        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(mOnMapReadyCallback);
        }
    }

    // GOOGLE MAPS - Request Location Permission
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
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
            /** GOOGLE MAPS Sydney location sample - onMapReady() snippet
            getSydneyLocation();
              */
            if (locationPermissionGranted) {
                getDeviceLocation(googleMap);

            } else {
                getLocationPermission();
            }
        }
        /** GOOGLE MAPS Sydney location sample - getSydneyLocation() snippet
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
        locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                mLatitude = task.getResult().getLatitude();
                mLongitude = task.getResult().getLongitude();
                LatLng deviceLocation= new LatLng(mLatitude, mLongitude);
                /** Debug googleMarker */
                /*
                googleMap.addMarker(new MarkerOptions()
                        .position(deviceLocation)
                        .title("I'm here"));
                 */
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation,DEFAULT_ZOOM));
                displayView(googleMap);
            }
        });
    }

    // GOOGLE PLACES - Show places around the device location
    @SuppressWarnings("MissingPermission")
    private void displayView(GoogleMap googleMap) {

        /** GOOGLE PLACES with API and MVVM
         *
         */
        mapViewModel.loadNearbyPlaces(mLatitude, mLongitude, RADIUS).observe(getViewLifecycleOwner(), mapViewState -> {
            for (NearbyPlaceModel place:mapViewState) {
                LatLng placeLocation= new LatLng(place.getGeometryAttributeForPlace().getLocation().getLat(),
                        place.getGeometryAttributeForPlace().getLocation().getLng());

                mapViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
                    @Override
                    public void onChanged(List<UserModel> workmatesState) {
                        /* We count the number of interested workmates for each loaded places */
                        int workmateCounter = 0;
                        for (UserModel workmate : workmatesState){
                            if (place.getPlaceId().equals(workmate.getPlaceId())) {
                                workmateCounter += 1;
                            }
                        }

                        /* We change the Marker's color if 1+ interested workmates counted */
                        if (workmateCounter > 0){
                            googleMap.addMarker(new MarkerOptions()
                                    .position(placeLocation)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .title(place.getName())
                                    .snippet(place.getRating().toString()));
                        } else {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(placeLocation)
                                    .title(place.getName())
                                    .snippet(place.getRating().toString()));
                        }
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        /* DEBUG Google Marker Click */
                        /*
                        String markerName = marker.getTitle();
                        Toast.makeText(getContext(), "Clicked location is " + markerName, Toast.LENGTH_SHORT).show();
                        */
                        Intent placeDetailsIntent=new Intent(MapViewFragment.this.getActivity(), PlaceDetailsActivity.class);
                        placeDetailsIntent.putExtra("PLACE_ID", place.getPlaceId());
                        MapViewFragment.this.startActivity(placeDetailsIntent);

                        // Return false to indicate that we have not consumed the event and that we wish
                        // for the default behavior to occur (which is for the camera to move such that the
                        // marker is centered and for the marker's info window to open, if it has one).
                        return false;
                    }
                });
            }
        });

        /** GOOGLE PLACE with SDK - Request places

        // Setup place's attributes to request
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.LAT_LNG, Place.Field.ICON_URL);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(task -> {
            FindCurrentPlaceResponse response = task.getResult();
            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                Log.i(TAG, String.format("NearbyPlaceModel '%s' has likelihood: %f",
                        placeLikelihood.getPlace().getName(),
                        placeLikelihood.getLikelihood()));

                googleMap.addMarker(new MarkerOptions()
                                .position(Objects.requireNonNull(placeLikelihood.getPlace().getLatLng()))
                                .title(placeLikelihood.getPlace().getName())
                                .snippet(placeLikelihood.getPlace().getAddress())

                        //.icon(BitmapDescriptorFactory
                        //        .fromBitmap(Objects.requireNonNull(BitmapFactory
                        //                .decodeFile(placeLikelihood.getPlace().getIconUrl()))))

                );
            }
        });
          */
    }
}