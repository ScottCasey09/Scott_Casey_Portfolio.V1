// Casey Scott
// FINAL PROJECT - 1712
// JobsManagementFragment.java

package com.fullsail.caseyscott.ontime.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.adapters.JobListAdapter;
import com.fullsail.caseyscott.ontime.helpers.FirebaseDatabaseHelper;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.fullsail.caseyscott.ontime.objects.Job;
import com.fullsail.caseyscott.ontime.services.LocationService;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;


public class JobsManagementFragment extends Fragment {

    public interface CalendarListener{
        void updateCalendarForTablet(Employee employee);
    }
        private CalendarListener calendarListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof CalendarListener){
            calendarListener = (CalendarListener) context;
        }
    }

    public static JobsManagementFragment newInstance(Employee employee) {

        Bundle args = new Bundle();
        args.putSerializable("employee", employee);
        JobsManagementFragment fragment = new JobsManagementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tablet_jobs_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getView() != null && getArguments() != null && getArguments().containsKey("employee")) {
            final FirebaseDatabaseHelper databaseHelper = new FirebaseDatabaseHelper();
            final Employee employee = (Employee) getArguments().getSerializable("employee");
            final ListView listView1 = getView().findViewById(android.R.id.list);
            TextView textView1 = new TextView(getContext());
            textView1.setText(R.string.no_jobs);
            textView1.setGravity(View.TEXT_ALIGNMENT_CENTER);
            if (getContext() != null) {
                textView1.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            }
            listView1.setEmptyView(textView1);

            if (employee != null && employee.getJobs() != null) {
                ArrayList<Job> jobs = new ArrayList<>();
                for (String s : employee.getJobs().keySet()) {
                    jobs.add(employee.getJobs().get(s));
                }
                listView1.setAdapter(new JobListAdapter(getContext(), jobs, employee));
            }
            final MaterialEditText jobTitle = getView().findViewById(R.id.job_title_create);
            final MaterialEditText jobDate = getView().findViewById(R.id.job_date);
            final MaterialEditText jobNotes = getView().findViewById(R.id.job_notes);
            final MaterialEditText jobLocation = getView().findViewById(R.id.job_location);
            Button addButton = getView().findViewById(R.id.submit_job);
            ImageButton datePicker = getView().findViewById(R.id.date_picker);
            ImageButton locationSetter = getView().findViewById(R.id.location_set_button);
            locationSetter.setOnClickListener(setClickListener(jobTitle, jobDate, jobLocation, jobNotes, databaseHelper, employee, listView1));
            datePicker.setOnClickListener(setClickListener(jobTitle, jobDate, jobLocation, jobNotes, databaseHelper, employee, listView1));
            addButton.setOnClickListener(setClickListener(jobTitle, jobDate, jobLocation, jobNotes, databaseHelper, employee, listView1));
        }
    }

    private View.OnClickListener setClickListener(final MaterialEditText jobTitle, final MaterialEditText jobDate, final MaterialEditText jobLocation, final MaterialEditText jobNotes,
                                                  final FirebaseDatabaseHelper databaseHelper, final Employee employee, final ListView listView1) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() != null) {


                    switch (view.getId()) {
                        case R.id.date_picker:
                            DatePickerDialog dpd;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                dpd = new DatePickerDialog(getContext());
                                dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                        String dateString = String.valueOf(i1 + 1) + "/" + String.valueOf(i2) + "/" + String.valueOf(i);
                                        jobDate.setText(dateString);
                                    }
                                });
                                dpd.show();
                            }
                            return;
                        case R.id.location_set_button:
                            //TODO: Ask the user if they want to set the address as their current address
                            String locat = getLatitude() + "_" + getLongitude();
                            jobLocation.setText(locat);
                            return;
                        case R.id.submit_job:
                            String titleStr = jobTitle.getText().toString().trim();
                            String locatStr = jobLocation.getText().toString().trim();
                            String dateStr = jobDate.getText().toString().trim();
                            String jobNotesStr = jobNotes.getText().toString().trim();
                            if (checkFields(jobTitle, jobDate, jobLocation)) {
                                if (!titleStr.equals("") && !dateStr.equals("")) {

                                    String stringAddrerss = "";
                                    String lati_long = "";
                                    if (isTextAddressType(locatStr)) {
                                        stringAddrerss = locatStr;
                                    } else {
                                        lati_long = locatStr;
                                    }
                                    if (employee != null && employee.getJobs() != null) {
                                        Date date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime();
                                        String key = String.valueOf(date.getTime());
                                        Job job = new Job(titleStr, stringAddrerss, lati_long, jobNotesStr, dateStr, key);

                                        if (employee.getJobs() == null) {
                                            employee.setJobs(new HashMap<String, Job>());
                                            employee.getJobs().put(key, job);
                                        } else {
                                            employee.getJobs().put(key, job);
                                        }
                                        databaseHelper.addJobsToDatabase(employee);
                                        ArrayList<Job> jobs = new ArrayList<>();
                                        for (String s : employee.getJobs().keySet()) {
                                            jobs.add(employee.getJobs().get(s));
                                        }
                                        calendarListener.updateCalendarForTablet(employee);
                                        listView1.setAdapter(new JobListAdapter(getContext(), jobs, employee));
                                    }
                                }
                            }

                    }
                }

            }
        };
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

    private boolean checkFields(MaterialEditText title, MaterialEditText date, MaterialEditText location) {
        String titleStr = title.getText().toString();
        String dateStr = date.getText().toString();
        String locationStr = location.getText().toString();
        boolean bool = true;
        if (titleStr.trim().equals("")) {
            bool = false;
            Toast.makeText(getContext(), getString(R.string.job_needs_title), Toast.LENGTH_SHORT).show();
        }
        if (dateStr.trim().equals("")) {
            bool = false;
            Toast.makeText(getContext(), getString(R.string.enter_a_date), Toast.LENGTH_SHORT).show();
        }
        if (locationStr.trim().equals("")) {
            bool = false;
            Toast.makeText(getContext(), R.string.jobs_need_locats, Toast.LENGTH_SHORT).show();
        }
        return bool;
    }
}
