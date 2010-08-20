package org.miggy.android.gpsant;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
//import android.location.GpsStatus;

public class GPSStatus extends Activity implements LocationListener {
	boolean GPSAllowed = false;
	boolean GPSActive = false;
	
	TextView myValueAllowed;
	TextView myValueActive;
	TextView myValueProvider;
	TextView myValueSatsSeen;
	TextView myValueSatsLocked;
	TextView myValueLatitude;
	TextView myValueLongitude;
	TextView myValueAltitude;
	TextView myValueSpeed;
	TextView myValueBearing;
	TextView myValueAccuracy;
	
	LocationManager locationManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    protected void onResume() {
     	super.onResume();
   
		// Get LocationManager to check GPS availability
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1.0f, this);

		GPSAllowed = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (GPSAllowed) {
			((TextView) findViewById(R.id.ValueAllowed)).setText("Y");
		} else {
			((TextView) findViewById(R.id.ValueAllowed)).setText("N");
		}

    }
            
    public void onProviderEnabled(String provider) {
    	// TODO Auto-generated method stub
    	
    	if (provider.equals(LocationManager.GPS_PROVIDER)) {
    		GPSAllowed = true;
    	}
    	
    }
    
    public void onProviderDisabled(String provider) {
    	// TODO Auto-generated method stub

    	if (provider.equals(LocationManager.GPS_PROVIDER)) {
    		GPSAllowed = false;
    	}
    }
    
    public void onLocationChanged(Location location) {
    
    	((TextView) findViewById(R.id.ValueProvider)).setText(location.getProvider());
    	((TextView) findViewById(R.id.ValueLatitude)).setText(Location.convert(location.getLatitude(), Location.FORMAT_DEGREES));
    	((TextView) findViewById(R.id.ValueLongitude)).setText(Location.convert(location.getLongitude(), Location.FORMAT_DEGREES));
    	if (location.hasAltitude()) {
    		((TextView) findViewById(R.id.ValueAltitude)).setText(String.valueOf(location.getAltitude()));
    	} else {
    		((TextView) findViewById(R.id.ValueAltitude)).setText(getString(R.string.DefaultAltitude));
    	}
    	if (location.hasSpeed()) {
    		((TextView) findViewById(R.id.ValueSpeed)).setText(String.valueOf(location.getSpeed()));
    	} else {
    		((TextView) findViewById(R.id.ValueSpeed)).setText(getString(R.string.DefaultSpeed));
    	}
    	if (location.hasBearing()) {
    		((TextView) findViewById(R.id.ValueBearing)).setText(String.valueOf(location.getBearing()));
    	} else {
    		((TextView) findViewById(R.id.ValueBearing)).setText(getString(R.string.DefaultBearing));
    	}
    	if (location.hasAccuracy()) {
    		((TextView) findViewById(R.id.ValueAccuracy)).setText(String.valueOf(location.getAccuracy()));
    	} else {
    		((TextView) findViewById(R.id.ValueAccuracy)).setText(getString(R.string.DefaultAccuracy));
    	}
    }
    
    public void onStatusChanged(String provider, int status, Bundle extras) {
    	// TODO Auto-generated method stub
    	
    	// This is where we'll be told about gaining or losing GPS lock
    }
    
}