// Casey Scott
// FINAL PROJECT - 1712
// MainActivity.java

package com.fullsail.caseyscott.ontime.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.fullsail.caseyscott.ontime.R;
import com.fullsail.caseyscott.ontime.adapters.SimplePageAdapter;
import com.fullsail.caseyscott.ontime.fragments.AdminFragment;
import com.fullsail.caseyscott.ontime.fragments.EmployeeFragment;
import com.fullsail.caseyscott.ontime.fragments.ManagerFragment;
import com.fullsail.caseyscott.ontime.objects.Employee;
import com.fullsail.caseyscott.ontime.services.LocationService;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AdminFragment.UpdateFragmentsListener, EmployeeFragment.UpdateUiListenerInterface, ManagerFragment.UpdateUiListenerInterfaceMan{

    public static final String ACTION_SETTINGS = "com.android.caseyscott.ontime.SETTINGS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getBoolean(R.bool.is_tablet)) {
            setContentView(R.layout.tablet_login);
        } else {
            setContentView(R.layout.activity_main);

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION);
            }

            String access = getIntent().getStringExtra(AdminFragment.ARG_ACCESS);
            Employee employee = (Employee) getIntent().getSerializableExtra("user");
            @SuppressWarnings("unchecked") ArrayList<Employee> failedEmployees = (ArrayList<Employee>) getIntent().getSerializableExtra("failed");
            setTitle(employee.getCompany());

            // Get the ViewPager and set it's PagerAdapter so that it can display items
            ViewPager viewPager = findViewById(R.id.viewpager);
            viewPager.setAdapter(new SimplePageAdapter(getSupportFragmentManager(), access, employee, failedEmployees));

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }

    @Override
    public void updateFragments(Employee employee, String access) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new SimplePageAdapter(getSupportFragmentManager(), access, employee, null));
        viewPager.setCurrentItem(2);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void goToEmployeeInfo(Employee employee, String access, Employee admin) {
        Intent intent = new Intent(LandingActivity.ACTION_EMPLOYEE_INFO);
        intent.putExtra("employee", employee);
        intent.putExtra("access", access);
        intent.putExtra("admin", admin);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String access1;
        Employee employee1;
        if (requestCode == 1 && data.getExtras() !=null) {
            employee1 = (Employee) data.getExtras().getSerializable("admin");
            access1 = data.getExtras().getString("access");
            // Get the ViewPager and set it's PagerAdapter so that it can display items
            ViewPager viewPager = findViewById(R.id.viewpager);
            viewPager.setAdapter(new SimplePageAdapter(getSupportFragmentManager(), access1, employee1, null));
            viewPager.setCurrentItem(0);

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);

        }
    }

    @Override
    public void updateUiEmployee(Employee employee, String access, int page) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new SimplePageAdapter(getSupportFragmentManager(), access, employee, null));
        viewPager.setCurrentItem(page);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void updateUiManager(Employee employee) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new SimplePageAdapter(getSupportFragmentManager(), "manager", employee, null));
        viewPager.setCurrentItem(1);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
