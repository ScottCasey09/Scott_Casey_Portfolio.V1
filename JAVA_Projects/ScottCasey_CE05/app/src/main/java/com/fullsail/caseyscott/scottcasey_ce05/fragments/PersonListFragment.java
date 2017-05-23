// Casey Scott
// AID - 1705
// PersonListFragment.java

package com.fullsail.caseyscott.scottcasey_ce05.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.fullsail.caseyscott.scottcasey_ce05.Administrator;
import com.fullsail.caseyscott.scottcasey_ce05.Persons;
import com.fullsail.caseyscott.scottcasey_ce05.R;
import com.fullsail.caseyscott.scottcasey_ce05.Student;
import com.fullsail.caseyscott.scottcasey_ce05.Teacher;
import com.fullsail.caseyscott.scottcasey_ce05.ViewAdapter;

import java.util.ArrayList;

public class PersonListFragment extends ListFragment {

    private static  final  String argPersons = "ARG_PERSONS";

    public static PersonListFragment newInstance(ArrayList<Persons> array) {

        Bundle args = new Bundle();
        args.putSerializable(argPersons, array);
        PersonListFragment fragment = new PersonListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            ArrayList<String> persons = getArguments().getStringArrayList(argPersons);

            if(persons != null) {
                ViewAdapter adapter = new ViewAdapter(getActivity(), persons);
                setListAdapter(adapter);

                setListAdapter(adapter);
            }

        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (l.getAdapter().getItem(position).getClass().equals(Student.class)){
            Student item = (Student) l.getAdapter().getItem(position);
            Toast.makeText(getActivity(), item.toString(), Toast.LENGTH_SHORT).show();
        }else if ( l.getAdapter().getItem(position).getClass().equals(Teacher.class)){
            Teacher item = (Teacher) l.getAdapter().getItem(position);
            Toast.makeText(getActivity(), item.toString(), Toast.LENGTH_SHORT).show();
        }else if ( l.getAdapter().getItem(position).getClass().equals(Administrator.class)){
            Administrator item = (Administrator) l.getAdapter().getItem(position);
            Toast.makeText(getActivity(), item.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}
