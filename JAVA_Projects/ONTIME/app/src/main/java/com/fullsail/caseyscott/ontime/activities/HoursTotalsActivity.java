// Casey Scott
// FINAL PROJECT - 1712
// HoursTotalsActivity.java

package com.fullsail.caseyscott.ontime.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.adapters.HoursTotalsAdapter;
import com.fullsail.caseyscott.ontime.helpers.FirebaseDatabaseHelper;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.fullsail.caseyscott.ontime.objects.Hours;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class HoursTotalsActivity extends AppCompatActivity {

    private Employee employee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hours_totals_list);
        //Make the action bar display the back arrow for going back to the last activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //Recover the passed employee form the intent
        if(getIntent()!=null&& getIntent().getExtras() != null) {
                employee = (Employee) getIntent().getExtras().getSerializable("employee");
        }
        //Set the title for the View/Activity
        setTitle(getString(R.string.hrs_totals));
        //Find the views
        ListView listView = findViewById(android.R.id.list);
        //Assign an empty state view to the listView
        TextView textView = new TextView(this);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(R.string.no_emp_list);
        listView.setEmptyView(textView);
        //Instantiate the calendar object
        Calendar c = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        //Get the current calendar year and week of the year
        int y = c.get(Calendar.YEAR);
        int x = c.get(Calendar.WEEK_OF_YEAR);
        //instantiate the ranges array
        ArrayList<String> ranges = new ArrayList<>();
        //Instantiate the database helper
        FirebaseDatabaseHelper helper = new FirebaseDatabaseHelper();
        //Instantiate the adapter
        HoursTotalsAdapter mAdapter = new HoursTotalsAdapter(this);

        //Cycle through the last 5 weeks of employee data
        for (int a = 0; a > -4; a--) {
            Pair<String, String> week = getWeekRange(y, x + a);
            //Add the section header String
            mAdapter.addSectionHeaderItem(week.first + " to " + week.second);

            //Go through each recorded day fo the selected employee
            for (String s : employee.getEmployees().keySet()) {
                if (employee.getEmployees().get(s).getDays() != null) {
                    double hoursTotal = 0.0;
                    for (String str : employee.getEmployees().get(s).getDays().keySet()) {

                        //Get the day as a string
                        String dateKey = employee.getEmployees().get(s).getDays().get(str).getDayKey();
                        //Split the date string to mimic the range string
                        dateKey = dateKey.replaceAll("_", "-");
                        //Cycle through the ranges to find values
                        for (int i = 0; i < 7; i++) {
                            ranges.add(getRange(y, x + a, i));

                            //Set the hours for the completed day
                            if (dateKey.contains(ranges.get(i))) {
                                if (employee.getEmployees().get(s).getDays().get(str).getHours() != null) {
                                    hoursTotal += Double.parseDouble(employee.getEmployees().get(s).getDays().get(str).getHours());

                                }
                                //set the default hours totals if the employee did not clock out that day
                                else if (employee.getEmployees().get(s).getDays().get(str).getClockInTime() != 0
                                        && employee.getEmployees().get(s).getDays().get(str).getClockOutTime() == 0
                                        && !Objects.equals(dateKey, getDateKey())){
                                    hoursTotal += 8.0;
                                    employee.getEmployees().get(s).getDays().get(str).setClockOutTime(employee.getEmployees().get(s).getDays().get(str).getClockInTime()+28800000);
                                    employee.getEmployees().get(s).getDays().get(str).setWasDefaulted();
                                    helper.setDefaultedDay(employee.getEmployees().get(s));
                                }
                            }

                        }
                        //Clear the ranges for the next week
                        ranges.clear();
                    }
                    //Add the newly found hours to the adapter
                    mAdapter.addItem(new Hours(parseHours(String.valueOf(hoursTotal)), employee.getEmployees().get(s).toString()));
                }
            }
        }
        //Set the list adapter
        listView.setAdapter(mAdapter);
    }

    //Method to return the string value of the parsed double
    private String parseHours(String hours) {
        String num = String.valueOf(roundNum(Double.parseDouble(hours)));
        String[] array = num.split("\\.");
        int hour = Integer.parseInt(array[0]);
        if (array.length == 2) {
            int min = Integer.parseInt(array[1]);
            for (int i = 0; i < min / 60; i++) {
                if (min > 59) {
                    hour++;
                    min -= 60;
                }
            }
            //Return the string
            return hour + "hrs" + min + "min";
        }
        return String.valueOf(hour);
    }
    //Round the hours total if they are more then 2 decimals
    private static double roundNum(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    //Method to return the date string for the required date
    private String getRange(int year, int week_no, int i) {
        //Get the calendar date as default to the current location of the device
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY + i);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week_no);
        Date date = cal.getTime();
        //Return the date string - formatted
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd", Locale.getDefault());
        return sdf.format(date);
    }
    //Method to return a string pair that represents the weeks date range for headers
    private Pair<String, String> getWeekRange(int year, int week_no) {

        //Get the calendar and assign it to the date for the first day of the week
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week_no);
        Date monday = cal.getTime();
        //Set the calendar to the last day of the week
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week_no);
        Date sunday = cal.getTime();
        //Return the formatted date
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd", Locale.getDefault());
        return new Pair<>(sdf.format(monday), sdf.format(sunday));
    }
    //Method to return the date for today
//    private String getToday(){
//        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
//        Date today = cal.getTime();
//        //Format the date string
//        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd", Locale.getDefault());
//        //return the string
//        return sdf.format(today);
//    }
    private String getDateKey() {
        Date date = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("y-M-dd-E", Locale.getDefault());
        return df2.format(date);
    }

    //Options menu actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Find the selected options button
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(LandingActivity.ACTION_MAIN_PAGE_VIEWER);
                intent.putExtra("user", employee);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
