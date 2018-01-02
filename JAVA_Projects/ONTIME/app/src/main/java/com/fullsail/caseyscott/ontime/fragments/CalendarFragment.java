// Casey Scott
// FINAL PROJECT - 1712
// CalendarFragment.java

package com.fullsail.caseyscott.ontime.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.helpers.FirebaseDatabaseHelper;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.fullsail.caseyscott.ontime.objects.Job;
import com.fullsail.caseyscott.ontime.services.LocationService;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;


public class CalendarFragment extends Fragment {

    //Interfaxce to handle updating the UI, Reloading the fragments
    public interface UpdateFromCalendarListener{
        void updateFromCalendar(Employee employee);
    }
        private UpdateFromCalendarListener updater;

    //Assign the Interface object a value of the context
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Make sure the Context conforms to the interface
        if(context instanceof UpdateFromCalendarListener){
            updater = (UpdateFromCalendarListener) context;
        }
    }

    public static CalendarFragment newInstance(Employee employee) {
        //Place the passed objects in the fregments bundle
        Bundle args = new Bundle();
        args.putSerializable("employee", employee);
        CalendarFragment fragment = new CalendarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_tablet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            Employee employee;
        if(savedInstanceState!=null){
            employee = (Employee) savedInstanceState.get("employee");
        }
        else {
            if(getArguments() != null) {
                employee = (Employee) getArguments().getSerializable("employee");
            }else{
                employee = new Employee();
            }
        }
        if(getView() != null && getArguments()!= null && getArguments().containsKey("employee")){

                CompactCalendarView calendarView = getView().findViewById(R.id.calendar_view);
                setCalendarAdapter(calendarView, employee);
                calendarView.setListener(setListener(calendarView, getView(), employee));
            }

    }

        //Method that sets up the calendar views listener
    private CompactCalendarView.CompactCalendarViewListener setListener(final CompactCalendarView compactCalendarView, final View v, final Employee employee) {
       //Instantiate the  DatabaseHealper object
        final FirebaseDatabaseHelper databaseHelper = new FirebaseDatabaseHelper();
        //Instantiate the views
        final MaterialEditText jobTitle = v.findViewById(R.id.job_title);
        final MaterialEditText jobDate = v.findViewById(R.id.job_date);
        final MaterialEditText location = v.findViewById(R.id.job_location);
        final MaterialEditText jobNotes = v.findViewById(R.id.job_notes);
        final ImageButton editButton = v.findViewById(R.id.editJob);
        final ImageButton datePicker = v.findViewById(R.id.date_picker);
        final TextView keyView = v.findViewById(R.id.key);
        datePicker.setEnabled(false);
        final ImageButton locationEditButton = v.findViewById(R.id.location_set_button);
        locationEditButton.setEnabled(false);
        //Make the buttons visable
        editButton.setVisibility(View.VISIBLE);
        datePicker.setVisibility(View.VISIBLE);
        locationEditButton.setVisibility(View.VISIBLE);
        View.OnClickListener clickListener = new View.OnClickListener() {
            String jobTitleString, jobNotesString, jobDateString, jobLocationString, key;
            boolean isEditing = true;
            @Override
            public void onClick(View view) {
                //Get the jobs key from the hidden view
                key = keyView.getText().toString();
                switch(view.getId()){
                    //Handle the buttons clicks
                    case R.id.editJob:
                        //Use a bool to determine the current mode
                        if(isEditing) {
                            datePicker.setEnabled(true);
                            locationEditButton.setEnabled(true);
                            setMetsEnabled(jobTitle, jobNotes, location, true);
                            //Set the valuse of the views
                            jobDateString = jobDate.getText().toString();
                            jobTitleString = jobTitle.getText().toString();
                            jobNotesString = jobNotes.getText().toString();
                            jobLocationString = location.getText().toString();
                            //Change the buttons image
                            editButton.setImageDrawable(getResources().getDrawable(R.drawable.checked));
                            isEditing = false;
                        }else{
                            setMetsEnabled(jobTitle, jobNotes, location, false);
                            String jobDateString1 = jobDate.getText().toString();
                            String jobTitleString1 = jobTitle.getText().toString();
                            String jobNotesString1 = jobNotes.getText().toString();
                            String jobLocationString1 = location.getText().toString();
                            datePicker.setEnabled(true);
                            locationEditButton.setEnabled(true);
                            isEditing = true;
                            editButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
                            if(checkForDifferentInput(jobTitleString, jobDateString, jobLocationString,jobNotesString,jobTitleString1, jobDateString1, jobLocationString1,jobNotesString1)){
                                if(employee.getJobs().containsKey(key)){
                                    Log.i(TAG, "Removed the containing job");
                                    employee.getJobs().remove(key);

                                }
                                if (key.equals("") || key == null){
                                    Date date =  Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()).getTime();
                                    key = String.valueOf(date.getTime());
                                }
                                String stringAddress1 = "";
                                String lati_long1 = "";
                                if(isTextAddressType(jobLocationString1)){
                                    stringAddress1 = jobLocationString1;
                                }else {
                                    lati_long1 = jobLocationString1;
                                }
                                //add the job to the employee and save the data to the DB
                                employee.getJobs().put(key, new Job(jobTitleString1, stringAddress1, lati_long1, jobNotesString1, jobDateString1, key));
                                databaseHelper.addJobsToDatabase(employee);
                                setCalendarAdapter(compactCalendarView, employee);
                                updater.updateFromCalendar(employee);
                            }
                        }
                        return;
                    case R.id.date_picker:
                        displayDatePickerDialog(jobDate);
                        return;
                    case R.id.location_set_button:
                        //Set the location
                        //Check for a null context
                        if (getContext() != null) {
                            //Make a dialog to ask the user if they want to set the location using GPS Coordinates
                            final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                            dialog.setTitle(getString(R.string.use_cur_locat));
                            dialog.setMessage(getString(R.string.are_you_sure_use_location));
                            //Set the buttons
                            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Place the current location in the EditText
                                    String locationString = getLatitude() + "," + getLongitude();
                                    location.setText(locationString);
                                    //Close the dialog
                                    dialog.dismiss();
                                }
                            });
                            //Set the Neg button
                            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Close the dialog
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                }
            }
        };
        //Set the listeners of the buttons
        editButton.setOnClickListener(clickListener);
        datePicker.setOnClickListener(clickListener);
        locationEditButton.setOnClickListener(clickListener);
        //Return the calendar listener
        return new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                //Get the events from the calendar date
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                //Check the size
                if (events.size() > 0) {
                    //Make the event a JOB
                    Job job = (Job) events.get(0).getData();
                    if (job != null) {
                        //Set the values of  the job to the views
                        jobTitle.setText(job.getTitle());
                        jobDate.setText(job.getDateCreated());
                        String locationString = "";
                        if(job.getAddress().equals("")){
                            locationString = job.getLocation_lat_long();
                        }else if(job.getLocation_lat_long().equals("")){
                            locationString = job.getAddress();
                        }
                        location.setText(locationString);
                        jobNotes.setText(job.getNotes());
                    }
                }else{
                    //Clear the views if there is no job
                    clearFieldsForCalendar(jobTitle, jobDate, jobNotes, location);
                }
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                TextView dateView = v.findViewById(R.id.date_display_calendar);
                SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd - yyyy", Locale.getDefault());
                String date = sdf.format(dateClicked);
                //set the date clicked as a title of the calendar
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
                        String locationString = "";
                        if(job.getAddress().equals("")){
                            locationString = job.getLocation_lat_long();
                        }else if(job.getLocation_lat_long().equals("")){
                            locationString = job.getAddress();
                        }
                        location.setText(locationString);
                        jobNotes.setText(job.getNotes());
                    }
                }else{
                    clearFieldsForCalendar(jobTitle, jobDate, jobNotes, location);
                }
            }
        };
    }

    private void setCalendarAdapter(CompactCalendarView calendarView, Employee employee){
        Event event;
        calendarView.removeAllEvents();
        if(employee.getJobs() != null) {
            for (String job : employee.getJobs().keySet()) {
                if (employee.getJobs().containsKey(job)) {
                    String[] strings = employee.getJobs().get(job).getDateCreated().split("/");
                    Date date = getDate(Integer.parseInt(strings[2]), Integer.parseInt(strings[0]) - 1, Integer.parseInt(strings[1]));
                    event = new Event(Color.GREEN, date.getTime(), employee.getJobs().get(job));
                    calendarView.addEvent(event, true);
                }
            }
        }
    }


    private boolean isTextAddressType(String locatStr){

        String[] lat_longSplit = locatStr.split("_");
        return lat_longSplit.length != 2;

    }

    //Display the date picker dialog
    private void displayDatePickerDialog(final MaterialEditText met) {

        DatePickerDialog dpd;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if(getContext()!=null) {
                dpd = new DatePickerDialog(getContext());
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

    }

    private void setMetsEnabled(MaterialEditText jobTitle, MaterialEditText jobNotes, MaterialEditText location, boolean bool){
        jobTitle.setEnabled(bool);
        jobNotes.setEnabled(bool);
        location.setEnabled(bool);
    }

    private boolean checkForDifferentInput(String t, String d, String l, String n, String t1, String d1, String l1, String n1) {
        return !(t.equals(t1) && d.equals(d1) && l.equals(l1) && n.equals(n1));
    }

    private void clearFieldsForCalendar(MaterialEditText jobTitle, MaterialEditText jobDate, MaterialEditText jobNotes, MaterialEditText jobLocation) {
        jobDate.setText("");
        jobLocation.setText("");
        jobNotes.setText("");
        jobTitle.setText("");
    }

    private static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(getArguments()!= null && getArguments().containsKey("employee")) {
            Employee employee = (Employee) getArguments().getSerializable("employee");
            outState.putSerializable("employee", employee);
        }
    }

        //Methods to grab the location data
    private double getLongitude() {
        return LocationService.getLocationManager(getContext()).location.getLongitude();
    }

    private double getLatitude() {
        return LocationService.getLocationManager(getContext()).location.getLatitude();
    }
}
