package crdma.genxcoders.com.disasterapp.network;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crdma.genxcoders.com.disasterapp.R;
import crdma.genxcoders.com.disasterapp.main.WLAppController;
import crdma.genxcoders.com.disasterapp.utils.AppCommonMethods;
import crdma.genxcoders.com.disasterapp.utils.AppConstants;
import crdma.genxcoders.com.disasterapp.utils.AppPrefs;

import static crdma.genxcoders.com.disasterapp.network.NCLUrls.LOGIN_URL;

/**
 * Created by Morya on 3/21/2018.
 */

public class WLAPIcalls implements AppConstants {
    private Context mContext;
    private String TAG = "WLAPIcalls";
    private ProgressDialog pDialog;
    private OnAPICallCompleteListener mApiCallCompleteListener;
    private OnAPICallError mApiCallError;
    private String mFlag = "";
    private boolean mIsFlag = false;
    public static boolean isActivityOn = false;
    public static Dialog dialog;
//        private NCLDatabaseAccess mAccess;

    public WLAPIcalls(Context mContext) {
        this.mContext = mContext;
//            mAccess = NCLDatabaseAccess.getInstance(mContext);
    }

    public WLAPIcalls(Context mContext, String flag, OnAPICallCompleteListener listener) {
        this.mContext = mContext;
        this.mFlag = flag;
        mApiCallCompleteListener = listener;
//            mAccess = NCLDatabaseAccess.getInstance(mContext);
    }

    public WLAPIcalls(Context mContext, String flag, OnAPICallCompleteListener listener, OnAPICallError mApiCallError) {
        this.mContext = mContext;
        this.mFlag = flag;
        mApiCallCompleteListener = listener;
        this.mApiCallError = mApiCallError;
//            mAccess = NCLDatabaseAccess.getInstance(mContext);
    }

    public WLAPIcalls(Context mContext, boolean flag) {
        this.mContext = mContext;
        this.mIsFlag = flag;
//            mAccess = NCLDatabaseAccess.getInstance(mContext);
    }


    public interface OnAPICallCompleteListener {
        void onAPICallCompleteListner(Object item, String flag, String result) throws JSONException;
    }

    public interface OnAPICallError {
        void Error(Object item, String flag, VolleyError error) throws JSONException;
    }
    /* *
     * <b>public Map<String, String> getNCLHeaders()</b>
     * <p>This method is used to get headers required for API calls</p>
     *
     * @return
     * @throws AuthFailureError*/

    /**
     * <b>public Map<String, String> getNCLHeaders()</b>
     * <p>This method is used to get headers required for API calls</p>
     *
     * @return
     * @throws AuthFailureError
     *
     */
    public Map<String, String> getNCLHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put(APP_CLIENT_TYPE_KEY, APP_CLIENT_TYPE);
        headers.put(mContext.getString(R.string.authorization), AppPrefs.getStringPref(AUTH_TOKEN, mContext));
        return headers;
    }

    /**
     * <b>public Map<String, String> getNCLHeaders()</b>
     * <p>This method is used to get headers required for API calls</p>
     *
     * @return
     * @throws AuthFailureError
     */
    public Map<String, String> getGuestHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put(APP_CLIENT_TYPE_KEY, APP_CLIENT_TYPE);
//        headers.put(mContext.getString(R.string.authorization), AppPrefs.getStringPref(AUTH_TOKEN, mContext));
        return headers;
    }

    /**
     * <b>public void addCustomJSONObjectRequestToQueue</b>
     * <p>This method is called to add every request to API queue and define retry policy</p>
     *
     * @param jsObjRequest - request to be added to queue
     * @param requestTag   - string tag which will be used to identify request
     */
    public void addCustomJSONObjectRequestToQueue(NCLCustomJSONObjectRequest jsObjRequest, String requestTag) {
        jsObjRequest.setRetryPolicy(new
                DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        // Adding request to request queue
        WLAppController.getInstance().addToRequestQueue(jsObjRequest, requestTag);
    }


    /**
     * <b>public void addJsonObjectRequestToQueue</b>
     * <p>This method is called to add every request to API queue and define retry policy</p>
     *
     * @param jsObjRequest - request to be added to queue
     * @param requestTag   - string tag which will be used to identify request
     */
    public void addJsonObjectRequestToQueue(JsonObjectRequest jsObjRequest, String requestTag) {
        jsObjRequest.setRetryPolicy(new
                DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        // Adding request to request queue
        WLAppController.getInstance().addToRequestQueue(jsObjRequest, requestTag);
    }

    public class MetaRequest extends JsonObjectRequest {

        public MetaRequest(int method, String url, JSONObject jsonRequest, Response.Listener
                <JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        public MetaRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject>
                listener, Response.ErrorListener errorListener) {
            super(url, jsonRequest, listener, errorListener);


        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                JSONObject jsonResponse = new JSONObject(jsonString);
                jsonResponse.put("headers", new JSONObject(response.headers));
                JSONObject headers = new JSONObject(response.headers);
                if (headers.has(mContext.getString(R.string.authorization))) {

                    AppPrefs.putStringPref(AUTH_TOKEN, headers.getString(mContext.getString(R.string.authorization)), mContext);
                    new AppCommonMethods(mContext).LOG(0, TAG, headers.getString(mContext.getString(R.string.authorization)));
                }
                return Response.success(jsonResponse,
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }

    /**
     * <b></b>
     * <p></p>
     */

    public void loginRequest(String username, String password) {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        JSONObject jsonObject = new JSONObject();


        String url = LOGIN_URL+"?username="+username+"&password="+password;
        new AppCommonMethods(mContext).LOG(0, TAG, url);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET,
                url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        if (!((Activity) mContext).isFinishing()) {
                            pDialog.dismiss();
                        }
                        // response
                        Log.d("Response", response.toString());


                        try {
                            mApiCallCompleteListener.onAPICallCompleteListner(true, mFlag, response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!((Activity) mContext).isFinishing()) {
                            // gotoNoInternet();
                            pDialog.dismiss();

                        }
                        Toast.makeText(mContext, "User Id or password incorrect", Toast.LENGTH_SHORT).show();

                        WLAPIcalls.this.onErrorResponse(error);
                    }
                }
        )
//        {
//            /**
//             * Passing some request headers
//             * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                return getNCLHeaders();
//            }
//        }
                ;
        WLAppController.getInstance().addToRequestQueue(getRequest);
    }










    public void helplineContactRequest() {

        JSONObject param = new JSONObject();

        MetaRequest getRequest = new MetaRequest(Request.Method.GET, NCLUrls.HELPLINE_CONTACTS_URL, param,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        // response
                        Log.d("Response", response.toString());
                        try {
                            if (!((Activity) mContext).isFinishing()) {

                            }


                            mApiCallCompleteListener.onAPICallCompleteListner(true, mFlag, response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(mContext, "Error loading contacts", Toast.LENGTH_SHORT).show();
                        if (!((Activity) mContext).isFinishing()) {

                        }
                        WLAPIcalls.this.onErrorResponse(error);
                        //logoutRequest();
                    }
                }
        ) {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getNCLHeaders();
            }
        };
        WLAppController.getInstance().addToRequestQueue(getRequest);
    }



    /**
     * This function is used to handle error in response
     *
     * @param error
     */
    private void onErrorResponse(VolleyError error) {
//        Crashlytics.logException(error);
        VolleyLog.d(TAG, "Error: " + error.getMessage());
        NetworkResponse response = error.networkResponse;
        if (response != null) {
            try {
                if (response.statusCode == STATUS_AUTH_ERROR) {
                    new AppCommonMethods(mContext).showToast(mContext,"" + (new JSONObject(new String(response.data))).getString("message"));
                    Log.d(TAG, "Error: " + error.getMessage() + " status code: " + error.networkResponse.statusCode+" "+(new JSONObject(new String(response.data))).getString("message"));
                } else {
                    new AppCommonMethods(mContext).showToast(mContext,"" + (new JSONObject(new String(response.data))).getString("message"));
                    Log.d(TAG, "Error: " + error.getMessage() + " status code: " + error.networkResponse.statusCode+" "+(new JSONObject(new String(response.data))).getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            VolleyLog.d(TAG, "Error: " + error.getMessage() + " status code: " + error.networkResponse.statusCode+" "+response.data);
        }
    }






}