// Casey Scott
// FINAL PROJECT - 1712
// HoursTotalsAdapter.java

package com.fullsail.caseyscott.ontime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.objects.Hours;

import java.util.ArrayList;
import java.util.TreeSet;

public class HoursTotalsAdapter extends BaseAdapter {

        //Constant variables to help with the headers and values cells
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    //Inflater to inflate views
    private final LayoutInflater mInflater;

    public HoursTotalsAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //List objects to house the data
    private final ArrayList<Hours> mData = new ArrayList<>();
    private final TreeSet<Integer> sectionHeader = new TreeSet<>();

    //Helper method to add data tpo the adapter
    public void addItem(final Hours hours) {
        mData.add(hours);
        notifyDataSetChanged();
    }
    //Helper method to add data tpo the adapter
    public void addSectionHeaderItem(final String item) {
        mData.add(new Hours("", item));
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        //Set up the view
        ViewHolder holder;
        int rowType = getItemViewType(i);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.hours_total_item, viewGroup,false);
                    convertView.setTag(holder);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.hours_total_section_header, viewGroup,false);
                    convertView.setTag(holder);
                    break;
            }

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //Assign the values depending on which type of row it is
        switch (rowType) {
            case TYPE_ITEM:
                holder.textView = convertView.findViewById(R.id.hrs_total_emp_name);
                holder.textView.setText(mData.get(i).getEmployeeName());
                holder.textView = convertView.findViewById(R.id.hrs_total_hrs_total);
                holder.textView.setText(mData.get(i).getHours());
                break;
            case TYPE_SEPARATOR:
                holder.textView = convertView.findViewById(R.id.textSeparator);
                holder.textView.setText(mData.get(i).getEmployeeName());
                break;
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView textView;
    }

}
