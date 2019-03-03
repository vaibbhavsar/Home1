package crdma.genxcoders.com.disasterapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import crdma.genxcoders.com.disasterapp.R;
import crdma.genxcoders.com.disasterapp.models.Helpline;
import crdma.genxcoders.com.disasterapp.network.WLAPIcalls;
import crdma.genxcoders.com.disasterapp.utils.AppCommonMethods;
import crdma.genxcoders.com.disasterapp.utils.AppPrefs;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.LOGIN;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, WLAPIcalls.OnAPICallCompleteListener {


    Button btnLogin;
    EditText etUserName, etPassword;
    Context mContext = LoginActivity.this;
    public static final String LOGIN = "login";
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        initController();

        Boolean saveLogin = loginPreferences.getBoolean("saveLogin", false);
        boolean isLogin = AppPrefs.getBooleanPref("IsLogin", mContext);

        if (saveLogin) {
//            todologin();

            etUserName.setText(loginPreferences.getString("username", ""));
            etPassword.setText(loginPreferences.getString("password", ""));
        }
        if (isLogin) {
            todologin();
        }

    }

    private void initController() {


        etUserName = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(this);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:

                String username = etUserName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty()) {

                    Toast.makeText(mContext, "Email ID not Entered", Toast.LENGTH_SHORT).show();

                } else if (password.isEmpty()) {
                    Toast.makeText(mContext, "Password not Entered", Toast.LENGTH_SHORT).show();

                } else {
                    getAuthentication(username, password);

                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();

                }


        }
    }


    private void getAuthentication(String username, String password) {
        if (new AppCommonMethods(mContext).isNetworkAvailable()) {
            WLAPIcalls mAPIcall = new WLAPIcalls(mContext, LOGIN, this);

            mAPIcall.loginRequest(username, password);


        } else {
            Toast.makeText(mContext, "Internet Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAPICallCompleteListner(Object item, String flag, String result) throws JSONException {

        if (flag.equals(LOGIN)) {
            Log.e("Response", result);
            JSONObject jsonObject = new JSONObject(result);
            // String hashID = jsonObject.getString("hashID");
            //  Log.e("LOGIN:", String.valueOf(hashID));


            try {
                JSONArray cast = jsonObject.getJSONArray("hashID");


                for (int i = 0; i < cast.length(); i++) {
                    JSONObject contactObjJson = cast.getJSONObject(i);
                    String status_code = contactObjJson.getString("status_code");
                    String message = contactObjJson.getString("message");

                    Log.e("Status Code:", status_code);
                    Log.e("Message", message);


                    if (message.equalsIgnoreCase("Login Successful")) {
                        Toast.makeText(mContext, "Login Successful", Toast.LENGTH_SHORT).show();

                        JSONObject jsonobjUser = contactObjJson.getJSONObject("data");
                        jsonobjUser = jsonobjUser.getJSONObject("user");
                        AppPrefs.putStringPref("user", jsonobjUser.toString(),mContext);

                        Intent mainIntent = new Intent(mContext, MainActivity.class);
                        startActivity(mainIntent);

                        AppPrefs.putBooleanPref("IsLogin", true, mContext);
                        finish();

                    } else {
                        Toast.makeText(mContext, "User Id or password incorrect", Toast.LENGTH_SHORT).show();

                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    public void todologin() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }
}