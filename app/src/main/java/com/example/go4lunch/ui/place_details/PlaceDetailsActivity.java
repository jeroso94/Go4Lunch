package com.example.go4lunch.ui.place_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityPlaceDetailsBinding;
import com.example.go4lunch.ui.ViewModelFactory;

public class PlaceDetailsActivity extends AppCompatActivity {

    private PlaceDetailsViewModel mPlaceDetailsView;
    private ActivityPlaceDetailsBinding mActivityPlaceDetails;
    private String mPlaceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* SETUP PLACE DETAILS VIEW OBJECT */
        mPlaceDetailsView = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(PlaceDetailsViewModel.class);;

        /* SETUP BINDING VIEW OBJECTS FROM LAYOUT TO ACTIVITY */
        mActivityPlaceDetails = ActivityPlaceDetailsBinding.inflate(getLayoutInflater());
        setContentView(mActivityPlaceDetails.getRoot());

        Intent placeDetailsIntent = getIntent();
        mPlaceId = placeDetailsIntent.getStringExtra("PLACE_ID");

        displayView();
    }

    private void displayView() {
        mPlaceDetailsView.loadPlaceDetails(mPlaceId).observe(this, placeDetailsViewState -> {

            /* POPULATE DATA IN THE LAYOUT VIEW */
            if (placeDetailsViewState.getPhotos() != null && placeDetailsViewState.getPhotos().size() > 0) {
                String photoBaseUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400";
                String photoReference = "&photo_reference=" + placeDetailsViewState.getPhotos().get(0).getPhotoReference();
                String placeApiKey = "&key=" + BuildConfig.PLACES_API_KEY;
                Glide.with(this)
                        .load(photoBaseUrl+ photoReference +placeApiKey)
                        .apply(RequestOptions.centerCropTransform())
                        .into(mActivityPlaceDetails.picture);
            } else {
                mActivityPlaceDetails.picture.setVisibility(View.GONE);
            }

            mActivityPlaceDetails.name.setText(placeDetailsViewState.getName());
            mActivityPlaceDetails.ratingBar.setRating(placeDetailsViewState.getRating());
            mActivityPlaceDetails.address.setText(placeDetailsViewState.getVicinity());

            mPlaceDetailsView.loadUserDetails().observe(this, userDetailsViewState -> {
                if (userDetailsViewState.getPlaceId() != null && placeDetailsViewState.getPlaceId().equals(userDetailsViewState.getPlaceId())) {
                    mActivityPlaceDetails.floatingActionButton.setImageResource(R.drawable.ic_baseline_check_circle);
                } else {
                    mActivityPlaceDetails.floatingActionButton.setImageResource(R.drawable.ic_baseline_check_circle_outline);
                }

                if (userDetailsViewState.getLikesList().size() > 0 ) {
                    for (String likedPlace : userDetailsViewState.getLikesList()) {
                        if (likedPlace.equals(placeDetailsViewState.getPlaceId())) {
                            mActivityPlaceDetails.likeButton.setImageResource(R.drawable.ic_baseline_star);
                        }
                    }
                }
            });

            /* CALL BUTTON INTERACTION */
            mActivityPlaceDetails.callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (placeDetailsViewState.getInternationalPhoneNumber() != null) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + placeDetailsViewState.getInternationalPhoneNumber()));
                        startActivity(callIntent);
                    }
                }
            });

            /* LIKE BUTTON INTERACTION */
            mActivityPlaceDetails.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPlaceDetailsView.updateLike(placeDetailsViewState.getPlaceId());
                    mActivityPlaceDetails.likeButton.setImageResource(R.drawable.ic_baseline_star);
                }
            });

            /* WEBSITE BUTTON INTERACTION */
            mActivityPlaceDetails.websiteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (placeDetailsViewState.getWebsite() != null) {
                        Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                        websiteIntent.setData(Uri.parse(placeDetailsViewState.getWebsite()));
                        startActivity(websiteIntent);
                    }
                }
            });

            /* CHOICE FAB INTERACTION */
            mActivityPlaceDetails.floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPlaceDetailsView.updateUserChoice(mPlaceId, placeDetailsViewState.getName(), placeDetailsViewState.getVicinity());
                    mActivityPlaceDetails.floatingActionButton.setImageResource(R.drawable.ic_baseline_check_circle);
                }
            });
        });
    }


}