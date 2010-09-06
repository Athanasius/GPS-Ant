//////////////////////////////////////////////////////////////////////////////////////////
// Class:GPSDisplay
//
// Implements an Activity that displays to the user a GPS fix and miscellaneous
// information about it.
//
// Uses the Service GPSReader to actually get GPS info.
//////////////////////////////////////////////////////////////////////////////////////////

package org.miggy.android.gpsant;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

import org.miggy.android.gpsant.GPSReader;

public class GPSDisplay extends Activity {
//////////////////////////////////////////////////////////////////////////////////////////
// Class globals
//////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private GPSReader myGPSReader;
    private boolean GPSReaderIsBound = false;
//////////////////////////////////////////////////////////////////////////////////////////
	
//////////////////////////////////////////////////////////////////////////////////////////
// Access methods for other classes to read/set data
//////////////////////////////////////////////////////////////////////////////////////////
	public void setSatsSeen(String s) {
		((TextView) findViewById(R.id.ValueSatsSeen)).setText(s);
		updateLastInfo();
	}
	public void setSatsSeenDefault() {
		((TextView) findViewById(R.id.ValueSatsSeen)).setText(getString(R.string.DefaultSatsSeen));
		updateLastInfo();
	}

	public void setSatsLockedDefault() {
		((TextView) findViewById(R.id.ValueSatsLocked)).setText(getString(R.string.DefaultSatsLocked));
		updateLastInfo();
	}

	public void setSatsLocked(String s) {
		((TextView) findViewById(R.id.ValueSatsLocked)).setText(s);
		updateLastInfo();
	}

	public void setActive(String s) {
		((TextView) findViewById(R.id.ValueActive)).setText(s);
		updateLastInfo();
	}

	public void setAllowed(String s) {
		((TextView) findViewById(R.id.ValueAllowed)).setText(s);
		updateLastInfo();
	}

//////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////
// Startup code
//////////////////////////////////////////////////////////////////////////////////////////
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        GPSReaderIsBound = bindService(new Intent(GPSDisplay.this, GPSReader.class), GPSReaderConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection GPSReaderConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
        	myGPSReader = ((GPSReader.LocalBinder)service).getService(GPSDisplay.this);
        	
        	Toast.makeText(GPSDisplay.this, R.string.GPSReader_service_connected,
                    Toast.LENGTH_SHORT).show();
        }
        
        public void onServiceDisconnected(ComponentName className) {
        	myGPSReader = null;
        	
        	Toast.makeText(GPSDisplay.this, R.string.GPSReader_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };

    void doUnbindService() {
        if (GPSReaderIsBound) {
            // Detach our existing connection.
            unbindService(GPSReaderConnection);
            GPSReaderIsBound = false;
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
//////////////////////////////////////////////////////////////////////////////////////////
       
//////////////////////////////////////////////////////////////////////////////////////////
// Pause/Resume/Stop/Start code
//////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onResume() {
     	super.onResume();
   
     	// need to ask GPSReader to start it now...
     	//myGPSReader.startGPS();
    }

    @Override
	protected void onStart() {
		super.onStart();
		
		//myGPSReader.startGPS();
	}
    
    @Override
    protected void onPause() {
    	super.onPause();
    	
    	//myGPSReader.stopGPS();
    }

    @Override
    protected void onStop() {
    	super.onStop();
    	
    	//myGPSReader.stopGPS();
    }
    
//////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////
// Location updates
//////////////////////////////////////////////////////////////////////////////////////////
    public void updateLocation(Location location, String status) {
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
    	Date now = new Date(location.getTime());
    	DateFormat df = DateFormat.getTimeInstance();
    	((TextView) findViewById(R.id.ValueTimestamp)).setText(df.format(now));
    	((TextView) findViewById(R.id.ValueActive)).setText(status);
    }
    
    public void updateLastInfo() {
    	Date now = Calendar.getInstance().getTime();
    	DateFormat df = DateFormat.getTimeInstance();
    	((TextView) findViewById(R.id.ValueLastInfo)).setText(df.format(now));
    }
//////////////////////////////////////////////////////////////////////////////////////////

    
//////////////////////////////////////////////////////////////////////////////////////////
// Menu code
//////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
    	
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.About:
    		View aboutView = getLayoutInflater().inflate(R.layout.about, null, false);
    		
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle(R.string.AboutTitle);
    		builder.setView(aboutView);
    		builder.create();
    		builder.show();
    		break;
    	case R.id.Satellites:
//    		Intent satsIntent = new Intent("org.miggy.android.gpsant.sats");
//    		satsIntent.addCategory("org.miggy.android.gpsant.subview");
    		Intent satsIntent = new Intent(getApplicationContext(), GPSSatsView.class);
    		startActivity(satsIntent);
    		break;
    	case R.id.Exit:
    		finish();
    		return true;
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
//////////////////////////////////////////////////////////////////////////////////////////
}