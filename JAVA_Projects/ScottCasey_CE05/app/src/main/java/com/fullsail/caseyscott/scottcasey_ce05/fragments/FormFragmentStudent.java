// Casey Scott
// AID - 1705
// FormFragmentStudent.java
package com.fullsail.caseyscott.scottcasey_ce05.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fullsail.caseyscott.scottcasey_ce05.R;
import com.fullsail.caseyscott.scottcasey_ce05.Student;
import com.fullsail.caseyscott.scottcasey_ce05.objects.SubmitInformationListener;

import java.util.Objects;

public class FormFragmentStudent extends Fragment{

    private SubmitInformationListener mListener;


    public static FormFragmentStudent newInstance() {

        Bundle args = new Bundle();

        FormFragmentStudent fragment = new FormFragmentStudent();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_fragment_student, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SubmitInformationListener){
            mListener = (SubmitInformationListener) context;
        }else{
            throw  new IllegalArgumentException(getString(R.string.must_implement_interface));
        }
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null) {
            Button addButton = (Button) getView().findViewById(R.id.addButton);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText fName = (EditText) getView().findViewById(R.id.editText_line1);
                    EditText lName = (EditText) getView().findViewById(R.id.editText_line2);
                    EditText optional = (EditText) getView().findViewById(R.id.editText_line3);
                    String firstName = fName.getText().toString();
                    String lastName = lName.getText().toString();
                    String opt = optional.getText().toString();

                    if (Objects.equals(firstName.trim(), getString(R.string.blank)) && Objects.equals(lastName.trim(), getString(R.string.blank)) && Objects.equals(opt.trim(), getString(R.string.blank))){
                        Toast.makeText(getActivity(), R.string.must_enter_all_fields, Toast.LENGTH_SHORT).show();
                    }else if (Objects.equals(firstName.trim(), getString(R.string.blank))) {
                        Toast.makeText(getActivity(), R.string.enter_first_name, Toast.LENGTH_SHORT).show();
                    }else if(Objects.equals(lastName.trim(), getString(R.string.blank))){
                        Toast.makeText(getActivity(), R.string.enter_last_name, Toast.LENGTH_SHORT).show();
                    }else if(Objects.equals(opt.trim(), getString(R.string.blank))){
                        Toast.makeText(getActivity(), R.string.enter_all_fields, Toast.LENGTH_SHORT).show();
                    }else{
                        Student s = new Student(firstName, lastName, opt);
                        mListener.submitInformation(s);

                        fName.setText(null);
                        lName.setText(null);
                        optional.setText(null);
                    }
                }
            });

        }
    }

}
