// Casey Scott
// Java 1 - 1704
// DataTasks.Java
package com.fullsail.caseyscott.scottcasey_ce09;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.GridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class DataTasks extends AsyncTask<String, Void, ArrayList<Books>>{
    private ProgressDialog progressDialog;

    DataTasks(GridView gridView, Activity activity) {
        this.gridView = gridView;
        this.activity = activity;
    }

    private final GridView gridView;
    private final Activity activity;

    @Override
    protected void onPreExecute() {
        
        progressDialog = new ProgressDialog(activity);
        String loadingMessage = "Please Wait, Loading...";
        progressDialog.setMessage(loadingMessage);
        progressDialog.create();
        progressDialog.show();
    }
    @Override
    protected final ArrayList<Books> doInBackground(String... params) {

        MainActivity mainActivity = (MainActivity) activity;
        String data = mainActivity.getNetworkData(params[0]);
        ArrayList<Books> books = new ArrayList<>();

        //Parse the data

        try{

            // Outer most bracket signify an object
            JSONObject outerObj = new JSONObject(data);

            // Grab the array from within the object
            JSONArray array = outerObj.getJSONArray("items");

            //iterate through eh array object to get the desired info within

            for(int i = 0; i < array.length(); i++){
                //Get objects from the array
                JSONObject obj = array.getJSONObject(i);

                JSONObject volumeInfo = obj.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String imageUrl = imageLinks.getString("thumbnail");

                books.add(new Books(title, imageUrl));

            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return books;
    }
    @Override
    protected void onPostExecute(ArrayList<Books> s) {

        progressDialog.cancel();

        Adapter adapter = new Adapter(activity, s);
        gridView.setAdapter(adapter);
    }


}