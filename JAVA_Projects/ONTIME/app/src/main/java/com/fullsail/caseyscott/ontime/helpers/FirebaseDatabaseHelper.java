// Casey Scott
// FINAL PROJECT - 1712
// FirebaseDatabaseHelper.java

package com.fullsail.caseyscott.ontime.helpers;

import com.fullsail.caseyscott.ontime.objects.Day;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class FirebaseDatabaseHelper {

    //private static final String TAG = "DatabaseHelper - ";
    private final FirebaseAuth mAuth;
    public FirebaseDatabaseHelper(){
        mAuth = FirebaseAuth.getInstance();

    }


    public void setDefaultedDay(Employee employeeInQuestion){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(mAuth.getCurrentUser() != null){
            DatabaseReference reference = database.getReference().child("users").child(employeeInQuestion.getAdminID()).child("employees");
            Map<String, Object> map = new HashMap<>();
            map.put(employeeInQuestion.getId(), employeeInQuestion);
            reference.updateChildren(map);
            DatabaseReference reference1 = database.getReference().child("users");
            Map<String, Object> map1 = new HashMap<>();
            map.put(employeeInQuestion.getId(), employeeInQuestion);
            reference1.updateChildren(map1);
        }

    }

    public void saveAdminUserToDatabase(Employee employer){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (mAuth.getCurrentUser() != null) {
            DatabaseReference reference = database.getReference().child("users");
            Map <String, Object> map = new HashMap<>();
            map.put(employer.getAdminID(), employer);
            reference.updateChildren(map);
        }
    }
//    public void saveEmployeeToDatabaseForAdmin(ArrayList<Employee> employees){
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        if (mAuth.getCurrentUser() != null) {
//            DatabaseReference reference = database.getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("employees");
//            Map <String, Object> map = new HashMap<>();
//            for(Employee e : employees) {
//                map.put(e.getId(), e);
//            }
//            reference.updateChildren(map);
//        }
//    }
    public void updateDaysForAdmin(final Employee e){
        final HashMap<String,Object> map = new HashMap<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference().child("users").child(e.getAdminID());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Day day = dataSnapshot.child("employees").child(e.getId())
                        .child("days").child(getDateKey()).getValue(Day.class);
                if (day != null) {
                    map.put(day.getDayKey(), day);
                    reference.updateChildren(map);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void updateDaysForEmployee(final Employee e){
        HashMap<String,Day> tempDays = e.getDays();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(mAuth.getCurrentUser()!=null){
            DatabaseReference reference = database.getReference().child("users");
            e.setDays(null);
            Map<String, Object> map = new HashMap<>();
            map.put(e.getId(), e);
            reference.updateChildren(map);
            map = new HashMap<>();
            e.setDays(tempDays);
            reference = database.getReference().child("users")
                    .child(e.getId())
                    .child("days");
            for(String s : e.getDays().keySet()) {
                map.put(e.getDays().get(s).getDayKey(), e.getDays().get(s));

            }
            reference.updateChildren(map);
            reference = database.getReference().child("users")
                    .child(e.getAdminID())
                    .child("employees")
                    .child(e.getId())
                    .child("days");
            for(String s : e.getDays().keySet()) {
                map.put(e.getDays().get(s).getDayKey(), e.getDays().get(s));

            }
            reference.updateChildren(map);
        }
    }

    public void saveEmployeeToDatabaseForEmployee(Employee employee){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (mAuth.getCurrentUser() != null) {
            DatabaseReference reference = database.getReference().child("users");
            Map<String, Object> map = new HashMap<>();
            map.put(employee.getId(), employee);
            reference.updateChildren(map);
        }
    }
    public void addJobsToDatabase( Employee employee){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (mAuth.getCurrentUser() != null) {
            DatabaseReference reference = database.getReference().child("users").child(employee.getAdminID());
            Map<String, Object> map = new HashMap<>();
            map.put("jobs", employee.getJobs());
            reference.updateChildren(map);
        }

    }
    public void addEmployeeToDatabaseForAdmin(Employee employee){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (mAuth.getCurrentUser() != null) {
            DatabaseReference reference = database.getReference().child("users").child(employee.getAdminID()).child("employees");
            Map<String, Object> map = new HashMap<>();
            map.put(employee.getId(), employee);
            reference.updateChildren(map);
        }
    }

    private String getDateKey() {
        Date date = Calendar.getInstance(TimeZone.getDefault()).getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("y_M_d_E", Locale.getDefault());
        return df2.format(date);
    }

}
