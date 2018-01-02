// Casey Scott
// FINAL PROJECT - 1712
// CreateAccountActivity.java

package com.fullsail.caseyscott.ontime.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fullsail.caseyscott.ontime.helpers.FirebaseStorageUtils;
import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.fragments.AdminFragment;
import com.fullsail.caseyscott.ontime.helpers.FirebaseDatabaseHelper;
import com.fullsail.caseyscott.ontime.objects.Day;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.fullsail.caseyscott.ontime.objects.Job;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class CreateAccountActivity extends AppCompatActivity implements FirebaseStorageUtils.SetImageUrl {

    //static variables
    private static final String TAG = "CreateAccountActivity";
    private static final int PICK_FROM_GALLERY = 0X001;
    private static final String ACTION_TO_MAIN = "com.android.caseyscott.ontime.MAIN_PAGE_VIEWER";

    //View variables and Object variables
    private MaterialEditText companyName;
    private MaterialEditText ownerFName;
    private MaterialEditText ownerLname;
    private MaterialEditText companyEmail;
    private MaterialEditText companyConfirmEmail;
    private MaterialEditText companyPassword;
    private MaterialEditText companyConfirmPassword;
    private MaterialEditText empFname;
    private MaterialEditText empLname;
    private MaterialEditText empDOB;
    private MaterialEditText empEmail;
    private MaterialEditText empPassword;
    private MaterialEditText empConfirmPassword;
    private Button addLogoButton;
    private ImageView logoImageView;
    private CheckBox managerCheckBox;
    private FirebaseAuth mAuth;
    private ArrayList<Employee> employees;
    private FirebaseStorageUtils mStorageReference;
    private String logoUri;
    private FirebaseDatabaseHelper databaseHelper;
    private Employee employer;

    //Variables
    private String empfName = "";
    private String emplName = "";
    private String empEmailStr = "";
    private String empDob = "";
    private String empPass = "";
    private String companyNameStr = "";
    private String ownFName = "";
    private String ownLname = "";
    private String companyEmailStr = "";
    private String companyPassStr = "";
    private boolean isCreatingEmployee;
    private boolean isCreatingAdmin;
    private ProgressBar bar;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getBoolean(R.bool.is_tablet)) {
            setContentView(R.layout.tablet_account_create);
        } else {
            setContentView(R.layout.create_account_screen);
        }
        //Set the action bar to display a back arrow option
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mStorageReference = new FirebaseStorageUtils(this);
        //Get reference to all the EditText Fields and other views in the View
        bar = findViewById(R.id.imageProgressBar);
        companyName = findViewById(R.id.company_name_editText_create);
        ownerFName = findViewById(R.id.first_name_editText_create);
        ownerLname = findViewById(R.id.last_name_editText_create);
        companyEmail = findViewById(R.id.email_create);
        companyConfirmEmail = findViewById(R.id.email_confirm_create);
        companyPassword = findViewById(R.id.password_create);
        companyConfirmPassword = findViewById(R.id.password_confirm_create);
        empFname = findViewById(R.id.emp_firstname_create);
        empLname = findViewById(R.id.emp_lastname_create);
        empEmail = findViewById(R.id.emp_Email_create);
        empDOB = findViewById(R.id.emp_dob_create);
        empDOB.setEnabled(false);
        empPassword = findViewById(R.id.emp_password_create);
        empConfirmPassword = findViewById(R.id.emp_passwordConfirm_create);
        addLogoButton = findViewById(R.id.logo_add_Button);
        logoImageView = findViewById(R.id.logo_entry);
        Button addEmployeeButton = findViewById(R.id.add_emp_button_create);
        ImageButton datePickerButton = findViewById(R.id.datePicker);
        addEmployeeButton.setOnClickListener(mOnClickListener);
        addLogoButton.setOnClickListener(mOnClickListener);
        logoImageView.setOnClickListener(mOnClickListener);
        addLogoButton.setOnClickListener(mOnClickListener);
        datePickerButton.setOnClickListener(mOnClickListener);
        logoImageView.setOnClickListener(mOnClickListener);
        managerCheckBox = findViewById(R.id.is_manager_checkBox);
        employees = new ArrayList<>();
        //Get Reference to the Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        databaseHelper = new FirebaseDatabaseHelper();
        progressDialog = new ProgressDialog(this);
        failedEmployees = new ArrayList<>();

    }

    //ON click listener
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //The button to press when You need to enter an image
                case R.id.logo_add_Button:
                    getImageFromFile();
                    return;
                //The logo but needs to be changeable
                case R.id.logo_entry:
                    getImageFromFile();
                    return;
                //The button to add an employee
                case R.id.add_emp_button_create:
                    //Check the entries then add the employee to the list
                    if (validateEmployeeInformation()) {
                        companyNameStr = companyName.getText().toString();
                        Date date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime();
                        Employee e;
                        if (logoUri == null || logoUri.equals("")) {
                            e = new Employee(empEmailStr, empfName, emplName, empPass,null, companyNameStr, getAccountType(), date.getTime(), null,new HashMap<String, Job>(), new HashMap<String, Employee>(), null, new HashMap<String, Day>(), empDob, 8);
                        } else {
                            e = new Employee(empEmailStr, empfName, emplName, empPass,null, companyNameStr, getAccountType(), date.getTime(), logoUri,new HashMap<String, Job>(), new HashMap<String, Employee>(), null, new HashMap<String, Day>(), empDob, 8);
                        }
                        employees.add(e);
                        updateTheUI(e);
                        clearInputs();

                    }
                    return;
                case R.id.datePicker:
                    //Display the date picker
                    displayDatePickerDialog();
            }
        }
    };

    //Method to get the image from the device
    private void getImageFromFile() {
        try {

            //Check for permissions
            if (ActivityCompat.checkSelfPermission(CreateAccountActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CreateAccountActivity.this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
            } else {
                //Got to the image gallery to select an image
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                setResult(RESULT_OK);
                startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Result handling method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check for the correct result codes
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                //get the data
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                //Set the image
                logoImageView.setImageBitmap(selectedImage);
                mStorageReference.uploadImageToFirebaseStorage(imageUri);
                //Change the views
                addLogoButton.setVisibility(View.GONE);
                logoImageView.setVisibility(View.VISIBLE);
                //Start the progress bar
                bar.setVisibility(View.VISIBLE);
                //While the image is loading to firebase animate the progress bar
                bar.animate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTheUI(final Employee e) {

        final LinearLayout ll = findViewById(R.id.added_employees_linearLayout_listView_rep);
        final ViewGroup nullParent = null;
        final View v = getLayoutInflater().inflate(R.layout.added_employee_linearlayout_list_cell_rep, nullParent);
        TextView tv = v.findViewById(R.id.employeeName_fakeList);
        tv.setText(e.toString());
        ImageButton ib = v.findViewById(R.id.delete_Employee_Button_fakeList);
        ib.setTag(e);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll.removeView(v);
                employees.remove(e);
            }
        });
        ll.addView(v);
    }

    //Method for setting the options menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Options menu actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Find the selected options button
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.check_option:
                if (validateCompanyInformation()) {

                    if (bar.getVisibility() == View.VISIBLE) {
                        Toast.makeText(CreateAccountActivity.this, "Wait! Uploading image to Database.", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.setMessage("Starting account build");
                        progressDialog.setTitle("Building account");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        isCreatingAdmin = true;
                        Date date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime();
                        employer = new Employee(companyEmailStr, ownFName, ownLname, companyPassStr, "",companyNameStr, "admin",date.getTime(), null, new HashMap<String, Job>(),new HashMap<String, Employee>(), null, new HashMap<String, Day>(), null, 8);
                        createUsersAccounts(employer.getEmail(), employer.getPassword());

                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }

    //Method to check all the information in the employee add field
    private boolean validateEmployeeInformation() {
        empfName = empFname.getText().toString().trim();
        emplName = empLname.getText().toString().trim();
        empEmailStr = empEmail.getText().toString().trim();
        empDob = empDOB.getText().toString().trim();
        empPass = empPassword.getText().toString().trim();
        String empConfirmPass = empConfirmPassword.getText().toString().trim();
        boolean bool = true;
        if (empfName.equals("")) {
            empFname.setError("Enter a name");
            bool = false;
        }
        if (emplName.equals("")) {
            empLname.setError("Enter a name");
            bool = false;
        }
        if (empEmailStr.equals("")) {
            empEmail.setError("Must have an Email");
            bool = false;
        }
        if (!checkValidEmail(empEmailStr)) {
            bool = false;
        }
        if (empPass.equals("")) {
            empPassword.setError("Enter a Password");
            bool = false;
        }
        if (empConfirmPass.equals("")) {
            empConfirmPassword.setError("Enter a Password");
            bool = false;
        }
        if (empDob.equals("")) {
            empDOB.setError("DOB needed");
            bool = false;
        }
        if (!empPass.equals(empConfirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            bool = false;
        }
        return bool;
    }

    //Method for checking all of the input fields
    private boolean validateCompanyInformation() {
        companyNameStr = companyName.getText().toString().trim();
        ownFName = ownerFName.getText().toString().trim();
        ownLname = ownerLname.getText().toString().trim();
        companyEmailStr = companyEmail.getText().toString().trim();
        String companyConfirmEmailStr = companyConfirmEmail.getText().toString().trim();
        companyPassStr = companyPassword.getText().toString().trim();
        String companyConfirmPass = companyConfirmPassword.getText().toString().trim();

        boolean bool = true;
        //Check all the fields
        if (companyNameStr.equals("")) {
            companyName.setError("Enter a Name");
            bool = false;
        }
        if (ownFName.equals("")) {
            ownerFName.setError("Enter a Name");
            bool = false;
        }
        if (companyEmailStr.equals("")) {
            companyEmail.setError("Enter a Email");
            bool = false;
        }
        if (companyConfirmEmailStr.equals("")) {
            companyConfirmEmail.setError("Enter a Email");
            bool = false;
        }
        if (ownLname.equals("")) {
            ownerLname.setError("Enter a Name");
            bool = false;
        }
        if (!companyEmailStr.equals("") && !companyConfirmEmailStr.equals("")) {
            if (!companyEmailStr.equals(companyConfirmEmailStr)) {
                Toast.makeText(this, "Emails do not match", Toast.LENGTH_SHORT).show();
                bool = false;
            } else {
                bool = checkValidEmail(companyEmailStr);
            }
        }
        if (companyPassStr.equals("")) {
            companyPassword.setError("Enter a Password");
            bool = false;
        }
        if (companyConfirmPass.equals("")) {
            companyConfirmPassword.setError("Enter a Password");
            bool = false;
        }
        if (!companyConfirmPass.equals(companyPassStr)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            bool = false;
        }
        return bool;
    }

    //Display the date picker dialog
    private void displayDatePickerDialog() {

        //Calendar now = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
        DatePickerDialog dpd;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dpd = new DatePickerDialog(this);
            dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    String dob = String.valueOf(i1 + 1) + "/" + String.valueOf(i2) + "/" + String.valueOf(i);
                    empDOB.setText(dob);
                }
            });
            dpd.show();
        }

    }

    //Checks the email address using a regex
    private boolean checkValidEmail(String string) {
        return string.contains("@");
    }

    //Clear all editText fields
    private void clearInputs() {
        empDOB.setText("");
        empConfirmPassword.setText("");
        empEmail.setText("");
        empFname.setText("");
        empLname.setText("");
        empPassword.setText("");
    }

    //Method th return the accounts type
    private String getAccountType() {
        if (managerCheckBox.isChecked()) {
            return "manager";
        } else {
            return "employee";
        }
    }

    //Interface method to set the added images firebase image URL
    @Override
    public void setImageUrl(String uri) {
        logoUri = uri;
        bar.setVisibility(View.INVISIBLE);
    }

    //Variable to cycle through the employees list (for the customized fore loop)
    private int i = 0;

    //Method to build the account
    private void buildAccount(final String email, final String password, final Employee employee) {
        //Sign in the added user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(mAuth.getCurrentUser()!=null){
                                employee.setId(mAuth.getCurrentUser().getUid());
                            }
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            if (employees.size() == i) {
                                isCreatingEmployee = false;
                                mAuth.signOut();
                                mAuth.signInWithEmailAndPassword(employer.getEmail(), employer.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //Create the admin account
                                        //If the admin account fails I need to remove all employees from auth and Db
                                        if (mAuth.getCurrentUser() != null) {
                                            HashMap<String, Employee> map = new HashMap<>();
                                            for (Employee e : employees) {
                                                e.setAdminID(mAuth.getCurrentUser().getUid());
                                                databaseHelper.saveEmployeeToDatabaseForEmployee(e);
                                                map.put(e.getId(), e);
                                            }
                                            //Add the image uri/URl to the employee for reference
                                            if (logoUri != null && (!logoUri.equals(""))) {
                                                employer.setImageUrl(logoUri);
                                            }
                                            String id = mAuth.getCurrentUser().getUid();
                                            //Set the properties of the employee
                                            employer.setId(id);
                                            employer.setAdminID(id);
                                            employer.setEmployees(map);
                                            databaseHelper.saveAdminUserToDatabase(employer);
                                            progressDialog.dismiss();
                                            //Go the app
                                            Intent intent = new Intent();
                                            intent.setAction(ACTION_TO_MAIN);
                                            intent.putExtra("user", employer);
                                            intent.putExtra(AdminFragment.ARG_ACCESS, "admin");
                                            intent.putExtra("failed", failedEmployees);
                                            startActivity(intent);
                                        }
                                    }
                                });

                            } else {

                                if (isCreatingAdmin) {
                                    isCreatingAdmin = false;
                                    //Sign out the auth account so the system can make the employees accounts
                                    mAuth.signOut();
                                    isCreatingEmployee = true;
                                    createUsersAccounts(employees.get(i).getEmail(), employees.get(i).getPassword());
                                } else if (isCreatingEmployee) {
                                    //Sign out and make the next account
                                    mAuth.signOut();
                                    createUsersAccounts(employees.get(i).getEmail(), employees.get(i).getPassword());
                                }
                            }

                        } else {
                            if (isCreatingAdmin) {
                                //Of the admin fails stop the process
                                if(mAuth.getCurrentUser()!=null){
                                    employer.setAdminID(mAuth.getCurrentUser().getUid());
                                    employer.setId(mAuth.getCurrentUser().getUid());
                                }
                                progressDialog.dismiss();
                                return;
                            } else {
                                //If the employee fails to create then try again
                                mAuth.signOut();
                                if (employees.size() < 1) {
                                    createUsersAccounts(employees.get(i).getEmail(), employees.get(i).getPassword());
                                }
                            }
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }


    //Make array of failed user accounts to pass so the admin can recreate the accounts later
    //without entering in the same information later
    private ArrayList<Employee> failedEmployees;

    ////Method to create the users
    private void createUsersAccounts(String email, String password) {
        //Set the progress message
        if (isCreatingAdmin) {
            progressDialog.setMessage("Creating admin: " + employer.toString());
        } else {
            progressDialog.setMessage("Creating employee: " + employees.get(i).toString());
        }
        //Create user in firebase
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (isCreatingAdmin) {
                        buildAccount(employer.getEmail(), employer.getPassword(), employer);
                    } else {
                        buildAccount(employees.get(i).getEmail(), employees.get(i).getPassword(), employees.get(i));
                        i += 1;
                    }
                    Log.d(TAG, "createAccountWithEmail:success");
                } else {
                    Log.w(TAG, "createAccountWithEmail:failure", task.getException());
                    if (isCreatingAdmin) {
                        progressDialog.dismiss();
                        Toast.makeText(CreateAccountActivity.this, "Failed to create account",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        failedEmployees.add(employees.get(i));
                        Toast.makeText(CreateAccountActivity.this, "Retrying" + employees.get(i).toString(),
                                Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        createUsersAccounts(employees.get(i).getEmail(), employees.get(i).getPassword());
                    }


                }
            }
        });
    }


}
