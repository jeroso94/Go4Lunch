package com.example.go4lunch.ui.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.databinding.PlaceAttributesBinding;
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

    public ListViewAdapter(@NonNull List<NearbyPlaceModel> listOfPlaces) {
        mListOfPlaces = listOfPlaces;
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

        /**
         * TODO: Rendre compatible l'URI de la photo Place API avec Glide.load
         * TODO: Règle de calcul pour l'affichage des STARS
         * TODO: Calcul de la distance entre le device et le restaurant
         */
        NearbyPlaceModel place = mListOfPlaces.get(position);
        holder.mPlaceAttributes.name.setText(place.getName());
        holder.mPlaceAttributes.address.setText(place.getPlaceId());
        holder.mPlaceAttributes.openHours.setText(place.getOpeningHours().toString());
        /**
        Glide.with(holder.mPlaceAttributes.placePhoto.getContext())
                .load(place.)
                .apply(RequestOptions.centerCropTransform())
                .into(holder.mPlaceAttributes.placePhoto);
         */
    }

    @Override
    public int getItemCount() {
        return mListOfPlaces.size();
    }

    public static class ListViewViewHolder extends RecyclerView.ViewHolder{
        private final PlaceAttributesBinding mPlaceAttributes;
        public ListViewViewHolder(@NonNull PlaceAttributesBinding placeAttributes) {
            super(placeAttributes.getRoot());
            this.mPlaceAttributes = placeAttributes;
        }
    }
}
