// Casey Scott
// FINAL PROJECT - 1712
// EmployeesManagementFragment.java

package com.fullsail.caseyscott.ontime.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.adapters.EmployeeListAdapter;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class EmployeesManagementFragment extends Fragment {

    public static EmployeesManagementFragment newInstance(Employee employee) {

        Bundle args = new Bundle();
        args.putSerializable("employee", employee);
        EmployeesManagementFragment fragment = new EmployeesManagementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.crew_layout, container ,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getView() != null && getArguments() != null && getArguments().containsKey("employee")
                && getContext()!=null){
            Employee employee = (Employee) getArguments().getSerializable("employee");
            TextView today = getView().findViewById(R.id.today);
            today.setText(getTodaysDate());
            ImageView imageView = getView().findViewById(R.id.company_logo);
            if (employee != null && getArguments() != null && employee.getImageUrl() != null && !employee.getImageUrl().equals("")) {
                Picasso.with(getContext()).load(employee.getImageUrl()).into(imageView);
            }
            ListView listView = getView().findViewById(android.R.id.list);
            TextView textView = new TextView(getContext());
            textView.setText(R.string.no_emp_list);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            textView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            listView.setEmptyView(textView);
            ArrayList<Employee> employees = new ArrayList<>();
            if (employee == null) {
                employee = (Employee) getArguments().getSerializable("user");
            }
            if(employee!=null&&employee.getEmployees()!=null) {
                for (String s : employee.getEmployees().keySet()) {
                    employees.add(employee.getEmployees().get(s));
                }
            }
            if (employees.size() > 0) {
                listView.setAdapter(new EmployeeListAdapter(getActivity(), employees));
            }
        }
    }
    private String getTodaysDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd - yyyy", Locale.getDefault());
        Date date = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()).getTime();
        return sdf.format(date);
    }

}
