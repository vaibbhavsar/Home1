package crdma.genxcoders.com.disasterapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;  
import android.content.Intent;  
import android.media.MediaPlayer;  
import android.widget.Toast;  
  import crdma.genxcoders.com.disasterapp.R;
import crdma.genxcoders.com.disasterapp.activity.SosActivity;

import static crdma.genxcoders.com.disasterapp.utils.AppConstants.IS_FROM_BRODCAST;

public class MyBroadcastReceiver extends BroadcastReceiver {
    MediaPlayer mp;  
    @Override  
    public void onReceive(Context context, Intent intent) {
        Intent in= new Intent(context,SosActivity.class);
        in.putExtra(IS_FROM_BRODCAST,true);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(in);    }
}  