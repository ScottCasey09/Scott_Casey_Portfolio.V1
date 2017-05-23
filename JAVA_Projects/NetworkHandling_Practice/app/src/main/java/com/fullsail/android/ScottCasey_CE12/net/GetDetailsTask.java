// Casey Scott
// Java 1 - 1704
// GetDetailsTask.Java
package com.fullsail.android.ScottCasey_CE12.net;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.fullsail.android.ScottCasey_CE12.MainActivity;
import com.fullsail.android.ScottCasey_CE12.R;

public class GetDetailsTask extends AsyncTask<Integer, String, HashMap<String, String>> {
	
	private static final String API_URL = "https://www.govtrack.us/api/v2/person/";
	
	private static final String NAME = "name";
	private static final String BIRTHDAY = "birthday";
	private static final String GENDER = "gender";
	private static final String TWITTER_ID = "twitter_id";
	private static final String NUM_COMMITTEES = "num_committees";
	private static final String NUM_ROLES = "num_roles";
	
	private ProgressDialog progressDialog;
	
	private final MainActivity mActivity;
	
	public GetDetailsTask(MainActivity _activity) {
		mActivity = _activity;
	}
	
	@Override
	protected HashMap<String, String> doInBackground(Integer... _params) {
		
		// Add member ID to the end of the URL
		String data = NetworkUtils.getNetworkData(API_URL + _params[0], mActivity);
		HashMap<String, String> retValues = new HashMap<>();
		
		try {
			if(data!=null) {
				JSONObject response = new JSONObject(data);

				String birthday = response.getString("birthday");
				retValues.put(BIRTHDAY, birthday);

				JSONArray committeeArray = response.getJSONArray("committeeassignments");
				int numCommittees = committeeArray.length();
				retValues.put(NUM_COMMITTEES, "" + numCommittees);

				String name = response.getString("name");
				retValues.put(NAME, name);

				String gender = response.getString("gender_label");
				retValues.put(GENDER, gender);

				JSONArray rolesArray = response.getJSONArray("roles");
				int numRoles = rolesArray.length();
				retValues.put(NUM_ROLES, "" + numRoles);

				String twitterId = response.getString("twitterid");
				retValues.put(TWITTER_ID, twitterId);
			}
			
		} catch(JSONException e) {
			e.printStackTrace();
		}

		// Update the UI
		String name = retValues.get(NAME);
		String birthday = retValues.get(BIRTHDAY);
		String gender = retValues.get(GENDER);
		String twitterId = retValues.get(TWITTER_ID);
		String numCommittees = retValues.get(NUM_COMMITTEES);
		String numRoles = retValues.get(NUM_ROLES);
		mActivity.populateMemberDetailsScreen(" "+name, " "+birthday, " "+gender, " "+twitterId, " "+numCommittees, " "+numRoles);
		
		return retValues;
	}
	
	@Override
	protected void onPostExecute(HashMap<String, String> _result) {
		super.onPostExecute(_result);
		progressDialog.cancel();
	}

	@Override
	protected void onPreExecute() {

		progressDialog = new ProgressDialog(mActivity);
		String loadingMessage = mActivity.getString(R.string.loading);
		progressDialog.setMessage(loadingMessage);
		progressDialog.create();
		progressDialog.show();
	}

}