package com.nawaqes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nawaqes.Activities.Home;
import com.nawaqes.Model.CountNotifications_Response;
import com.nawaqes.ViewModel.CountNotifications_ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    String title,message,address,time,Day;
    MyNotification mNotificationManager;
    Intent intent;
    Context context;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        context=getApplicationContext();
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                Map<String, String> params = remoteMessage.getData();

                JSONObject json = new JSONObject(params);
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            String status = NetworkUtil.getConnectivityStatusString(context);
            Log.e("Receiver ", "" + status);
            if (status.equals("Not connected to Internet")) {
                Log.e("Receiver ", "not connction");// your code when internet lost
            } else {
                Log.e("Receiver ", "connected to internet");//your code when internet connection come back
            }
        }
    }
    private void sendPushNotification(JSONObject json) {
        mNotificationManager = new MyNotification(getApplicationContext());
        intent = new Intent(getApplicationContext(), Home.class);
        intent.putExtra("notification","notification");
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
//            JSONObject data = json.getJSONObject("data");

            title = json.getString("title");
            message = json.getString("message");
//            if(address.equals("null")) {
            mNotificationManager.showSmallNotificati(title, message, intent);

//            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }


}
