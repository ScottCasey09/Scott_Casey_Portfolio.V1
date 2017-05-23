package com.fullsail.caseyscott.actionbardemo.fragments;

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
import android.widget.Button;
import android.widget.TextView;


import com.fullsail.caseyscott.actionbardemo.R;
import com.fullsail.caseyscott.actionbardemo.objects.Contact;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class FormFragment extends Fragment {

    public static final String TAG = "FormFragment.TAG";

    private Listener mListener;

    public interface Listener {
        public void addContact(Contact contact);
    }

    public static FormFragment newInstance() {
        FormFragment fragment = new FormFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof Listener){
            mListener = (Listener) context;
        } else {
            Log.e(TAG, "onAttach: " + context.toString() + " must implement MainFragment.Listener");
        }
    }

    // TODO: Override onCreate lifecycle method


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Remove this once options menu is functional

    }

    // TODO: Create Options Menu


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.form_menu_save){
            mListener.addContact(verifyForm());
        }
        return super.onOptionsItemSelected(item);

    }

    //In an activity you have to use getMenuInflater() to get the context
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.form_menu, menu);
    }

    // TODO: Handle Options Menu Select

    private Contact verifyForm() {
        if (getView() != null) {
            TextView firstName = (TextView) getView().findViewById(R.id.editTextFirstName);
            TextView lastName = (TextView) getView().findViewById(R.id.editTextLastName);
            TextView phoneNumber = (TextView) getView().findViewById(R.id.editTextPhone);

            Contact contact = new Contact(
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    phoneNumber.getText().toString()
            );

            return contact;
        }

        return null;
    }
}
