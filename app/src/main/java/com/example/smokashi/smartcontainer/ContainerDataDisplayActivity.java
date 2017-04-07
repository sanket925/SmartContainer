package com.example.smokashi.smartcontainer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
//import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ContainerDataDisplayActivity extends AppCompatActivity {

    BarChart barChart;
    ArrayList<HashMap<String, String>> containerList;
    public String TAG = ContainerListActivity.class.getSimpleName();
    private static String url ="http://10.192.39.123:8080/messenger2/webapi/Database/containers/";
    private ProgressDialog pDialog;
    ListAdapter adapter;
    private ListView lv;
    String container_no;
    String container_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_data_display);

        containerList = new ArrayList<>();
        container_no = null;
        container_name = null;
        lv = (ListView) findViewById(R.id.list);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            container_no = extras.getString("container_no");
            container_name = extras.getString("container_name");
        }

        //url = url + container_no;

        new GetContainerData().execute();

       //drawBarGraph();
    }


    private class GetContainerData extends AsyncTask<Void , Void , Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            pDialog = new ProgressDialog(ContainerDataDisplayActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url + container_no,"GET");

            Log.e(TAG, "Response from URL: " + jsonStr);

            if(jsonStr != null){
                try{

                    JSONArray containerDataArray = new JSONArray(jsonStr);

                    for(int i = 0; i< containerDataArray.length(); i++){
                        JSONObject a = containerDataArray.getJSONObject(i);

                        String date = "Date : " + a.getString("date");
                        String weight = "Weight : " + a.getString("weight");

                        HashMap<String, String> containerMap = new HashMap<>();

                        containerMap.put("date" , date);
                        containerMap.put("weight" , weight);

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

            }else{
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

            //drawBarGraph();

            TextView containername = (TextView)findViewById(R.id.containerNameTextView);
            containername.setText(container_no + ". " + container_name);


            adapter = new SimpleAdapter(
                    ContainerDataDisplayActivity.this, containerList,
                    R.layout.data_list, new String[]{ "date" , "weight" },
                    new int[]{ R.id.date , R.id.weight });

            lv.setAdapter(adapter);
        }



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ContainerDataDisplayActivity.this, ContainerListActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }



    /*
    void drawBarGraph()
    {

        barChart = (BarChart) findViewById(R.id.bargraph);

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(0,45f));
        barEntries.add(new BarEntry(1,62f));
        barEntries.add(new BarEntry(2,30f));
        barEntries.add(new BarEntry(3,15f));
        barEntries.add(new BarEntry(4,75f));
        barEntries.add(new BarEntry(5,55f));
        barEntries.add(new BarEntry(6,40f));
        barEntries.add(new BarEntry(7,27f));

        ArrayList<String> theDates = new ArrayList<>();

        theDates.add("April");
        theDates.add("May");
        theDates.add("June");
        theDates.add("July");
        theDates.add("Aug");
        theDates.add("Sept");
        theDates.add("Oct");
        theDates.add("Nov");


        BarDataSet barDataSet = new BarDataSet(barEntries,"theDates");

        BarData theData = new BarData(barDataSet);

        barChart.setData(theData);

        barChart.setTouchEnabled(true);
        barChart.setTouchEnabled(true);
        barChart.setTouchEnabled(true);


    }
    */
}
