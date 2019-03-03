package crdma.genxcoders.com.disasterapp.activity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.state.Weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import crdma.genxcoders.com.disasterapp.R;
import crdma.genxcoders.com.disasterapp.network.Function;
import crdma.genxcoders.com.disasterapp.network.MyNotificationManager;
import crdma.genxcoders.com.disasterapp.utils.AppConstants;
import crdma.genxcoders.com.disasterapp.utils.AppPrefs;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

     Context mContext = MainActivity.this;
    ProgressBar loader;


    CardView cvReportDisaster, cvRecentEvent,cvHelpline,cvFir;
     BroadcastReceiver mReceiver;
     IntentFilter mIntentFilter;
     String day;
     String month;
     TextView tvDay,tvMonth,tvTime;
     TextView  tvWeather, tvClimate,tvIcon;
    Typeface weatherFont;
    String city = "Pune";
    /* Please Put your API KEY here */
    String OPEN_WEATHER_MAP_API = "f50b8fef75f56416ee4f5fcbd46244f8";
    /* Please Put your API KEY here */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        notification();
        initController();

        weatherFont = Typeface.createFromAsset(getAssets(), "weathericons-regular-webfont.ttf");
        tvIcon.setTypeface(weatherFont);

        taskLoadUp(city);


        try {
            JSONObject jsonObject=new JSONObject(AppPrefs.getStringPref("user",mContext));


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.tv_user_name);
        TextView navDesignation = (TextView) headerView.findViewById(R.id.tv_designation);
        navUsername.setText(jsonObject.getString("fname")+" "+jsonObject.getString("lname"));
            navDesignation.setText(jsonObject.getString("designation"));




        } catch (JSONException e) {
            e.printStackTrace();
        }



        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent myNewActivity = new Intent(mContext, MainActivity.class);
                startActivity(myNewActivity);
            }
        };

        mIntentFilter=new IntentFilter("OPEN_NEW_ACTIVITY");





    }

    private void initController() {


        cvReportDisaster=findViewById(R.id.cv_reportdisaster);
        cvRecentEvent=findViewById(R.id.cv_event);
        cvHelpline=findViewById(R.id.cv_helpline);
        cvFir=findViewById(R.id.cv_fir);



        tvDay=findViewById(R.id.tvDay);
        tvMonth=findViewById(R.id.tvMonth);
        tvTime =findViewById(R.id.tvTime);

        tvClimate = findViewById(R.id.tvClimate);
        tvWeather= findViewById(R.id.tvWeather);
        tvIcon =findViewById(R.id.tvIcon);






        cvReportDisaster.setOnClickListener(this);
        cvRecentEvent.setOnClickListener(this);
        cvHelpline.setOnClickListener(this);
        cvFir.setOnClickListener(this);
//        cvEmergencyContact.setOnClickListener(this);
        Calendar cal =Calendar.getInstance();
        // year = cal.get(Calendar.YEAR);
        // month = cal.get(Calendar.MONTH);
        // day = cal.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM", Locale.US);
        month =month_date.format(cal.getTime());
        tvMonth.setText(""+month);

        SimpleDateFormat day_date = new SimpleDateFormat("dd,yyyy", Locale.US);
        day =day_date.format(cal.getTime());
        tvDay.setText(""+day);


        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("h:mm a");
        format.setTimeZone(c.getTimeZone());
        String myFormattedtime = format.format(c.getTime());
        tvTime.setText(""+myFormattedtime);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_warning_black_24dp)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to Exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                            finish();
                        }


                    })
                    .setNegativeButton("No", null)
                    .show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fir) {
            Intent eventIntent= new Intent(this,RecentEventActivity.class);
            startActivity(eventIntent);
        } else if (id == R.id.nav_sos) {
            Intent sosIntent = new Intent(mContext,SosActivity.class);
            startActivity(sosIntent);
        } else if (id == R.id.nav_emergency_contacts) {
            Intent contactIntent = new Intent(mContext,EmergencyContactActivity.class);
            startActivity(contactIntent);
        } else if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(mContext,ProfileActivity.class);
            startActivity(profileIntent);
        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(mContext)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to LogOut?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            AppPrefs.putBooleanPref("IsLogin",false,
                                    mContext);

                            Intent intent = new Intent(mContext,
                                    LoginActivity.class);
                            startActivity(intent);

                        }


                    })
                    .setNegativeButton("No", null)
                    .show();



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.cv_reportdisaster:
                Intent reportIntent= new Intent(this,ReportDisasterActivity.class);
                startActivity(reportIntent);
                break;
            case R.id.cv_event:
                Intent eventIntent= new Intent(this,RecentEventActivity.class);
                startActivity(eventIntent);
                break;

            case R.id.cv_helpline:
                Intent helplineIntent= new Intent(this,EmergencyContactActivity.class);
                startActivity(helplineIntent);
                break;
            case R.id.cv_fir:
                Intent firIntent= new Intent(this,ReportDisasterActivity.class);
                startActivity(firIntent);
                break;

        }
    }


    private void notification(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(AppConstants.CHANNEL_ID, AppConstants.CHANNEL_NAME, importance);
            mChannel.setDescription(AppConstants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

        /*
         * Displaying a notification locally
         */



    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }






    @Override
    protected void onPause() {
        super.onPause();
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
        mReceiver = null;
    }



    public void taskLoadUp(String query) {
        if (Function.isNetworkAvailable(getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(query);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }



    @SuppressLint("StaticFieldLeak")
    class DownloadWeather extends AsyncTask< String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loader.setVisibility(View.VISIBLE);

        }
        protected String doInBackground(String...args) {
            String xml = Function.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = null;
                    try {
                        details = json.getJSONArray("weather").getJSONObject(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject main = json.getJSONObject("main");
                   // DateFormat df = DateFormat.getDateTimeInstance();

                   String city= json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country");
                    String details2=(details.getString("description").toUpperCase(Locale.US));
                    tvWeather.setText(String.format("%.1f", main.getDouble("temp")) + "°");
                   // humidity_field.setText("Humidity: " + main.getString("humidity") + "%");
                  //  pressure_field.setText("Pressure: " + main.getString("pressure") + " hPa");
                   // updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));
                    tvIcon.setText(Html.fromHtml(Function.setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000)));

                   // loader.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }


        }



    }



}