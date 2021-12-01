package org.techtown.location_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    Location mLastlocation = null;
    List<String> listProviders;
    String TAG = "LocationProvider";

    TextView tvGpsEnable, tvGpsLatitude, tvGpsLongitude;

    int on=1;

    TextView Speed;
    double speed;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvGpsEnable = (TextView) findViewById(R.id.tvGpsEnable);
        tvGpsLatitude = (TextView) findViewById(R.id.GpsLatitude);
        tvGpsLongitude = (TextView) findViewById(R.id.GpsLongitude);
        Speed = (TextView)findViewById(R.id.Speed);


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return ;
        }

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(lastKnownLocation != null) {
            double lng = lastKnownLocation.getLongitude();
            double lat = lastKnownLocation.getLatitude();
            Log.d(TAG, "longitude=" +lng+ ", latitude=" + lat);
            tvGpsLatitude.setText(Double.toString(lat));
            tvGpsLongitude.setText(Double.toString(lng));
        }

        listProviders = locationManager.getAllProviders();
        boolean [] isEnable = new boolean[3];
        for(int i=0; i< listProviders.size(); i++) {
            if(listProviders.get(i).equals(LocationManager.GPS_PROVIDER)) {
                isEnable[0] = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                tvGpsEnable.setText(": "+ String.valueOf(isEnable[0]));

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, (android.location.LocationListener)this);
            }
        }

        Log.d(TAG, listProviders.get(0) + '/' + String.valueOf(isEnable[0]));

    }


    @Override
    public void onProviderEnabled(String provider) {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, (LocationListener) this);
    }

    @Override
    public void onLocationChanged(Location location){

        double latitude = 0.0;
        double longitude = 0.0;

        if(location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            tvGpsLongitude.setText(": " + Double.toString(latitude));
            tvGpsLongitude.setText(": " + Double.toString(longitude));
            Log.d(TAG + "GPS: ", Double.toString(latitude) + '/' + Double.toString(longitude));
        }

        String getSpeed = String.format("%.3f", location.getSpeed());
        Speed.setText(getSpeed);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){

    }

    @Override
    public void onProviderDisabled(String provider){

    }

    @Override
    protected void onStart(){
        super.onStart();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
               return;
            } else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        locationManager.removeUpdates((LocationListener) this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, (LocationListener) this);
    }








}