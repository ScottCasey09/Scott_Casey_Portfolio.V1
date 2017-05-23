package com.fullsail.caseyscott.scottcasey_ce05.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fullsail.caseyscott.scottcasey_ce05.R;

public class EmptyFragment extends Fragment {

    public static EmptyFragment newInstance() {
        
        Bundle args = new Bundle();
        
        EmptyFragment fragment = new EmptyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.empty, container, false);
    }
}
