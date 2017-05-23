// Casey Scott
// Java 1 - 1704
// NetworkUtils.Java
package com.fullsail.android.ScottCasey_CE12.net;

import java.io.InputStream;
import java.net.URL;
import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.fullsail.android.ScottCasey_CE12.R;

import javax.net.ssl.HttpsURLConnection;

class NetworkUtils
{

	private static final String LOG_TAG = "PROJECT_DEBUGGING";
	
	private static boolean isConnected(final Context _context) {
		
		ConnectivityManager mgr = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if(mgr != null) {
			NetworkInfo info = mgr.getActiveNetworkInfo();

			if(info != null) {
				if(info.isConnected()) {
					return true;
				}

			}else {
				Handler h = new Handler(Looper.getMainLooper());
				h.post(new Runnable() {
					public void run() {
						Toast.makeText(_context, R.string.no_network, Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
		
		return false;
	}
	
	static String getNetworkData(String _url, Context _context) {

		try {
			if(isConnected(_context)) {
				URL url = new URL(_url);

				HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

				connection.connect();

				InputStream is = connection.getInputStream();

				String data = IOUtils.toString(is);

				is.close();

				connection.disconnect();

				return data;
			}


		}catch (Exception e){
			Log.e(LOG_TAG, "getNetworkData() \n" + e);
			e.printStackTrace();
		}
		
		return null;
	}
}