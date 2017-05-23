// Casey Scott
// AID - 1705
// MainActivity.JAVA

package com.fullsail.caseyscott.scottcasey_ce06;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fullsail.caseyscott.scottcasey_ce06.Objects.OpenDetailsListener;
import com.fullsail.caseyscott.scottcasey_ce06.Objects.SubmitContactListener;
import com.fullsail.caseyscott.scottcasey_ce06.fragments.Contacts_listFrag;
import com.fullsail.caseyscott.scottcasey_ce06.fragments.Details_frag;
import com.fullsail.caseyscott.scottcasey_ce06.fragments.Empty_frag;
import com.fullsail.caseyscott.scottcasey_ce06.fragments.Entry_form_frag;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements Details_frag.DetailsListener, SubmitContactListener, OpenDetailsListener {

    private FloatingActionButton fab;
    private final String ENTRY = "ENTRY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.fragment_content, Empty_frag.newInstance()).commit();
         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    fab.setVisibility(View.GONE);

                   getFragmentManager().beginTransaction()
                           .setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right)
                           .add(R.id.fragment_content, Entry_form_frag.newInstance(), Entry_form_frag.TAG)
                           .addToBackStack(ENTRY)
                           .commit();


            }
        });


        }

    @Override
    public void closeDetails() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void openDetails(Contact contact) {

        fab.setVisibility(View.GONE);

        String DETAILS = "DETAILS";
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slider_in_right,0,0, R.animator.slider_out_left)
                .add(R.id.fragment_content, Details_frag.newInstance(contact), Details_frag.TAG)
                .addToBackStack(DETAILS)
                .commit();
    }

    @Override
    public void submitContact(Contact contact) {

        ArrayList<Contact> contacts = new ArrayList<>();
        ListView fragment = (ListView) findViewById(android.R.id.list);
        if (fragment != null) {
            for (int x = 0; x < fragment.getAdapter().getCount(); x++) {
                contacts.add((Contact)fragment.getAdapter().getItem(x));
            }
            ArrayAdapter<Contact> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
            fragment.setAdapter(adapter);
        }
            contacts.add(contact);
        String CONTACTS_LIST = "CONTACTS_LIST_FRAGMENT";
        getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slider_in_right, 0, 0, R.animator.slider_out_left)
                    .replace(R.id.fragment_content, Contacts_listFrag.newInstance(contacts))
                    .addToBackStack(CONTACTS_LIST)
                    .commit();

        Toast.makeText(this, contact.toString() + " " +getString(R.string.added_contact),Toast.LENGTH_SHORT).show();

        fab.setVisibility(View.VISIBLE);
    }


}
