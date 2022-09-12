package com.example.go4lunch.ui.place_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityPlaceDetailsBinding;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.model.place_details_search.PlaceDetailsModel;
import com.example.go4lunch.ui.HomeActivity;
import com.example.go4lunch.ui.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailsActivity extends AppCompatActivity {


    private ActivityPlaceDetailsBinding mActivityPlaceDetails;

    private PlaceDetailsViewModel mPlaceDetailsViewModel;
    private PlaceDetailsModel mPlaceDetails;
    private UserModel mUser;

    private RecyclerView mRecyclerView;
    private GuestsAdapter mGuestsAdapter;

    private String mPlaceId;
    List<UserModel> mGuestsList = new ArrayList<>();
    private Boolean mPlaceIsChecked;
    private Boolean mLikeIsChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent placeDetailsIntent = getIntent();
        mPlaceId = placeDetailsIntent.getStringExtra("PLACE_ID");

        /* SETUP PLACE DETAILS VIEW OBJECT */
        mPlaceDetailsViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(PlaceDetailsViewModel.class);

        /* LAYOUT
         * - SETUP BINDING VIEW OBJECTS FROM LAYOUT TO ACTIVITY
        */
        mActivityPlaceDetails = ActivityPlaceDetailsBinding.inflate(getLayoutInflater());
        setContentView(mActivityPlaceDetails.getRoot());

        displayView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void displayView() {
        /* LAYOUT - OVERRIDE THE RECYCLERVIEW WIDGET */
        mRecyclerView = mActivityPlaceDetails.listOfGuests;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));


        mPlaceDetailsViewModel.loadPlaceDetails(mPlaceId).observe(this, placeDetailsViewState -> {
            mPlaceDetails = placeDetailsViewState;

            /* POPULATE DATA IN THE LAYOUT VIEW */
            if (mPlaceDetails.getPhotos() != null && mPlaceDetails.getPhotos().size() > 0) {
                String photoBaseUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400";
                String photoReference = "&photo_reference=" + mPlaceDetails.getPhotos().get(0).getPhotoReference();
                String placeApiKey = "&key=" + BuildConfig.PLACES_API_KEY;
                Glide.with(this)
                        .load(photoBaseUrl+ photoReference +placeApiKey)
                        .apply(RequestOptions.centerCropTransform())
                        .into(mActivityPlaceDetails.picture);
            } else {
                mActivityPlaceDetails.picture.setVisibility(View.GONE);
            }

            mActivityPlaceDetails.name.setText(mPlaceDetails.getName());
            mActivityPlaceDetails.ratingBar.setRating(mPlaceDetails.getRating());
            mActivityPlaceDetails.address.setText(mPlaceDetails.getVicinity());

            mPlaceDetailsViewModel.loadUserDetails().observe(this, userDetailsViewState -> {
                mUser = userDetailsViewState;
                if (mUser.getPlaceId() != null && mPlaceDetails.getPlaceId().equals(mUser.getPlaceId())) {
                    mActivityPlaceDetails.floatingActionButton.setImageResource(R.drawable.ic_baseline_check_circle);
                    mPlaceIsChecked = true;
                } else {
                    mActivityPlaceDetails.floatingActionButton.setImageResource(R.drawable.ic_baseline_check_circle_outline);
                    mPlaceIsChecked = false;
                }

                if (mUser.getLikesList().size() > 0 ) {
                    for (String likedPlace : mUser.getLikesList()) {
                        if (likedPlace.equals(mPlaceDetails.getPlaceId())) {
                            mActivityPlaceDetails.likeButton.setImageResource(R.drawable.ic_baseline_star);
                            mLikeIsChecked = true;
                        } else {
                            mActivityPlaceDetails.likeButton.setImageResource(R.drawable.ic_baseline_star_outline);
                            mLikeIsChecked = false;
                        }
                    }
                }
            });

            /* CALL BUTTON INTERACTION */
            mActivityPlaceDetails.callButton.setOnClickListener(view -> {
                if (mPlaceDetails.getInternationalPhoneNumber() != null) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + mPlaceDetails.getInternationalPhoneNumber()));
                    startActivity(callIntent);
                }
            });

            /* LIKE BUTTON INTERACTION */
            mActivityPlaceDetails.likeButton.setOnClickListener(view -> {
                mPlaceDetailsViewModel.likeAPlace(mPlaceDetails.getPlaceId());

                if (!mLikeIsChecked){
                    mLikeIsChecked = true;
                    mActivityPlaceDetails.likeButton.setImageResource(R.drawable.ic_baseline_star);
                } else {
                    mLikeIsChecked = false;
                    mActivityPlaceDetails.likeButton.setImageResource(R.drawable.ic_baseline_star_outline);
                }
            });

            /* WEBSITE BUTTON INTERACTION */
            mActivityPlaceDetails.websiteButton.setOnClickListener(view -> {
                if (mPlaceDetails.getWebsite() != null) {
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                    websiteIntent.setData(Uri.parse(mPlaceDetails.getWebsite()));
                    startActivity(websiteIntent);
                }
            });

            /* CHOICE FAB INTERACTION */
            mActivityPlaceDetails.floatingActionButton.setOnClickListener(view -> {
                if (!mPlaceIsChecked){
                    mPlaceIsChecked = true;
                    mPlaceDetailsViewModel.bookAPlace(mPlaceId, mPlaceDetails.getName(), mPlaceDetails.getVicinity());
                    mActivityPlaceDetails.floatingActionButton.setImageResource(R.drawable.ic_baseline_check_circle);
                    refreshGuestsList();
                } else {
                    mPlaceIsChecked = false;
                    mActivityPlaceDetails.floatingActionButton.setImageResource(R.drawable.ic_baseline_check_circle_outline);
                    mPlaceDetailsViewModel.bookAPlace(null, null, null);
                    refreshGuestsList();
                }
            });

            /* LAYOUT
             * - POPULATE ITEMS IN THE RECYCLERVIEW WIDGET
             */
            mGuestsAdapter = new GuestsAdapter(this, mGuestsList);
            mRecyclerView.setAdapter(mGuestsAdapter);

            refreshGuestsList();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshGuestsList() {
        mPlaceDetailsViewModel.loadUsers().observe(this, guestsViewStates -> {
            mGuestsList.clear();
            for (UserModel guest : guestsViewStates) {
                if (guest.getPlaceId() != null && guest.getPlaceId().equals(mPlaceDetails.getPlaceId())){
                    mGuestsList.add(guest);
                }
            }
            mGuestsAdapter.notifyDataSetChanged();
        });
    }
}