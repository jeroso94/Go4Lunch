package com.example.go4lunch.ui.list;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.PlaceAttributesBinding;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;

import java.util.List;

/**
 * Created by JeroSo94 on 15/03/2022.
 */
public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ListViewViewHolder>{
    /**
     * The list of places the adapter deals with
     */
    @NonNull
    private final List<NearbyPlaceModel> mListOfPlaces;
    private final List<UserModel> mListOfWorkmates;
    private double mDeviceLatitude;
    private double mDeviceLongitude;

    public ListViewAdapter(@NonNull List<NearbyPlaceModel> listOfPlaces, List<UserModel> listOfWorkmates) {
        mListOfPlaces = listOfPlaces;
        mListOfWorkmates = listOfWorkmates;
    }

    @NonNull
    @Override
    public ListViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PlaceAttributesBinding placeItems = PlaceAttributesBinding.inflate(inflater, parent, false);
        return new ListViewViewHolder(placeItems);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewViewHolder holder, int position) {

        NearbyPlaceModel place = mListOfPlaces.get(position);
        holder.mPlaceAttributes.name.setText(place.getName());
        holder.mPlaceAttributes.address.setText(place.getVicinity());

        /* OPENINGS */
        if (place.getOpeningHours() != null){
            if (place.getOpeningHours().getOpenNow()) {
                holder.mPlaceAttributes.openHours.setText(R.string.place_attribute_opennow_true);
            } else{
                holder.mPlaceAttributes.openHours.setText(R.string.place_attribute_opennow_false);
            }
        } else {
            holder.mPlaceAttributes.openHours.setVisibility(View.GONE);
        }

        /* Distance */
        double placeLatitude = place.getGeometryAttributeForPlace().getLocation().getLat();
        double placeLongitude = place.getGeometryAttributeForPlace().getLocation().getLng();
        float[] result = new float[1];
        Location.distanceBetween(mDeviceLatitude, mDeviceLongitude, placeLatitude, placeLongitude, result);
        holder.mPlaceAttributes.distance.setText(String.format("%s%s", (int) result[0], "m"));

        /* Interested Workmates */
        int workmateCounter = 0;
        for (UserModel workmate : mListOfWorkmates){
            if (place.getPlaceId().equals(workmate.getPlaceId())) {
                workmateCounter += 1;
            }
        }
        holder.mPlaceAttributes.interestedWorkmates.setText(String.valueOf(workmateCounter));

        /* RatingBar */
        holder.mPlaceAttributes.ratingBar.setRating(place.getRating().floatValue());

        /* Photo */
        if (place.getPhotos() != null && place.getPhotos().size() > 0) {
            String photoBaseUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400";
            String photoReference = "&photo_reference=" + place.getPhotos().get(0).getPhotoReference();
            String placeApiKey = "&key=" + BuildConfig.PLACES_API_KEY;
            Glide.with(holder.mPlaceAttributes.placePhoto.getContext())
                    .load(photoBaseUrl+ photoReference +placeApiKey)
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.mPlaceAttributes.placePhoto);
        } else {
            holder.mPlaceAttributes.placePhoto.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mListOfPlaces.size();
    }

    public void setLocation(double deviceLatitude, double deviceLongitude) {
        mDeviceLatitude = deviceLatitude;
        mDeviceLongitude = deviceLongitude;
    }

    public static class ListViewViewHolder extends RecyclerView.ViewHolder{
        private final PlaceAttributesBinding mPlaceAttributes;
        public ListViewViewHolder(@NonNull PlaceAttributesBinding placeAttributes) {
            super(placeAttributes.getRoot());
            this.mPlaceAttributes = placeAttributes;
        }
    }
}
