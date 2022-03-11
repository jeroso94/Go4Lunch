package com.example.go4lunch.ui.map;

import static com.example.go4lunch.BuildConfig.PLACES_API_KEY;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.R;
import com.example.go4lunch.data.di.MapViewModelFactoryModule;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    private MapViewModel mMapViewModel;

    // GOOGLE PLACES with API and MVVM - Instance the ViewModel object (mMapViewModel)
    // based on the Factory MapViewModelFactory (mMapViewModelFactory)
    // and the Dependency Injection (MapViewModelFactoryModule.provideMapViewModelFactory())
    private void setupViewModel() {
        MapViewModelFactory mMapViewModelFactory = MapViewModelFactoryModule.provideMapViewModelFactory(getContext());
        mMapViewModel = new ViewModelProvider(getActivity(), mMapViewModelFactory).get(MapViewModel.class);
    }

    public MapViewFragment() {
        // Required public constructor (empty)
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //GOOGLE MAPS & PLACES - LOCATION PERMISSION
        getLocationPermission();

        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frameLayout);
        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(mOnMapReadyCallback);
        }

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
                showCurrentPlace(googleMap);
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

        /** GOOGLE PLACES with API and MVVM
         *
         */
        mMapViewModel.displayNearbyPlaces(mLatitude, mLongitude, RADIUS).observe(getActivity(),mapViewState -> {
            for (NearbyPlaceModel listOfPlace:mapViewState) {
                LatLng placeLocation= new LatLng(listOfPlace.getGeometryAttributeForPlace().getLocation().getLat(),
                        listOfPlace.getGeometryAttributeForPlace().getLocation().getLng());
                googleMap.addMarker(new MarkerOptions()
                        .position(placeLocation)
                        .title(listOfPlace.getName())
                        .snippet(listOfPlace.getRating().toString()));
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