package crdma.genxcoders.com.disasterapp.network;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


//the class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {


    //this method will be called
    //when the token is generated
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        //now we will have the token
        String token = FirebaseInstanceId.getInstance().getToken();

        //for now we are displaying the token in the log
        //copy it as this method is called only when the new token is generated
        //and usually new token is only generated when the app is reinstalled or the data is cleared
        Log.e("MyRefreshedToken", token);

    }
}