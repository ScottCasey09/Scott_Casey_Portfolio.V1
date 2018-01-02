// Casey Scott
// FINAL PROJECT - 1712
// EmployeeFragment.java

package com.fullsail.caseyscott.ontime.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fullsail.caseyscott.ontime.activities.LandingActivity;
import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.adapters.EmployeesHoursHistoryAdapter;
import com.fullsail.caseyscott.ontime.helpers.FirebaseDatabaseHelper;
import com.fullsail.caseyscott.ontime.objects.Day;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.fullsail.caseyscott.ontime.objects.Job;
import com.fullsail.caseyscott.ontime.objects.Week;
import com.fullsail.caseyscott.ontime.services.LocationService;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class EmployeeFragment extends Fragment {
    private static final String ARG_PAGE = "ARG_PAGE";
    private static final String ARG_ACCESS = "access";
    private static final String TAG = "EmployeeFragment ";

    private int mPage;
    private Employee mEmployee;

    public interface UpdateUiListenerInterface{
        void updateUiEmployee(Employee employee, String access, int page);
    }
    private UpdateUiListenerInterface uiUpdater;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof UpdateUiListenerInterface){
            uiUpdater = (UpdateUiListenerInterface) context;
        }
    }

    public static EmployeeFragment newInstance(int page, String access, Employee employee) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable("user", employee);
        args.putString(ARG_ACCESS, access);
        EmployeeFragment fragment = new EmployeeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
            mEmployee = (Employee) getArguments().getSerializable("user");
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        switch (mPage) {
            case 1:
                view = inflater.inflate(R.layout.employee_my_stuff, container, false);
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
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        if (getView() != null) {
            switch (mPage) {
                case 1:

                    final ArrayList<Week> weeks = getWeeksData();
                    final ListView listView = getView().findViewById(R.id.employee_hours_history_myStuff);
                    listView.setAdapter(new EmployeesHoursHistoryAdapter(getContext(), weeks));
                    TextView sunday = getView().findViewById(R.id.sunday);
                    TextView monday = getView().findViewById(R.id.monday);
                    TextView tuesday = getView().findViewById(R.id.tuesday);
                    TextView wednesday = getView().findViewById(R.id.wednesday);
                    TextView thursday = getView().findViewById(R.id.thursday);
                    TextView friday = getView().findViewById(R.id.friday);
                    TextView saturday = getView().findViewById(R.id.saturday);
                    TextView total = getView().findViewById(R.id.total);
                    TextView title = getView().findViewById(R.id.range_title);
                    final TextView inTime = getView().findViewById(R.id.clockInTime);
                    final TextView outTime = getView().findViewById(R.id.clockOutTime);
                    final LinearLayout inBox = getView().findViewById(R.id.inBox);
                    final LinearLayout outBox = getView().findViewById(R.id.outBox);
                    setTimeViews(auth, inTime, outTime, inBox, outBox);
                    sunday.setText(parseTimeStringToFormat(weeks.get(0).getSunday()));
                    monday.setText(parseTimeStringToFormat(weeks.get(0).getMonday()));
                    tuesday.setText(parseTimeStringToFormat(weeks.get(0).getTuesday()));
                    wednesday.setText(parseTimeStringToFormat(weeks.get(0).getWednesday()));
                    thursday.setText(parseTimeStringToFormat(weeks.get(0).getThursday()));
                    friday.setText(parseTimeStringToFormat(weeks.get(0).getFriday()));
                    saturday.setText(parseTimeStringToFormat(weeks.get(0).getSaturday()));
                    String totalStr = "Total: "+parseTimeStringToFormat(weeks.get(0).getTotal());
                    total.setText(totalStr);
                    ImageButton clock = getView().findViewById(R.id.clock_clocking);
                    clock.setOnClickListener(new View.OnClickListener() {
                        private boolean isChangingBox;
                        private final FirebaseDatabaseHelper helper = new FirebaseDatabaseHelper();
                        @Override
                        public void onClick(View view) {
                            if (getContext() != null && auth.getCurrentUser()!=null) {
                                final Dialog dialog = new Dialog(getContext());
                                final Employee employee = mEmployee.getEmployees().get(auth.getCurrentUser().getUid());
                                dialog.setContentView(R.layout.clock_employee_dialog);
                                TextView title = dialog.findViewById(R.id.name_clocking_dialog);
                                title.setText(employee.toString());
                                final CheckBox in_checkBox = dialog.findViewById(R.id.in_checkBox);
                                final CheckBox out_checkBox = dialog.findViewById(R.id.out_checkBox);
                                final CheckBox break_start = dialog.findViewById(R.id.breakStart_checkBox);
                                final CheckBox break_end = dialog.findViewById(R.id.breakEnd_checkBox);
                                in_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if (b) {
                                            isChangingBox = true;
                                        }
                                        if (isChangingBox) {
                                            in_checkBox.setChecked(b);
                                            isChangingBox = false;
                                            out_checkBox.setChecked(false);
                                            break_end.setChecked(false);
                                            break_start.setChecked(false);

                                        }
                                    }
                                });
                                out_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if (b) {
                                            isChangingBox = true;
                                        }
                                        if (isChangingBox) {
                                            out_checkBox.setChecked(b);
                                            isChangingBox = false;
                                            in_checkBox.setChecked(false);
                                            break_end.setChecked(false);
                                            break_start.setChecked(false);
                                        }
                                    }
                                });
                                break_start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if (b) {
                                            isChangingBox = true;
                                        }
                                        if (isChangingBox) {
                                            break_start.setChecked(b);
                                            isChangingBox = false;
                                            break_end.setChecked(false);
                                            in_checkBox.setChecked(false);
                                            out_checkBox.setChecked(false);
                                        }
                                    }
                                });
                                break_end.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if (b) {
                                            isChangingBox = true;
                                        }
                                        if (isChangingBox) {
                                            break_end.setChecked(b);
                                            isChangingBox = false;
                                            break_start.setChecked(false);
                                            in_checkBox.setChecked(false);
                                            out_checkBox.setChecked(false);
                                        }
                                    }
                                });
                                dialog.show();

                                final Button cancelButton = dialog.findViewById(R.id.cancel_clocking);
                                cancelButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                                final Button clockButton = dialog.findViewById(R.id.clock_clocking);
                                clockButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        int box = 0;
                                        if (in_checkBox.isChecked()) {
                                            box = 1;
                                        } else if (out_checkBox.isChecked()) {
                                            box = 2;
                                        } else if (break_end.isChecked()) {
                                            box = 4;
                                        } else if (break_start.isChecked()) {
                                            box = 3;
                                        }
                                        switch (box) {
                                            case 1:
                                                if (employee.getDays() == null || !employee.getDays().containsKey(getDateKey())) {
                                                    addNewDayAndClockIn(employee, dialog);
                                                    inTime.setText(getCurrentTime());
                                                    listView.setAdapter(new EmployeesHoursHistoryAdapter(getContext(), getWeeksData()));
                                                    listView.setSelection(0);
                                                    setUpTimer();
                                                    setTimeViews(auth, inTime, outTime, inBox, outBox);
                                                    return;
                                                } else {
                                                    Day day = employee.getDays().get(getDateKey());
                                                    if (day.getClockInTime() == 0) {
                                                        setNewClockInTimeForDay(employee, dialog, day);
                                                        inTime.setText(getCurrentTime());
                                                        listView.setAdapter(new EmployeesHoursHistoryAdapter(getContext(), getWeeksData()));
                                                        listView.setSelection(0);
                                                        setUpTimer();
                                                        setTimeViews(auth, inTime, outTime, inBox, outBox);
                                                        return;
                                                    } else {
                                                        Toast.makeText(getContext(), "Already clocked in", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                }
                                            case 2:
                                                if (employee.getDays() == null || !employee.getDays().containsKey(getDateKey())) {
                                                    Toast.makeText(getContext(), "Must start a day first", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Day day = employee.getDays().get(getDateKey());
                                                    if (day.getClockOutTime() == 0) {
                                                        employee.getDays().remove(getDateKey());
                                                        day.setClockOutTime(getDateTime());
                                                        day.setClockOutLatitude(getLatitude());
                                                        day.setClockOutLongitude(getLongitude());
                                                        day.setHours(getTotalHours(day.getClockInTime(), day.getClockOutTime(), day.getBreakStartTime(), day.getBreakStopTime()));
                                                        HashMap<String, Day> map = employee.getDays();
                                                        map.put(getDateKey(), day);
                                                        employee.setDays(map);
                                                        helper.updateDaysForEmployee(employee);
                                                        helper.updateDaysForAdmin(employee);
                                                        dialog.dismiss();
                                                        outTime.setText(getCurrentTime());
                                                        listView.setAdapter(new EmployeesHoursHistoryAdapter(getContext(), getWeeksData()));
                                                        listView.setSelection(0);
                                                        uiUpdater.updateUiEmployee(employee, employee.getAccount_type(), 0);
                                                        setTimeViews(auth, inTime, outTime, inBox, outBox);
                                                    }
                                                }
                                                return;
                                            case 3:
                                                if (employee.getDays() != null && employee.getDays().containsKey(getDateKey())
                                                        && employee.getDays().get(getDateKey()).getBreakStartTime() == 0
                                                        && employee.getDays().get(getDateKey()).getBreakStopTime() == 0) {
                                                    Day day = employee.getDays().get(getDateKey());
                                                    employee.getDays().remove(getDateKey());
                                                    day.setBreakStartTime(getDateTime());
                                                    HashMap<String, Day> map = employee.getDays();
                                                    map.put(getDateKey(), day);
                                                    employee.setDays(map);
                                                    helper.updateDaysForEmployee(employee);
                                                    helper.updateDaysForAdmin(employee);
                                                    dialog.dismiss();
                                                    setTimeViews(auth, inTime, outTime, inBox, outBox);
                                                    return;

                                                } else {
                                                    Toast.makeText(getContext(), "Can't go on break", Toast.LENGTH_SHORT).show();
                                                }
                                                return;
                                            case 4:
                                                if (employee.getDays() != null && employee.getDays().containsKey(getDateKey())
                                                        && employee.getDays().get(getDateKey()).getBreakStartTime() != 0
                                                        && employee.getDays().get(getDateKey()).getBreakStopTime() == 0) {
                                                    Day day = employee.getDays().get(getDateKey());
                                                    employee.getDays().remove(getDateKey());
                                                    day.setBreakStopTime(getDateTime());

                                                    HashMap<String, Day> map = employee.getDays();
                                                    map.put(getDateKey(), day);
                                                    employee.setDays(map);
                                                    helper.updateDaysForEmployee(employee);
                                                    helper.updateDaysForAdmin(employee);
                                                    dialog.dismiss();
                                                    setTimeViews(auth, inTime, outTime, inBox, outBox);
                                                } else {
                                                    Toast.makeText(getContext(), "Can't go off break", Toast.LENGTH_SHORT).show();
                                                }
                                        }
                                    }
                                });
                            }
                        }
                    });

                    listView.setOnItemClickListener(setOnClickListener(sunday, monday, tuesday, wednesday, thursday, friday, saturday, total, title));
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

    private void setTimeViews(FirebaseAuth auth, TextView inTime, TextView outTime, LinearLayout inBox, LinearLayout outBox){
        if(auth.getCurrentUser() != null) {
            if (mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().containsKey(getDateKey())) {
                if (mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getClockInTime() != 0) {
                    inTime.setText(getStartTime(mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getClockInTime()));
                    inBox.setBackground(getResources().getDrawable(R.drawable.customborder_green));
                    outBox.setBackground(getResources().getDrawable(R.drawable.customborder_green));
                }
                if (mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getClockOutTime() != 0) {
                    outTime.setText(getStartTime(mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getClockOutTime()));
                }
                if(mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getClockInTime() != 0
                        && mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getClockOutTime() == 0){
                    setUpTimer();
                }
                if(mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getClockInTime() != 0
                        && mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getClockOutTime() != 0){
                    inBox.setBackground(getResources().getDrawable(R.drawable.customborder_red));
                    outBox.setBackground(getResources().getDrawable(R.drawable.customborder_red));
                }
                if(mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getClockInTime() != 0
                        && mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getBreakStartTime() != 0
                        && mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getBreakStopTime() == 0
                        && mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getClockOutTime() == 0){
                    inBox.setBackground(getResources().getDrawable(R.drawable.customborder_yellow));
                    outBox.setBackground(getResources().getDrawable(R.drawable.customborder_yellow));
                }
            }
        }
    }

    private void setUpTimer(){
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final Handler handler = new Handler();
        final Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            if(auth.getCurrentUser()!=null) {
                                if (mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getClockOutTime() == 0) {
                                    changeCountTime(getView(), mEmployee.getEmployees().get(auth.getCurrentUser().getUid()).getDays().get(getDateKey()).getClockInTime());
                                } else {
                                    timer.cancel();
                                    if(getView()!=null) {
                                        TextView tv = getView().findViewById(R.id.timeCounter);
                                        tv.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        }
                        catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 60000);
    }

    private void changeCountTime(View view, long startTime) {
        Date date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime();
        long diff = date.getTime() - startTime;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        while (diffMinutes>59){
            diffHours+=1;
            diffMinutes-=60;
        }
        TextView countingTimer = view.findViewById(R.id.timeCounter);
        countingTimer.setVisibility(View.VISIBLE);
        String s = String.valueOf(diffHours)+"h " + String.valueOf(diffMinutes)+"m";
        countingTimer.setText(s);
    }

    private AdapterView.OnItemClickListener setOnClickListener(final TextView sunday,final  TextView monday,
                                                               final TextView tuesday,final  TextView wednesday,
                                                               final  TextView thursday, final TextView friday,
                                                               final  TextView saturday, final TextView total,
                                                               final TextView title){

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Week week = (Week) adapterView.getAdapter().getItem(i);
                sunday.setText(parseTimeStringToFormat(week.getSunday()));
                monday.setText(parseTimeStringToFormat(week.getMonday()));
                tuesday.setText(parseTimeStringToFormat(week.getTuesday()));
                wednesday.setText(parseTimeStringToFormat(week.getWednesday()));
                thursday.setText(parseTimeStringToFormat(week.getThursday()));
                friday.setText(parseTimeStringToFormat(week.getFriday()));
                saturday.setText(parseTimeStringToFormat(week.getSaturday()));
                title.setText(week.getRange());
                String totalStr = "Total: " + parseTimeStringToFormat(week.getTotal());
                total.setText(totalStr);

            }
        };
    }

    private String parseTimeStringToFormat(String value){
        String returned = value;
        String[] valueArray = value.split("\\.");
        if(valueArray.length>1) {
            if (valueArray[0].equals("0")) {
                returned = valueArray[1] + "m";
            }else{
                returned = valueArray[0]+"h"+valueArray[1]+"m";
            }
        }
        return  returned;
    }

    private ArrayList<Week> getWeeksData() {
        ArrayList<Week> allWeeks = new ArrayList<>();
        Calendar c = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        int y = c.get(Calendar.YEAR);
        int x = c.get(Calendar.WEEK_OF_YEAR);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<String> ranges = new ArrayList<>();
        if (currentUser != null) {
            String id = currentUser.getUid();
            if (mEmployee.getEmployees().get(currentUser.getUid()).getDays() != null) {
                if (mEmployee.getEmployees().get(currentUser.getUid()).getDays().size() > 0) {

                    for (int a = 0; a > -50; a--) {
                        Week weekSet = new Week();
                        Pair<String, String> week = getWeekRange(y, x + a);
                        String[] str1 = week.first.split("_");
                        String[] str2 = week.second.split("_");
                        weekSet.setRange(str1[1]+"/" + str1[2]+" - " + str2[1]+"/" + str2[2]);

                        double hoursTotal = 0.0;

                            for (int i = 0; i < 7; i++) {
                                ranges.add(getRange(y, x + a, i));

                                if (mEmployee.getEmployees().get(id).getDays().keySet().contains(ranges.get(i))) {
                                    if (ranges.get(i).equals(getToday())&& mEmployee.getEmployees().get(id).getDays().get(ranges.get(i)).getClockOutTime() == 0) {
                                        setWeekDayValue(weekSet, i, "...");
                                    } else if (mEmployee.getEmployees().get(id).getDays().get(ranges.get(i)).getClockInTime() != 0
                                            && mEmployee.getEmployees().get(id).getDays().get(ranges.get(i)).getClockOutTime() == 0) {
                                        setWeekDayValue(weekSet, i, String.valueOf(mEmployee.getDefaultHour()));
                                        hoursTotal += mEmployee.getDefaultHour();
                                    } else {
                                        if (mEmployee.getEmployees().get(id).getDays().get(ranges.get(i)).getHours() != null) {
                                            setWeekDayValue(weekSet, i, mEmployee.getEmployees().get(id).getDays().get(ranges.get(i)).getHours());
                                            hoursTotal += roundNum(Double.parseDouble(mEmployee.getEmployees().get(id).getDays().get(ranges.get(i)).getHours()));
                                        }else {
                                            setWeekDayValue(weekSet, i, "0");
                                        }
                                    }
                                } else {
                                    setWeekDayValue(weekSet, i, "0");
                                }

                            }
                            weekSet.setTotal(String.valueOf(hoursTotal));
                            allWeeks.add(weekSet);
                            ranges.clear();
                        }
                    }
                }
        }
        return allWeeks;
    }

    private void setWeekDayValue(Week weekSet, int i, String value) {
        switch (i) {
            case 0:
                weekSet.setSunday(value);
                return;
            case 1:
                weekSet.setMonday(value);
                return;
            case 2:
                weekSet.setTuesday(value);
                return;
            case 3:
                weekSet.setWednesday(value);
                return;
            case 4:
                weekSet.setThursday(value);
                return;
            case 5:
                weekSet.setFriday(value);
                return;
            case 6:
                weekSet.setSaturday(value);
        }
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

    private Job theJob = null;

    private CompactCalendarView.CompactCalendarViewListener setListener(final CompactCalendarView compactCalendarView, final View v, ImageButton locationGoButton1, MaterialEditText location) {
        final MaterialEditText jobTitle = v.findViewById(R.id.job_title);
        final MaterialEditText jobDate = v.findViewById(R.id.job_date);
        final MaterialEditText locationMet = location;
        final MaterialEditText jobNotes = v.findViewById(R.id.job_notes);
        setMetsEnabled(jobTitle, jobDate, jobNotes, location);
        final ImageButton locationGoButton = locationGoButton1;
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
                    uiUpdater.updateUiEmployee(mEmployee, "employee", 1);
                }
            }
        });
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
                    if(theJob!= null&&theJob.getLocation_lat_long() != null) {
                        lat_longs = theJob.getLocation_lat_long().replace("_", ",");
                    }
                    if(theJob!= null&&theJob.getAddress() != null) {
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

        return new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                if (events.size() > 0) {
                    theJob = (Job) events.get(0).getData();
                    if (theJob != null) {
                        jobTitle.setText(theJob.getTitle());
                        jobDate.setText(theJob.getDateCreated());
                        if (theJob!= null&&theJob.getAddress().equals("")) {
                            locationMet.setText(theJob.getLocation_lat_long());
                        } else {
                            locationMet.setText(theJob.getAddress());
                        }

                        if (locationMet.getText().toString().trim().equals("")) {
                            locationGoButton.setEnabled(false);
                        } else {
                            locationGoButton.setEnabled(true);
                            locationGoButton.setClickable(true);
                        }
                        jobNotes.setText(theJob.getNotes());
                    }
                } else {
                    theJob = null;
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
                    theJob = (Job) events.get(0).getData();

                    if (theJob != null) {
                        jobTitle.setText(theJob.getTitle());
                        jobDate.setText(theJob.getDateCreated());
                        if (theJob.getAddress().equals("")) {
                            locationMet.setText(theJob.getLocation_lat_long());
                        } else {
                            locationMet.setText(theJob.getAddress());
                        }

                        if (locationMet.getText().toString().trim().equals("")) {
                            locationGoButton.setEnabled(false);
                        } else {
                            locationGoButton.setEnabled(true);
                            locationGoButton.setClickable(true);
                        }
                        jobNotes.setText(theJob.getNotes());
                    }
                } else {
                    clearFieldsForCalendar(jobTitle, jobDate, jobNotes, locationMet);
                }
            }
        };
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

    private static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
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

    private String getRange(int year, int week_no, int i) {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY + i);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week_no);
        Date date = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("y_M_dd_E", Locale.getDefault());
        return sdf.format(date);
    }

    private Pair<String, String> getWeekRange(int year, int week_no) {

        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week_no);
        Date monday = cal.getTime();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week_no);
        Date sunday = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("y_M_dd_E", Locale.getDefault());
        return new Pair<>(sdf.format(monday), sdf.format(sunday));
    }

    private String getToday() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        Date today = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("y_M_dd_E", Locale.getDefault());

        return sdf.format(today);
    }
    
    private static double roundNum(double value) {

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    private String getDateKey() {
        Date date = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()).getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("y_M_dd_E", Locale.getDefault());
        return df2.format(date);
    }
    private String getDateStamp() {
        Date date = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()).getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("E-M-dd h:mma", Locale.getDefault());
        return df2.format(date);
    }
    private String getCurrentTime() {
        Date date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("h:mma", Locale.getDefault());
        return df2.format(date);
    }
    private String getStartTime(long time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault());
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("h:mma", Locale.getDefault());
        return df2.format(date);
    }

    private void addNewDayAndClockIn(Employee employee, Dialog dialog) {
        Date date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime();
        ArrayList<Day> days = new ArrayList<>();
        if (employee.getDays() != null && employee.getDays().size() > 0) {
            for (String s : employee.getDays().keySet()) {
                days.add(employee.getDays().get(s));
            }
        }
        days.add(new Day(date.getTime(), getLongitude(), getLatitude(), date.getTime(), getDateKey()));
        HashMap<String, Day> map = new HashMap<>();
        for (Day d : days) {
            map.put(d.getDayKey(), d);
        }
        employee.setDays(map);
        dialog.dismiss();
        FirebaseDatabaseHelper helper = new FirebaseDatabaseHelper();
        helper.updateDaysForEmployee(employee);
    }
    private long getDateTime(){
        return Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTimeInMillis();
    }
    private double getLongitude() {
        return LocationService.getLocationManager(getContext()).longitude;
    }

    private double getLatitude() {
        return LocationService.getLocationManager(getContext()).latitude;
    }


    private void setNewClockInTimeForDay(Employee employee, Dialog dialog, Day day) {
        Date date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime();
        ArrayList<Day> days = new ArrayList<>();
        if (employee.getDays() != null && employee.getDays().size() > 0) {
            for (String s : employee.getDays().keySet()) {
                days.add(employee.getDays().get(s));
            }
        }
        day.setClockInTime(date.getTime());
        day.setClockInLatitude(getLatitude());
        day.setClockInLongitude(getLongitude());
        day.setDayKey(getDateKey());
        days.add(day);
        HashMap<String, Day> map = new HashMap<>();
        for (Day d : days) {
            map.put(d.getDayKey(), d);
        }
        employee.setDays(map);
        dialog.dismiss();
        FirebaseDatabaseHelper helper = new FirebaseDatabaseHelper();
        helper.updateDaysForEmployee(employee);
    }
    private String getTotalHours(long in, long out, long start, long stop){
        long breaks = stop-start;
        long day = out-in;
        long total = day-breaks;
        long minutes = total / (60 * 1000) % 60;
        if(minutes<10){
            return String.valueOf(total / (60 * 60 * 1000))+".0"+minutes;
        }
        return String.valueOf(total / (60 * 60 * 1000))+"."+minutes;
    }

}



