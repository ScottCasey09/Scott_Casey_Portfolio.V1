package fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullsail.caseyscott.customfragmentanimations.R;

public class DetailFragment extends Fragment implements View.OnClickListener{


    public static final String TAG = "DETAILS_FRAG";
    DetailsListener mListener;

    public static DetailFragment newInstance() {

        Bundle args = new Bundle();

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null){
            TextView detailText = (TextView) getView().findViewById(R.id.fragmentText);
            detailText.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        mListener.closeDetails();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail,container, false);
    }

    public static interface DetailsListener{
        public void openDetails();
        public void closeDetails();
    }
}
