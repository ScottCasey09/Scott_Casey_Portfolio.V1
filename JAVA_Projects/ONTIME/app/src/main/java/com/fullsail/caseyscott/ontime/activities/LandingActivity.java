// Casey Scott
// FINAL PROJECT - 1712
// LandingActivity.java

package com.fullsail.caseyscott.ontime.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.helpers.NetworkUtils;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class LandingActivity extends AppCompatActivity {

    //Intent filter action strings
    public static final String ACTION_MAIN_PAGE_VIEWER = "com.android.caseyscott.ontime.MAIN_PAGE_VIEWER";
    private static final String ACTION_CREATE_ACCOUNT = "com.android.caseyscott.ontime.ACTION_CREATE_ACCOUNT";
    public static final String ACTION_LANDING = "com.android.caseyscott.ontime.LANDING";
    public static final String ACTION_FORECAST = "com.android.fullsail.caseyscott.ontime.ACTION_FORECAST";
    public static final String ACTION_EMPLOYEE_INFO = "com.android.fullsail.caseyscott.ontime.ACTION_EMPLOYEE_INFO";
    private static final String ACTION_PASSWORD_RESET = "com.android.fullsail.caseyscott.ontime.ACTION_PASSWORD_RESET";

    //Tag for logging messages and errors
    private static final String TAG = "LandingActivity";

    //Views and objects
    private ImageButton adminButton;
    private ImageButton empButton;
    private ImageButton managerButton;
    private ImageButton newButton;
    private FirebaseAuth mAuth;
    private ProgressBar bar;
    private ProgressBar progressBar;
    private NetworkUtils networkUtils;
    private MaterialEditText emailET;
    private MaterialEditText passwordET;
    private Button scan;
    private Button newAccount;
    private Button signin;
    private static final int PHOTO_REQUEST = 10;
    private BarcodeDetector detector;
    private Uri imageUri;
    private static final int REQUEST_WRITE_PERMISSION = 20;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private String scanResults = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Instantiate the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        //Grab any data from the saved instance state
        if (savedInstanceState!= null){
            String[] creds = savedInstanceState.getStringArray(SAVED_INSTANCE_RESULT);
            if(creds!=null) {
                if (creds.length > 1 && creds.length < 3) {
                    signInUserFromScan(creds[0], creds[1]);
                }
            }
        }
        //Set up the views for the tablet
        if (getResources().getBoolean(R.bool.is_tablet)) {
            setContentView(R.layout.tablet_login_layout);
            scan = findViewById(R.id.scan_button);
            newAccount = findViewById(R.id.create_new_account);
            signin = findViewById(R.id.sign_in_button_cred);
            scan.setOnClickListener(onClickListener);
            newAccount.setOnClickListener(onClickListener);
            signin.setOnClickListener(onClickListener);
            emailET = findViewById(R.id.cred_dialog_id);
            passwordET = findViewById(R.id.cred_dialog_password);
            bar = findViewById(R.id.progressBar);
            (findViewById(R.id.forgot_password)).setOnClickListener(onClickListener);
        }
        //Setup the views for the phone
        else {
            setContentView(R.layout.landing_screen_layout);
            (adminButton = findViewById(R.id.admin_signin_button)).setOnClickListener(mLoginListeners);
            (empButton = findViewById(R.id.employee_signin_button)).setOnClickListener(mLoginListeners);
            (managerButton = findViewById(R.id.manager_signin_button)).setOnClickListener(mLoginListeners);
            (newButton = findViewById(R.id.new_signup_button)).setOnClickListener(mLoginListeners);
            networkUtils = new NetworkUtils(this);
            progressBar = findViewById(R.id.progressBar);

        }
        //Instantiate the decoder object
        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
        //Make sure the decoder works
        if (!detector.isOperational()) {
            Log.i(TAG, "Could not set up detector");

        }
    }
    //Click listener for the admin login buttons
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.sign_in_button_cred:
                    String email = emailET.getText().toString().trim();
                    String password = passwordET.getText().toString().trim();
                    signInUser(email, password, newAccount, signin, scan);
                    return;
                case R.id.create_new_account:
                    Intent intent = new Intent(LandingActivity.ACTION_CREATE_ACCOUNT);
                    startActivity(intent);
                    return;
                case R.id.scan_button:
                    ActivityCompat.requestPermissions(LandingActivity.this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
                case R.id.forgot_password:
                    askUserIfToResetPassword(false, "", "");

            }
        }
    };

    //Handle the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //If ok then get the scanner
                    takePicture();
                } else {
                    //Inform the user that the permission was denied
                    Toast.makeText(LandingActivity.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            launchMediaScanIntent();
            try {
                //Create the bitmap from the uri using the created method
                Bitmap bitmap = decodeBitmapUri(this, imageUri);
                if (detector.isOperational() && bitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<Barcode> barcodes = detector.detect(frame);
                    for (int index = 0; index < barcodes.size(); index++) {
                        Barcode code = barcodes.valueAt(index);

                        //Required only if you need to extract the type of barcode
                        int type = barcodes.valueAt(index).valueFormat;
                        switch (type) {
                            //Check the data type and capture the result
                            case Barcode.TEXT:
                                Log.i(TAG, code.rawValue);
                                //split the string to an email and a password
                                String[] creds = code.rawValue.split(",");
                                if(creds.length>1 && creds.length<3) {
                                    //Sign in the user with the captured data
                                    signInUserFromScan(creds[0], creds[1]);
                                    scanResults = code.rawValue;
                                }
                                break;
                            default:
                                Log.i(TAG, code.rawValue);
                                break;
                        }
                    }
                    if (barcodes.size() == 0) {
                        Log.e(TAG,"Scan Failed: Found nothing to scan");
                    }
                } else {
                    Log.e(TAG,"Could not set up the detector!");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
                Log.e(TAG, e.toString());
            }
        }
    }
    //method to launch the QR code scanner save the QR image for parsing
    private void takePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_WRITE_PERMISSION);
        }else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photo = new File(Environment.getExternalStorageDirectory(), "picture.jpg");
            imageUri = FileProvider.getUriForFile(this,
                    this.getPackageName() + ".provider", photo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, PHOTO_REQUEST);
        }
    }
    //Save the data items
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageUri != null) {
            outState.putString(SAVED_INSTANCE_URI, imageUri.toString());

                if (imageUri != null) {
                    outState.putString(SAVED_INSTANCE_URI, imageUri.toString());
                    outState.putString(SAVED_INSTANCE_RESULT, scanResults);
            }
        }
        super.onSaveInstanceState(outState);
    }

    //method to launch the QR decoder
    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    //Method for decoding the image from uri to bitmap
    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }

    //Method to handle start up
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if(!currentUser.getUid().equals("")) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mRef = database.getReference();
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (mAuth.getCurrentUser() != null) {
                            String id = mAuth.getCurrentUser().getUid();
                            Employee employee = dataSnapshot.child("users").child(id).getValue(Employee.class);
                            Employee e = null;
                            if (employee != null) {
                                e = dataSnapshot.child("users").child(employee.getAdminID()).getValue(Employee.class);
                            }
                            if (e != null) {
                                //Go to the content for the signed in user
                                Intent intent = new Intent();
                                intent.putExtra("user", e);
                                intent.putExtra("access", employee.getEmployeeType());
                                //Check device
                                if (getResources().getBoolean(R.bool.is_tablet)) {
                                    //Check the type of user (this may change with further development of the tablet version)
                                    if(employee.getAccount_type().equals("admin")) {
                                        intent.setAction(ACTION_FORECAST);
                                        startActivity(intent);
                                    }else{
                                        informUserTheyAreNotAdmin();
                                        mAuth.signOut();
                                    }
                                } else {
                                    //Go to the content
                                    intent.setAction(ACTION_MAIN_PAGE_VIEWER);
                                    startActivity(intent);
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Log.e(TAG, databaseError.getMessage());
                    }
                });
            }
        }
    }

    private void informUserTheyAreNotAdmin(){
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getString(R.string.admin_only));
        dialog.setMessage(getString(R.string.not_admin_mesg));
        dialog.show();
    }

    //Listeners for my login buttons
    private final View.OnClickListener mLoginListeners = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            String accountAccess;
            switch (view.getId()) {
                case R.id.admin_signin_button:
                    if (networkUtils.checkInternetConnection()) {
                        showDialogForSignin("", "");
                    } else {
                        showNotConnectionToast();
                    }
                    return;
                case R.id.employee_signin_button:
                    if (networkUtils.checkInternetConnection()) {
                        showDialogForSignin("", "");
                    } else {
                        showNotConnectionToast();
                    }
                    return;
                case R.id.manager_signin_button:
                    if (networkUtils.checkInternetConnection()) {
                        showDialogForSignin("", "");
                    } else {
                        showNotConnectionToast();
                    }
                    return;
                case R.id.new_signup_button:
                    accountAccess = "admin";
                    intent.setAction(ACTION_CREATE_ACCOUNT);
                    intent.putExtra("access", accountAccess);
            }
            startActivity(intent);
        }

    };

    private void showNotConnectionToast() {
        Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
    }

    //Dialog for the sign in method
    private void showDialogForSignin(final String emailStr, String passStr) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.enter_credentials_dialog_layout);
        final MaterialEditText email, password;
        TextView forgot = dialog.findViewById(R.id.forgot_password);
        email = dialog.findViewById(R.id.cred_dialog_id);
        email.setText(emailStr);
        password = dialog.findViewById(R.id.cred_dialog_password);
        password.setText(passStr);
        final Button scan, cancel, signin;
        scan = dialog.findViewById(R.id.scan_button_cred);
        cancel = dialog.findViewById(R.id.cancel_button_cred);
        signin = dialog.findViewById(R.id.sign_in_button_cred);

        View.OnClickListener mDialogListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordString;
                String emailString;
                switch (view.getId()) {
                    case R.id.scan_button_cred:
                        ActivityCompat.requestPermissions(LandingActivity.this, new
                                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
                        dialog.dismiss();
                        return;
                    case R.id.cancel_button_cred:
                        //Dismiss the dialog
                        dialog.dismiss();
                        return;
                    case R.id.sign_in_button_cred:
                        bar = dialog.findViewById(R.id.progressBar);
                        startProgressBar(bar);
                        //Get the entered information
                         emailString = email.getText().toString().trim();
                         passwordString = password.getText().toString().trim();
                        //Validate the information
                        //Disable all views
                        cancel.setEnabled(false);
                        email.setEnabled(false);
                        password.setEnabled(false);
                        signin.setEnabled(false);
                        scan.setEnabled(false);
                        //Sign in the user
                        signInUser(emailString, passwordString, cancel, signin, scan);
                        return;
                    case R.id.forgot_password:
                        //Grab the credentials
                         emailString = email.getText().toString().trim();
                         passwordString = password.getText().toString().trim();
                        //Get rid of the dialog so you don't have 2 dialogs open
                        dialog.dismiss();
                         //Show the change password question dialog and pass true because your coming form a dialog
                        askUserIfToResetPassword(true, emailString, passwordString);
                }
            }
        };
        //Assign the clock listeners
        forgot.setOnClickListener(mDialogListener);
        scan.setOnClickListener(mDialogListener);
        cancel.setOnClickListener(mDialogListener);
        signin.setOnClickListener(mDialogListener);
        //Show the dialog
        dialog.show();

    }
    //Method to check if the user wants to reset their forgotten password
    private  void askUserIfToResetPassword(final boolean bool, final String email, final String password){
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getResources().getString(R.string.forgot_password));
        dialog.setMessage(getString(R.string.reset_password_question));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ACTION_PASSWORD_RESET);
                startActivity(intent);
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(bool) {
                    dialog.dismiss();
                    //Reopen the sign in dialog and assign the email and password the same values
                    showDialogForSignin(email, password);
                }else{
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }//Method to start the progress bar
    private void startProgressBar(ProgressBar bar) {
        bar.setVisibility(View.VISIBLE);
        bar.animate();
    }
        //Method to stop the progress bar
    private void stopProgressBar(ProgressBar bar) {
        bar.setVisibility(View.GONE);

    }
        // method to sign in the user
    private void signInUser(String email, final String password, final Button cancel_or_signup, final Button signin, final Button scan) {
       if(email !=null & password!=null) {
           //Disable the views
           cancel_or_signup.setEnabled(false);
           signin.setEnabled(false);
           scan.setEnabled(false);
           //Check which device is running
           if(getResources().getBoolean(R.bool.is_tablet)) {
               //Disable the views
               emailET.setEnabled(false);
               passwordET.setEnabled(false);
           }
           //Sign in the user with email and password
           mAuth.signInWithEmailAndPassword(email, password)
                   .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               // Sign in success, update UI with the signed-in user's information
                               Log.d(TAG, "signInWithEmail:success");
                               if (mAuth.getCurrentUser() != null && !mAuth.getCurrentUser().getUid().equals("")) {
                                   //Get a reference to the database to grab the user data
                                   final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                   DatabaseReference mRef = database.getReference();
                                   mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                           String id = mAuth.getCurrentUser().getUid();
                                           //Assign the data to an employee object
                                           Employee employee = dataSnapshot.child("users").child(id).getValue(Employee.class);
                                           Employee e = null;
                                           //Get the admin user object
                                           if (employee != null) {
                                               e = dataSnapshot.child("users").child(employee.getAdminID()).getValue(Employee.class);
                                           }

                                           if (e != null) {
                                               //Go th the main views and pass the admin object. Admin holds all the data
                                               Intent intent = new Intent();
                                               intent.putExtra("user", e);
                                               DatabaseReference databaseReference = database.getReference().child("users");
                                               //To make sure the employee has a reference to the admin id, assign the admin Id field and save the employee to the BD
                                               employee.setId(id);
                                               employee.setPassword(password);
                                               Map<String, Object> map = new HashMap<>();
                                               map.put(id, employee);
                                               //Update the BD
                                               databaseReference.updateChildren(map);
                                               //Pass the empoyee type (not used for much)
                                               intent.putExtra("access", employee.getEmployeeType());
                                               if (getResources().getBoolean(R.bool.is_tablet)) {
                                                  // if the employee is not admin don't continue ( this may be changed later as the tablet version is developed more)
                                                   if (employee.getAccount_type().equals("admin")) {
                                                       intent.setAction(ACTION_FORECAST);
                                                       startActivity(intent);
                                                   } else {
                                                       //Tell the user what's happening
                                                       informUserTheyAreNotAdmin();
                                                       mAuth.signOut();
                                                   }
                                               } else {
                                                   //Go to the content
                                                   intent.setAction(ACTION_MAIN_PAGE_VIEWER);
                                                   startActivity(intent);
                                               }

                                           }
                                       }
                                        //Method for any cancelation errors or issues
                                       @Override
                                       public void onCancelled(DatabaseError databaseError) {
                                           Log.e(TAG, databaseError.getMessage());
                                           Toast.makeText(LandingActivity.this, "Network or Authentication issue", Toast.LENGTH_SHORT).show();
                                           //Enable all the fields
                                           cancel_or_signup.setEnabled(true);
                                           signin.setEnabled(true);
                                           scan.setEnabled(true);
                                           emailET.setEnabled(true);
                                           passwordET.setEnabled(true);
                                       }
                                   });
                               }
                           } else {
                               // If sign in fails, display a message to the user.
                               Log.w(TAG, "signInWithEmail:failure", task.getException());
                               //Tell the user the auth has failed
                               Toast.makeText(LandingActivity.this, "Authentication failed.",
                                       Toast.LENGTH_SHORT).show();
                               //Enable the views
                               cancel_or_signup.setEnabled(true);
                               signin.setEnabled(true);
                               scan.setEnabled(true);
                               //Stop the progress bar
                               stopProgressBar(bar);
                               //Express possible issue
                               Toast.makeText(LandingActivity.this, "Network or Authentication issue", Toast.LENGTH_SHORT).show();

                           }


                       }
                   });
       }
    }

    //Method to handle the sign in from scanner (QR code)
    private void signInUserFromScan(String email, String password) {
        //Disable all the views
        adminButton.setEnabled(false);
        managerButton.setEnabled(false);
        empButton.setEnabled(false);
        newButton.setEnabled(false);
        progressBar.animate();
        progressBar.setVisibility(View.VISIBLE);
        //Sign in user with captured information
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            if (mAuth.getCurrentUser() != null) {
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference mRef = database.getReference();

                                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String id = mAuth.getCurrentUser().getUid();
                                        Employee employee = dataSnapshot.child("users").child(id).getValue(Employee.class);
                                        Employee e = null;
                                        if (employee != null) {
                                            e = dataSnapshot.child("users").child(employee.getAdminID()).getValue(Employee.class);
                                        }

                                        if (e != null) {
                                            Intent intent = new Intent();
                                            intent.putExtra("user", e);
                                            //To make sure the user has a reference to the admin id, assign it and save it
                                            DatabaseReference databaseReference = database.getReference().child("users");
                                            employee.setId(id);
                                            Map<String, Object> map = new HashMap<>();
                                            map.put(id, employee);
                                            databaseReference.updateChildren(map);
                                            intent.putExtra("access", employee.getEmployeeType());
                                            //Check device
                                            if (getResources().getBoolean(R.bool.is_tablet)) {
                                                intent.setAction(ACTION_FORECAST);
                                            } else {
                                                intent.setAction(ACTION_MAIN_PAGE_VIEWER);
                                            }
                                            //Go to the content
                                            startActivity(intent);
                                        }
                                    }
                                    //Handle errors
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e(TAG, databaseError.getMessage());
                                        Toast.makeText(LandingActivity.this, "Network or Authentication issue", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            //Handle errors
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LandingActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(LandingActivity.this, "Network or Authentication issue", Toast.LENGTH_SHORT).show();
                            //Enable the views
                            adminButton.setEnabled(true);
                            managerButton.setEnabled(true);
                            empButton.setEnabled(true);
                            newButton.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);
                        }


                    }
                });
    }

}

