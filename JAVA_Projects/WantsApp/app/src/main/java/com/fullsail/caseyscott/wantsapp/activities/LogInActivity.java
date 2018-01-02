// Casey Scott
// PAPVI - 1710
// LogInActivity.java

package com.fullsail.caseyscott.wantsapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fullsail.caseyscott.wantsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";
    public static final String TO_LOG_IN = "com.fullsail.caseyscott.wantsapp.TO_LOG_IN";
    public static final String TO_SIGN_UP = "com.fullsail.caseyscott_wantsapp_TO_SIGN_UP";
    public static final String TO_USERS_LIST = "com.fullsail.caseyscott.wantsapp.TO_USERS_LIST";
    public static final String TO_CONTACTS_LIST = "com.fullsail.caseyscott.wantsapp.TO_CONTACTS_LIST";
    public static final String TO_SEARCH_LIST = "com.fullsail.caseyscott.wantsapp.TO_SEARCH_LIST";
    private EditText username_editText, password_editText;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button sign_in_button;
    private Button sign_up_button;
    private Button skip_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);

        username_editText = (EditText) findViewById(R.id.username_editText);
        password_editText = (EditText) findViewById(R.id.password_editText);
        sign_in_button = (Button) findViewById(R.id.signin_button);
        sign_up_button = (Button) findViewById(R.id.signup_button);
        skip_button = (Button) findViewById(R.id.skip_button);
        sign_in_button.setOnClickListener(mOnclickListener);
        sign_up_button.setOnClickListener(mOnclickListener);
        skip_button.setOnClickListener(mOnclickListener);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //Onclick Listener for all buttons in the Login screen UI
    private final View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String email = username_editText.getText().toString();
            String password = password_editText.getText().toString();

            //Switch to control the buttons in the login interface
            switch (v.getId()){
                case R.id.signin_button:
                    if (email.trim().equals("")){
                        return;
                    }
                    if (password.trim().equals("") || password.trim().length() > 12 || password.trim().length() < 8){
                        return;
                    }
                   enableAllViews(false);
                    validateCred(email, password);
                    return;
                case R.id.signup_button:
                    navigateToRegisterScreen(email, password);
                    return;
                case R.id.skip_button:
                    navigateToSearch();

            }
        }
    };

    private void validateCred(String email, String password){
        //TODO: Implement the firebase check method
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(getApplicationContext(), R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            enableAllViews(true);
                        }else{
                            Toast.makeText(getApplicationContext(), R.string.success_log,
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TO_USERS_LIST);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void navigateToRegisterScreen(String email, String password){
        Intent intent = new Intent();
        intent.setAction(TO_SIGN_UP);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
    }
    private void navigateToSearch(){
        Intent intent = new Intent(LogInActivity.TO_SEARCH_LIST);
        intent.putExtra("not_auth", false);
        startActivity(intent);
    }

    private void enableAllViews(boolean bool){
        sign_in_button.setEnabled(bool);
        sign_up_button.setEnabled(bool);
        skip_button.setEnabled(bool);
        password_editText.setEnabled(bool);
        username_editText.setEnabled(bool);
    }
}
