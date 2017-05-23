package com.fullsail.cmaxwell.screenrotations;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // A means to identify the logs from this class
    private static final String LOG_TAG = "FS_ROT_DEMO_MA";

    // The unique identifiers for our data to be saved
    // TODO: Declare unique keys
    private static final  String SAVE_KEY_NAME = "SAVE_KEY_NAME";
    private static final  String SAVE_KEY_UPDATES = "SAVE_KEY_UPDATES";


    // Cache references to our app's "current" views for speedy access later
    private class ViewHolder {
        public TextView tvFullName;
        public EditText etFirstName;
        public EditText etLastName;
        public Button btnSubmit;
        public ArrayList<View> vImages = new ArrayList<>();
    }
    private ViewHolder mViewHolder = new ViewHolder();

    // Data not visible on the UI but still updated by user actions
    private int mUpdateAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "--> onCreate() " + savedInstanceState);
        super.onCreate(savedInstanceState);

        //****************************************************
        // NOTE: The system is smart enough to use the
        // appropriate layout for the current orientation
        // as long as "layout-land" and "layout-port" folders
        // are present in the project.
        //****************************************************
        //----> setContentView(R.layout.activity_main); <-----
        //****************************************************
        // Please see the documentation for means to handle
        // different screen orientations and sizes.
        // https://developer.android.com/training/basics/supporting-devices/screens.html#create-layouts
        //***************************************************

        // TODO: Setup activity
        setupActivity();
    }

    // TODO: Implement the setup activity method

    private void setupActivity(){

        // Step 1.) load the correct layout and access views based on current orientation
        // Try and get the current configuration
        try {
            Configuration config = getResources().getConfiguration();

            // Determine which orientation is currently in use
            if(config.orientation == Configuration.ORIENTATION_PORTRAIT) {

                // Set the content view to use portrait
                setContentView(R.layout.activity_main_portrait);

                // Cache a reference to the single image used in this layout
                mViewHolder.vImages.clear();
                mViewHolder.vImages.add(findViewById(R.id.iv_android_icon));
            }
            else if(config.orientation == Configuration.ORIENTATION_LANDSCAPE) {

                // Set the content view to use landscape
                setContentView(R.layout.activity_main_landscape);

                // Cache a reference to the multiple images used in the layout
                mViewHolder.vImages.clear();
                mViewHolder.vImages.add(findViewById(R.id.iv_android_icon_left));
                mViewHolder.vImages.add(findViewById(R.id.iv_android_icon_center));
                mViewHolder.vImages.add(findViewById(R.id.iv_android_icon_right));
            }
            //Catch anything which could have gone wrong
        }catch (Exception e){
            Log.e(LOG_TAG, "setupActivity \n" + e);
        }
        // Anything not dependent upon the orientation can be below, outside
        //of those if-statements which handle the current configuration

        // The remainder of these views ARE DIFFERENT objects between the two layouts, but
        // since each view matches one-for-one between the two layouts the same IDs were used.

        // Step 2.) Find and store the button view and attach the click listener
        try{
            // Two different IDs "could" have been used but it would involve more code to check the
            // current orientation.
            mViewHolder.btnSubmit = (Button) findViewById(R.id.button_save);
            mViewHolder.btnSubmit.setOnClickListener(this);

        }catch (Exception e){

            Log.e(LOG_TAG, "setupActivity \n" + e);

        }


        //Step 3.) Cache references to the app's "current" views for optimized (speedy) access later
        try {
            // Again, unlike the images, there IS a 1-for-1 view match between layouts
            mViewHolder.etFirstName = (EditText) findViewById(R.id.et_first_name);
            mViewHolder.etLastName = (EditText) findViewById(R.id.et_last_name);
            mViewHolder.tvFullName = (TextView) findViewById(R.id.tv_full_name);

        }catch (Exception e) {
            //Catch anything which could have gone wrong.
            Log.e(LOG_TAG, "setupActivity \n" + e);
        }
        // Step 4.) Update the icon's visibility


        displayIcons();
    }
    
    // TODO: Implement the save instance method

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, " --> onSaveInstanceState()");

        super.onSaveInstanceState(outState);
        try{
            String fullName = mViewHolder.tvFullName.getText().toString();
            outState.putString(SAVE_KEY_NAME, fullName);
            outState.putInt(SAVE_KEY_UPDATES, mUpdateAttempts);

        }catch (Exception e){

            Log.i(LOG_TAG, "onSaveInstanceState() \n" + e);
        }

    }


    // TODO: Implement the restore instance method

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(LOG_TAG, " --> onRestoreInstanceState()");

        super.onRestoreInstanceState(savedInstanceState);
        try{

            String fullName = savedInstanceState.getString(SAVE_KEY_NAME, null);
            if(fullName!=null){
                mViewHolder.tvFullName.setText(fullName);
            }

            mUpdateAttempts = savedInstanceState.getInt(SAVE_KEY_UPDATES, 0);
            if(mUpdateAttempts > 0){
                mViewHolder.btnSubmit.setText(getString(R.string.update));
            }


        }
        //Catch anything which could have gone wrong
        catch (Exception e){

            Log.i(LOG_TAG, "onRestoreInstanceState() \n" + e);
        }


        displayIcons();
    }


    // TODO: Implement the configuration change method


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(LOG_TAG, " --> onConfigurationChanged()");

        //Always invoke the parent's versions
        super.onConfigurationChanged(newConfig);

        // Try to perform operations
        try {
            // Temporarily store all the data from the previous / old views
            String firstName = mViewHolder.etFirstName.getText().toString();
            String lastName = mViewHolder.etLastName.getText().toString();
            String fullName = mViewHolder.tvFullName.getText().toString();

            // Setup the activity to handle the new layout and views
            setupActivity();

            // Write the data to the new / current views
            mViewHolder.etFirstName.setText(firstName);
            mViewHolder.etLastName.setText(lastName);
            mViewHolder.tvFullName.setText(fullName);

            // If the "mUpdateAttempts" variable is grater then zero, change the
            if (mUpdateAttempts > 0) {
                //  button's label to "update".
                mViewHolder.btnSubmit.setText(getString(R.string.update));
            }
        }catch (Exception e){
            Log.e(LOG_TAG, "onConfigurationChanged() \n" + e);
        }

        //This is ran when the device changes orientation
    }

    public void onClick(View var1) {
        if(var1.getId() == R.id.button_save) {
            try {
                String firstName = mViewHolder.etFirstName.getText().toString().trim();
                String lastName = mViewHolder.etLastName.getText().toString().trim();
                if(!firstName.isEmpty() && !lastName.isEmpty()) {
                    String fullName = lastName;
                    fullName += ", ";
                    fullName += firstName;

                    mViewHolder.etFirstName.setText("");
                    mViewHolder.etLastName.setText("");
                    mViewHolder.tvFullName.setText(fullName);
                    mViewHolder.btnSubmit.setText(R.string.update);

                    ++mUpdateAttempts;
                    displayIcons();
                }
            }
            catch (Exception e) {
                Log.e(LOG_TAG, "onClick() \n" + e);
            }
        }
        else {
            Log.i(LOG_TAG, "onClick() Unhandled ID " + var1.getId() +
                    ", expecting ID " + R.id.button_save);
        }
    }

    private void displayIcons() {
        for(int i=0; i<mViewHolder.vImages.size(); ++i){
            View v = mViewHolder.vImages.get(i);
            if(v!=null) {
                if (i < mUpdateAttempts)
                    v.setVisibility(View.VISIBLE);
                else
                    v.setVisibility(View.INVISIBLE);
            }
        }
    }
}
