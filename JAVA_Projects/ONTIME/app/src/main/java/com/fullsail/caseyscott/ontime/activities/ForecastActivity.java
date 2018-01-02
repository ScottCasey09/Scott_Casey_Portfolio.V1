// Casey Scott
// FINAL PROJECT - 1712
// ForecastActivity.java

package com.fullsail.caseyscott.ontime.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.adapters.EmployeeListAdapter;
import com.fullsail.caseyscott.ontime.fragments.CalendarFragment;
import com.fullsail.caseyscott.ontime.fragments.EmployeesManagementFragment;
import com.fullsail.caseyscott.ontime.fragments.JobsManagementFragment;
import com.fullsail.caseyscott.ontime.helpers.FirebaseDatabaseHelper;
import com.fullsail.caseyscott.ontime.objects.Day;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.fullsail.caseyscott.ontime.objects.Job;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class ForecastActivity extends AppCompatActivity implements JobsManagementFragment.CalendarListener, CalendarFragment.UpdateFromCalendarListener {

    private static final String TAG = "ForecastActivity - ";
    private static final String ACTION_HOURS_TABLET = "com.android.fullsail.caseyscott.ontime.ACTION_HOURS_TABLET";
    private static final String ACTION_SETTINGS = "com.android.caseyscott.ontime.SETTINGS_TABLET";

    //Variables
    private String empfName = "";
    private String emplName = "";
    private String empEmailStr = "";
    private String empDob = "";
    private String empPass = "";
    private MaterialEditText fName;
    private MaterialEditText lName;
    private MaterialEditText dob;
    private MaterialEditText email;
    private MaterialEditText password;
    private MaterialEditText confirmPassword;
    private FirebaseAuth mAuth;
    private FirebaseDatabaseHelper databaseHelper;
    private Employee employee;
    private FirebaseAuth secondAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablet_forcast);
        mAuth = FirebaseAuth.getInstance();
        databaseHelper = new FirebaseDatabaseHelper();
        employee = (Employee) getIntent().getSerializableExtra("user");
        getSupportFragmentManager()
                .beginTransaction().add(R.id.employees_content, EmployeesManagementFragment.newInstance(employee))
                .commit();
        getSupportFragmentManager()
                .beginTransaction().add(R.id.calendar_content, CalendarFragment.newInstance(employee))
                .commit();
        getSupportFragmentManager()
                .beginTransaction().add(R.id.jobs_content, JobsManagementFragment.newInstance(employee))
                .commit();
    }

    @Override
    public void updateCalendarForTablet(Employee employee) {
        getSupportFragmentManager()
                .beginTransaction().add(R.id.calendar_content, CalendarFragment.newInstance(employee))
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.signout_action:
                presentDialogForSignOut();
                return true;
            case R.id.check_hours:
                if (employee.getEmployees().size() > 0 && employee.getEmployees() != null) {
                    Intent intent1 = new Intent(ACTION_HOURS_TABLET);
                    intent1.putExtra("employee", employee);
                    startActivity(intent1);
                }else{
                    Toast.makeText(this, R.string.add_emp_to_use, Toast.LENGTH_SHORT).show();
                }
                return  true;
            case R.id.add_emp_action:
                showAddEmpDialog();
                return true;
            case R.id.account_settings:
                Intent settingsIntent = new Intent(ACTION_SETTINGS);
                settingsIntent.putExtra("employee", employee);
                startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_employee, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showAddEmpDialog() {

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.add_employee_dialog);
            Button add = dialog.findViewById(R.id.add_emp_button_create);
            fName = dialog.findViewById(R.id.emp_firstname_create);
            lName = dialog.findViewById(R.id.emp_lastname_create);
            dob = dialog.findViewById(R.id.emp_dob_create);
            dob.setEnabled(false);
            ImageButton datePicker = dialog.findViewById(R.id.datePicker);
            datePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayDatePickerDialog(dob);
                }
            });
            email = dialog.findViewById(R.id.emp_Email_create);
            password = dialog.findViewById(R.id.emp_password_create);
            confirmPassword = dialog.findViewById(R.id.emp_passwordConfirm_create);
            final CheckBox manager = dialog.findViewById(R.id.is_manager_checkBox);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId() == R.id.add_emp_button_create) {
                        if (validateEmployeeInformation()) {
                            if (checkValidEmail(empEmailStr)) {
                                Date time = Calendar.getInstance(TimeZone.getDefault()).getTime();
                                Employee e = new Employee(empEmailStr, empfName, emplName, empPass, null,employee.getCompany(), getAccountType(manager), time.getTime(),null,new HashMap<String, Job>(), new HashMap<String, Employee>(),employee.getAdminID(), new HashMap<String, Day>(), empDob, 8);
                                createFirebaseUsers(empEmailStr, empPass, e, dialog);
                            }
                        }
                    }
                }
            });
            dialog.show();

    }
    private boolean isCreatingNewUser;

    //Create new Firebase Users
    private void createFirebaseUsers(final String email, final String password, final Employee e, final Dialog dialog) {

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://ontime-55bcc.firebaseio.com/")
                .setProjectId("ontime-55bcc")
                .setApiKey("AIzaSyDoSol8iB3byUG0cNj-iC2UN4x_6uzmINA")
                .setApplicationId("1:291860944094:android:64a6ddeb1615de19")
                .build();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating new employee account...");
        progressDialog.show();
        FirebaseApp app;

            app = FirebaseApp.initializeApp(this, options, "second");

            isCreatingNewUser = true;

            secondAuth = FirebaseAuth.getInstance(app);

            secondAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String adminId = employee.getId();
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                if (isCreatingNewUser) {
                                    Date date = Calendar.getInstance(TimeZone.getDefault()).getTime();
                                    if(secondAuth.getCurrentUser()!=null) {
                                        e.setId(secondAuth.getCurrentUser().getUid());
                                    }
                                    e.setDateCreated(date.getTime());
                                    e.setAdminID(adminId);
                                    Log.d(TAG, "signInWithEmail:success");
                                    databaseHelper.saveEmployeeToDatabaseForEmployee(e);
                                    isCreatingNewUser = false;
                                    databaseHelper.addEmployeeToDatabaseForAdmin(e);
                                    dialog.dismiss();
                                    employee.getEmployees().put(e.getId(), e);
                                }
                                signInNewUser(employee.getEmail(), employee.getPassword());
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(ForecastActivity.this, "Failed to create user.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

    }

    private void signInNewUser(String email, String password) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                if (!isCreatingNewUser) {
                                        ArrayList<Employee> employees = new ArrayList<>();
                                        for (String s : employee.getEmployees().keySet()) {
                                            employees.add(employee.getEmployees().get(s));
                                        }
                                        ListView listView = findViewById(android.R.id.list);
                                        listView.setAdapter(new EmployeeListAdapter(ForecastActivity.this, employees));
                                        progressDialog.dismiss();

                                } else {
                                    isCreatingNewUser = false;
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());

                            }
                        }
                    });

    }
    //Method to check all the information in the employee add field
    private boolean validateEmployeeInformation() {
        empfName = fName.getText().toString().trim();
        emplName = lName.getText().toString().trim();
        empEmailStr = email.getText().toString().trim();
        empDob = dob.getText().toString().trim();
        empPass = password.getText().toString().trim();
        String empConfirmPass = confirmPassword.getText().toString().trim();
        boolean bool = true;
        if (empfName.equals("")) {
            fName.setError("Enter a name");
            bool = false;
        }
        if (emplName.equals("")) {
            lName.setError("Enter a name");
            bool = false;
        }
        if (empEmailStr.equals("")) {
            email.setError("Must have an Email");
            bool = false;
        }
        if (empPass.equals("")) {
            password.setError("Enter a Password");
            bool = false;
        }
        if (empConfirmPass.equals("")) {
            confirmPassword.setError("Enter a Password");
            bool = false;
        }
        if (empDob.equals("")) {
            dob.setError("DOB needed");
            bool = false;
        }
        if (!empPass.equals(empConfirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            bool = false;
        }
        if(!checkValidEmail(empEmailStr)) {
            bool = false;
        }
        return bool;
    }

    //Checks the email address using a regex
    private boolean checkValidEmail(String string) {
        //NOT the best email check but rexex pattern was failing  this is a basic band aid
        if (string.contains("@") && string.contains(".")) {
            return true;
        } else {
            Toast.makeText(this, "Not proper email address", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("employee", employee);
//        outState.putString("access", mAccess);
//        outState.putInt("page", mPage);
    }

    //Display the date picker dialog
    private void displayDatePickerDialog(final MaterialEditText met) {

        DatePickerDialog dpd;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                dpd = new DatePickerDialog(this);
                dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String dobStr = String.valueOf(i1 + 1) + "/" + String.valueOf(i2) + "/" + String.valueOf(i);
                        met.setText(dobStr);
                    }
                });
                dpd.show();
            }
    }
    private void presentDialogForSignOut(){
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Sign out?");
        dialog.setMessage("Are you sure you want to sign out?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(LandingActivity.ACTION_LANDING);
                startActivity(intent);
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private String getAccountType(CheckBox manager) {
        if (manager.isChecked()) {
            return "manager";
        } else {
            return "employee";
        }
    }

    @Override
    public void updateFromCalendar(Employee employee) {
        getSupportFragmentManager()
                .beginTransaction().add(R.id.jobs_content, JobsManagementFragment.newInstance(employee))
                .commit();
    }
}
