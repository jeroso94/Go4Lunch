package com.example.go4lunch.ui.list;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.R;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;
import com.example.go4lunch.ui.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment {

    /** GEOLOCATION
     * Location Services object which store the GPS&Network location
     */
    private FusedLocationProviderClient mFusedLocationProviderClient;

    /** GOOGLE MAPS & PLACES **/
    /* Location Permission */
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    /* Geolocation and Nearby Places */
    private double deviceLatitude;
    private double deviceLongitude;
    private final int RADIUS = 2000;

    private ListViewAdapter mListViewAdapter;
    private List<NearbyPlaceModel> mNearbyPlace = new ArrayList<>();

    /** MVVM - Object declaration **/
    private ListViewModel mListViewModel;

    /** The RecyclerView which displays the list of places
     * Suppress warning is safe because variable is initialized in onCreate
     */
    @NonNull
    private RecyclerView mRecyclerView;

    /** GOOGLE PLACES with API and MVVM - Instance the ViewModel object (mMapViewModel)
     * based on the Factory ViewModelFactory (ViewModelFactory.getInstance())
     */
    private void setupViewModel() {
        mListViewModel = new ViewModelProvider(getActivity(), ViewModelFactory.getInstance()).get(ListViewModel.class);
    }

    /** Constructor **/
    public ListViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** GEOLOCATION
         * - Construct a FusedLocationProviderClient object.
         */
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        /** GEOLOCATION & PLACES
         * - GRANT LOCATION PERMISSION
         */
        getLocationPermission();

        if (locationPermissionGranted){
            getDeviceLocation();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /** GOOGLE PLACES with API and MVVM
         * - setupViewModel()
         */
        setupViewModel();

        /** LAYOUT
         * - CONNECT THE LIST VIEW FRAGMENT
         * - OVERRIDE THE RECYCLERVIEW WIDGET
         * - POPULATE ITEMS IN THE RECYCLERVIEW WIDGET
         */
        View fragmentListView = inflater.inflate(R.layout.fragment_list_view, container, false);
        mRecyclerView = (RecyclerView) fragmentListView;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(fragmentListView.getContext(),LinearLayoutManager.VERTICAL, false));
        mListViewAdapter = new ListViewAdapter(mNearbyPlace);
        mRecyclerView.setAdapter(mListViewAdapter);

        // Inflate the layout for this fragment
        return fragmentListView;
    }

    /** GEOLOCATION
     * - Request Location Permission
     */
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

    /** GEOLOCATION
     * - What's happen with the Location Permission result
     */
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

    /** GEOLOCATION
     * - Display the device location on the map
     */
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {

        Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                deviceLatitude = task.getResult().getLatitude();
                deviceLongitude = task.getResult().getLongitude();
                displayView();
                mListViewAdapter.setLocation(deviceLatitude, deviceLongitude);
            }
        });
    }

    /** GOOGLE PLACES
     * - Show places around the device location
     */
    @SuppressWarnings("MissingPermission")
    private void displayView() {

        /** GOOGLE PLACES with API and MVVM **/
        mListViewModel.loadNearbyPlaces(deviceLatitude, deviceLongitude, RADIUS).observe(getViewLifecycleOwner(), listViewState -> {
            mNearbyPlace.clear();
            mNearbyPlace.addAll(listViewState);
            mListViewAdapter.notifyDataSetChanged();
        });
    }
}