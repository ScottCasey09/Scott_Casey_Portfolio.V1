// Casey Scott
// AID - 1705
// Details_frag.JAVA

package com.fullsail.caseyscott.scottcasey_ce06.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fullsail.caseyscott.scottcasey_ce06.Contact;
import com.fullsail.caseyscott.scottcasey_ce06.R;

public class Details_frag extends Fragment implements View.OnClickListener{


    public static final String TAG = "DETAILS_FRAG";
    private static final String CONTACT = "CONTACT";
    private DetailsListener mListener;

    public static Details_frag newInstance(Contact contact) {

        Bundle args = new Bundle();
        args.putSerializable(CONTACT, contact);
        Details_frag fragment = new Details_frag();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_view,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null){
            Button detailExitButton = (Button) getView().findViewById(R.id.back_button_details);
            detailExitButton.setOnClickListener(this);

                Contact contact = (Contact) getArguments().get(CONTACT);

           if(contact != null){
               TextView fullname = (TextView) getView().findViewById(R.id.fullName);
               fullname.setText(contact.getFirstName() + " " + contact.getLastName());
               TextView phoneNumber = (TextView) getView().findViewById(R.id.phone_number_details);
               phoneNumber.setText(contact.getPhoneNUmber());
           }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Details_frag.DetailsListener){
            mListener = (Details_frag.DetailsListener) context;
        }else{
            Log.e(TAG, "onAttach: must be instance of DetailsListener");
        }

    }

    @Override
    public void onClick(View v) {
        mListener.closeDetails();
    }

    public interface DetailsListener{

        void closeDetails();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
    }
}
