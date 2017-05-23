// Casey Scott
// Java 1 - 1704
// MainActivity.Java
package com.fullsail.caseyscott.scottcasey_ce08;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //Properties
    private EditText minText;
    private EditText secText;
    private Integer minInput;
    private Integer secInput;
    private TaskAsync taskAsync;
    private Button startButton;
    private Button endButton;

    //First Called to build activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign the OnClick Listener to the buttons
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(mListener);
        endButton = (Button) findViewById(R.id.stopButton);
        endButton.setOnClickListener(mListener);

        minText = (EditText) findViewById(R.id.minutesEditText);
        secText = (EditText) findViewById(R.id.secondsEditText);

        //Disable the endButton button
        endButton.setEnabled(false);

    }

    //Create an instance on the onClickListener
     private final View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //Find the button by id reference
            switch (v.getId()){

                case R.id.startButton:

                    //Check for invalid entry or null
                    if(checkIsValid()){
                        //Parse the text into an int
                        minInput = Integer.parseInt(minText.getText().toString());
                        secInput = Integer.parseInt(secText.getText().toString());

                        //Convert the secs to mins
                        int temp = secInput;
                        while(temp > 0){
                            if(temp >= 60){
                                temp -= 60;
                                minInput++;
                            }else{
                                secInput = temp;
                                temp = secInput - temp;
                            }
                        }
                        //Disable the start button disable the end button
                        startButton.setEnabled(false);
                        endButton.setEnabled(true);
                        minText.setEnabled(false);
                        secText.setEnabled(false);

                        //Reset the EditText field to mimic actual time
                        minText.setText(String.valueOf(minInput));
                        secText.setText(String.valueOf(secInput));

                        //Start the task
                        startTime();
                    }
                    break;

                case R.id.stopButton:

                    stopTime();
                    break;
            }
        }
    };
    //Method for starting the async task
    private void startTime(){

        taskAsync = new TaskAsync();
        taskAsync.execute(minInput,secInput);

    }
    //Method for stopping the task    //////////Project crashes on stop FIX THIS
    private void stopTime(){

        endButton.setEnabled(false);
        if (!taskAsync.isCancelled()) {

            taskAsync.cancel(true);

        }
    }

    //Method that checks if the input fields have proper input
    private boolean checkIsValid() {

        if(Objects.equals(minText.getText().toString().trim(), "")){
            if(Objects.equals(secText.getText().toString().trim(), "")){
                Toast.makeText(MainActivity.this, R.string.need_both_fields, Toast.LENGTH_SHORT).show();
                return false;
            }
            Toast.makeText(MainActivity.this, R.string.need_minutes_field, Toast.LENGTH_SHORT).show();
                return false;
        }
        if(Objects.equals(secText.getText().toString().trim(), "")){
            if(Objects.equals(minText.getText().toString().trim(), "")){
                Toast.makeText(MainActivity.this, R.string.need_both_fields, Toast.LENGTH_SHORT).show();
                return false;
            }
            Toast.makeText(MainActivity.this, R.string.need_seconds_field, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(Integer.parseInt((minText.getText().toString()) + Integer.parseInt(secText.getText().toString())) == 0 ){

            Toast.makeText(MainActivity.this, R.string.need_higher_value, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
    //Create a class of the AsyncTask class
    private class TaskAsync extends AsyncTask<Integer, Integer, String> {

        //This method executes just before the doInBackground
        @Override
        protected void onPreExecute() {

        }
            protected JSONObject g =
        //This method returns the first parameter in the signature of the class
        //The only method in an async task that gets ran on the background thread
        //Cannot touch the UI from this method
        @Override
        protected String  doInBackground(Integer... params) {

            //Make toast of time started
            publishProgress();

                Integer totalSeconds = (params[0] * 60) + params[1];
                Integer mins = params[0];
                Long minMillis = TimeUnit.SECONDS.toMillis(params[0] * 60);
                Integer secs = params[1];
                Long secMillis = TimeUnit.SECONDS.toMillis(params[1]);
                Long totalMillis = minMillis + secMillis;

                //Get the current time of the system for the start time
                Long getStartMillis = System.currentTimeMillis();

                for (int x = 0; x < totalMillis; x++) {
                    if (!taskAsync.isCancelled()) {
                    try {
                        Thread.sleep(1000);//Mimics one second of time
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (secs == 0 && mins == 0) {
                        return String.valueOf(totalSeconds);

                    } else if (secs == 0 && mins > 0) {
                        mins--;
                        secs = 59;
                    } else {

                        secs--;
                    }

                    publishProgress(mins, secs);//Calls the onProgressUpdate method

                }else{
                      break;
                    }
            }
                //Get the  ending mills from the task
                Long getEndMills = System.currentTimeMillis();
                //Get the difference between the start time and the end time
                Long difference = getEndMills - getStartMillis;
                //Convert to seconds
                Long secondsPassed = TimeUnit.MILLISECONDS.toSeconds(difference);
                return String.valueOf(secondsPassed);
        }

        //This method uses the second/Middle parameter
        //use this method to update the UI
        @Override
        protected void onProgressUpdate(Integer... values) {

            if(values.length > 1) {
                minText.setText(String.format(Locale.US,"%02d", values[0]));
                secText.setText(String.format(Locale.US,"%02d", values[1]));
            }else{
                Toast.makeText(MainActivity.this, R.string.time_started,Toast.LENGTH_SHORT).show();
            }
                  }
        //Method that is called after the  background method is finished
        @Override
        protected void onPostExecute(String s) {
           //Set the text back to empty
            minText.setText("");
            secText.setText("");

            //Show the alert dialog of time elapsed
            showAlertDialog(s);
        }

        @Override
        protected void onCancelled(String s) {

            //Set the text back to empty
            minText.setText("");
            secText.setText("");

            //Show the alert dialog of time elapsed
            showAlertDialog(s);
        }
    }

    //Method that shows the Alert Dialog
    private void showAlertDialog(String totalTime){


        int[] time = convertTime(Integer.parseInt(totalTime));
        String message = "Time Elapsed:\nMinute(s): " + time[0] + "\nSecond(s): " + time[1];

        //Alert Dialog for displaying the relative information of the selected item
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
        adb.setTitle("Results");
        adb.setMessage(message);


        adb .setPositiveButton("Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog,
                            int whichButton) {
                        startButton.setEnabled(true);
                        endButton.setEnabled(false);
                        minText.setEnabled(true);
                        secText.setEnabled(true);
                        dialog.dismiss();
                    }
                });
        adb.setCancelable(false);
        adb.create();
        adb.show();
    }


    private int[] convertTime(int i){

        int min = i/60;
        int secs = i - (min*60);
        return new int[]{min,secs};
    }
}
