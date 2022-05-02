package com.example.go4lunch.ui.map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

    /** GOOGLE PLACES with SDK - Object declaration
    private PlacesClient mPlacesClient;
     */

    /** GOOGLE MAPS & PLACES **/
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
            mGoogleMap = googleMap;
            mRadius = mMapViewModel.getRADIUS();

            mMapViewModel.getMyLocation().observe(getViewLifecycleOwner(), new Observer<LocationModel>() {
                @Override
                public void onChanged(LocationModel locationViewState) {
                    mMyLocation = locationViewState;
                    loadSearchViewData();
                }
            });
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

    private void loadSearchViewData() {
        mMapViewModel.getSearchViewQuery().observe(getViewLifecycleOwner(), searchViewQueryViewState ->{
            mSearchViewQuery = searchViewQueryViewState;
            mMapViewModel.getAllUsers().observe(getViewLifecycleOwner(), workmatesState -> {
                mListOfWorkmates = workmatesState;
            });
            displayView();
        });
    }

    // GOOGLE MAPS - Display the device location on the map
    @SuppressLint("MissingPermission")
    private void loadMyLocationOnMap() {
        /** Debug googleMarker */
        /*
        googleMap.addMarker(new MarkerOptions()
                .position(deviceLocation)
                .title("I'm here"));
         */

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
                        marker.setTag(place.getPlaceId());
                    } else {
                        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                .position(placeLocation)
                                .title(place.getName())
                                .snippet(place.getVicinity()));
                        marker.setTag(place.getPlaceId());
                    }
                    loadPlaceDetailsActivityOnMarkerClick(place, null);
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
                            marker.setTag(placePrediction.getPlaceId());
                        } else {
                            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(placeLocation)
                                    .title(placeDetailsViewState.getName())
                                    .snippet(placeDetailsViewState.getVicinity()));
                            marker.setTag(placePrediction.getPlaceId());
                        }
                    });
                    loadPlaceDetailsActivityOnMarkerClick(null, placePrediction);
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

    private void loadPlaceDetailsActivityOnMarkerClick(NearbyPlaceModel place, PlacePredictionModel placePrediction) {
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                /* DEBUG Google Marker Click */
                                /*
                                String markerName = marker.getTitle();
                                Toast.makeText(getContext(), "Clicked location is " + markerName, Toast.LENGTH_SHORT).show();
                                */
                String placeId = (String) marker.getTag();
                Intent placeDetailsIntent = new Intent(MapViewFragment.this.getActivity(), PlaceDetailsActivity.class);
                placeDetailsIntent.putExtra("PLACE_ID", placeId);
                MapViewFragment.this.startActivity(placeDetailsIntent);

                // Return false to indicate that we have not consumed the event and that we wish
                // for the default behavior to occur (which is for the camera to move such that the
                // marker is centered and for the marker's info window to open, if it has one).
                return false;
            }
        });
    }
}