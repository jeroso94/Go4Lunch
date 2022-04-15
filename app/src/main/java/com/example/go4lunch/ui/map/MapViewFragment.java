package com.example.go4lunch.ui.map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.R;
import com.example.go4lunch.event.MapViewEvent;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;
import com.example.go4lunch.model.place_autocomplete.PlacePredictionModel;
import com.example.go4lunch.ui.ViewModelFactory;
import com.example.go4lunch.ui.place_details.PlaceDetailsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.atomic.AtomicInteger;

public class MapViewFragment extends Fragment {

    // GOOGLE MAPS - GEOLOCATION
    // Camera settings
    private static final int DEFAULT_ZOOM = 15;

    /** GOOGLE PLACES with SDK - Object declaration
    private PlacesClient mPlacesClient;
     */

    /** GOOGLE MAPS & PLACES **/
    // Geolocation and Nearby Places
    private double mLatitude;
    private double mLongitude;
    private int mRadius;

    // Search View
    private String mSearchViewQuery;

    // MVVM - Object declaration
    private MapViewModel mMapViewModel;

    // GOOGLE PLACES with API and MVVM - Instance the ViewModel object (mMapViewModel)
    // based on the Factory ViewModelFactory (ViewModelFactory.getInstance())
    private void setupViewModel() {
        mMapViewModel = new ViewModelProvider(getActivity(), ViewModelFactory.getInstance()).get(MapViewModel.class);
    }

    public MapViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /** GOOGLE PLACES with SDK - onCreateView() snippet
        // Initialize the SDK and create a new PlacesClient instance
        Places.initialize(requireContext(), PLACES_API_KEY);
        mPlacesClient = Places.createClient(requireActivity());
          */

        /* GOOGLE PLACES with API and MVVM - setupViewModel() */
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

    // GOOGLE MAPS - When the map is loaded in memory
    private final OnMapReadyCallback mOnMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            /** GOOGLE MAPS Sydney location sample - onMapReady() snippet
            getSydneyLocation();
              */
            displayView(googleMap);
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
    private void loadMyLocationOnMap(GoogleMap googleMap) {
        /** Debug googleMarker */
        /*
        googleMap.addMarker(new MarkerOptions()
                .position(deviceLocation)
                .title("I'm here"));
         */

        // Enable "MyLocation" layer and "MyLocation" button
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng deviceLocation= new LatLng(mLatitude, mLongitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation,DEFAULT_ZOOM));
    }

    // GOOGLE PLACES - Show places around the device location
    @SuppressWarnings("MissingPermission")
    private void displayView(GoogleMap googleMap) {
        if (mSearchViewQuery.equals("NO_QUERY")) {
            loadMyLocationOnMap(googleMap);

            /* GOOGLE PLACES with API and MVVM */
            mMapViewModel.loadNearbyPlaces(mLatitude, mLongitude, mRadius).observe(getViewLifecycleOwner(), mapViewState -> {
                for (NearbyPlaceModel place : mapViewState) {
                    LatLng placeLocation = new LatLng(place.getGeometryAttributeForPlace().getLocation().getLat(),
                            place.getGeometryAttributeForPlace().getLocation().getLng());

                    /* We change the Marker's color if 1+ interested workmates counted */
                    if (countWorkmatesForAPlace(place, null) > 0 ) {
                        googleMap.addMarker(new MarkerOptions()
                                .position(placeLocation)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .title(place.getName())
                                .snippet(place.getVicinity()));
                    } else {
                        googleMap.addMarker(new MarkerOptions()
                                .position(placeLocation)
                                .title(place.getName())
                                .snippet(place.getVicinity()));
                    }
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation,DEFAULT_ZOOM));
                    loadPlaceDetailsActivityOnMarkerClick(googleMap, place, null);
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

        } else {
            mMapViewModel.loadPlacesPrediction(mSearchViewQuery, mLatitude, mLongitude, mRadius).observe(getViewLifecycleOwner(), searchViewState -> {
                for (PlacePredictionModel placePrediction : searchViewState) {
                    mMapViewModel.loadPlaceDetails(placePrediction.getPlaceId()).observe(getViewLifecycleOwner(), placeDetailsViewState -> {
                        LatLng placeLocation = new LatLng(placeDetailsViewState.getGeometry().getLocation().getLat(),
                                placeDetailsViewState.getGeometry().getLocation().getLng());

                        if (countWorkmatesForAPlace(null, placePrediction) > 0 ) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(placeLocation)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .title(placeDetailsViewState.getName())
                                    .snippet(placeDetailsViewState.getVicinity()));
                        } else {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(placeLocation)
                                    .title(placeDetailsViewState.getName())
                                    .snippet(placeDetailsViewState.getVicinity()));
                        }
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation,DEFAULT_ZOOM));
                    });
                    loadPlaceDetailsActivityOnMarkerClick(googleMap, null, placePrediction);
                }
            });
        }
    }

    private int countWorkmatesForAPlace(NearbyPlaceModel place, PlacePredictionModel placePrediction) {
        AtomicInteger workmateCounter = new AtomicInteger();
        mMapViewModel.getAllUsers().observe(getViewLifecycleOwner(), workmatesState -> {
            /* We count the number of interested workmates for each loaded places */
            for (UserModel workmate : workmatesState) {
                if (place != null && placePrediction == null) {
                    if (place.getPlaceId().equals(workmate.getPlaceId())) {
                        workmateCounter.getAndIncrement();
                    }
                } else if (place == null && placePrediction != null) {
                    if (placePrediction.getPlaceId().equals(workmate.getPlaceId())) {
                        workmateCounter.getAndIncrement();
                    }
                }
            }
        });
        return workmateCounter.get();
    }

    private void loadPlaceDetailsActivityOnMarkerClick(GoogleMap googleMap, NearbyPlaceModel place, PlacePredictionModel placePrediction) {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                /* DEBUG Google Marker Click */
                                /*
                                String markerName = marker.getTitle();
                                Toast.makeText(getContext(), "Clicked location is " + markerName, Toast.LENGTH_SHORT).show();
                                */
                Intent placeDetailsIntent = new Intent(MapViewFragment.this.getActivity(), PlaceDetailsActivity.class);
                if (place != null && placePrediction == null) {
                    placeDetailsIntent.putExtra("PLACE_ID", place.getPlaceId());
                } else if (place == null && placePrediction != null) {
                    placeDetailsIntent.putExtra("PLACE_ID", placePrediction.getPlaceId());
                }
                MapViewFragment.this.startActivity(placeDetailsIntent);

                // Return false to indicate that we have not consumed the event and that we wish
                // for the default behavior to occur (which is for the camera to move such that the
                // marker is centered and for the marker's info window to open, if it has one).
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMapView(MapViewEvent event) {
        mSearchViewQuery = event.mSearchViewQuery;
        mLatitude = event.mLatitude;
        mLongitude = event.mLongitude;
        mRadius = event.mRadius;
    }
}