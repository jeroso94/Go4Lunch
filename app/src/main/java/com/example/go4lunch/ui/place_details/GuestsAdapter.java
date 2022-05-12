package com.example.go4lunch.ui.place_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.GuestAttributesBinding;
import com.example.go4lunch.databinding.WorkmateAttributesBinding;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.model.place_details_search.PlaceDetailsModel;
import com.example.go4lunch.ui.workmates.WorkmatesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JeroSo94 on 09/04/2022.
 */
public class GuestsAdapter extends RecyclerView.Adapter<GuestsAdapter.GuestsViewHolder>{

    private final List<UserModel> mGuestsList;
    private PlaceDetailsModel mPlaceDetails;
    private Context mContext;

    public GuestsAdapter(Context context, List<UserModel> guestsList, PlaceDetailsModel placeDetails) {
        this.mContext = context;
        mGuestsList = guestsList;
        mPlaceDetails = placeDetails;
    }

    @NonNull
    @Override
    public GuestsAdapter.GuestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        GuestAttributesBinding guestAttributes  = GuestAttributesBinding.inflate(inflater, parent, false);
        return new GuestsAdapter.GuestsViewHolder(guestAttributes);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestsAdapter.GuestsViewHolder holder, int position) {
        UserModel guest = mGuestsList.get(position);

        if (mGuestsList.size() == 0){
            holder.mGuestAttributesViewHolder.guest.setText(String.format("%s", mContext.getResources().getString(R.string.no_guest)));
        } else {
            if (guest.getPlaceId() != null && guest.getPlaceId().equals(mPlaceDetails.getPlaceId())) {
                if (guest.getUrlPicture() != null) {
                    Glide.with(holder.mGuestAttributesViewHolder.picture.getContext())
                            .load(guest.getUrlPicture())
                            .apply(RequestOptions.centerCropTransform())
                            .into(holder.mGuestAttributesViewHolder.picture);
                } else {
                    holder.mGuestAttributesViewHolder.picture.setVisibility(View.GONE);
                }

                holder.mGuestAttributesViewHolder.guest.setText(String.format("%s %s", guest.getUsername(), mContext.getResources().getString(R.string.guest_registered)));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mGuestsList.size();
    }


    public class GuestsViewHolder extends RecyclerView.ViewHolder {
        private final GuestAttributesBinding mGuestAttributesViewHolder;
        public GuestsViewHolder(@NonNull GuestAttributesBinding guestAttributesViewHolder) {
            super(guestAttributesViewHolder.getRoot());
            this.mGuestAttributesViewHolder = guestAttributesViewHolder;
        }
    }
}
