// Casey Scott
// JAV1 - 1704
// MainActivity.java
package com.fullsail.caseyscott.calculator;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //Variables for the Activity
    private long first, second, answer;
    final private int zero = 0;
   private String operator;
   private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        disableAllOps();

        //Assign all of the buttons to the mListner for onClick
        Button one = (Button) findViewById(R.id.one);
        one.setOnClickListener(mListener);
        Button two = (Button) findViewById(R.id.two);
        two.setOnClickListener(mListener);
        Button three = (Button) findViewById(R.id.three);
        three.setOnClickListener(mListener);
        Button four = (Button) findViewById(R.id.four);
        four.setOnClickListener(mListener);
        Button five = (Button) findViewById(R.id.five);
        five.setOnClickListener(mListener);
        Button six = (Button) findViewById(R.id.six);
        six.setOnClickListener(mListener);
        Button seven = (Button) findViewById(R.id.seven);
        seven.setOnClickListener(mListener);
        Button eight = (Button) findViewById(R.id.eight);
        eight.setOnClickListener(mListener);
        Button nine = (Button) findViewById(R.id.nine);
        nine.setOnClickListener(mListener);
        Button zero = (Button) findViewById(R.id.zero);
        zero.setOnClickListener(mListener);
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(mListener);
        Button sub = (Button) findViewById(R.id.minus);
        sub.setOnClickListener(mListener);
        Button multi = (Button) findViewById(R.id.multiply);
        multi.setOnClickListener(mListener);
        Button divide = (Button) findViewById(R.id.divide);
        divide.setOnClickListener(mListener);
        Button clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(mListener);
        Button equal = (Button) findViewById(R.id.equals);
        equal.setOnClickListener(mListener);

    }

    private final View.OnClickListener mListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            //Local variables for the OnClick method
            int one=1, two=2, three=3,four=4,five=5,six=6,seven=7,eight=8,nine=9;
            tv = (TextView) findViewById(R.id.textView);

            //Determin which button is pressed
                switch (v.getId()) {
                    case (R.id.one):
                        setNumbers(one);
                        enableAllOps();
                        break;
                    case (R.id.two):
                        setNumbers(two);
                        enableAllOps();
                        break;
                    case (R.id.three):
                        setNumbers(three);
                        enableAllOps();
                        break;
                    case (R.id.four):
                        setNumbers(four);
                        enableAllOps();
                        break;
                    case (R.id.five):
                        setNumbers(five);
                        enableAllOps();
                        break;
                    case (R.id.six):
                        setNumbers(six);
                        enableAllOps();
                        break;
                    case (R.id.seven):
                        setNumbers(seven);
                        enableAllOps();
                        break;
                    case (R.id.eight):
                        setNumbers(eight);
                        enableAllOps();
                        break;
                    case (R.id.nine):
                        setNumbers(nine);
                        enableAllOps();
                        break;
                    case (R.id.zero):
                        setNumbers(zero);
                        break;
                    case (R.id.add):
                        checkForTwoValues();
                        operator = "+";
                        enableAllNums();
                        break;
                    case (R.id.minus):
                        checkForTwoValues();
                        operator = "-";
                        enableAllNums();
                        break;
                    case (R.id.multiply):
                        checkForTwoValues();
                        operator = "*";
                        enableAllNums();
                        break;
                    case (R.id.divide):
                        enableAllOps();
                        operator = "/";
                        enableAllNums();
                        break;
                    case (R.id.clear):
                        first = 0;
                        second = 0;
                        tv.setText("");
                        answer = 0;
                        operator = null;
                        enableAllNums();
                        disableAllOps();
                        break;
                    case (R.id.equals):
                        if (Objects.equals(operator, null)) {
                       //Do Nothing
                        }else{
                            total();
                            operator = null;
                        }
                        break;
                }

        }

    };

    private void total(){

        if (Objects.equals(first+second, zero)) {
            //DO Nothing
        }else{

            if (Objects.equals(operator, null)) {
                //DO Nothing
            }else{
                disableAllNums();
                switch (operator) {
                    case "+":
                        answer = first + second;
                        break;
                    case "-":
                        answer = first - second;
                        break;
                    case "/":
                        answer = first / second;
                        break;
                    case "*":
                        answer = first * second;
                        break;
                }
                //Set the size of the text based on the lenght of the Answer as a string
                if (String.valueOf(answer).length() <= 6){
                    tv.setTextSize(90);
                    tv.setText(String.valueOf(answer));
                    first = answer;
                    second = 0;
                    disableAllNums();

                }
                else if (String.valueOf(answer).length() > 6) {

                    tv.setTextSize(70);
                    tv.setText(String.valueOf(answer));
                    first = answer;
                    second = 0;
                    disableAllNums();
                }
                else if (String.valueOf(answer).length() > 10) {
                    tv.setTextSize(45);
                    tv.setText(String.valueOf(answer));
                    first = answer;
                    second = 0;
                    disableAllNums();
                }
                else {
                    if (String.valueOf(answer).length() > 30) {
                        tv.setTextSize(25);
                        tv.setText(String.valueOf(answer));
                        first = answer;
                        second = 0;
                        disableAllNums();
                    }
                }

            }
        }
    }

    private  void setNumbers(int i){

        //Check to see if the first selection is zero if so give it a value based on user input
        if (first == 0){
            //First equals the input
                first = i;
                tv.setText(String.valueOf(i));
        }
        //If a number has already been assigned to the first variable then check to see if we are extending the number or making a second number
        else if(Objects.equals(operator, null)){
            //Check to see if there is an operator assigned

                //Add a number to the end of the textView
                String s = String.valueOf(first) + String.valueOf(i);
                tv.setText(s);
                first = Integer.parseInt(tv.getText().toString());

                //If there is an operator then assign the second variable a value
            }else {
                //Check to see if the second variable has a value or not
                if(Objects.equals(second, zero)){
                    //Add a number to the end of the textView
                    tv.setText(String.valueOf(i));
                    second = i;
                }else {
                    //Add a number to the end of the textView
                    String s = String.valueOf(second) + String.valueOf(i);
                    tv.setText(s);
                    second = Integer.parseInt(tv.getText().toString());
                }
            }

    }

    //Disable all number buttons
    private  void disableAllNums(){

        Button one = (Button) findViewById(R.id.one);
        one.setEnabled(false);
        Button two = (Button) findViewById(R.id.two);
        two.setEnabled(false);
        Button three = (Button) findViewById(R.id.three);
        three.setEnabled(false);
        Button four = (Button) findViewById(R.id.four);
        four.setEnabled(false);
        Button five = (Button) findViewById(R.id.five);
        five.setEnabled(false);
        Button six = (Button) findViewById(R.id.six);
        six.setEnabled(false);
        Button seven = (Button) findViewById(R.id.seven);
        seven.setEnabled(false);
        Button eight = (Button) findViewById(R.id.eight);
        eight.setEnabled(false);
        Button nine = (Button) findViewById(R.id.nine);
        nine.setEnabled(false);
        Button zero = (Button) findViewById(R.id.zero);
        zero.setEnabled(false);

    }
    //Enable all number buttons
    private  void enableAllNums(){

        Button one = (Button) findViewById(R.id.one);
        one.setEnabled(true);
        Button two = (Button) findViewById(R.id.two);
        two.setEnabled(true);
        Button three = (Button) findViewById(R.id.three);
        three.setEnabled(true);
        Button four = (Button) findViewById(R.id.four);
        four.setEnabled(true);
        Button five = (Button) findViewById(R.id.five);
        five.setEnabled(true);
        Button six = (Button) findViewById(R.id.six);
        six.setEnabled(true);
        Button seven = (Button) findViewById(R.id.seven);
        seven.setEnabled(true);
        Button eight = (Button) findViewById(R.id.eight);
        eight.setEnabled(true);
        Button nine = (Button) findViewById(R.id.nine);
        nine.setEnabled(true);
        Button zero = (Button) findViewById(R.id.zero);
        zero.setEnabled(true);

    }
    //Enable all of the operations
    private void enableAllOps(){

        Button add = (Button) findViewById(R.id.add);
        add.setEnabled(true);
        Button div = (Button) findViewById(R.id.divide);
        div.setEnabled(true);
        Button multi = (Button) findViewById(R.id.multiply);
        multi.setEnabled(true);
        Button equals = (Button) findViewById(R.id.equals);
        equals.setEnabled(true);
        Button sub = (Button) findViewById(R.id.minus);
        sub.setEnabled(true);

    }
    //Disable all of the operations
    private void disableAllOps(){

        Button add = (Button) findViewById(R.id.add);
        add.setEnabled(false);
        Button div = (Button) findViewById(R.id.divide);
        div.setEnabled(false);
        Button multi = (Button) findViewById(R.id.multiply);
        multi.setEnabled(false);
        Button equals = (Button) findViewById(R.id.equals);
        equals.setEnabled(false);
        Button sub = (Button) findViewById(R.id.minus);
        sub.setEnabled(false);

    }
    //Method that checks if the variables have values, if the do then do the operation, assign the answer to the first and move on to the second
    private void checkForTwoValues(){

        switch ((int) second){
            case 0:
                break;
            default:
                String tempOp = operator;
                total();
                operator = tempOp;
        }
    }
}
