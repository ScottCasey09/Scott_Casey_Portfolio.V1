// Casey Scott
// AID - 1705
// MainActivity.java

package com.fullsail.caseyscott.scottcasey_ce01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button changeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_layout);

        setClickListenerForButton();

    }

    private final View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (Objects.equals(changeButton.getText().toString().toLowerCase(), getString(R.string.relative_layout).toLowerCase())){

                setContentView(R.layout.relative_layout);
                setClickListenerForButton();

            } else {

                setContentView(R.layout.linear_layout);
                setClickListenerForButton();

            }
        }
    };

    private void setClickListenerForButton(){
        changeButton = (Button)findViewById(R.id.button_layout_change);
        changeButton.setOnClickListener(mListener);
    }
}
