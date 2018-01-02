// Casey Scott
// PAPVI - 1710
// SignUpActivity.java

package com.fullsail.caseyscott.wantsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fullsail.caseyscott.wantsapp.objects.Items;
import com.fullsail.caseyscott.wantsapp.objects.User;
import com.fullsail.caseyscott.wantsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity{

    private EditText firstname_ET;
    private EditText lastname_ET;
    private EditText phone_ET;
    private EditText email_ET;
    private EditText confirmEmail_ET;
    private EditText pass_ET;
    private EditText confirmPass_ET;
    private EditText username_ET;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        setTitle(getString(R.string.sign_up_for_wants));

        firstname_ET = (EditText) findViewById(R.id.firstname);
        lastname_ET = (EditText) findViewById(R.id.lastname);
        phone_ET = (EditText) findViewById(R.id.phoneNumber);
        username_ET = (EditText) findViewById(R.id.username);
        email_ET = (EditText) findViewById(R.id.emailAddress);
        confirmEmail_ET = (EditText) findViewById(R.id.confirm_email);
        pass_ET = (EditText) findViewById(R.id.password);
        confirmPass_ET = (EditText) findViewById(R.id.confirm_password);
        Button signUp_button = (Button) findViewById(R.id.signup_button_signup_activity);
        signUp_button.setOnClickListener(mOnClickListener);

        // Get the extras from the intent
        // Assign the values from the extras to the proper fields for better UX
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(intent.hasExtra("email")) {
            email_ET.setText(b.getString("email"));
        }
        if(intent.hasExtra("password")){
            pass_ET.setText(b.getString("password"));
        }

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
                // ...
            }
        };

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mAuth.getCurrentUser() != null) {
            Toast.makeText(getApplicationContext(), R.string.signed_out, Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }

    //Click Listener for the button
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.signup_button_signup_activity){
                if(confirmFirstName()&&confirmLastName()&&confirmPhone()&&confirmEmail1()&&confirmEmail2()&&confirmUName()
                       &&confirmPass1()&&confirmPass2()&&checkPassMatch()&&checkEmailMatch()){

                    signUpNewUserInFirebase(email_ET.getText().toString(), pass_ET.getText().toString());
                }
            }
        }
    };

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

    private void createUserInDatabase(User user) {
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference ref = fb.getReference("users");
        String uid = "";
        if (mAuth.getCurrentUser() != null) {
            uid = mAuth.getCurrentUser().getUid();
        }
        user.setId(uid);
        Map<String, Object> map = new HashMap<>();
        map.put(uid, user);
        ref.updateChildren(map);

        //Navigate to the MYList Screen
        Intent intent = new Intent(LogInActivity.TO_USERS_LIST);
        startActivity(intent);
    }

    private void signUpNewUserInFirebase(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.auth_failed),
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), R.string.success_log,
                                    Toast.LENGTH_SHORT).show();
                            String firstName = firstname_ET.getText().toString();
                            String lastName = lastname_ET.getText().toString();
                            String username = username_ET.getText().toString();
                            String email = email_ET.getText().toString();
                            String phoneNumber = phone_ET.getText().toString();
                            String id = "";
                            HashMap<String, User> contacts = new HashMap<>();
                            HashMap<String, HashMap<String,Items>> items = new HashMap<>();
                            String searchByEmail = email.toLowerCase();
                            String searchByUsername = username.toLowerCase();
                            String searchByFullName = firstName.toLowerCase() + " " + lastName.toLowerCase();
                            User user = new User(firstName,lastName,username,phoneNumber,email, id, contacts, items, searchByEmail, searchByUsername, searchByFullName);
                            createUserInDatabase(user);

                            Intent intent = new Intent(LogInActivity.TO_USERS_LIST);
                            startActivity(intent);
                        }
                    }
                });
    }

    private boolean checkEmailMatch(){
        String email = "";
        if(confirmEmail_ET.getText() != null){
            email = confirmEmail_ET.getText().toString();
        }
        String confirm_email = "";
        if(email_ET.getText() != null){
            confirm_email = email_ET.getText().toString();
        }
        if (!email.toLowerCase().equals(confirm_email.toLowerCase())){
            Toast.makeText(getApplicationContext(), R.string.email_no_match, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean checkPassMatch(){
        String password = "";
        if (confirmPass_ET.getText() != null){
            password = confirmPass_ET.getText().toString();
        }
        String password2 = "";
        if (pass_ET.getText() != null){
            password2 = pass_ET.getText().toString();
        }
        if (!password.equals(password2)){
            Toast.makeText(getApplicationContext(), R.string.pass_no_match,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean confirmPass2(){
        String password = "";
        if (confirmPass_ET.getText() != null){
            password = confirmPass_ET.getText().toString();
        }
        if (password.trim().equals("") || password.length()<8 || password.length() >12){
            confirmPass_ET.setError(getString(R.string.invalid_pass));
            return false;
        }
        return true;
    }

    private boolean confirmPass1(){
        String password = "";
        if (pass_ET.getText() != null){
            password = pass_ET.getText().toString();
        }
        if (password.trim().equals("") || password.length()<8 || password.length() >12){
            pass_ET.setError(getString(R.string.invalid_pass));
            return false;
        }
        return true;
    }

    private boolean confirmEmail2(){
        String email = "";
        if(confirmEmail_ET.getText() != null){
            email = confirmEmail_ET.getText().toString();
        }
        if(email.trim().equals("")){
            confirmEmail_ET.setError(getString(R.string.miss_email));
            return false;
        }
        return true;
    }

    private boolean confirmEmail1(){
        String email = "";
        if(email_ET.getText() != null){
            email = email_ET.getText().toString();
        }
        if(email.trim().equals("")){
            email_ET.setError(getString(R.string.miss_email));
            return false;
        }
        return true;
    }

    private boolean confirmUName(){
        String username = username_ET.getText().toString();
        if (username.trim().equals("")){
            username_ET.setError(getString(R.string.miss_uName));
            return false;
        }
        return  true;
    }
    private boolean confirmPhone(){
        String phoneNum = phone_ET.getText().toString();
        if ((phoneNum.trim().equals("") || phoneNum.length()<9)){
            phone_ET.setError(getString(R.string.invalid_num));
            return false;
        }
        return true;
    }
    private boolean confirmLastName() {
        String lastname = lastname_ET.getText().toString();
        if(lastname.trim().equals("")){
            lastname_ET.setError(getString(R.string.miss_lName));
            return false;
        }
        return true;
    }

    private boolean confirmFirstName() {
        String firstname = firstname_ET.getText().toString();
        if(firstname.trim().equals("")){
            firstname_ET.setError(getString(R.string.missing_fName));
            return false;
        }
        return true;
    }
}
