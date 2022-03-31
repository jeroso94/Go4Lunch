package com.example.go4lunch.ui.workmates;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.Manager.UserManager;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentWorkmatesBinding;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;
import com.example.go4lunch.ui.list.ListViewAdapter;
import com.example.go4lunch.ui.place_details.PlaceDetailsActivity;
import com.example.go4lunch.utils.ItemClickSupport;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WorkmatesFragment extends Fragment {

    private UserManager mUserManager = UserManager.getInstance();

    private FragmentWorkmatesBinding mFragmentWorkmates;

    private WorkmatesAdapter mWorkmatesAdapter;
    private List<UserModel> mUsersList = new ArrayList<>();

    /** The RecyclerView which displays the list of places
     * Suppress warning is safe because variable is initialized in onCreate
     */
    @NonNull
    private RecyclerView mRecyclerView;

    public WorkmatesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /** LAYOUT
         * - CONNECT THE LIST VIEW FRAGMENT
         * - OVERRIDE THE RECYCLERVIEW WIDGET
         * - POPULATE ITEMS IN THE RECYCLERVIEW WIDGET
         */
        mFragmentWorkmates = FragmentWorkmatesBinding.inflate(inflater, container, false);
        mRecyclerView = (RecyclerView) mFragmentWorkmates.listOfWorkmates;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        mWorkmatesAdapter = new WorkmatesAdapter(mUsersList);
        mRecyclerView.setAdapter(mWorkmatesAdapter);

        displayView();

        // Inflate the layout for this fragment
        return mFragmentWorkmates.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void displayView() {
        mUsersList.clear();
        mUsersList.addAll((Collection<? extends UserModel>) mUserManager.getUsersCollection());
        mWorkmatesAdapter.notifyDataSetChanged();

        ItemClickSupport.addTo(mFragmentWorkmates.listOfWorkmates, R.layout.workmate_attributes)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        UserModel workmate = mUsersList.get(position);
                        // Start a new PlaceDetailsActivity
                        Intent placeDetailsIntent = new Intent(getActivity(), PlaceDetailsActivity.class);
                        placeDetailsIntent.putExtra("PLACE_ID", workmate.getPlaceId());
                        startActivity(placeDetailsIntent);
                    }
                });
    }
}