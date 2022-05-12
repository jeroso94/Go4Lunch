package com.example.go4lunch.ui.workmates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.WorkmateAttributesBinding;
import com.example.go4lunch.model.UserModel;

import java.util.List;

/**
 * Created by JeroSo94 on 31/03/2022.
 */
public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.WorkmatesViewHolder>{

    @NonNull
    private final List<UserModel> mUsersList;
    private Context mContext;

    public WorkmatesAdapter(Context context, @NonNull List<UserModel> usersList) {
        this.mContext = context;
        mUsersList = usersList;
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        WorkmateAttributesBinding workmateAttributes  = WorkmateAttributesBinding.inflate(inflater, parent, false);
        return new WorkmatesViewHolder(workmateAttributes);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesAdapter.WorkmatesViewHolder holder, int position) {
        UserModel user = mUsersList.get(position);


        if (user.getUrlPicture() != null) {
            Glide.with(holder.mWorkmateAttributesViewHolder.picture.getContext())
            .load(user.getUrlPicture())
            .apply(RequestOptions.centerCropTransform())
            .into(holder.mWorkmateAttributesViewHolder.picture);
        } else {
            holder.mWorkmateAttributesViewHolder.picture.setVisibility(View.GONE);
        }

        if (user.getPlaceName() != null) {
            holder.mWorkmateAttributesViewHolder.workmateChoice.setText(String.format("%s %s (%s)", user.getUsername(), mContext.getResources().getString(R.string.workmate_choice_done), user.getPlaceName()));
        } else {

            holder.mWorkmateAttributesViewHolder.workmateChoice.setText(String.format("%s ", user.getUsername(), mContext.getResources().getResourceName(R.string.workmates_choice_todo)));
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
