// Casey Scott
// JAV1 - 1704
// MainActivity.java
package com.fullsail.caseyscott.scottcasey_ce11;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

   final private class ViewHolder{

        Button submitButton;
        EditText editText;
        ListView listView;
        ArrayList<String> arrayList;
        ArrayAdapter<String> arrayAdapter;
    }

    private static final String LOG_TAG = "CONFIG_EXERCISE";
    private static final  String SAVE_KEY_NAME = "SAVE_KEY_NAME";


    private final ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActivity();
    }

   private final View.OnClickListener mListener = new View.OnClickListener() {
       @Override
       public void onClick(View v) {

           String enteredText = mViewHolder.editText.getText().toString();

           if(enteredText.length() == 0){
               Toast.makeText(MainActivity.this, "Invalid - Blank Entry.", Toast.LENGTH_SHORT).show();
               mViewHolder.editText.setText(null);
           }
           else if (enteredText.trim().length() == 0){
               Toast.makeText(MainActivity.this, "Invalid - All Spaces.", Toast.LENGTH_SHORT).show();
               mViewHolder.editText.setText(null);
           }
           else{
               mViewHolder.arrayList.add(enteredText.trim());
               mViewHolder.listView.setAdapter(mViewHolder.arrayAdapter);
               mViewHolder.editText.setText(null);
           }
       }
   };

    private void setupActivity(){

        // Cache references to the current views
        try {
            mViewHolder.editText = (EditText) findViewById(R.id.editText);
            mViewHolder.listView = (ListView) findViewById(R.id.listView);
            TextView textView = (TextView) findViewById(R.id.emptyTextView);
            mViewHolder.listView.setEmptyView(textView);
            mViewHolder.submitButton = (Button) findViewById(R.id.submitButton);
            mViewHolder.submitButton.setOnClickListener(mListener);
            mViewHolder.arrayList = new ArrayList<>();
            mViewHolder.arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mViewHolder.arrayList);

        }catch (Exception e) {
            //Catch anything which could have gone wrong.
            Log.e(LOG_TAG, "setupActivity \n" + e);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, " --> onSaveInstanceState()");

        super.onSaveInstanceState(outState);
        try{
            ArrayList<String> arrayList = new ArrayList<>();
            for(int x=0; x < mViewHolder.listView.getAdapter().getCount();x++) {
                String enteredString = mViewHolder.listView.getAdapter().getItem(x).toString();
                arrayList.add(enteredString);
            }
            outState.putStringArrayList(SAVE_KEY_NAME, arrayList);
        }catch (Exception e){

            Log.i(LOG_TAG, "onSaveInstanceState() \n" + e);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(LOG_TAG, " --> onRestoreInstanceState()");

        super.onRestoreInstanceState(savedInstanceState);
        try{

            ArrayList<String> arrayList = savedInstanceState.getStringArrayList(SAVE_KEY_NAME);
            if(arrayList!=null){
                mViewHolder.arrayAdapter.clear();
                mViewHolder.arrayAdapter.addAll(arrayList);
                mViewHolder.listView.setAdapter(mViewHolder.arrayAdapter);
            }

        }
        //Catch anything which could have gone wrong
        catch (Exception e){

            Log.e(LOG_TAG, "onRestoreInstanceState() \n" + e);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        //Always invoke the parent's versions
        super.onConfigurationChanged(newConfig);

        // Try to perform operations
        try {
            // Temporarily store all the data from the previous / old views
            String editTextString = mViewHolder.editText.getText().toString();

            // Setup the activity to handle the new layout and views
            setupActivity();

            // Write the data to the new / current views
            mViewHolder.editText.setText(editTextString);

        }catch (Exception e){
            Log.e(LOG_TAG, "onConfigurationChanged --- " + e);
        }
    }
}
