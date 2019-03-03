package crdma.genxcoders.com.disasterapp.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import crdma.genxcoders.com.disasterapp.R;
import crdma.genxcoders.com.disasterapp.services.MyBroadcastReceiver;
import crdma.genxcoders.com.disasterapp.utils.AppPrefs;

import static crdma.genxcoders.com.disasterapp.utils.AppConstants.IS_FROM_BRODCAST;
import static crdma.genxcoders.com.disasterapp.utils.AppConstants.PREFS_SOS_SNOOZ;

public class SosActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isFromBrodcast=false;
    private Uri alrmSosURI;
    private Ringtone ringtoneSOS;
    private MediaPlayer mpSos;

    private Context mContext=SosActivity.this;


    private Button btnignore;
    private Button btnAccept;
    private Button btnNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        setTitle("SOS Alert");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        isFromBrodcast=getIntent().getBooleanExtra(IS_FROM_BRODCAST,false);
        btnNavigation=findViewById(R.id.btn_navigate_sos);
        btnAccept=findViewById(R.id.btn_accept_sos);
        btnignore=findViewById(R.id.btn_ignor_sos);

        btnAccept.setOnClickListener(this);

        btnNavigation.setOnClickListener(this);

        btnignore.setOnClickListener(this);

        if(isFromBrodcast)
        {
            createSOS();
        }
    }
    void createSOS()
    {
        try {
            mpSos= MediaPlayer.create(mContext, R.raw.dod);
            mpSos.setLooping(true);
            mpSos.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    void ignoresos()
    {

        mpSos.stop();
        int snoozInt=15+AppPrefs.getIntPref(PREFS_SOS_SNOOZ,mContext);
        AppPrefs.putIntegerPref(PREFS_SOS_SNOOZ,snoozInt,mContext);

        startSOSOnSnooz(snoozInt);
//        startSOSOnSnooz(5);
    }
    private void startNavigation() {
        String location = "21.1523063,79.0882217";
        Uri intentUrl = Uri.parse("google.navigation:q="+location);
        Intent intent = new Intent(Intent.ACTION_VIEW,intentUrl);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
    void acceptNavigation(){
        mpSos.stop();
        startNavigation();
    }
    void onAccept()
    {
        mpSos.stop();
    }
    public void startSOSOnSnooz(int i){
//        EditText text = findViewById(R.id.time);
//        int i = Integer.parseInt(text.getText().toString());


        mpSos.stop();
        Intent intent = new Intent(mContext, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);
        Toast.makeText(this, "SOS set in " + i + " seconds",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btn_navigate_sos:
                acceptNavigation();
                break;
            case R.id.btn_accept_sos:
                onAccept();
                break;
            case R.id.btn_ignor_sos:
                ignoresos();
                break;
        }


    }
}
