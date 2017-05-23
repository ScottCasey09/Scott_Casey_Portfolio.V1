package com.fullsail.caseyscott.guessinggame;
// Casey Scott
// JAV1 - 1604
// MainActivity.Java

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Objects;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    //Variables for the game
    private Integer[] randomCombo;
    private Integer turnCount = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View view = findViewById(R.id.submitButton);
        if (view instanceof Button) {
            Button submitButton = (Button) view;
            submitButton.setOnClickListener(mListener);
        }

        //Create an array of random numbers between 0-9
        randomCombo = setNewCombo();
    }

    private final View.OnClickListener mListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            //Assign the EditText fields to variables
            EditText et0 = (EditText) findViewById(R.id.number0);
            EditText et1 = (EditText) findViewById(R.id.number1);
            EditText et2 = (EditText) findViewById(R.id.number2);
            EditText et3 = (EditText) findViewById(R.id.number3);
            //Create an array of EditText Objects
            EditText[] eText = new EditText[]{et0, et1, et2, et3};
            //Check for missing guesses
            boolean b = checkForNULLValues(eText);
            if (b) {
                //Parse the strings into integers
            Integer enteredText0 = Integer.parseInt(et0.getText().toString());
            Integer enteredText1 = Integer.parseInt(et1.getText().toString());
            Integer enteredText2 = Integer.parseInt(et2.getText().toString());
            Integer enteredText3 = Integer.parseInt(et3.getText().toString());
                //Array fot the guesses as integers
                Integer[] intArray = new Integer[]{enteredText0, enteredText1, enteredText2, enteredText3};

                for (EditText et : eText) {

                    checkForCorrectGuess(et);
                }
                checkForWin(intArray);
            }

        }

    };

    //Method for creating a random number between 0-9
    private Integer getRandomNumber(){
        return new Random().nextInt(10);
    }

    //Method for Checking for correct guesses
    private void checkForCorrectGuess(EditText et){

        //Assign the corresponding color to the edit text
        switch (et.getId()){

            case R.id.number0:
                setNumColor(et, 0);
                break;

            case R.id.number1:
                setNumColor(et, 1);
                break;

            case R.id.number2:
                setNumColor(et, 2);
                break;

            case R.id.number3:
                setNumColor(et, 3);
                break;

        }
    }

    //Method for setting the color of the text
    private  void setNumColor(EditText et, Integer i){
        Integer x = Integer.parseInt(et.getText().toString());
        if(x > randomCombo[i]){
            et.setTextColor(Color.RED);

        }
        else  if (x < randomCombo[i]){
            et.setTextColor(Color.BLUE);

        }
        else {
            et.setTextColor(Color.GREEN);

        }
    }
    //Method for setting a new set of numbers for the game
    private Integer[] setNewCombo(){
        return randomCombo = new Integer[]{getRandomNumber(), getRandomNumber(), getRandomNumber(), getRandomNumber()};
    }
    //Method for checking for a win
    private void checkForWin(Integer[] intArray){
        //Variables for the method
        int matches = 0;
        turnCount = turnCount - 1;
        //Loop through the number to check for matches with the guesses
        for (int x=0;x<randomCombo.length;x++){
            //Check for a match
            if (Objects.equals(intArray[x], randomCombo[x])){
                //Add a match
                matches += 1;
            }
        }
        //Check for the win
        if (matches == randomCombo.length){
            presentDialog("WINNER");
            randomCombo = setNewCombo();
        }
        else if (turnCount > 0){
            //Display a toast to infor the user about how many turn they have left
            Toast.makeText(MainActivity.this, turnCount.toString() + " Turns Remaining",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            //If the turns run out info the user and ask if they want to play again
            presentDialog("LOOSER");

        }

    }
    //Alert message for informing the user that they have won or lost
    private  void presentDialog(String status){
        new AlertDialog.Builder(this)
                .setTitle(status)
                .setMessage(
                        "Do you want to play again?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int whichButton) {
                                resetGame();
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                                System.exit(0);
                            }
                        })
                .create()
                .show();
    }
    //Function for determining if the entries have text
    private boolean checkForNULLValues(EditText[] etA){
        //Check all of the edit Text fields
        for (EditText et : etA) {
            String s = et.getText().toString();
            switch (s) {
                case "":
                    return false;
            }
        }
            return  true;

    }
    //Method for resetting the game
    private void resetGame(){
        //Reset all fields and variables
        turnCount = 4;
        EditText et0 = (EditText)findViewById(R.id.number0);
        et0.setText(null);
        et0.setTextColor(Color.BLACK);
        EditText et1 = (EditText)findViewById(R.id.number1);
        et1.setText(null);
        et1.setTextColor(Color.BLACK);
        EditText et2 = (EditText)findViewById(R.id.number2);
        et2.setText(null);
        et2.setTextColor(Color.BLACK);
        EditText et3 = (EditText)findViewById(R.id.number3);
        et3.setText(null);
        et3.setTextColor(Color.BLACK);


    }
}
