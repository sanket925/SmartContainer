package com.example.smokashi.smartcontainer;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContainerDataDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_data_display);

        String data="";

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            data= extras.getString("data");
        }

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage(data).show();
    }
}
