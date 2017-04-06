package com.example.smokashi.smartcontainer;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by smokashi on 06-04-2017.
 */

public class FCMInstanceIDService extends FirebaseInstanceIdService {
    public final String url ="http://localhost:8080/messenger2/webapi/Database/addFCMTokenToDb";
    @Override
    public void onTokenRefresh()
    {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        HttpHandler sh = new HttpHandler();

        JSONObject tokenInfo = new JSONObject();
        try {
            tokenInfo.put("token_value",recent_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Boolean  result = sh.makeServiceCall(url,"POST",tokenInfo);
        if(!result)
        {
            Log.e(TAG, "Error making request");
            Toast.makeText(getApplicationContext(),"Error making request",Toast.LENGTH_LONG).show();
        }


    }

}
