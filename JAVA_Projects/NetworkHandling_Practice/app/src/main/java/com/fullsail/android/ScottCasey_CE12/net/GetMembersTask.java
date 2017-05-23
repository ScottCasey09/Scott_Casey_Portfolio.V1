// Casey Scott
// Java 1 - 1704
// GetMembersTask.Java
package com.fullsail.android.ScottCasey_CE12.net;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.fullsail.android.ScottCasey_CE12.MainActivity;
import com.fullsail.android.ScottCasey_CE12.object.Member;

public class
GetMembersTask extends AsyncTask<Void, Void, ArrayList<Member>> {
	
	private static final String API_URL = "https://www.govtrack.us/api/v2/role?current=true";
	
	private final MainActivity mActivity;

	private ProgressDialog progressDialog;
	
	public GetMembersTask(MainActivity _activity) {
		mActivity = _activity;
	}
	
	@Override
	protected ArrayList<Member> doInBackground(Void... _params) {
		
		String data = NetworkUtils.getNetworkData(API_URL, mActivity);
		
		try {
			if (data != null) {
				JSONObject response = new JSONObject(data);

				JSONArray membersJson = response.getJSONArray("objects");

				ArrayList<Member> members = new ArrayList<>();

				for (int i = 0; i < membersJson.length(); i++) {
					JSONObject obj = membersJson.getJSONObject(i);
					JSONObject person = obj.getJSONObject("person");

					int id = person.getInt("id");
					String name = person.getString("name");
					String party = obj.getString("party");

					members.add(new Member(id, name, party));
				}

				mActivity.members = members;
				return members;
			}

			} catch(JSONException e){
				e.printStackTrace();
			}

		return null;
	}


	@Override
	protected void onPostExecute(ArrayList<Member> _result) {
		super.onPostExecute(_result);
		progressDialog.cancel();
		mActivity.showMembersListScreen(_result);
	}

	@Override
	protected void onPreExecute() {

		progressDialog = new ProgressDialog(mActivity);
		String loadingMessage = "Please Wait, Loading...";
		progressDialog.setMessage(loadingMessage);
		progressDialog.create();
		progressDialog.show();
	}
}