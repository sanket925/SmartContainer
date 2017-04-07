/**
 * Created by viyadav on 22-03-2017.
 */

package com.example.smokashi.smartcontainer;


import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler(){
    }

    public String makeServiceCall(String reqUrl,String Method){
        String response = null;

        try{
            URL url = new URL(reqUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(Method);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e){
            Log.e(TAG, "MalformedURLException: "+ e.getMessage());
        } catch (ProtocolException e){
            Log.e(TAG, "ProtocolException: "+ e.getMessage());
        } catch (IOException e){
            Log.e(TAG, "IOException: "+ e.getMessage());
        } catch (Exception e){
            Log.e(TAG, "Exception: "+ e.getMessage());
        }

        return response;
    }
    public Boolean makeServiceCall(String reqUrl, String Method, String input) {
        String response = null;
        Boolean rc = true;
        try{
            URL url = new URL(reqUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(Method);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "text/plain");
            OutputStream os = conn.getOutputStream();

           // String input = json.toString();
            os.write(input.getBytes());
            os.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
        } catch (MalformedURLException e){
            Log.e(TAG, "MalformedURLException: "+ e.getMessage());
            rc = false;
        } catch (ProtocolException e){
            Log.e(TAG, "ProtocolException: "+ e.getMessage());
            rc = false;
        } catch (IOException e){
            Log.e(TAG, "IOException: "+ e.getMessage());
            rc = false;
        } catch (Exception e){
            Log.e(TAG, "Exception: "+ e.getMessage());
            rc = false;
        }
        return rc ;
    }


    private String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;

        try{
            while((line = reader.readLine()) != null){
                sb.append(line).append('\n');
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
