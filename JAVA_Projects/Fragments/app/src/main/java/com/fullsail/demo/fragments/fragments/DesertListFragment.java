package com.fullsail.demo.fragments.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fullsail.demo.fragments.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class DesertListFragment extends ListFragment {
    private static  final  String argDeserts = "ARG_DESERTS";

    DesertListener mListener;

    public static interface  DesertListener{
        public void getDesert(int position);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.getDesert(position);
    }

    public static DesertListFragment newInstance(ArrayList<String> deserts) {

        Bundle args = new Bundle();

        args.putStringArrayList(argDeserts, deserts);
        DesertListFragment fragment = new DesertListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DesertListener){
        mListener = (DesertListener) context;
        }else{
            Log.e(TAG, "onAttach: " + context.toString() + " must implement DesertListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            ArrayList<String> deserts = getArguments().getStringArrayList(argDeserts);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_list_item_1, deserts
            );

            setListAdapter(adapter);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_desert, container,false);
    }
}

