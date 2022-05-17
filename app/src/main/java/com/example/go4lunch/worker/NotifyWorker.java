package com.example.go4lunch.worker;

import static android.content.ContentValues.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.go4lunch.R;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JeroSo94 on 04/05/2022.
 */
public class NotifyWorker extends Worker {
    private static final String COLLECTION_NAME = "users";
    List<UserModel> mUsersList = new ArrayList<>();
    FirebaseUser mCurrentUser;

    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                mUsersList.add(document.toObject(UserModel.class));
                            }
                            remindMeBookedLunchPlace();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return Result.success();
    }

    private void remindMeBookedLunchPlace() {
        final String CHANNEL_ID = "fcm_default_channel";
        int NOTIFICATION_ID = 112233;
        String NOTIFICATION_TAG = "GO4LUNCH";

        String notificationTitle = getApplicationContext().getString(R.string.notification_title);
        String notificationText = getApplicationContext().getString(R.string.notification_default_text);

        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String[] myResto = new String[3];
        List<String> workmatesToLunchWith = new ArrayList<>();

        for (UserModel user:mUsersList){
            if (user.getUid().equals(mCurrentUser.getUid())) {
                myResto[0] = user.getPlaceId();
                myResto[1] = user.getPlaceName();
                myResto[2] = user.getPlaceAddress();
            }

            if (user.getPlaceId().equals(myResto[0])) {
                workmatesToLunchWith.add(user.getUsername());
            }
        }

        notificationText = String.format("%s, %s - %s", myResto[1], myResto[2], workmatesToLunchWith);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                /*.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationText))*/
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Go4Lunch Messages";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}
