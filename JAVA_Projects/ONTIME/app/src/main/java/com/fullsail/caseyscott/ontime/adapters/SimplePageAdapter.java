// Casey Scott
// FINAL PROJECT - 1712
// CreateAccountActivity.java

package com.fullsail.caseyscott.ontime.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fullsail.caseyscott.ontime.fragments.AdminFragment;
import com.fullsail.caseyscott.ontime.fragments.EmployeeFragment;
import com.fullsail.caseyscott.ontime.fragments.ManagerFragment;
import com.fullsail.caseyscott.ontime.objects.Employee;

import java.util.ArrayList;


public class SimplePageAdapter extends FragmentPagerAdapter {
    private final String[] tabTitlesAdmin = new String[] { "Crew", "Calendar", "Jobs" };
    private final String[] tabTitlesManager = new String[] { "Crew", "Calendar" };
    private final String[] tabTitlesEmployee = new String[] { "My Stuff", "Calendar"};
    private String access = "admin";
    private final Employee employee;
    private final ArrayList<Employee> failedEmployees;
    //Constructor to build the page adapter
    public SimplePageAdapter(FragmentManager fm, String access, Employee employee, ArrayList<Employee> failedEmployees) {
        super(fm);
        this.access = access;
        this.employee = employee;
        this.failedEmployees = failedEmployees;
    }

    @Override
    public int getCount() {
        if(access == null){
            access = "admin";
        }
        switch (access){
            case "admin":
                return 3;
            case "manager":
                return 2;
            case "employee":
                return 2;
            default:
                return 3;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (access){
            case "admin":
                return AdminFragment.newInstance(position + 1,access, employee, failedEmployees);
            case "manager":
                return ManagerFragment.newInstance(position+1,access, employee);
            case "employee":
                return EmployeeFragment.newInstance(position+1, access, employee);
            default:
                return AdminFragment.newInstance(position + 1,access, employee, failedEmployees);
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (access){
            case "admin":
                return tabTitlesAdmin[position];
            case "manager":
                return tabTitlesManager[position];
            case "employee":
                return tabTitlesEmployee[position];
                default:
                    return tabTitlesAdmin[position];
        }

    }
}
