// Casey Scott
// FINAL PROJECT - 1712
// ResetPasswordActivity.java

package com.fullsail.caseyscott.ontime.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fullsail.caseyscott.ontime.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ResetPasswordActivity extends AppCompatActivity {
    //Buttons and EditText Views
    private MaterialEditText emailEditText;
    private Button send;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_reset);
        //Set the action bar to display a back arrow option
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //get references to the views and set listeners
        emailEditText = findViewById(R.id.email);
        emailEditText.addTextChangedListener(textWatcher);
        (findViewById(R.id.cancel)).setOnClickListener(mListener);
        send = findViewById(R.id.send);
        send.setOnClickListener(mListener);
        //Disable the send button until the email address has been entered
        send.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            //Go back to parent activity
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    //Listener to enable the button when teh email address in entered
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            //Check to see if the entered string contains the characteristics of an email then enable the button to submit the request
            if(editable.toString().contains("@") && editable.toString().contains(".")){
                send.setEnabled(true);
            }else {
                //Disable the button
                send.setEnabled(false);
            }
        }
    };
    //Click listener for the buttons in the view
    private final View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.send:
                    //Get a ref to the Firebase Auth
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(emailEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                //The email has been
                                //Tell the user and go back to parent activity
                                Toast.makeText(ResetPasswordActivity.this.getApplicationContext(), R.string.email_sent, Toast.LENGTH_SHORT).show();
                                NavUtils.navigateUpFromSameTask(ResetPasswordActivity.this);
                            }else
                            {
                                //Tell the user the email did not send
                                Toast.makeText(ResetPasswordActivity.this, R.string.failed_to_send_email, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    return;
                case R.id.cancel:
                    //Go back to parent activity
                    NavUtils.navigateUpFromSameTask(ResetPasswordActivity.this);
            }
        }
    };
}
