package com.example.smokashi.smartcontainer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by smokashi on 06-04-2017.
 */

public class FCMTokenHelper  extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private final String url ="http://10.192.39.123:8080/messenger2/webapi/Database/addFCMTokenToDb";

    public FCMTokenHelper(Context context)
    {
        mContext = context;
    }
    @Override
    protected Void doInBackground(Void... arg0) {


           String token = FirebaseInstanceId.getInstance().getToken();

        HttpHandler sh = new HttpHandler();

                /*   JSONObject tokenInfo = new JSONObject();
                    try {
                        tokenInfo.put("token_value",token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
        Boolean  result = sh.makeServiceCall(url,"POST",token);
        if(!result)
        {
            // Log.e(TAG, "Error making request");
            Toast.makeText(mContext,"Error making request", Toast.LENGTH_LONG).show();
        }
        return null;
    }



}
