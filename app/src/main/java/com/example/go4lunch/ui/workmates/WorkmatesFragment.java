package com.example.go4lunch.ui.workmates;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentWorkmatesBinding;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.ui.ViewModelFactory;
import com.example.go4lunch.ui.place_details.PlaceDetailsActivity;
import com.example.go4lunch.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding mFragmentWorkmates;

    private WorkmatesAdapter mWorkmatesAdapter;
    private List<UserModel> mUsersList = new ArrayList<>();

    /** MVVM - Object declaration **/
    private WorkmatesViewModel mWorkmatesViewModel;

    /** The RecyclerView which displays the list of places
     * Suppress warning is safe because variable is initialized in onCreate
     */
    @NonNull
    private RecyclerView mRecyclerView;

    public WorkmatesFragment() {
        // Required empty public constructor
    }

    private void setupViewModel() {
        mWorkmatesViewModel = new ViewModelProvider(getActivity(), ViewModelFactory.getInstance()).get(WorkmatesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setupViewModel();

        /** LAYOUT
         * - CONNECT THE LIST VIEW FRAGMENT
         * - OVERRIDE THE RECYCLERVIEW WIDGET
         * - POPULATE ITEMS IN THE RECYCLERVIEW WIDGET
         */
        mFragmentWorkmates = FragmentWorkmatesBinding.inflate(inflater, container, false);
        mRecyclerView = (RecyclerView) mFragmentWorkmates.listOfWorkmates;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        mWorkmatesAdapter = new WorkmatesAdapter(getContext(), mUsersList);
        mRecyclerView.setAdapter(mWorkmatesAdapter);

        displayView();

        // Inflate the layout for this fragment
        return mFragmentWorkmates.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void displayView() {
        mWorkmatesViewModel.loadUsers().observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> workmatesState) {
                mUsersList.clear();
                mUsersList.addAll(workmatesState);
                mWorkmatesAdapter.notifyDataSetChanged();

                ItemClickSupport.addTo(mFragmentWorkmates.listOfWorkmates, R.layout.workmate_attributes)
                        .setOnItemClickListener((recyclerView, position, v) -> {
                            UserModel workmate = mUsersList.get(position);
                            if (workmate.getPlaceId() != null) {
                                // Start a new PlaceDetailsActivity
                                Intent placeDetailsIntent = new Intent(WorkmatesFragment.this.getActivity(), PlaceDetailsActivity.class);
                                placeDetailsIntent.putExtra("PLACE_ID", workmate.getPlaceId());
                                WorkmatesFragment.this.startActivity(placeDetailsIntent);
                            }
                        });
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        mWorkmatesAdapter.notifyDataSetChanged();
    }
}