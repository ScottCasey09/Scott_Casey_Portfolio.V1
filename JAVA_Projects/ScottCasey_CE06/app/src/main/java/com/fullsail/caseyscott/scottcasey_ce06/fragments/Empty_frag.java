// Casey Scott
// AID - 1705
// Empty_frag.JAVA

package com.fullsail.caseyscott.scottcasey_ce06.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fullsail.caseyscott.scottcasey_ce06.R;

public class Empty_frag extends Fragment {

    public static Empty_frag newInstance() {

        Bundle args = new Bundle();
        Empty_frag fragment = new Empty_frag();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.empty_fragment, container, false);
    }
}
