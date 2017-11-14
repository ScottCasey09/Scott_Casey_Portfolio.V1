// Casey Scott
// AID - 1705
// DetailsFragment.JAVA

package com.fullsail.caseyscott.scottcasey_ce07.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullsail.caseyscott.scottcasey_ce07.Objects.Person;
import com.fullsail.caseyscott.scottcasey_ce07.R;


public class DetailsFragment extends Fragment {

    private TextView firstName;
    private TextView lastName;
    private TextView age;

    private static final String TAG = "DETAILS = ";

    private DetailsFragment.Listener mListener;
    public interface Listener {
        void deletePerson(Person person);
    }

    public static DetailsFragment newInstance(Bundle bundle) {

        Log.e(TAG, bundle.keySet().toString());
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        Log.e(TAG, "onAttached executed +" + " " + getView());
        super.onAttach(context);

        if(context instanceof DetailsFragment.Listener){
            mListener = (DetailsFragment.Listener) context;
        } else {
            Log.e(TAG, "onAttach: " + context.toString() + " must implement DetailsFragment.Listener");
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e(TAG, "onCreateOptionsMenu() executed");

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.e(TAG, "onOptionsItemSelected() executed");
        if (item.getItemId() == R.id.form_menu_delete) {
            mListener.deletePerson(newPerson());
        }
        return newPerson() != null && super.onOptionsItemSelected(item);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.detailsfrag_layout, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setHasOptionsMenu(true);
        Log.e(TAG, "onCreate executed");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(TAG, getArguments().keySet().toString());

        if(getView() != null) {

            firstName = (TextView) getView().findViewById(R.id.firstName_details);
            lastName = (TextView) getView().findViewById(R.id.lastName_details);
            age = (TextView) getView().findViewById(R.id.age_details);

            Bundle bundle = getArguments();
            Log.e(TAG, String.valueOf(bundle.size()));

            firstName.setText(bundle.getString("com.fullsail.android.jav2ce08.EXTRA_STRING_FIRST_NAME"));
            lastName.setText(bundle.getString("com.fullsail.android.jav2ce08.EXTRA_STRING_LAST_NAME"));
            age.setText(String.valueOf(bundle.getInt("com.fullsail.android.jav2ce08.EXTRA_INT_AGE")));

        }

    }

    private Person newPerson(){

        Log.e(TAG, "newPerson() executed");

        if(getView() != null) {
            Log.e(TAG, "getView() does NOT equal null");

            return new Person(firstName.getText().toString(),
                    lastName.getText().toString(),
                    age.getText().toString());

        }

        Log.e(TAG, "getView() does equal null");
        return  null;
    }
}
