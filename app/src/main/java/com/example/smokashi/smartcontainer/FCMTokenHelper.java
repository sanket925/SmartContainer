package com.example.smokashi.smartcontainer;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by smokashi on 06-04-2017.
 */

public class FCMTokenHelper {
    private static FCMTokenHelper instance;
    private static Context mContext;
    private RequestQueue requestQueue;

    private FCMTokenHelper(Context context)
    {
        mContext = context;
        requestQueue=getRequestQueue();
    }

    private RequestQueue getRequestQueue()
    {
        if(requestQueue ==null)
        {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized FCMTokenHelper getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new FCMTokenHelper(context);
        }
        return instance;
    }

    public<T> void addToRequestQueue(Request<T> request)
    {
            getRequestQueue().add(request);
    }

}
