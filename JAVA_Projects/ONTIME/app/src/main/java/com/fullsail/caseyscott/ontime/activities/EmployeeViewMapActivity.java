// Casey Scott
// FINAL PROJECT - 1712
// EmployeeViewMapActivity.java

package com.fullsail.caseyscott.ontime.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.helpers.FirebaseDatabaseHelper;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class EmployeeViewMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Employee employee;
    private Employee admin;
    private TextView passwordTV;
    private Button disabledButton;
    private Button enabledButton;
    private Switch aSwitch;
    private String password;
    private ProgressBar bar;
    private FirebaseDatabaseHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.employee_info_activity);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        if(getIntent().hasExtra("employee") && getIntent().getExtras()!=null){
            employee = (Employee) getIntent().getExtras().getSerializable("employee");
            admin = (Employee)getIntent().getSerializableExtra("admin");
        }
        //Set the fragment to hold / support the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_container);
        mapFragment.getMapAsync(this);
        password = "Password: "+employee.getPassword();
        //Get instance references to the views
        MaterialEditText adminPassword = findViewById(R.id.adminPassword);
        TextView emailTV = findViewById(R.id.employeeEmail);
        emailTV.setText(employee.getEmail());
        passwordTV = findViewById(R.id.employeePassword);
        TextView nameTV = findViewById(R.id.employeeName);
        nameTV.setText(employee.toString());
        disabledButton = findViewById(R.id.deleteButtonDisabled);
        enabledButton = findViewById(R.id.deleteButtonEnabled);
        aSwitch = findViewById(R.id.deleteButtonSwitch);
        aSwitch.setOnCheckedChangeListener(switchListener);
        enabledButton.setOnClickListener(clickListener);
        bar = findViewById(R.id.progressBar);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton employee_rb = findViewById(R.id.employee_rb);
        RadioButton manager_rb = findViewById(R.id.manager_rb);
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        //Add the click listeners
        adminPassword.addTextChangedListener(textWatcher);

        //set the checked radio button
        if(Objects.equals(employee.getAccount_type(), "employee")){
            employee_rb.setChecked(true);
        }else{
            manager_rb.setChecked(true);
        }

        //instantiate the DB helper object
        helper = new FirebaseDatabaseHelper();

    }

    private final RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {
            switch (id){
                case R.id.employee_rb:
                    employee.setAccount_type("employee");
                    admin.getEmployees().get(employee.getId()).setAccount_type("employee");
                    return;
                case R.id.manager_rb:
                    employee.setAccount_type("manager");
                    admin.getEmployees().get(employee.getId()).setAccount_type("manager");
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b){
                disabledButton.setVisibility(View.GONE);
                disabledButton.setEnabled(false);
                enabledButton.setVisibility(View.VISIBLE);
                enabledButton.setEnabled(true);
            }else{
                disabledButton.setVisibility(View.VISIBLE);
                disabledButton.setEnabled(false);
                enabledButton.setVisibility(View.GONE);
                enabledButton.setEnabled(false);
            }
        }
    };
    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(view.getId() == R.id.deleteButtonEnabled){
                if(aSwitch.isChecked()){
                    final AlertDialog dialog = new AlertDialog.Builder(EmployeeViewMapActivity.this).create();
                    dialog.setTitle("Remove Employee!");
                    dialog.setMessage("Are you sure you want to delete " + employee.toString()+"? \nThis will delete all records of this" +
                            "employee forever!");
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            removeEmployee(employee);
                            dialog.dismiss();
                            bar.setVisibility(View.VISIBLE);
                            bar.animate();
                        }
                    });
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            aSwitch.setChecked(false);
                        }
                    });
                    dialog.show();
                }
            }
        }
    };


    @Override
    public void onBackPressed() {
        //save the info
        helper.saveAdminUserToDatabase(admin);
        Intent intent = new Intent();
        intent.putExtra("employee", employee);
        intent.putExtra("admin", admin);
        intent.putExtra("access", admin.getAccount_type());
        setResult(RESULT_OK, intent);
        finish();
    }
    //Options menu actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Find the selected options button
        switch (item.getItemId()){
            case android.R.id.home:
                //save the info
                helper.saveAdminUserToDatabase(admin);
                Intent intent = new Intent();
                intent.putExtra("employee", employee);
                intent.putExtra("admin", admin);
                intent.putExtra("access", admin.getAccount_type());
                setResult(RESULT_OK, intent);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MarkerOptions options = new MarkerOptions();
        if(employee.getDays().containsKey(getDateKey())) {
            Marker marker;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (String s: employee.getDays().keySet()) {
                if(employee.getDays().get(s).getClockInLatitude() != 0 && employee.getDays().get(s).getClockInLongitude() != 0) {
                    options.position(new LatLng(employee.getDays().get(s).getClockInLatitude(), employee.getDays().get(s).getClockInLongitude()));
                    String in = "Clock in for " + s;
                    options.title(in);
                    marker = googleMap.addMarker(options);
                    builder.include(marker.getPosition());
                }
                if(employee.getDays().get(s).getClockOutLatitude() != 0 && employee.getDays().get(s).getClockOutLongitude() != 0) {
                    options.position(new LatLng(employee.getDays().get(s).getClockOutLatitude(), employee.getDays().get(s).getClockOutLongitude()));
                    String out = "Clock in for " + s;
                    options.title(out);
                    googleMap.addMarker(options);
                    marker = googleMap.addMarker(options);
                    builder.include(marker.getPosition());
                }

            }
            LatLngBounds bounds = builder.build();
            int padding = 0;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.moveCamera(cu);
        }
    }

    private String getDateKey() {
        Date date = Calendar.getInstance(TimeZone.getDefault()).getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("y_M_dd_E", Locale.getDefault());
        return df2.format(date);
    }

    private void removeEmployee(final Employee employee){

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        mAuth.signInWithEmailAndPassword(employee.getEmail(), employee.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    admin.getEmployees().remove(employee.getId());
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference().child("users");
                    Map<String ,Object> map = new HashMap<>();
                    map.put(admin.getId(), admin);
                    databaseReference.updateChildren(map);
                    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference1 = database1.getReference().child("users");
                    Map<String ,Object> map1 = new HashMap<>();
                    map.put(employee.getId(), new Employee());
                    databaseReference1.updateChildren(map1);
                    if(mAuth.getCurrentUser()!=null) {
                        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mAuth.signOut();
                                if (task.isSuccessful()) {
                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                    mAuth.signInWithEmailAndPassword(admin.getEmail(), admin.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if ((task.isSuccessful())) {
                                                Toast.makeText(EmployeeViewMapActivity.this, "Removed " + employee.toString(), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent();
                                                intent.putExtra("employee", employee);
                                                intent.putExtra("admin", admin);
                                                intent.putExtra("access", admin.getAccount_type());
                                                setResult(RESULT_OK, intent);
                                                finish();
                                            } else {
                                                bar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(EmployeeViewMapActivity.this, R.string.failed_to_reAuth, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }

                }else{
                    bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(EmployeeViewMapActivity.this, R.string.failed_to_delete_user, Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().equals(admin.getPassword())){
                passwordTV.setText(password);
            }
            else {
                passwordTV.setText(getResources().getString(R.string.password_hidden));
            }
        }
    };

}
