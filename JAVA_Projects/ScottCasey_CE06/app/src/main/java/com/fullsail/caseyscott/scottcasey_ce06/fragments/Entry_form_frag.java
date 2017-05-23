// Casey Scott
// AID - 1705
// Entry_form_frag.JAVA

package com.fullsail.caseyscott.scottcasey_ce06.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fullsail.caseyscott.scottcasey_ce06.Contact;
import com.fullsail.caseyscott.scottcasey_ce06.Objects.SubmitContactListener;
import com.fullsail.caseyscott.scottcasey_ce06.R;

import java.util.Objects;

public class Entry_form_frag extends Fragment{

    private SubmitContactListener mListener;
    public static final String TAG = "ENTRY_FORM_FRAG";
    private EditText num;

    public static Entry_form_frag newInstance() {

        Bundle args = new Bundle();

        Entry_form_frag fragment = new Entry_form_frag();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_entry_form, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            num = (EditText) getView().findViewById(R.id.phone_number_editText);
            num.addTextChangedListener(tw);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof SubmitContactListener){
            mListener=(SubmitContactListener)context;
        }else{
            throw  new IllegalArgumentException("Containing context does not implement the SubmitContactListener interface");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null){
            Button submitButton = (Button) getView().findViewById(R.id.submitButton);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText fName = (EditText) getView().findViewById(R.id.firstName_editText);
                    EditText lName = (EditText) getView().findViewById(R.id.lastName_editText);
                    num = (EditText) getView().findViewById(R.id.phone_number_editText);
                    Boolean fBool = Objects.equals(fName.getText().toString().trim(), "");
                    Boolean lBool = Objects.equals(lName.getText().toString().trim(), "");
                    Boolean numBool = Objects.equals(num.getText().toString().trim(), "");
                    if (fBool){

                        fName.setError(getString(R.string.cant_be_blank));

                    }if(lBool){
                        lName.setError(getString(R.string.cant_be_blank));

                    }if (numBool){
                        num.setError(getString(R.string.cant_be_blank));

                    }
                    else if(num.length() != 12){
                        num.setError(getString(R.string.wrong_num_format));
                    }if(!fBool && ! lBool && ! numBool && num.length() == 12){
                        String f = fName.getText().toString().trim();
                        String l = lName.getText().toString().trim();
                        String n = num.getText().toString().trim();
                        Contact contact = new Contact(f,l,n);
                        getFragmentManager().popBackStack();
                        mListener.submitContact(contact);

                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
    }

    private final TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if(s.length() == 3){
                num.append("-");
            }
            else if(s.length() == 7){
                num.append("-");
            }

        }
    };


}
