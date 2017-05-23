package com.fullsail.demo.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fullsail.demo.fragments.fragments.BasicFragment;
import com.fullsail.demo.fragments.fragments.DesertListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DesertListFragment.DesertListener{

    private static final String TAG = "MainActivity.TAG";

    ArrayList<String> mDesserts;

    @Override
    public void getDesert(int position) {
        String dessert = mDesserts.get(position);

        Toast.makeText(this, dessert, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define stand-in data
        String[] desserts = getResources().getStringArray(R.array.desserts);
        mDesserts = new ArrayList<String>(Arrays.asList(desserts));

        // Define UI components
        Button fragment1Button = (Button) findViewById(R.id.fragment1Button);
        Button fragment2Button = (Button) findViewById(R.id.fragment2Button);

        // Set Button Click Events
        fragment1Button.setOnClickListener(this);
        fragment2Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int buttonId = view.getId();

        switch (buttonId){
            case R.id.fragment1Button:
                // TODO: Load Basic Fragment

                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, BasicFragment.newInstance()).commit();
                break;

            case R.id.fragment2Button:
                // TODO: Load List Fragment

                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, DesertListFragment.newInstance(mDesserts)).commit();

                break;

            default:
                Log.e(TAG, "onClick: Ya broke it.");
                break;
        }
    }
}




