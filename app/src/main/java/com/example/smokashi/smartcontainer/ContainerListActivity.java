package com.example.smokashi.smartcontainer;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ContainerListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public String TAG = ContainerListActivity.class.getSimpleName();
    ListAdapter adapter;
    private ProgressDialog pDialog;
    private ListView lv;

    private static String url ="http://10.192.39.123:8080/messenger2/webapi/Database/getCurrentWeight";

    ArrayList<HashMap<String, String>> containerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_list);

        containerList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetContainers().execute();

        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String container_no= ((TextView) view.findViewById(R.id.container_no)).getText().toString();
        String container_name= ((TextView) view.findViewById(R.id.container_name)).getText().toString();

        Intent intent = new Intent(getApplicationContext(),ContainerDataDisplayActivity.class);
        intent.putExtra("container_no" , container_no);
        intent.putExtra("container_name" , container_name);
        startActivity(intent);
        finish();
    }

    private class GetContainers extends AsyncTask<Void, Void, Void>{

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

        String jsonStr = sh.makeServiceCall(url,"GET");

        Log.e(TAG, "Response from URL: " + jsonStr);

        if(jsonStr != null){
            try{
            //    JSONObject jsonObj = new JSONObject(jsonStr);

                JSONArray containersArray = new JSONArray(jsonStr);

                for(int i = 0; i< containersArray.length(); i++){
                    JSONObject a = containersArray.getJSONObject(i);

                    String container_name = /*"Name : " + */a.getString("container_name");
                    String current_weight = /*"Weight : " + */a.getString("Current_weight");
                    String container_id = /*"Id : " + */a.getString("container_id");

                    HashMap<String, String> containerMap = new HashMap<>();

                    containerMap.put("container_name" , container_name);
                    containerMap.put("container_current_weight" , current_weight);
                    containerMap.put("container_no" , container_id);

                    containerList.add(containerMap);
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

        adapter = new SimpleAdapter(
          ContainerListActivity.this, containerList,
                R.layout.list_item, new String[]{ "container_no" , "container_name" , "container_current_weight" },
                new int[]{ R.id.container_no , R.id.container_name , R.id.container_current_weight });

        lv.setAdapter(adapter);
    }

}


}


