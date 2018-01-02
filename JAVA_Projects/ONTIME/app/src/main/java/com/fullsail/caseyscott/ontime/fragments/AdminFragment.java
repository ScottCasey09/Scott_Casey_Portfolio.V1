// Casey Scott
// FINAL PROJECT - 1712
// AdminFragment.java

package com.fullsail.caseyscott.ontime.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fullsail.caseyscott.ontime.activities.MainActivity;
import com.fullsail.caseyscott.ontime.adapters.EmployeeListAdapter;
import com.fullsail.caseyscott.ontime.helpers.FirebaseDatabaseHelper;
import com.fullsail.caseyscott.ontime.adapters.JobListAdapter;
import com.fullsail.caseyscott.ontime.activities.LandingActivity;
import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.objects.Day;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.fullsail.caseyscott.ontime.objects.Job;
import com.fullsail.caseyscott.ontime.services.LocationService;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class AdminFragment extends android.support.v4.app.Fragment {


    //Interface that helps with updating the fragments if they need to be reloaded
    public interface UpdateFragmentsListener {
        //Interface methods
        void updateFragments(Employee employee, String access);
        void goToEmployeeInfo(Employee employee, String access, Employee admin);
    }
//Interface object reference
    private UpdateFragmentsListener fragUpdater;

    //Attach the interface to the context
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Make sure the activity implements the interface methods
        if (context instanceof UpdateFragmentsListener) {
            fragUpdater = (UpdateFragmentsListener) context;
        }
    }
    //keys and Action strings + tag
    private static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_ACCESS = "access";
    private static final String TAG = "AdminFragment";
    private static final String ACTION_HOURS = "com.android.fullsail.caseyscott.ontime.ACTION_HOURS";

    private int mPage;
    private String mAccess;
    //Variables // these variables are not good practice for fragments but it works
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
    private CompactCalendarView calendarView;

    //Fragment constructor / instance method
    public static AdminFragment newInstance(int page, String access, Employee employee, ArrayList<Employee> failedEmployees) {
        Bundle args = new Bundle();
        //Place the passed objects into the fragments bundle
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_ACCESS, access);
        args.putSerializable("user", employee);
        args.putSerializable("failed", failedEmployees);
        AdminFragment fragment = new AdminFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This fragment has options
        setHasOptionsMenu(true);
        //Get reference to the FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //Check to see if the view has already been loaded
        if (savedInstanceState == null) {
            if (getArguments() != null) {
                mPage = getArguments().getInt(ARG_PAGE);
                mAccess = getArguments().getString(ARG_ACCESS);
                employee = (Employee) getArguments().getSerializable("user");

            }

        } else {
            //Get the objects from the intent
            employee = (Employee) savedInstanceState.getSerializable("employee");
            mAccess = savedInstanceState.getString("access");
            mPage = savedInstanceState.getInt("page");
        }
        //Reference to the DatabaseHealper class to save data
        databaseHelper = new FirebaseDatabaseHelper();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        String admin = "admin";
        //Set up the views by page
        if (Objects.equals(mAccess, admin)) {
            switch (mPage) {
                case 1:
                    view = inflater.inflate(R.layout.crew_layout, container, false);
                    return view;
                case 2:
                    view = inflater.inflate(R.layout.calendar_layout, container, false);
                    return view;
                case 3:
                    view = inflater.inflate(R.layout.job_list, container, false);
                    return view;
            }
            return null;
        }
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Check for a null view
        if (getView() != null) {
            ImageView imageView;
            //Set up by page
            switch (mPage) {
                case 1:
                    //Get references to the views and assign click listeners and values
                    TextView today = getView().findViewById(R.id.today);
                    today.setText(getTodaysDate());
                    imageView = getView().findViewById(R.id.company_logo);
                    //Check to make sure the object has value of Picasso will not agree, Causes Crash
                    if (employee != null) {
                        if (employee.getImageUrl() != null) {
                            if (!employee.getImageUrl().equals("")) {
                                Picasso.with(getContext()).load(employee.getImageUrl()).into(imageView);
                            }
                        }
                    }
                    //Build the empty view for the listView
                    ListView listView = getView().findViewById(android.R.id.list);
                    TextView textView = new TextView(getContext());
                    textView.setText(R.string.no_emp_list);
                    textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    //Set the color of the text, must check the context to avoid warning
                    if (getContext() != null) {
                        textView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                    }
                    listView.setEmptyView(textView);
                    //Instantiate a new ArrayList Opject
                    ArrayList<Employee> employees = new ArrayList<>();
                    //Check for nulls then get the emploee object
                    if (employee == null && getArguments() != null && getArguments().containsKey("user")) {
                        employee = (Employee) getArguments().getSerializable("user");
                    }
                    //Check for null values so the aap avoids a null exception
                    if (employee != null && employee.getEmployees() != null) {
                        //Iterate through the Keys and add them to the list because the adapter take an ArrayList
                        for (String s : employee.getEmployees().keySet()) {
                            employees.add(employee.getEmployees().get(s));
                        }
                    }
                    //Check to see is the employees array has value
                    if (employees.size() > 0) {
                        //Set the adapter to the listView
                        listView.setAdapter(new EmployeeListAdapter(getActivity(), employees));
                    }
                    //Set the click listener for the listView
                    listView.setOnItemLongClickListener(onItemLongClickListener);
                    return;
                case 2:
                    //Get references to the views and assign click listeners and values
                    calendarView = getView().findViewById(R.id.calendar_view);
                    calendarView.setFirstDayOfWeek(Calendar.getInstance(Locale.US).getFirstDayOfWeek());
                    //Set the adapter and the listener for the  calendar
                    setCalendarAdapter();
                    calendarView.setListener(setListener(calendarView, getView()));

                    return;
                case 3:
                    //Get references to the views and assign click listeners and values
                    final ListView listView1 = getView().findViewById(android.R.id.list);
                    // Build the TextView for the empty list
                    TextView textView1 = new TextView(getContext());
                    textView1.setText(R.string.no_jobs);
                    textView1.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    if (getContext() != null) {
                        textView1.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                    }
                    //Set the views empty display
                    listView1.setEmptyView(textView1);
                    //Instantiate the floating action button
                    FloatingActionButton fab = getView().findViewById(R.id.fab);
                    //Set the on clock listener for the FAB
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showAddJobDialog(listView1);
                        }
                    });
                    //Get a reference to the image view
                    imageView = getView().findViewById(R.id.company_logo);
                    //Make sur the value is not empty
                    if (employee != null) {
                        if (employee.getImageUrl() != null) {
                            if (!employee.getImageUrl().equals("")) {
                                //Put the image in the view
                                Picasso.with(getContext()).load(employee.getImageUrl()).into(imageView);
                            }
                        }

                        if (employee.getJobs() != null) {
                            //Instantiate the list to hold the jobs
                            ArrayList<Job> jobArrayList = new ArrayList<>();
                            for (String s : employee.getJobs().keySet()) {
                                //Add the job to the list
                                jobArrayList.add(employee.getJobs().get(s));
                            }
                            //set the adapter to give the list data
                            listView1.setAdapter(new JobListAdapter(getContext(), jobArrayList, employee));
                        }
                    }
            }
        }
    }

    //Click listener for the listView (long press)
    private final AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            fragUpdater.goToEmployeeInfo((Employee) adapterView.getAdapter().getItem(i), employee.getAccount_type(), employee);
            return false;
        }
    };

    //Custom method to setup the calendar views events
    private void setCalendarAdapter() {
        Event event;
        calendarView.removeAllEvents();
        if (employee.getJobs() != null) {
            //Iterate through the jobs
            for (String job : employee.getJobs().keySet()) {
                //Check for match
                if (employee.getJobs().containsKey(job)) {
                    //Build events to the calendar
                    String[] strings = employee.getJobs().get(job).getDateCreated().split("/");
                    Date date = getDate(Integer.parseInt(strings[2]), Integer.parseInt(strings[0]) - 1, Integer.parseInt(strings[1]));
                    event = new Event(Color.GREEN, date.getTime(), employee.getJobs().get(job));
                    //Add the event
                    calendarView.addEvent(event, true);
                }
            }
        }
    }
    //Custom method to set the listeners for the Compact calendar View
    private CompactCalendarView.CompactCalendarViewListener setListener(final CompactCalendarView compactCalendarView, final View v) {
        //Instantiate the views in the Calendar view screen
        final MaterialEditText jobTitle = v.findViewById(R.id.job_title);
        final MaterialEditText jobDate = v.findViewById(R.id.job_date);
        final MaterialEditText location = v.findViewById(R.id.job_location);
        final MaterialEditText jobNotes = v.findViewById(R.id.job_notes);
        final TextView keyView = v.findViewById(R.id.key);
        final ImageButton editButton = v.findViewById(R.id.editJob);
        final ImageButton datePicker = v.findViewById(R.id.date_picker);
        final ImageButton locationEditButton = v.findViewById(R.id.location_set_button);
        //IUnhide the action buttons
        editButton.setVisibility(View.VISIBLE);
        datePicker.setVisibility(View.VISIBLE);
        locationEditButton.setVisibility(View.VISIBLE);
        @SuppressWarnings("unused") View.OnClickListener clickListener = new View.OnClickListener() {
            String jobTitleString, jobNotesString, jobDateString, jobLocationString, key;
            boolean isEditing = true;

            @Override
            public void onClick(View view) {
                //Get a reference to the  hidden key
                key = keyView.getText().toString();
                //React to which view is selected
                switch (view.getId()) {
                    case R.id.editJob:
                        if (isEditing) {
                            //Enabel the views
                            datePicker.setEnabled(true);
                            locationEditButton.setEnabled(true);
                            setMetsEnabled(jobTitle, jobNotes, location, true);
                            //Set the text to the views
                            jobDateString = jobDate.getText().toString();
                            jobTitleString = jobTitle.getText().toString();
                            jobNotesString = jobNotes.getText().toString();
                            jobLocationString = location.getText().toString();
                            //Change the editButtons image
                            editButton.setImageDrawable(getResources().getDrawable(R.drawable.checked));
                            isEditing = false;
                        } else {
                            //Disable the views to prevent editing
                            setMetsEnabled(jobTitle, jobNotes, location, false);
                            //Set the text to the views
                            String jobDateString1 = jobDate.getText().toString();
                            String jobTitleString1 = jobTitle.getText().toString();
                            String jobNotesString1 = jobNotes.getText().toString();
                            String jobLocationString1 = location.getText().toString();
                            datePicker.setEnabled(true);
                            locationEditButton.setEnabled(true);
                            isEditing = true;
                            //Change the editButtons image
                            editButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
                            //Check to see if the user made any changes
                            if (checkForDifferentInput(jobTitleString, jobDateString, jobLocationString, jobNotesString, jobTitleString1, jobDateString1, jobLocationString1, jobNotesString1)) {
                                if (employee.getJobs().containsKey(key)) {
                                    Log.i(TAG, "Removed the containing job");
                                    //Remove the job from the HashMap
                                    employee.getJobs().remove(key);
                                }
                                //Check for a null key, if it is null assign it a new value
                                if (key.equals("") || key == null) {
                                    Date date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime();
                                    key = String.valueOf(date.getTime());
                                }
                                String stringAddress1 = "";
                                String lati_long1 = "";
                                //Figure out if the address is coordinates or text address
                                if (isTextAddressType(jobLocationString1)) {
                                    stringAddress1 = jobLocationString1;
                                } else {
                                    lati_long1 = jobLocationString1;
                                }
                                //Make the new job and add it to the employee
                                employee.getJobs().put(key, new Job(jobTitleString1, stringAddress1, lati_long1, jobNotesString1, jobDateString1, key));
                                //Save the list to the DB
                                databaseHelper.addJobsToDatabase(employee);
                                //Reset the calendar adapter to refelct the changes
                                setCalendarAdapter();
                            }
                        }
                        return;
                    case R.id.date_picker:
                        //Display the datePicker
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
                                    String locationString = getLatitude()+","+getLongitude();
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
        //Set the click listeners for the buttons
        editButton.setOnClickListener(clickListener);
        datePicker.setOnClickListener(clickListener);
        locationEditButton.setOnClickListener(clickListener);
        //Return the Calendar listener
        return new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                //Set the values if the day contains a job
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                //Check to see if there is an event to view
                if (events.size() > 0) {
                    //Set the event as a Job object
                    Job job = (Job) events.get(0).getData();
                    //See if the job is null
                    if (job != null) {
                        //Use the jobs values to fill the views
                        jobTitle.setText(job.getTitle());
                        jobDate.setText(job.getDateCreated());
                        keyView.setText(job.getKey());
                        String locationString = "";
                        //Figure out if the location is coordinates or text address
                        if (job.getAddress().equals("")) {
                            locationString = job.getLocation_lat_long();
                        } else if (job.getLocation_lat_long().equals("")) {
                            locationString = job.getAddress();
                        }
                        //Set the value
                        location.setText(locationString);
                        jobNotes.setText(job.getNotes());

                    }
                } else {
                    //if no job clear the views
                    clearFieldsForCalendar(jobTitle, jobDate, jobNotes, location);
                }
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                TextView dateView = v.findViewById(R.id.date_display_calendar);
                //Set the date header
                SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd - yyyy", Locale.getDefault());
                String date = sdf.format(dateClicked);
                dateView.setText(date);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                //When the user scrolls to a new month set the data and views
                TextView dateView = v.findViewById(R.id.date_display_calendar);
                //Set the date header
                SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd - yyyy", Locale.getDefault());
                String date = sdf.format(firstDayOfNewMonth);
                dateView.setText(date);
                //Get the events
                List<Event> events = compactCalendarView.getEvents(firstDayOfNewMonth);
                //See if there are events
                if (events.size() > 0) {
                    //Turn the event object into a job object
                    Job job = (Job) events.get(0).getData();
                    //Check for null
                    if (job != null) {
                        //Assign the values of the job to the views
                        jobTitle.setText(job.getTitle());
                        jobDate.setText(job.getDateCreated());
                        keyView.setText(job.getKey());
                        String locationString = "";
                        //Check address type
                        if (job.getAddress().equals("")) {
                            locationString = job.getLocation_lat_long();
                        } else if (job.getLocation_lat_long().equals("")) {
                            locationString = job.getAddress();
                        }
                        //Assign the values
                        location.setText(locationString);
                        jobNotes.setText(job.getNotes());
                    }
                } else {
                    //Clear the fields in the event of an empty day
                    clearFieldsForCalendar(jobTitle, jobDate, jobNotes, location);
                }
            }
        };
    }

    //Method to  enable and disable the views
    private void setMetsEnabled(MaterialEditText jobTitle, MaterialEditText jobNotes, MaterialEditText location, boolean bool) {
        jobTitle.setEnabled(bool);
        jobNotes.setEnabled(bool);
        location.setEnabled(bool);
    }
        //Method to check for values change
    private boolean checkForDifferentInput(String t, String d, String l, String n, String t1, String d1, String l1, String n1) {
        return !(t.equals(t1) && d.equals(d1) && l.equals(l1) && n.equals(n1));
    }
        //Method to clear the Views
    private void clearFieldsForCalendar(MaterialEditText jobTitle, MaterialEditText jobDate, MaterialEditText jobNotes, MaterialEditText jobLocation) {
        jobDate.setText("");
        jobLocation.setText("");
        jobNotes.setText("");
        jobTitle.setText("");
    }
        //Method to get the date String
    private static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //Inflate the options by PAGE
        switch (mPage) {
            case 1:
                inflater.inflate(R.menu.add_employee, menu);
                return;
            case 2:
                inflater.inflate(R.menu.general_menu, menu);
                return;
            case 3:
                inflater.inflate(R.menu.general_menu, menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Figure which button has been clicked
        switch (item.getItemId()) {
            case R.id.add_emp_action:
                showAddEmpDialog();
                return true;
            case R.id.signout_action:
                presentDialogForSignOut();
                return true;
            case R.id.check_hours:
                //Make sur the object is not null
                if (employee.getEmployees().size() > 0 && employee.getEmployees() != null) {
                    //Go to the hours activity and pass the employee object
                    Intent intent1 = new Intent(ACTION_HOURS);
                    intent1.putExtra("employee", employee);
                    startActivity(intent1);
                } else {
                    //If no employees the Admin must add some, inform the user
                    Toast.makeText(getContext(), R.string.add_emp_to_use, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.account_settings:
                Intent settingsIntent = new Intent(MainActivity.ACTION_SETTINGS);
                settingsIntent.putExtra("employee", employee);
                startActivity(settingsIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Method to handle the add job gialog
    private void showAddJobDialog(final ListView listView) {
        if (getContext() != null) {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.job_creation_layout);
            //Find the views and instantiate them
            final MaterialEditText title = dialog.findViewById(R.id.job_title_create);
            final MaterialEditText date = dialog.findViewById(R.id.job_date);
            date.setEnabled(false);
            final MaterialEditText location_met = dialog.findViewById(R.id.job_location);
            final MaterialEditText notes = dialog.findViewById(R.id.job_notes);
            ImageButton datePickerButton = dialog.findViewById(R.id.date_picker);
            ImageButton locationButton = dialog.findViewById(R.id.location_set_button);
            Button addButton = dialog.findViewById(R.id.submit_job);
            Button cancelButton = dialog.findViewById(R.id.cancel);
            //Click listener for the buttons
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.date_picker:
                            DatePickerDialog dpd;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                dpd = new DatePickerDialog(getContext());
                                dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                        String dateString = String.valueOf(i1 + 1) + "/" + String.valueOf(i2) + "/" + String.valueOf(i);
                                        date.setText(dateString);
                                    }
                                });
                                dpd.show();
                            }
                            return;
                        case R.id.location_set_button:
                            String locat = getLatitude() + "_" + getLongitude();
                            location_met.setText(locat);
                            return;
                        case R.id.submit_job:
                            String titleStr = title.getText().toString().trim();
                            String locatStr = location_met.getText().toString().trim();
                            String dateStr = date.getText().toString().trim();
                            String jobNotesStr = notes.getText().toString().trim();
                            if (!titleStr.equals("") && !dateStr.equals("")) {
                                String stringAddress = "";
                                String lati_long = "";
                                if (isTextAddressType(locatStr)) {
                                    stringAddress = locatStr;
                                } else {
                                    lati_long = locatStr;
                                }
                                Date date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime();
                                String key = String.valueOf(date.getTime());
                                Job job = new Job(titleStr, stringAddress, lati_long, jobNotesStr, dateStr, key);

                                if (employee.getJobs() == null) {
                                    employee.setJobs(new HashMap<String, Job>());
                                    employee.getJobs().put(key, job);
                                } else {
                                    employee.getJobs().put(key, job);
                                }
                                databaseHelper.addJobsToDatabase(employee);
                                dialog.dismiss();
                                ArrayList<Job> jobs = new ArrayList<>();
                                for (String s : employee.getJobs().keySet()) {
                                    jobs.add(employee.getJobs().get(s));
                                }
                                listView.setAdapter(new JobListAdapter(getContext(), jobs, employee));
                                fragUpdater.updateFragments(employee, employee.getAccount_type());
                            }
                            return;
                        case R.id.cancel:
                            dialog.dismiss();
                    }
                }
            };
            datePickerButton.setOnClickListener(onClickListener);
            locationButton.setOnClickListener(onClickListener);
            addButton.setOnClickListener(onClickListener);
            cancelButton.setOnClickListener(onClickListener);
            dialog.show();
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            }
        }

    }

    private double getLongitude() {
        return LocationService.getLocationManager(getContext()).location.getLongitude();
    }

    private double getLatitude() {
        return LocationService.getLocationManager(getContext()).location.getLatitude();
    }

    private boolean isTextAddressType(String locatStr) {

        String[] lat_longSplit = locatStr.split("_");
        return lat_longSplit.length != 2;

    }

    private void showAddEmpDialog() {
        //Check for null activity
        if (getActivity() != null) {
            //Build the custom dialog
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.add_employee_dialog);
            //Find the views
            Button add = dialog.findViewById(R.id.add_emp_button_create);
            fName = dialog.findViewById(R.id.emp_firstname_create);
            lName = dialog.findViewById(R.id.emp_lastname_create);
            dob = dialog.findViewById(R.id.emp_dob_create);
            dob.setEnabled(false);
            ImageButton datePicker = dialog.findViewById(R.id.datePicker);
            //Set datePicker click listener
            datePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Display the date picker
                    displayDatePickerDialog(dob);
                }
            });
            //Find the views
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
                                Date time = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()).getTime();
                                //Build the new employee object
                                Employee e = new Employee(empEmailStr, empfName, emplName, empPass, null, employee.getCompany(), getAccountType(manager), time.getTime(), null, new HashMap<String, Job>(), new HashMap<String, Employee>(), employee.getAdminID(), new HashMap<String, Day>(), empDob, 8);
                                //Create the user in fire base Auth
                                createFirebaseUsers(empEmailStr, empPass, e, dialog);
                            }
                        }
                    }
                }
            });
            dialog.show();
        }
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
            Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            bool = false;
        }
        if (!checkValidEmail(empEmailStr)) {
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
            Toast.makeText(getActivity(), "Not proper email address", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isCreatingNewUser;

    //Create new Firebase Users
    private void createFirebaseUsers(final String email, final String password, final Employee e, final Dialog dialog) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Adding employee "+e.toString());
        progressDialog.show();
        if (getActivity() != null) {

            isCreatingNewUser = true;

            secondAuth = FirebaseAuth.getInstance();

            secondAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String adminId = employee.getId();
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                if (isCreatingNewUser) {
                                    Date date = Calendar.getInstance(TimeZone.getDefault()).getTime();
                                    if (secondAuth.getCurrentUser() != null) {
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
                                Toast.makeText(getActivity(), "Failed to create user.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    private void signInNewUser(String email, String password) {
        if (getActivity() != null) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                if (!isCreatingNewUser) {
                                    if (getView() != null) {
                                        ArrayList<Employee> employees = new ArrayList<>();

                                        //TODO: this was changed said add(employee) but s was never used???????
                                        for (String s : employee.getEmployees().keySet()) {
                                            employees.add(employee.getEmployees().get(s));
                                        }
                                        ListView listView = getView().findViewById(android.R.id.list);
                                        listView.setAdapter(new EmployeeListAdapter(getContext(), employees));
                                        progressDialog.dismiss();
                                    }
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
    }

    private String getAccountType(CheckBox manager) {
        if (manager.isChecked()) {
            return "manager";
        } else {
            return "employee";
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("employee", employee);
        outState.putString("access", mAccess);
        outState.putInt("page", mPage);
    }

    //Display the date picker dialog
    private void displayDatePickerDialog(final MaterialEditText met) {

        DatePickerDialog dpd;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (getContext() != null) {
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

    private void presentDialogForSignOut() {
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

    private String getTodaysDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd - yyyy", Locale.getDefault());
        Date date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime();
        return sdf.format(date);
    }

}
