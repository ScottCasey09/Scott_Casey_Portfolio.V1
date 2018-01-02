// Casey Scott
// FINAL PROJECT - 1712
// EmployeeListAdapter.java

package com.fullsail.caseyscott.ontime.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fullsail.caseyscott.ontime.helpers.FirebaseDatabaseHelper;
import com.fullsail.caseyscott.ontime.services.LocationService;
import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.objects.Day;
import com.fullsail.caseyscott.ontime.objects.Employee;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class EmployeeListAdapter extends BaseAdapter {

    //members of the adapter
    private final ArrayList<Employee> mEmployees;
    private final Context mContext;
    private static final int ID_CONSTANT = 0X00001;
    private final FirebaseDatabaseHelper helper;
    private final LocationService locat;
    private boolean isChangingBox;

    //Constructor for the adapter
    public EmployeeListAdapter(Context context, ArrayList<Employee> employees) {
        this.mContext = context;
        this.mEmployees = employees;
        helper = new FirebaseDatabaseHelper();
        locat = new LocationService(mContext);
    }

    @Override
    public int getCount() {
        return mEmployees.size();
    }

    @Override
    public Object getItem(int i) {
        return mEmployees.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i + ID_CONSTANT;
    }

    //A class to hold the views in the list cell
    class ViewHolder {
        private final TextView textView;
        private final ImageButton clockButton;
        private final TextView in_time;
        private final TextView out_time;

        ViewHolder(View v) {
            //Assign the views by ids
            textView = v.findViewById(R.id.crewMember_name_textView);
            clockButton = v.findViewById(R.id.clock_button_crew_cell);
            in_time = v.findViewById(R.id.clockin_time_textView);
            out_time = v.findViewById(R.id.clockout_time_textView);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Get the view and assign the correct values to the correct places
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.crew_list_cell, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Employee e = mEmployees.get(i);
        //Make map for fast checking
        if(e.getDays() != null) {
            Day today = e.getDays().get(getDateKey());
            if (e.getDays() != null && today != null) {
                //Make sure everything has values to read to avoid error
                if (e.getDays().size() > 0) {

                    if (today.getClockInTime() != 0) {
                        holder.in_time.setText(getClockInTime(today.getClockInTime()));
                        //Set the color for the status of the employees day
                        if(today.getBreakStartTime()!=0 && today.getBreakStopTime()==0&&today.getClockOutTime()==0){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                holder.in_time.setBackgroundColor(mContext.getResources().getColor(R.color.colorOnBreakYellow, mContext.getTheme()));
                                holder.out_time.setBackgroundColor(mContext.getResources().getColor(R.color.colorOnBreakYellow, mContext.getTheme()));
                            }else{
                                holder.in_time.setBackgroundColor(mContext.getResources().getColor(R.color.colorOnBreakYellow));
                                holder.out_time.setBackgroundColor(mContext.getResources().getColor(R.color.colorOnBreakYellow));
                            }
                        }else if((today.getClockOutTime()==0 && today.getBreakStartTime()==0&&today.getBreakStopTime()==0) ||
                                (today.getClockOutTime()==0 && today.getBreakStartTime()!=0&&today.getBreakStopTime()!=0)){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                holder.in_time.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen, mContext.getTheme()));
                                holder.out_time.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen, mContext.getTheme()));
                            }else{
                                holder.in_time.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen));
                                holder.out_time.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen));
                            }
                        }
                        else if(today.getClockOutTime()!=0){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                holder.in_time.setBackgroundColor(mContext.getResources().getColor(R.color.colorStopRed, mContext.getTheme()));
                                holder.out_time.setBackgroundColor(mContext.getResources().getColor(R.color.colorStopRed, mContext.getTheme()));
                            }else{
                                holder.in_time.setBackgroundColor(mContext.getResources().getColor(R.color.colorStopRed));
                                holder.out_time.setBackgroundColor(mContext.getResources().getColor(R.color.colorStopRed));
                            }

                        }

                    } else {
                        //Set the view default for no data
                        holder.in_time.setText("--:--");
                    }
                    if (today.getClockOutTime() != 0) {
                        //Set the view with a value
                        holder.out_time.setText(getClockInTime(today.getClockOutTime()));
                    } else {
                        //Set the view default for no data
                        holder.out_time.setText("--:--");
                    }
                }
            } else {
                //Set the view default for no data
                holder.in_time.setText("--:--");
                holder.out_time.setText("--:--");
            }

        }else {
            //Set the view default for no data
            holder.in_time.setText("--:--");
            holder.out_time.setText("--:--");
        }
        //Set the views and return it
        //Set the view with a value
        holder.textView.setText(e.toString());
        holder.clockButton.setTag(e);
        holder.clockButton.setFocusable(false);
        holder.clockButton.setFocusableInTouchMode(false);
        if(e.getDays() != null && e.getDays().get(getDateKey()) != null) {
            if (e.getDays().get(getDateKey()).getClockOutTime() != 0) {
                holder.clockButton.setVisibility(View.INVISIBLE);
            }
        }
        //Set the click listener
        holder.clockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set up the dialog and find the views
                final Dialog dialog = new Dialog(mContext);
                final Employee employee = (Employee) view.getTag();
                dialog.setContentView(R.layout.clock_employee_dialog);
                TextView title = dialog.findViewById(R.id.name_clocking_dialog);
                //Set the employees name as the title of the dialog
                title.setText(employee.toString());
                final CheckBox in_checkBox = dialog.findViewById(R.id.in_checkBox);
                final CheckBox out_checkBox = dialog.findViewById(R.id.out_checkBox);
                final CheckBox break_start = dialog.findViewById(R.id.breakStart_checkBox);
                final CheckBox break_end = dialog.findViewById(R.id.breakEnd_checkBox);

                //Manage the check changes
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
                //Show the dialog
                dialog.show();
                //Set up the cancel button
                final Button cancelButton = dialog.findViewById(R.id.cancel_clocking);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Dismis the dialog
                        dialog.dismiss();
                    }
                });
                //Set up the clock button to handel clocking
                final Button clockButton = dialog.findViewById(R.id.clock_clocking);
                clockButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Check to see which box is checked
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
                        //Location data must be available to clock in and out
                        LocationManager lm = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
                        boolean gps_enabled= true;
                        try {
                            if (lm != null) {
                                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            }
                        }catch (Exception ignored){}
                        boolean network_enabled = true;
                        try{
                            if (lm != null) {
                                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                            }
                        }catch (Exception ignored){}
                        //Make sure the device has network data
                        if(!gps_enabled && !network_enabled){
                            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                            dialog.setMessage(R.string.locat_serv_not_enabled);
                            dialog.setPositiveButton(R.string.open_locat_, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    mContext.startActivity(myIntent);
                                }
                            });
                            dialog.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                }
                            });
                            dialog.show();
                        }else {

                            //Depending on which box is checked do action for that box
                                switch (box) {
                                    case 1:
                                        //Check to see if the day exists
                                        //If not make new day
                                        //else get instance of existing day and set the new values
                                        //Also need to check if the current parameter has a value
                                        //If it does then tell the user that they cannot use this action ex.has already checked in so can't select clock in
                                        //If on break but wants to clock out you can use the initial break time as clockout time then just calculate the hrs without breaks

                                        if (employee.getDays() == null || !employee.getDays().containsKey(getDateKey())) {
                                            addNewDayAndClockIn(employee, dialog);
                                            return;
                                        } else {
                                            Day day = employee.getDays().get(getDateKey());
                                            if (day.getClockInTime() == 0) {
                                                setNewClockInTimeForDay(employee, dialog, day);
                                                notifyDataSetChanged();
                                                return;
                                            } else {
                                                Toast.makeText(mContext, "Already clocked in", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                                return;
                                            }
                                        }
                                    case 2:
                                        if (employee.getDays() == null || !employee.getDays().containsKey(getDateKey())) {
                                            Toast.makeText(mContext, "Must start a day first", Toast.LENGTH_SHORT).show();
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
                                                notifyDataSetChanged();
                                                dialog.dismiss();
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
                                            notifyDataSetChanged();
                                            return;

                                        } else {
                                            Toast.makeText(mContext, "Can't go on break", Toast.LENGTH_SHORT).show();
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
                                            notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(mContext, "Can't go off break", Toast.LENGTH_SHORT).show();
                                        }
                                }
                            }

                    }
                });
            }
        });

        return view;
    }
//Gets the current time
    private long getDateTime(){
        return Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()).getTimeInMillis();
    }
        //Gets the date for a key as a formatted string
    private String getDateKey() {
        Date date = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()).getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("y_M_dd_E", Locale.getDefault());
        return df2.format(date);
    }
        //Gets the clock in time as a formatted string
    private String getClockInTime(long l) {

        SimpleDateFormat df2 = new SimpleDateFormat("h:mma", Locale.getDefault());
        return df2.format(l);
    }
    //Methods to retrieve the lat and long from the location service
    private double getLongitude() {
        return locat.longitude;
    }

    private double getLatitude() {
        return locat.latitude;
    }
        //Method to create a new day and clock the employee in   saves the data to the Firebase DB
    private void addNewDayAndClockIn(Employee employee, Dialog dialog) {
        Date date = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()).getTime();
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
        notifyDataSetChanged();
        helper.updateDaysForEmployee(employee);
    }

    private void setNewClockInTimeForDay(Employee employee, Dialog dialog, Day day) {
        Date date = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()).getTime();
        ArrayList<Day> days = new ArrayList<>();
        if (employee.getDays() != null && employee.getDays().size() > 0) {
            for (String s : employee.getDays().keySet()) {
                days.add(employee.getDays().get(s));
            }
        }
        //Set the values
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
        notifyDataSetChanged();
        //Save the data
        helper.updateDaysForEmployee(employee);
    }

    //Gets the string representation of the total hours for the day
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
