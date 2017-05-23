// Casey Scott
// Java 1 - 1704
// MainActivity.Java
package com.fullsail.caseyscott.scottcasey_ce09;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiate the gridView Object
        GridView gridView = (GridView) findViewById(R.id.gridView);


        //Get a Connectivity manager object
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get the activity network info and store it into a variable
        //Check if the manager is null  --  Almost never null
        if(cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();

            //Check to see if the info returned a null value
            if(info != null){
                boolean isConnected = info.isConnected();

                if(isConnected){

                    //Network operations cannot be done on the Main thread  need to do on background thread
                    String apiURL = "https://www.googleapis.com/books/v1/volumes?q=android&filter=full&key=AIzaSyCa_os8ZkB7EtOwUPCZfcj5DkCbwx2InkU&maxResults=30";
                    new DataTasks(gridView, MainActivity.this).execute(apiURL);

                }

            }else {

                //Inform the user that the connection failed
                String noNet = "Network Unavailable.";
                Toast.makeText(MainActivity.this, noNet,Toast.LENGTH_LONG).show();
            }
        }
    }

    //Method for getting the network data
    public String getNetworkData(String urlString) {

        try {
            URL url = new URL(urlString);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

            //Use OutputStream for writing data
            //Use InputStream to read data
            InputStream is = connection.getInputStream();
            String data = IOUtils.toString(is);
            is.close();
            return data;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
