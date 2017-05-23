package com.fullsail.caseyscott.spinner_arrayadapty_practice;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context mContext = this;

        //Import Spinner Component
        Spinner androidSpinner = (Spinner) findViewById(R.id.androidSpinner);

        //Create Collection
        ArrayList<AndroidVersion> androidVersoins = new ArrayList<>();

        //Populate our Collection
        androidVersoins.add(new AndroidVersion("Marshmellow", 6.0));
        androidVersoins.add(new AndroidVersion("Lollipop", 5.0));
        androidVersoins.add(new AndroidVersion("Kitkat", 4.0));

        ArrayAdapter<AndroidVersion> androidAdapter = new ArrayAdapter<>(
                mContext,                      //Context
                android.R.layout.activity_list_item,  //Item Layout
                androidVersoins                 //Collection
        );
        //Set array adapter to adapter view
        androidSpinner.setAdapter(androidAdapter);
    }


}
