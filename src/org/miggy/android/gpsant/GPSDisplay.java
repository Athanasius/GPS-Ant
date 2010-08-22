package org.miggy.android.gpsant;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

import org.miggy.android.gpsant.GPSStatus;

public class GPSDisplay extends Activity implements LocationListener {
	boolean GPSAllowed = false;
	boolean GPSActive = false;
	GPSStatus gpsStatus = new GPSStatus(this);
	LocationManager locationManager = null;
	
	public void setSatsSeen(String s) {
		((TextView) findViewById(R.id.ValueSatsSeen)).setText(s);
	}
	public void setSatsSeenDefault() {
		((TextView) findViewById(R.id.ValueSatsSeen)).setText(getString(R.string.DefaultSatsSeen));
	}

	public void setSatsLockedDefault() {
		((TextView) findViewById(R.id.ValueSatsLocked)).setText(getString(R.string.DefaultSatsLocked));
	}

	public void setSatsLocked(String s) {
		((TextView) findViewById(R.id.ValueSatsLocked)).setText(s);
	}

	public void setActive(String s) {
		((TextView) findViewById(R.id.ValueActive)).setText(s);
	}

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
				
		// Hook in our GpsStatus.Listener
		locationManager.addGpsStatusListener(gpsStatus);

    }
            
    public void onProviderEnabled(String provider) {
    	
    	if (provider.equals(LocationManager.GPS_PROVIDER)) {
    		GPSAllowed = true;
    	}
    	
    }
    
    public void onProviderDisabled(String provider) {

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
    	Date now = Calendar.getInstance().getTime();
    	DateFormat df = DateFormat.getTimeInstance();
    	((TextView) findViewById(R.id.ValueTimestamp)).setText(df.format(now));
    	
    }
    
    public void onStatusChanged(String provider, int status, Bundle extras) {
    	
    	if (provider.equals(LocationManager.GPS_PROVIDER)) {
    		switch (status) {
    		case LocationProvider.OUT_OF_SERVICE:
    			GPSActive = false;
    			((TextView) findViewById(R.id.ValueActive)).setText("Out of Service");
   			break;
    		case LocationProvider.TEMPORARILY_UNAVAILABLE:
    			GPSActive = false;
    			((TextView) findViewById(R.id.ValueActive)).setText("Temp. Unavail");
    			setSatsSeenDefault();
    			setSatsLockedDefault();
    			break;
    		case LocationProvider.AVAILABLE:
    			GPSActive = true;
    			((TextView) findViewById(R.id.ValueActive)).setText("Available");
    			break;
    		}
    	}
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
    	
    	return super.onCreateOptionsMenu(menu);
    	
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	
    	switch (item.getItemId()) {
    	case R.id.About:
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("About");
    		builder.setMessage(getString(R.string.AboutText) + " " + getString(R.string.Version));
    		builder.setCancelable(true);
    		builder.show();
    		break;
    	case R.id.Exit:
    		finish();
    		return true;
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
}