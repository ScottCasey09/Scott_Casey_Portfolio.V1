package fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fullsail.caseyscott.customfragmentanimations.R;

public class MainFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = "MainFragment.TAG";
    DetailFragment.DetailsListener mListener;

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null) {
            Button openFragmentButton = (Button) getView().findViewById(R.id.buttonOpenFragment);
            openFragmentButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        mListener.openDetails();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailFragment.DetailsListener){
            mListener = (DetailFragment.DetailsListener) context;
        }else{
            Log.e(TAG, "onAttach: must be instance of OpenFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_main, container, false);
    }
}
