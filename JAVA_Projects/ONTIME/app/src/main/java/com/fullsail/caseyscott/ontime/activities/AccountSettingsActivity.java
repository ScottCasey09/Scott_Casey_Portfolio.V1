// Casey Scott
// FINAL PROJECT - 1712
// AccountSettingsActivity.java

package com.fullsail.caseyscott.ontime.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.helpers.FirebaseDatabaseHelper;
import com.fullsail.caseyscott.ontime.helpers.NetworkUtils;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AccountSettingsActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText firstname;
    private EditText lastname;
    private Spinner hoursSettingSpinner;
    private Employee employee;
    private FirebaseAuth mAuth;
    private NetworkUtils utils;
    private boolean inDialog;
    private Integer originHour;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        setTitle(getString(R.string.settings));
        //Make the action bar display the back arrow for going back to the last activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Check the save instance state
        if(savedInstanceState != null){
            employee = (Employee) savedInstanceState.getSerializable("employee");
            inDialog = savedInstanceState.getBoolean("inDialog");
            originHour = savedInstanceState.getInt("originHour");
            if(inDialog){
                askUserIfTheyReallyWantToChange();
            }

        } //Get the employee object from the intent
        else if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("employee")) {
            employee = (Employee) getIntent().getSerializableExtra("employee");
            inDialog = false;
            originHour = employee.getDefaultHour();
        }else{
            //This is just to avoid a null exception, has no other use
            employee = new Employee();
        }
        //Get a reference of the Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Get the references to the views
        firstname = findViewById(R.id.settings_firstname);
        lastname = findViewById(R.id.settings_lastname);
        email = findViewById(R.id.settings_email);
        password = findViewById(R.id.settings_password);
        hoursSettingSpinner = findViewById(R.id.settings_default_hours);
        //Integer array for the spinner
        Integer[] nums = new Integer[] {1,2,3,4,5,6,7,8,9,10};
        //Set the spinners adapter
        hoursSettingSpinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, nums));
        hoursSettingSpinner.setSelection(employee.getDefaultHour()-1);
        hoursSettingSpinner.setOnItemSelectedListener(itemSelectedListener);
        Button save = findViewById(R.id.saveButton);
        //Set the click listener
        save.setOnClickListener(onClickListener);
        //Set the text of the editText views to represent the employee
        firstname.setText(employee.getFirstname());
        lastname.setText(employee.getLastname());
        email.setText(employee.getEmail());
        password.setText(employee.getPassword());
        //Get a reference to the network status
        utils = new NetworkUtils(AccountSettingsActivity.this);
    }
    private final AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                employee.setDefaultHour((Integer) adapterView.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            //Do nothing...
        }
    };
    //Click listener for the save button
    private final View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.saveButton){
                //Check the internet status
                if(utils.checkInternetConnection()){
                    askUserIfTheyReallyWantToChange();
                }
            }
        }
    };

    private void goBackFromSettings(){
        Intent intent = new Intent();
        if(getResources().getBoolean(R.bool.is_tablet)){
            intent.setAction(LandingActivity.ACTION_FORECAST);
        }else{
            intent.setAction(LandingActivity.ACTION_MAIN_PAGE_VIEWER);

        }
        intent.putExtra("user", employee);
        startActivity(intent);

    }

    private void askUserIfTheyReallyWantToChange(){
        //Set the boolean to true for in dialog
        inDialog = true;
        //Create an instance of a dialog
        final Dialog dialog = new Dialog(this);
        //Assign the custion layout to the dialog
        dialog.setContentView(R.layout.settings_change_dialog);
        //Set up the views to the layout
        TextView email_n, email_o, pass_n, pass_o, fName_n, fName_o, lName_n, lName_o,hours_n,hours_o;
        email_n = dialog.findViewById(R.id.email_new);
        email_o = dialog.findViewById(R.id.email_old);
        pass_n = dialog.findViewById(R.id.password_new);
        pass_o = dialog.findViewById(R.id.password_old);
        fName_n = dialog.findViewById(R.id.firstname_new);
        fName_o = dialog.findViewById(R.id.firstname_old);
        lName_n = dialog.findViewById(R.id.lastname_new);
        lName_o = dialog.findViewById(R.id.lastname_old);
        hours_n = dialog.findViewById(R.id.hours_new);
        hours_o = dialog.findViewById(R.id.hours_old);
        //Assign the values to the views
        email_o.setText(employee.getEmail());
        email_n.setText(email.getText().toString());
        pass_o.setText(employee.getPassword());
        pass_n.setText(password.getText().toString());
        fName_o.setText(employee.getFirstname());
        fName_n.setText(firstname.getText().toString());
        lName_o.setText(employee.getLastname());
        lName_n.setText(lastname.getText().toString());
        hours_n.setText(String.valueOf(hoursSettingSpinner.getSelectedItem()));
        Integer new_hour = (Integer) hoursSettingSpinner.getSelectedItem();
        hours_o.setText(String.valueOf(originHour));
        //Check for differences
        if(!email.getText().toString().trim().equals(employee.getEmail())){
            email_n.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if(!password.getText().toString().trim().equals(employee.getPassword())){
            pass_n.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if(!firstname.getText().toString().trim().equals(employee.getFirstname())){
            fName_n.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if(!lastname.getText().toString().trim().equals(employee.getLastname())){
            lName_n.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        (dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if(!Objects.equals(new_hour, originHour)){
            hours_n.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        (dialog.findViewById(R.id.commit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set up a progress dialog to simulate saving processes and inform the user
                ProgressDialog progressDialog = new ProgressDialog(AccountSettingsActivity.this);
                progressDialog.setMessage(getString(R.string.saving));
                //Get a reference to the network status
                utils = new NetworkUtils(AccountSettingsActivity.this);
                //Check for internet connection
                if(utils.checkInternetConnection()){
                    //Check for differences
                    final String emailStr, passwordStr, fnameStr, lnameStr;
                    emailStr = email.getText().toString();
                    passwordStr = password.getText().toString();
                    fnameStr = firstname.getText().toString();
                    lnameStr = lastname.getText().toString();

                    //Check to see if the first name has been changed
                    if(!fnameStr.equals(employee.getFirstname())){
                        //Assign the new first name
                        employee.setFirstname(fnameStr);
                    }
                    //Check to see if that last name has been changed
                    if(!lnameStr.equals(employee.getLastname())){
                        //assign the new last name
                        employee.setLastname(lnameStr);
                    }
                    //Check to see if the email and the password have been changed
                    if( passwordStr.length()>7 || emailStr.contains("@") && emailStr.contains(".")) {
                        if (!emailStr.equals(employee.getEmail()) && !passwordStr.equals(employee.getPassword())){
                            if ((mAuth != null && mAuth.getCurrentUser() != null)) {
                                mAuth.getCurrentUser().updateEmail(emailStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            employee.setEmail(emailStr);
                                            if ((mAuth != null && mAuth.getCurrentUser() != null)) {
                                                mAuth.getCurrentUser().updateEmail(emailStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            employee.setPassword(passwordStr);
                                                            if (!lnameStr.equals(employee.getLastname()) || !fnameStr.equals(employee.getFirstname())
                                                                    || !passwordStr.equals(employee.getPassword()) || emailStr.equals(employee.getEmail())) {
                                                                FirebaseDatabaseHelper helper = new FirebaseDatabaseHelper();
                                                                //save the data in the DB
                                                                helper.saveAdminUserToDatabase(employee);
                                                                //Go to the activity
                                                                goBackFromSettings();
                                                            }
                                                        } else {
                                                            Toast.makeText(AccountSettingsActivity.this, R.string.password_not_updated, Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                });
                                            }
                                        } else {
                                            Toast.makeText(AccountSettingsActivity.this, R.string.email_not_updated, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                            }
                        }
                        //Check to see if the email is valid
                        else if (emailStr.contains("@") && emailStr.contains(".")) {
                            if ((mAuth != null && mAuth.getCurrentUser() != null)) {
                                mAuth.getCurrentUser().updateEmail(emailStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            employee.setEmail(emailStr);
                                            if (!lnameStr.equals(employee.getLastname()) || !fnameStr.equals(employee.getFirstname())
                                                    || !passwordStr.equals(employee.getPassword()) || emailStr.equals(employee.getEmail())) {
                                                FirebaseDatabaseHelper helper = new FirebaseDatabaseHelper();
                                                //save the data in the DB
                                                helper.saveAdminUserToDatabase(employee);
                                                //Go to the activity
                                                goBackFromSettings();
                                            }
                                        } else {
                                            Toast.makeText(AccountSettingsActivity.this, R.string.email_not_updated, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                            }
                        } else if (!passwordStr.equals(employee.getPassword()) && passwordStr.length() > 7) {
                            if ((mAuth != null && mAuth.getCurrentUser() != null)) {
                                mAuth.getCurrentUser().updateEmail(emailStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            employee.setPassword(passwordStr);
                                            if (!lnameStr.equals(employee.getLastname()) || !fnameStr.equals(employee.getFirstname())
                                                    || !passwordStr.equals(employee.getPassword()) || emailStr.equals(employee.getEmail())) {
                                                FirebaseDatabaseHelper helper = new FirebaseDatabaseHelper();
                                                //save the data in the DB
                                                helper.saveAdminUserToDatabase(employee);
                                                //Go to the activity
                                                goBackFromSettings();
                                            }
                                        } else {
                                            Toast.makeText(AccountSettingsActivity.this, R.string.password_not_updated, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                            }
                        }



                    }else{
                        //Set the errors on the proper fields
                        if (!emailStr.equals(employee.getEmail())) {
                            email.setError(getString(R.string.invalid));
                        }
                        if(passwordStr.length()>7 ) {
                            password.setError(getString(R.string.to_short));
                        }
                        dialog.dismiss();
                    }
                    //Check to see if only the names were changed
                    //if only the names were changed the system will not have to go through the process
                    //of changing the auth credentials.
                    //Checking this last because the code will get to this line before the completion listeners execute
                    if(!lnameStr.equals(employee.getLastname()) ||!fnameStr.equals(employee.getFirstname())
                            && (emailStr.equals(employee.getEmail()) && passwordStr.equals(employee.getPassword()))){
                        FirebaseDatabaseHelper helper = new FirebaseDatabaseHelper();
                        helper.saveAdminUserToDatabase(employee);
                        goBackFromSettings();
                    }

                }else{
                    //Tell the user why you cant save
                    Toast.makeText(AccountSettingsActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });
        dialog.show();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("employee", employee);
        outState.putBoolean("inDialog", inDialog);
        outState.putInt("originHour", originHour);
    }

    //Options menu actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Find the selected options button
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(LandingActivity.ACTION_MAIN_PAGE_VIEWER);
                intent.putExtra("user", employee);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
