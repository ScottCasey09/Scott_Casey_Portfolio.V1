// Casey Scott
// FINAL PROJECT - 1712
// ManagerFragment.java

package com.fullsail.caseyscott.ontime.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fullsail.caseyscott.ontime.adapters.EmployeeListAdapter;
import com.fullsail.caseyscott.ontime.activities.LandingActivity;
import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.helpers.FirebaseDatabaseHelper;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.fullsail.caseyscott.ontime.objects.Job;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ManagerFragment extends android.support.v4.app.Fragment {

    private static final String ARG_PAGE = "ARG_PAGE";
    private static final String ARG_ACCESS = "access";
    private static final String TAG = "ManagerFragment";

    private int mPage;
    private Employee mEmployee;

    public interface UpdateUiListenerInterfaceMan{
        void updateUiManager(Employee employee);
    }
    private UpdateUiListenerInterfaceMan uiUpdater;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof UpdateUiListenerInterfaceMan){
            uiUpdater = (UpdateUiListenerInterfaceMan) context;
        }
    }


    public static ManagerFragment newInstance(int page, String access, Employee employee) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_ACCESS, access);
        args.putSerializable("user", employee);
        ManagerFragment fragment = new ManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
            //access = getArguments().getString(ARG_ACCESS);
            mEmployee = (Employee) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;

            switch (mPage) {
                case 1:
                    view = inflater.inflate(R.layout.crew_layout, container, false);
                    return view;
                case 2:
                    view = inflater.inflate(R.layout.calendar_layout, container, false);
                    return view;
            }
            return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //FirebaseDatabaseHelper helper = new FirebaseDatabaseHelper();
        if (getView() != null) {
            switch (mPage) {
                case 1:
                    TextView today = getView().findViewById(R.id.today);
                    today.setText(getTodaysDate());
                    ListView listView = getView().findViewById(android.R.id.list);
                    TextView textView = new TextView(getContext());
                    textView.setText(R.string.no_emp_list);
                    textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    if(getContext()!=null) {
                        textView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                    }
                    listView.setEmptyView(textView);
                    ArrayList<Employee> employeeArrayList = new ArrayList<>();
                    for (String s : mEmployee.getEmployees().keySet()){
                        employeeArrayList.add(mEmployee.getEmployees().get(s));
                    }
                    if(employeeArrayList.size()>0) {
                        //interfaceAdapter.setAdapter(listView, employees);
                        listView.setAdapter(new EmployeeListAdapter(getActivity(), employeeArrayList));
                    }
                    return;
                case 2:
                    ImageButton locationGoButton = getView().findViewById(R.id.location_set_button);
                    MaterialEditText location = getView().findViewById(R.id.job_location);
                    if (location.getText().toString().trim().equals("")) {
                        locationGoButton.setEnabled(false);
                    }
                    CompactCalendarView calendarView = getView().findViewById(R.id.calendar_view);
                    ArrayList<Job> jobs = new ArrayList<>();
                    for(String s : mEmployee.getJobs().keySet()){
                        jobs.add(mEmployee.getJobs().get(s));
                    }
                    setCalendarAdapter(calendarView, jobs);
                    calendarView.setListener(setListener(calendarView, getView(), locationGoButton, location));
            }
        }
    }



    private void setCalendarAdapter(CompactCalendarView calendarView, ArrayList<Job> jobs) {
        Event event;
        calendarView.removeAllEvents();
        if (jobs != null) {
            for (Job job : jobs) {
                if (job != null) {
                    String[] strings = job.getDateCreated().split("/");
                    Date date = getDate(Integer.parseInt(strings[2]), Integer.parseInt(strings[0]) - 1, Integer.parseInt(strings[1]));
                    event = new Event(Color.GREEN, date.getTime(), job);
                    calendarView.addEvent(event, true);
                }
            }
        }
    }
        private Job  theJob = new Job();
    private CompactCalendarView.CompactCalendarViewListener setListener(final CompactCalendarView compactCalendarView, final View v, ImageButton locationGoButton1, MaterialEditText location) {
        final MaterialEditText jobTitle = v.findViewById(R.id.job_title);
        final MaterialEditText jobDate = v.findViewById(R.id.job_date);
        final MaterialEditText locationMet = location;
        final MaterialEditText jobNotes = v.findViewById(R.id.job_notes);
        setMetsEnabled(jobTitle, jobDate, jobNotes, location);
        final ImageButton locationGoButton = locationGoButton1;
        locationGoButton.setVisibility(View.VISIBLE);
        locationGoButton.setClickable(true);
        locationGoButton.setEnabled(true);
        locationGoButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_add));
        locationGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationMet.getText().toString().equals("")) {
                    Toast.makeText(getContext(), R.string.no_address_found, Toast.LENGTH_SHORT).show();
                } else {
                    String lat_longs = "";
                    String addressQuery = "";
                    if(theJob.getLocation_lat_long() != null) {
                        lat_longs = theJob.getLocation_lat_long().replace("_", ",");
                    }
                    if(theJob.getAddress() != null) {
                        addressQuery = theJob.getAddress().replace(" ", "+");
                    }
                    String query = lat_longs;
                    if(lat_longs.equals("") || lat_longs.equals("0.0,0.0")){
                        query = addressQuery;
                    }
                    // Create a Uri from an intent string. Use the result to create an Intent.
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + query );
                    // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    // Make the Intent explicit by setting the Google Maps package
                    mapIntent.setPackage("com.google.android.apps.maps");
                    // Attempt to start an activity that can handle the Intent
                    startActivity(mapIntent);
                }
            }
        });
        final MaterialEditText addNote = v.findViewById(R.id.add_notes);
        addNote.setVisibility(View.VISIBLE);
        addNote.setEnabled(true);
        final ImageButton add_button = v.findViewById(R.id.add_notes_button);
        add_button.setVisibility(View.VISIBLE);
        add_button.setEnabled(true);
        add_button.setClickable(true);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if(view.getId()==R.id.add_notes_button && mAuth.getCurrentUser()!=null&& theJob!=null){
                    String addedBy = "Added by - "+mEmployee.getEmployees().get(mAuth.getCurrentUser().getUid()).toString();
                    String date = getDateStamp();
                    String finalString = jobNotes.getText().toString()+"\n\n"+date+"\n"+addedBy+"\n"+addNote.getText().toString().trim();
                    jobNotes.setText(finalString);
                    mEmployee.getJobs().get(theJob.getKey()).setNotes(finalString);
                    FirebaseDatabaseHelper helper = new FirebaseDatabaseHelper();
                    helper.saveAdminUserToDatabase(mEmployee);
                    addNote.setText("");
                    uiUpdater.updateUiManager(mEmployee);
                }
            }
        });

        return new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                if (events.size() > 0) {
                    theJob = (Job) events.get(0).getData();

                    if (theJob != null) {
                        jobTitle.setText(theJob.getTitle());
                        jobDate.setText(theJob.getDateCreated());
                        if(theJob.getAddress().equals("")){
                            locationMet.setText(theJob.getLocation_lat_long());
                        }else{
                            locationMet.setText(theJob.getAddress());
                        }

                        if (locationMet.getText().toString().trim().equals("")) {
                            locationGoButton.setEnabled(false);
                        }else{
                            locationGoButton.setEnabled(true);
                            locationGoButton.setClickable(true);
                        }
                        jobNotes.setText(theJob.getNotes());
                    }
                } else {
                    clearFieldsForCalendar(jobTitle, jobDate, jobNotes, locationMet);
                }
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                TextView dateView = v.findViewById(R.id.date_display_calendar);
                SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd - yyyy", Locale.getDefault());
                String date = sdf.format(dateClicked);
                dateView.setText(date);
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                TextView dateView = v.findViewById(R.id.date_display_calendar);
                SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd - yyyy", Locale.getDefault());
                String date = sdf.format(firstDayOfNewMonth);
                dateView.setText(date);
                List<Event> events = compactCalendarView.getEvents(firstDayOfNewMonth);
                if (events.size() > 0) {
                    Job job = (Job) events.get(0).getData();

                    if (job != null) {
                        jobTitle.setText(job.getTitle());
                        jobDate.setText(job.getDateCreated());
                        if(job.getAddress().equals("")){
                            locationMet.setText(job.getLocation_lat_long());
                        }else{
                            locationMet.setText(job.getAddress());
                        }

                        if (locationMet.getText().toString().trim().equals("")) {
                            locationGoButton.setEnabled(false);
                        }else{
                            locationGoButton.setEnabled(true);
                            locationGoButton.setClickable(true);
                        }
                        jobNotes.setText(job.getNotes());
                    }
                } else {
                    clearFieldsForCalendar(jobTitle, jobDate, jobNotes, locationMet);
                }
            }
        };
    }

    private static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private void setMetsEnabled(MaterialEditText jobTitle, MaterialEditText jobDate, MaterialEditText jobNotes, MaterialEditText location) {
        jobTitle.setEnabled(false);
        jobDate.setEnabled(false);
        jobNotes.setEnabled(false);
        location.setEnabled(false);
    }

            private void clearFieldsForCalendar(MaterialEditText jobTitle, MaterialEditText jobDate, MaterialEditText jobNotes, MaterialEditText jobLocation) {
        jobDate.setText("");
        jobLocation.setText("");
        jobNotes.setText("");
        jobTitle.setText("");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.employee_options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.signout_action) {
                    presentDialogForSignOut();
                    return true;
                }

        return super.onOptionsItemSelected(item);
    }
    private void presentDialogForSignOut(){
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
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

    private String getTodaysDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd - yyyy", Locale.getDefault());
        Date date = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()).getTime();
        return sdf.format(date);
    }
    private String getDateStamp() {
        Date date = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()).getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("E-M-dd h:mma", Locale.getDefault());
        return df2.format(date);
    }

}


