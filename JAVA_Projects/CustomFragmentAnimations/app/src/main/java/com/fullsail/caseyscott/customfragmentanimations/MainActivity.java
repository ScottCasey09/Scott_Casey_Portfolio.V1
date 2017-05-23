package com.fullsail.caseyscott.customfragmentanimations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fragments.DetailFragment;
import fragments.MainFragment;

public class MainActivity extends AppCompatActivity implements DetailFragment.DetailsListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().add(R.id.fragment_content, MainFragment.newInstance(), MainFragment.TAG).commit();
    }

    @Override
    public void openDetails() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left,0,0, R.animator.slide_out_right)
                .add(R.id.fragment_content, DetailFragment.newInstance(), DetailFragment.TAG)
                .addToBackStack("details")
                .commit();
    }

    @Override
    public void closeDetails() {

        getFragmentManager().popBackStack();
    }
}
