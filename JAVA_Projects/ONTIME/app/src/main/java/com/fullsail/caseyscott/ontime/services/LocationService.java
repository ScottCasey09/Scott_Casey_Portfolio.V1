// Casey Scott
// FINAL PROJECT - 1712
// LocationService.java

package com.fullsail.caseyscott.ontime.services;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.fullsail.caseyscott.ontime.R;

import static android.content.ContentValues.TAG;


public class LocationService implements LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;
    private final static boolean forceNetwork = false;
    public static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 0x002;
    private static LocationService instance = null;

    public Location location;
    public double longitude;
    public double latitude;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;

    public static LocationService getLocationManager(Context context)     {
        if (instance == null) {
            instance = new LocationService(context);
        }
        return instance;
    }

    public LocationService(Context context)     {

        initLocationService(context);
        Log.i(TAG,"LocationService created");
    }

    @TargetApi(23)
    private void initLocationService(Context context) {


        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        try   {
            this.longitude = 0.0;
            this.latitude = 0.0;
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // Get GPS and network status
            if (locationManager != null) {
                this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
            if (locationManager != null) {
                this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }

            if (forceNetwork) isGPSEnabled = false;

            //boolean locationServiceAvailable;
            if (!isNetworkEnabled && !isGPSEnabled)    {
                // cannot get location
                Toast.makeText(context, R.string.no_location, Toast.LENGTH_SHORT).show();
            }
            //else
            {

                if (isNetworkEnabled) {
                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    }
                    if (locationManager != null)   {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    }
                }//end if

                if (isGPSEnabled)  {
                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    }

                    if (locationManager != null)  {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    }
                }
            }
        } catch (Exception ex)  {
            Log.e(TAG, "Error creating location service: " + ex.getMessage() );

        }
    }

//    public boolean isLocationEnabled(final Context context){
//        LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
//        if (manager != null) {
//            if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                //Ask the user to enable GPS
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Location Manager");
//                builder.setMessage("Would you like to enable GPS?");
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Launch settings, allowing user to make a change
//                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        context.startActivity(i);
//                    }
//                });
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //No location service, no Activity
//                        dialog.dismiss();
//                    }
//                });
//                builder.create().show();
//            }else {
//                return true;
//            }
//        }
//        return false;
//    }

//    public boolean checkLocationPermissions(Context context){
//        if ( Build.VERSION.SDK_INT >= 23 &&
//                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }else {
//
//            int permission = PermissionChecker.checkSelfPermission(context, LocationManager.GPS_PROVIDER);
//
//            if (permission == PermissionChecker.PERMISSION_GRANTED) {
//                    return true;
//            } else {
//                final AlertDialog dialog = new AlertDialog.Builder(context).create();
//                dialog.setTitle(context.getString(R.string.permission_issue));
//                dialog.setMessage(context.getString(R.string.get_permissions));
//                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//                return  false;
//            }
//        }
//
//    }



    @Override
    public void onLocationChanged(Location location)     {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
