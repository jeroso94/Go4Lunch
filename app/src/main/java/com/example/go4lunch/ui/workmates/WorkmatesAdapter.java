package com.example.go4lunch.ui.workmates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentWorkmatesBinding;
import com.example.go4lunch.databinding.PlaceAttributesBinding;
import com.example.go4lunch.databinding.WorkmateAttributesBinding;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;
import com.example.go4lunch.ui.ViewModelFactory;
import com.example.go4lunch.ui.list.ListViewAdapter;
import com.example.go4lunch.ui.place_details.PlaceDetailsViewModel;

import java.util.List;

/**
 * Created by JeroSo94 on 31/03/2022.
 */
public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.WorkmatesViewHolder>{

    @NonNull
    private final List<UserModel> mUsersList;

    public WorkmatesAdapter(@NonNull List<UserModel> usersList) {
        mUsersList = usersList;
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        WorkmateAttributesBinding workmateItems  = WorkmateAttributesBinding.inflate(inflater, parent, false);
        return new WorkmatesViewHolder(workmateItems);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesAdapter.WorkmatesViewHolder holder, int position) {
        UserModel user = mUsersList.get(position);

        /* TODO: Réviser le code pour obtentir la photo (pas sûr que ce soit une url). Faut-il plutôt la charger depuis sampledata ?*/
        if (user.getUrlPicture() != null) {
            Glide.with(holder.mWorkmateAttributesViewHolder.picture.getContext())
            .load(user.getUrlPicture())
            .apply(RequestOptions.centerCropTransform())
            .into(holder.mWorkmateAttributesViewHolder.picture);
        } else {
            holder.mWorkmateAttributesViewHolder.picture.setVisibility(View.GONE);
        }

        if (user.getPlaceName() != null) {
            holder.mWorkmateAttributesViewHolder.workmateChoice.setText(String.format("%s is eating at (%s)", user.getUsername(), user.getPlaceName()));
        } else {
            holder.mWorkmateAttributesViewHolder.workmateChoice.setText(String.format("%s hasn't decided yet", user.getUsername()));
        }
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    public static class WorkmatesViewHolder extends RecyclerView.ViewHolder {
        private final WorkmateAttributesBinding mWorkmateAttributesViewHolder;
        public WorkmatesViewHolder(@NonNull WorkmateAttributesBinding workmateAttributesViewHolder) {
            super(workmateAttributesViewHolder.getRoot());
            this.mWorkmateAttributesViewHolder=workmateAttributesViewHolder;
        }
    }
}
