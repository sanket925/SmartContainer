package com.example.smokashi.smartcontainer;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ContainerListActivity extends AppCompatActivity {

    public String TAG = ContainerListActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    private static String url ="http://10.0.2.2:8080/messenger/webapi/messages";

    ArrayList<HashMap<String, String>> authorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_list);

        authorList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetAuthor().execute();

    }

private class GetAuthor extends AsyncTask<Void, Void, Void>{

    @Override
    protected void onPreExecute(){
        super.onPreExecute();

        pDialog = new ProgressDialog(ContainerListActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        HttpHandler sh = new HttpHandler();

        String jsonStr = sh.makeServiceCall(url);

        Log.e(TAG, "Response from URL: " + jsonStr);

        if(jsonStr != null){
            try{
            //    JSONObject jsonObj = new JSONObject(jsonStr);

                JSONArray authors = new JSONArray(jsonStr);

                for(int i=0; i<authors.length(); i++){
                    JSONObject a = authors.getJSONObject(i);

                    String author = a.getString("author");
                    String id = a.getString("id");
                    String message = a.getString("message");

                    HashMap<String, String> authorMap = new HashMap<>();

                    authorMap.put("author" , author);
                    authorMap.put("id" , id);
                    authorMap.put("message" , message);

                    authorList.add(authorMap);
                }
            } catch(final JSONException e){
                Log.e(TAG, "Json PArsing Error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "JSOn parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else{
            Log.e(TAG, "Couldn't get json from server");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Couldn't get json from server. Check LogCat for possible errors! ", Toast.LENGTH_LONG ).show();
                }
            });
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute(result);

        if(pDialog.isShowing())
            pDialog.dismiss();

        ListAdapter adapter = new SimpleAdapter(
          ContainerListActivity.this, authorList,
                R.layout.list_item, new String[]{"author","id","message"},
                new int[]{R.id.author,R.id.id,R.id.message});

        lv.setAdapter(adapter);
    }

}


}


