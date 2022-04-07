package com.example.go4lunch.ui.list;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentListViewBinding;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;
import com.example.go4lunch.ui.ViewModelFactory;
import com.example.go4lunch.ui.place_details.PlaceDetailsActivity;
import com.example.go4lunch.ui.workmates.WorkmatesViewModel;
import com.example.go4lunch.utils.ItemClickSupport;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment {

    private FragmentListViewBinding mFragmentListView;

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

    private ListViewAdapter mListViewAdapter;
    private final List<NearbyPlaceModel> mNearbyPlacesList = new ArrayList<>();
    private final List<UserModel> mUsersList = new ArrayList<>();

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
        mFragmentListView = FragmentListViewBinding.inflate(inflater, container, false);
        mRecyclerView = (RecyclerView) mFragmentListView.listOfPlaces;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        mListViewAdapter = new ListViewAdapter(mNearbyPlacesList, mUsersList);
        mRecyclerView.setAdapter(mListViewAdapter);

        // Inflate the layout for this fragment
        return mFragmentListView.getRoot();
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
        int RADIUS = 2000;
        mListViewModel.loadNearbyPlaces(deviceLatitude, deviceLongitude, RADIUS).observe(getViewLifecycleOwner(), new Observer<List<NearbyPlaceModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<NearbyPlaceModel> listViewState) {
                mNearbyPlacesList.clear();
                mNearbyPlacesList.addAll(listViewState);
                /*TODO: Trier la liste par ordre croissant de distance avec l'utilisateur */
                mListViewAdapter.notifyDataSetChanged();

                ItemClickSupport.addTo(mFragmentListView.listOfPlaces, R.layout.place_attributes)
                        .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                NearbyPlaceModel nearbyPlace = listViewState.get(position);
                                // Start a new PlaceDetailsActivity
                                Intent placeDetailsIntent = new Intent(getActivity(), PlaceDetailsActivity.class);
                                placeDetailsIntent.putExtra("PLACE_ID", nearbyPlace.getPlaceId());
                                startActivity(placeDetailsIntent);
                            }
                        });
            }
        });

        mListViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<UserModel> workmatesState) {
                mUsersList.clear();
                mUsersList.addAll(workmatesState);
                mListViewAdapter.notifyDataSetChanged();
            }
        });
    }
}