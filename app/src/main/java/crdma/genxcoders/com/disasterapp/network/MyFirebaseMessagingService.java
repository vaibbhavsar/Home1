package crdma.genxcoders.com.disasterapp.network;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import crdma.genxcoders.com.disasterapp.activity.MainActivity;
import crdma.genxcoders.com.disasterapp.activity.SosActivity;
import crdma.genxcoders.com.disasterapp.utils.AppPrefs;

import static android.support.constraint.Constraints.TAG;
import static crdma.genxcoders.com.disasterapp.utils.AppConstants.IS_FROM_BRODCAST;
import static crdma.genxcoders.com.disasterapp.utils.AppConstants.PREFS_SOS_SNOOZ;

//class extending FirebaseMessagingService
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);



        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Log.d(TAG, "Message data payload: " + remoteMessage.getData());


        }

        //getting the title and the body
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            Intent broadcast = new Intent();
            broadcast.setAction("OPEN_NEW_ACTIVITY");
                    sendBroadcast(broadcast);
        }



    }

    @Override
    public void onCreate() {
        super.onCreate();


        AppPrefs.putIntegerPref(PREFS_SOS_SNOOZ,0,this);

        Intent in= new Intent(this,SosActivity.class);
        in.putExtra(IS_FROM_BRODCAST,true);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);


    }



//then here we can use the title and body to build a notification
    }
