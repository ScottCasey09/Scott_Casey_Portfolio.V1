// Casey Scott
// JAVA 1 - 1704
// MainActivity
package com.fullsail.caseyscott.scottcasey_ce05v3;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Word words;
    private NumberPicker numberPicker;
    private EditText editTextView;
    private TextView avgText;
    private TextView medianText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign the OnClick listener to the add button
        findViewById(R.id.add_Button).setOnClickListener(mListener);
        //Assign the on click listener tot he View button
        findViewById(R.id.view_Button).setOnClickListener(mListener);
        //Assign the numberPicker variable to the UIElement
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        //Assign the textView Variables to the textView Ui Elements
        avgText = (TextView) findViewById(R.id.average_Value);
        medianText = (TextView) findViewById(R.id.median_Value);
        //Assign the EditText Variable to the EditText UI Elements
        editTextView = (EditText) findViewById(R.id.editText);
        //Instantiate the class objects to their default constructors
        words = new Word();
        words.words = new ArrayList<>();
        words.wordsEntryOrder = new ArrayList<>();
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(words.words.size());

    }

    final private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String entry = editTextView.getText().toString();

            //What happens when the buttons are clicked
            switch (v.getId()){

                case R.id.add_Button:
                    //Implement what happens when the add button is clicked

                    if(validateString(entry) != null){

                        //Add the word to the collections
                        words.words.add(entry.trim().toLowerCase());
                        words.wordsEntryOrder.add(entry.trim());
                        //set the edit text to blank
                        editTextView.setText("");
                        //Update the UI
                        updateUI();

                    }
                    break;

                case R.id.view_Button:
                    //Implement what happens when the view button is clicked

                        presentDialog();
                    break;

            }
        }
    };

    private  void presentDialog(){
        //Set the message ans remove text  based on the size of the collection
        String message;
        String removeWord;
        if(words.words.size() == 0){

            message = "The list has no values.";
            removeWord = "";
        }else{
            message = words.wordsEntryOrder.get(numberPicker.getValue());
            removeWord = "Remove Word";
        }

        new AlertDialog.Builder(this)
                .setTitle("Selected Word")
                .setMessage(message)
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int whichButton) {

                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(removeWord,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {

                                words.words.remove(words.wordsEntryOrder.get(numberPicker.getValue()));
                                words.wordsEntryOrder.remove(numberPicker.getValue());
                                updateUI();
                                dialog.dismiss();
                            }
                        })
                .create()
                .show();
    }

    //This almost works correctly
    private String validateString(String s){
        String message = " Default message// For testing";
        String returnedString;

        if (Objects.equals(s, "")){
            message = "You must enter a valid word.";

        }else if(words.words.contains(s.trim().toLowerCase())){

            message = "Must enter a unique word.";

        }
        else if (Objects.equals(s.trim().length(), 0)){
            message = "Spaces are not considered a valid entry.";

        } else if (!Objects.equals(s.trim(), "")){

            returnedString = s.trim();
            return returnedString;

        }

        //Notify the user of a invalid entry
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        return null;

    }
    //Method for updating the UI elements
    private void updateUI(){

        //Check for an empty collection
        if (words.words.size() != 0) {
            //Make a format for the doubles 0.00
            Locale formatLocal = new Locale("%.2f");

            //Set the text of the TextViews
            avgText.setText(String.format(String.valueOf(formatLocal), words.getAverage()));
            medianText.setText(String.format(String.valueOf(formatLocal), words.getMedian()));

            //Update the values for the picker
            numberPicker.setMaxValue(words.words.size()-1);
        }else{
            //Set the text to 0
            avgText.setText("0");
            medianText.setText("0");
        }
    }

}
