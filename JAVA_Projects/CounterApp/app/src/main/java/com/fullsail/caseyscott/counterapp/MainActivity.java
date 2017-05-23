package com.fullsail.caseyscott.counterapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button plus = (Button) findViewById(R.id.button);
        Button minus = (Button) findViewById(R.id.button2);

        textView = (TextView) findViewById(R.id.text);

        plus.setOnClickListener(mListener);
        minus.setOnClickListener(mListener);



    }
    View.OnClickListener mListener = new View.OnClickListener(){


        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.button:

                    textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString())+ 1));

                    break;
                case R.id.button2:

                    textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString())-1));
                    break;
            }
        }
    };
}
