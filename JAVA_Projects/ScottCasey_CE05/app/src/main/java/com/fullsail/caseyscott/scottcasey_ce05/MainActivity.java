// Casey Scott
// AID - 1705
// MainActivity.java

package com.fullsail.caseyscott.scottcasey_ce05;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.fullsail.caseyscott.scottcasey_ce05.fragments.EmptyFragment;
import com.fullsail.caseyscott.scottcasey_ce05.fragments.FormFragmentAdmin;
import com.fullsail.caseyscott.scottcasey_ce05.fragments.FormFragmentStudent;
import com.fullsail.caseyscott.scottcasey_ce05.fragments.FormFragmentInstructor;
import com.fullsail.caseyscott.scottcasey_ce05.fragments.PersonListFragment;
import com.fullsail.caseyscott.scottcasey_ce05.objects.SubmitInformationListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SubmitInformationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.drop_list_array));
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            spinner.setAdapter(arrayAdapter);
            //if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.emptyFragment, EmptyFragment.newInstance()).commit();
            // }
            spinner.setOnItemSelectedListener(itemSelectedListener);

            Button refreshButton = (Button) findViewById(R.id.refreshButton);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListView frag = (ListView) findViewById(android.R.id.list);
                    if(frag != null) {
                        frag.deferNotifyDataSetChanged();//Not sure about this
                    }
                }
            });
        }
    }

    @Override
    public void submitInformation(Persons person) {

        ArrayList<Persons> p = new ArrayList<>();
        ListView fragment = (ListView) findViewById(android.R.id.list);

        if (fragment != null) {
            for (int x = 0; x < fragment.getAdapter().getCount(); x++) {
                p.add((Persons)fragment.getAdapter().getItem(x));

            }
            ArrayAdapter<Persons> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, p);
            fragment.setAdapter(adapter);
        }
        p.add(person);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.emptyFragment, PersonListFragment.newInstance(p))
                .commit();


        Toast.makeText(this, person.toString() + " " +getString(R.string.added_to_list),Toast.LENGTH_SHORT).show();

    }


    private final AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch(position) {

                case 0:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerForm, FormFragmentStudent.newInstance()).commit();               // }
                    break;
                case 1:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerForm, FormFragmentInstructor.newInstance()).commit();
                    break;
                case 2:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerForm, FormFragmentAdmin.newInstance()).commit();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


}
