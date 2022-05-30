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
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.model.all_searches.geometry.location.LocationModel;
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

import java.util.List;

public class MapViewFragment extends Fragment {

    // GOOGLE MAPS - GEOLOCATION
    // Camera settings
    private static final int DEFAULT_ZOOM = 15;

    /* GOOGLE PLACES with SDK - Object declaration
    private PlacesClient mPlacesClient;
     */

    /* GOOGLE MAPS & PLACES */
    // Geolocation and Nearby Places
    private GoogleMap mGoogleMap;
    private LocationModel mMyLocation;
    private int mRadius;

    // Search View
    private String mSearchViewQuery;

    // MVVM - Object declaration
    private MapViewModel mMapViewModel;
    private List<UserModel> mListOfWorkmates;

    // GOOGLE PLACES with API and MVVM - Instance the ViewModel object (mMapViewModel)
    // based on the Factory ViewModelFactory (ViewModelFactory.getInstance())
    private void setupViewModel() {
        mMapViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MapViewModel.class);
    }

    public MapViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
            mGoogleMap = googleMap;
            mRadius = mMapViewModel.getRADIUS();

            mMapViewModel.getMyLocation().observe(getViewLifecycleOwner(), locationViewState -> {
                mMyLocation = locationViewState;
                loadSearchViewData();
            });
        }
    };

    private void loadSearchViewData() {
        mMapViewModel.getSearchViewQuery().observe(getViewLifecycleOwner(), searchViewQueryViewState ->{
            mSearchViewQuery = searchViewQueryViewState;
            if (mListOfWorkmates != null) {
                displayView();
            }
        });
        loadWorkmates();
    }

    private void loadWorkmates() {
        mMapViewModel.getAllUsers().observe(getViewLifecycleOwner(), workmatesState -> {
            mListOfWorkmates = workmatesState;
            displayView();
        });
    }

    // GOOGLE MAPS - Display the device location on the map
    @SuppressLint("MissingPermission")
    private void loadMyLocationOnMap() {

        // Enable "MyLocation" layer and "MyLocation" button
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng deviceLocation= new LatLng(mMyLocation.getLat(), mMyLocation.getLng());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation,DEFAULT_ZOOM));
    }

    // GOOGLE PLACES - Show places around the device location
    @SuppressWarnings("MissingPermission")
    private void displayView() {
        if (mSearchViewQuery.equals("NO_QUERY")) {
            loadMyLocationOnMap();

            /* GOOGLE PLACES with API and MVVM */
            mMapViewModel.loadNearbyPlaces(mMyLocation.getLat(), mMyLocation.getLng(), mRadius).observe(getViewLifecycleOwner(), mapViewState -> {
                for (NearbyPlaceModel place : mapViewState) {
                    LatLng placeLocation = new LatLng(place.getGeometryAttributeForPlace().getLocation().getLat(),
                            place.getGeometryAttributeForPlace().getLocation().getLng());

                    /* We change the Marker's color if 1+ interested workmates counted */
                    if (DoesWorkmateBookThisPlace(place, null)) {
                        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                .position(placeLocation)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .title(place.getName())
                                .snippet(place.getVicinity()));
                        assert marker != null;
                        marker.setTag(place.getPlaceId());
                    } else {
                        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                .position(placeLocation)
                                .title(place.getName())
                                .snippet(place.getVicinity()));
                        assert marker != null;
                        marker.setTag(place.getPlaceId());
                    }
                    loadPlaceDetailsActivityOnMarkerClick();
                }
            });

        } else {
            mMapViewModel.loadPlacesPrediction(mSearchViewQuery, mMyLocation.getLat(), mMyLocation.getLng(), mRadius).observe(getViewLifecycleOwner(), searchViewState -> {
                mGoogleMap.clear();
                for (PlacePredictionModel placePrediction : searchViewState) {
                    mMapViewModel.loadPlaceDetails(placePrediction.getPlaceId()).observe(getViewLifecycleOwner(), placeDetailsViewState -> {
                        LatLng placeLocation = new LatLng(placeDetailsViewState.getGeometry().getLocation().getLat(),
                                placeDetailsViewState.getGeometry().getLocation().getLng());

                        if (DoesWorkmateBookThisPlace(null, placePrediction)) {
                            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(placeLocation)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .title(placeDetailsViewState.getName())
                                    .snippet(placeDetailsViewState.getVicinity()));
                            assert marker != null;
                            marker.setTag(placePrediction.getPlaceId());
                        } else {
                            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(placeLocation)
                                    .title(placeDetailsViewState.getName())
                                    .snippet(placeDetailsViewState.getVicinity()));
                            assert marker != null;
                            marker.setTag(placePrediction.getPlaceId());
                        }
                    });
                    loadPlaceDetailsActivityOnMarkerClick();
                }
            });
        }
    }

    private Boolean DoesWorkmateBookThisPlace(NearbyPlaceModel place, PlacePredictionModel placePrediction) {
        String placeId = "";

        if (place != null && placePrediction == null) {
            placeId = place.getPlaceId();
        } else if (place == null && placePrediction != null) {
            placeId = placePrediction.getPlaceId();
        }

        /* We count the number of interested workmates for each loaded places */
        for (UserModel workmate : mListOfWorkmates) {
            if (placeId.equals(workmate.getPlaceId())) {
                return true;
            }
        }
        return false;
    }

    private void loadPlaceDetailsActivityOnMarkerClick() {
        mGoogleMap.setOnMarkerClickListener(marker -> {
            String placeId = (String) marker.getTag();
            Intent placeDetailsIntent = new Intent(MapViewFragment.this.getActivity(), PlaceDetailsActivity.class);
            placeDetailsIntent.putExtra("PLACE_ID", placeId);
            MapViewFragment.this.startActivity(placeDetailsIntent);

            // Return false to indicate that we have not consumed the event and that we wish
            // for the default behavior to occur (which is for the camera to move such that the
            // marker is centered and for the marker's info window to open, if it has one).
            return false;
        });
    }
}