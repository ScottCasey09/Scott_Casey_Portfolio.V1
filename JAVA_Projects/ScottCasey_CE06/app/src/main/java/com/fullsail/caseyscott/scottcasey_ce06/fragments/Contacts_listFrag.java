// Casey Scott
// AID - 1705
// Contacts_listFrag.JAVA

package com.fullsail.caseyscott.scottcasey_ce06.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import com.fullsail.caseyscott.scottcasey_ce06.Contact;
import com.fullsail.caseyscott.scottcasey_ce06.Objects.OpenDetailsListener;
import com.fullsail.caseyscott.scottcasey_ce06.R;
import com.fullsail.caseyscott.scottcasey_ce06.ViewAdapter;
import java.util.ArrayList;

public class Contacts_listFrag extends ListFragment {

    private final static String argContacts = "CONTACTS_ARGS";
    private OpenDetailsListener mListener;

    public static Contacts_listFrag newInstance(ArrayList<Contact> contacts) {

        Bundle args = new Bundle();
        args.putSerializable(argContacts, contacts);
        Contacts_listFrag fragment = new Contacts_listFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            ArrayList<String> contacts = getArguments().getStringArrayList(argContacts);
                if(contacts != null) {

                    ViewAdapter adapter = new ViewAdapter(getActivity(), contacts);
                    setListAdapter(adapter);
                }
        }
        if(getView() != null) {
            //Resign keyboard sometimes it gets stuck
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  OpenDetailsListener){
            mListener = (OpenDetailsListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_main,container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Contact item = (Contact)l.getAdapter().getItem(position);
        mListener.openDetails(item);

    }
}
