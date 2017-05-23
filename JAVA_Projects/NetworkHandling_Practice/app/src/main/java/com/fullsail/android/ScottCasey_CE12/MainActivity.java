// Casey Scott
// Java 1 - 1704
// MainActivity.Java

package com.fullsail.android.ScottCasey_CE12;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fullsail.android.ScottCasey_CE12.net.GetDetailsTask;
import com.fullsail.android.ScottCasey_CE12.net.GetMembersTask;
import com.fullsail.android.ScottCasey_CE12.object.Member;

public class MainActivity extends Activity {
	
	private View mMembersListScreen;
	private View mMemberDetailsScreen;
    public ArrayList<Member> members;

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mMembersListScreen = findViewById(R.id.members_list_screen);
		mMemberDetailsScreen = findViewById(R.id.member_details_screen);
		
		GetMembersTask task = new GetMembersTask(this);

            task.execute();

	}
	
	public void showMembersListScreen(ArrayList<Member> _members) {
		try {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mMemberDetailsScreen.setVisibility(View.GONE);
					mMembersListScreen.setVisibility(View.VISIBLE);

				}
			});

			ListView lv = (ListView) mMembersListScreen.findViewById(R.id.members_list);
			lv.setAdapter(new MembersAdapter(this, _members));
            lv.setOnItemClickListener(mItemClickListener);

		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void showMemberDetailsScreen(int _id) {
		mMembersListScreen.setVisibility(View.GONE);
		mMemberDetailsScreen.setVisibility(View.VISIBLE);
		
		GetDetailsTask task = new GetDetailsTask(this);
		task.execute(_id);
	}

	public void populateMemberDetailsScreen(final String _name,final String _birthday, final String _gender,
			final String _twitterId, final String _numCommittees, final String _numRoles) {
		try {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

            TextView tv = (TextView) findViewById(R.id.text_name);
            tv.setText(_name);

            tv = (TextView) findViewById(R.id.text_birthday);
            tv.setText(_birthday);

            tv = (TextView) findViewById(R.id.text_gender);
            tv.setText(_gender);

            tv = (TextView) findViewById(R.id.text_twitter_id);
            tv.setText(_twitterId);

            tv = (TextView) findViewById(R.id.text_num_committees);
            tv.setText(_numCommittees);

            tv = (TextView) findViewById(R.id.text_num_roles);
            tv.setText(_numRoles);
                }
            });
        }catch (Exception e){
            e.printStackTrace();

        }
	}
	
	private final AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> _parent, View _view, int _position, long _id) {

            ConnectivityManager mgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(mgr != null) {

                NetworkInfo info = mgr.getActiveNetworkInfo();

                if(info != null) {
                    showMemberDetailsScreen(members.get(_position).getId());
                }
                else {
                    Toast.makeText(MainActivity.this, R.string.no_network, Toast.LENGTH_SHORT).show();
                }
            }
		}
		
	};
	
	public void onBackPressed() {
		if(mMemberDetailsScreen.getVisibility() == View.VISIBLE) {
			mMemberDetailsScreen.setVisibility(View.GONE);
			mMembersListScreen.setVisibility(View.VISIBLE);
		} else {
			super.onBackPressed();
		}
	}

}
