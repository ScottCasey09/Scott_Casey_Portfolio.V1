// Casey Scott
// FINAL PROJECT - 1712
// NetworkUtils.java

package com.fullsail.caseyscott.ontime.helpers;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    private final Context context;

   public NetworkUtils(Context context){
       this.context = context;
   }

    public boolean checkInternetConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return  activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }
}
