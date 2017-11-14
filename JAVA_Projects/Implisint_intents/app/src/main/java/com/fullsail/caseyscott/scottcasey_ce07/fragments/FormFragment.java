// Casey Scott
// AID - 1705
// FormFragment.JAVA

package com.fullsail.caseyscott.scottcasey_ce07.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fullsail.caseyscott.scottcasey_ce07.Objects.Person;
import com.fullsail.caseyscott.scottcasey_ce07.R;

import java.util.Objects;

public class FormFragment extends Fragment {

    private Listener mListener;
    public interface Listener {
        void addPerson(Person person);
    }

    private EditText firstName;
    private EditText lastName;
    private EditText age;

    public static final String TAG = "FormFragment";

    public static FormFragment newInstance() {
        Log.e(TAG, "FormFragment.newInstance() executed");

        Bundle args = new Bundle();

        FormFragment fragment = new FormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView() executed");

        return inflater.inflate(R.layout.formfrag_layout, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreate() executed");
        super.onCreate(savedInstanceState);



        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        Log.e(TAG, "onAttached executed");
        super.onAttach(context);

        if(context instanceof Listener){
            mListener = (Listener) context;
        } else {
            Log.e(TAG, "onAttach: " + context.toString() + " must implement FormFragment.Listener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e(TAG, "onCreateOptionsMenu() executed");

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.form_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.e(TAG, "onOptionsItemSelected() executed");
        if (item.getItemId() == R.id.form_menu_save) {
            mListener.addPerson(newPerson());
        }
        return newPerson() != null && super.onOptionsItemSelected(item);

    }

    private Person newPerson(){

        Log.e(TAG, "newPerson() executed");

        if(getView() != null) {
            Log.e(TAG, "getView() does NOT equal null");


                firstName = (EditText) getView().findViewById(R.id.firstName);
                lastName = (EditText) getView().findViewById(R.id.lastname);
                age = (EditText) getView().findViewById(R.id.age);
                firstName.addTextChangedListener(textWatcher);
                lastName.addTextChangedListener(textWatcher);
                age.addTextChangedListener(textWatcher);


            return new Person(firstName.getText().toString(),
                    lastName.getText().toString(),
                    age.getText().toString());

        }

        Log.e(TAG, "getView() does equal null");
        return  null;
    }
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (Objects.equals(firstName.getText().toString(), "")){
                firstName.setError("Enter a name");
            }
            if (Objects.equals(lastName.getText().toString(), "")){
                lastName.setError("Enter a name");
            }
            if (Objects.equals(age.getText().toString(), "")){
                age.setError("Enter an age");
            }

        }
    };

}
