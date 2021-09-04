package com.kjh.safechildren.data;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kjh.safechildren.Global;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GPS extends Service {
    private Activity activity;
    private Context context;
    private  LocationManager locationManager;
    private int minTime = 1000*60*5; //5m
    private int minDistance = 5;

    public GPS(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void getLocation() {
        try{
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
            locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

            boolean GPS_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean Net_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (Net_enabled){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        minTime, // 통지사이의 최소 시간간격 (miliSecond)
                        minDistance, // 통지사이의 최소 변경거리 (m)
                        gpsLocationListener);
            }
            if (GPS_enabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        minTime, // 통지사이의 최소 시간간격 (miliSecond)
                        minDistance, // 통지사이의 최소 변경거리 (m)
                        gpsLocationListener);
            }

        }catch (Exception e){
            Toast.makeText(context,"위치 정보를 불러오지 못했습니다.",Toast.LENGTH_LONG).show();

        }
    }

    public void removeGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(gpsLocationListener);
        }
    }




    public final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            try{
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                Global.user.setLocation(latitude+","+longitude);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                Global.user.setLastStatusUpdate(currentDateandTime);
                User_Firebase.updateUser();

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(context,"위치 정보를 불러오지 못했습니다.",Toast.LENGTH_LONG).show();
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
