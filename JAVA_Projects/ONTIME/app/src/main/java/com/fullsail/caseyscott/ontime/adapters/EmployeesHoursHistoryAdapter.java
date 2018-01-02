// Casey Scott
// FINAL PROJECT - 1712
// EmployeesHoursHistoryAdapter.java

package com.fullsail.caseyscott.ontime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.objects.Week;

import java.util.ArrayList;


public class EmployeesHoursHistoryAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Week> weeks;
    private static final int ID_CONSTANT = 0X001;


    //Constructor
    public EmployeesHoursHistoryAdapter(Context context, ArrayList<Week> weeks){
        this.context = context;
        this.weeks = weeks;
    }
    //Views Holder Class
    class ViewHolder{
        final TextView weekRange;
        final TextView hoursTotal;
        //View Holder Constructor
        ViewHolder(View v){
            weekRange = v.findViewById(R.id.hrs_total_emp_name);
            hoursTotal = v.findViewById(R.id.hrs_total_hrs_total);
        }
    }


    @Override
    public int getCount() {
        return weeks.size();
    }

    @Override
    public Object getItem(int i) {
        return weeks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i+ID_CONSTANT;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        //Make the view to display
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.hours_total_item,viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        //Assign the values to the views
        holder.hoursTotal.setText(parseTimeStringToFormat(weeks.get(i).getTotal()));
        holder.weekRange.setText(weeks.get(i).getRange());

        return view;
    }
    //Method to parse the datetime into a readable string of hours and minutes
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
}
