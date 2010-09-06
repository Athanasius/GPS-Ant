//////////////////////////////////////////////////////////////////////////////////////////
// Class:GPSReader
//
// This class is a Service that takes care of receiving updates from the GPS
// LocationProvider.  It uses an instance of GPSStatus to read status changes
// of the GPS provider, including satellite info.
//
// Makes calls to GPSDisplay to update the UI.
//////////////////////////////////////////////////////////////////////////////////////////


package org.miggy.android.gpsant;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import org.miggy.android.gpsant.GPSStatus;


public class GPSReader extends Service implements LocationListener {
//////////////////////////////////////////////////////////////////////////////////////////
// Class globals
//////////////////////////////////////////////////////////////////////////////////////////
	boolean GPSAllowed = false;
	boolean GPSActive = false;
	GPSStatus myGPSStatus = new GPSStatus(this);
	GPSDisplay myGPSDisplay = null;
	LocationManager locationManager = null;
	long defaultMinTime = 1000L;
	float defaultMinDistance = 1.0f;
//////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////
// Access methods for other classes to read/set data
//////////////////////////////////////////////////////////////////////////////////////////
	public void setActive(String s) {
		if (myGPSDisplay != null) {
			myGPSDisplay.setActive(s);
		}
	}
		
	public void setSatsSeenDefault() {
		if (myGPSDisplay != null) {
			myGPSDisplay.setSatsSeenDefault();
		}
	}
		
	public void setSatsLockedDefault() {
		if (myGPSDisplay != null) {
			myGPSDisplay.setSatsLockedDefault();
		}
	}
		
	public void setSatsSeen(String s) {
		if (myGPSDisplay != null) {
			myGPSDisplay.setSatsSeen(s);
		}
	}
		
	public void setSatsLocked(String s) {
		if (myGPSDisplay != null) {
			myGPSDisplay.setSatsLocked(s);
		}
	}
	
	public void startGPS() {
		startGPS(-1L, 1.0f);
	}
	public void startGPS(long minTime, float minDistance) {
		if (minTime == -1L) {
			minTime = defaultMinTime;
		}
		if (minDistance == -1.0f) {
			minDistance = defaultMinDistance;
		}

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
		GPSAllowed = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
				
		// Hook in our GpsStatus.Listener
		locationManager.addGpsStatusListener(myGPSStatus);
    }
    
    void stopGPS() {
    	locationManager.removeGpsStatusListener(myGPSStatus);
    	locationManager.removeUpdates(this);
		if (myGPSDisplay != null) {
			myGPSDisplay.updateLastInfo();
		}
    }

//////////////////////////////////////////////////////////////////////////////////////////
		
//////////////////////////////////////////////////////////////////////////////////////////
// Startup code
//////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        GPSReader getService(GPSDisplay GpsDisplay) {
        	myGPSDisplay = GpsDisplay;
        	if (GPSAllowed) {
    			myGPSDisplay.setAllowed(getString(R.string.GPSAllowedEnabled));
    		} else {
    			myGPSDisplay.setAllowed(getString(R.string.GPSAllowedDisabled));
    		}
    		// See if there's a stored last position
    		Location last = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    		if (last != null) {
    			myGPSDisplay.updateLocation(last, getString(R.string.OldFix));
    		}
    		myGPSDisplay.updateLastInfo();
    		
            return GPSReader.this;
        }
        
        GPSReader getService(GPSSatsView GpsSatsView) {
        	return GPSReader.this;
        }
    }
    
    private NotificationManager mNM;
    @Override
	public void onCreate() {
    	mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	// Display a notification about us starting.  We put an icon in the status bar.
    	showNotification();
    	
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		startGPS();
	}
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	startGPS();
    	return super.onStartCommand(intent, flags, startId);
   	}
    
    @Override
    public void onDestroy() {
    	stopGPS();
    	myGPSDisplay = null;
    	myGPSStatus = null;
    	// Cancel the persistent notification.
    	mNM.cancel(R.string.GPSReader_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.GPSReader_stopped, Toast.LENGTH_SHORT).show();
    }
    
    // This is the object that receives interactions from clients.
    private final IBinder mBinder = new LocalBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.GPSReader_started);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.icon, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, GPSDisplay.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.GPSReader_service_label),
                       text, contentIntent);

        // Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        mNM.notify(R.string.GPSReader_started, notification);
    }
//////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////
//Status change/update code
//////////////////////////////////////////////////////////////////////////////////////////
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
    	if (myGPSDisplay != null) {
		 	myGPSDisplay.updateLocation(location, getString(R.string.GotFix));
    	}
    }
       
    public void onStatusChanged(String provider, int status, Bundle extras) {
    	if (provider.equals(LocationManager.GPS_PROVIDER)) {
    		switch (status) {
    		case LocationProvider.OUT_OF_SERVICE:
    			GPSActive = false;
    			if (myGPSDisplay != null) {
    				myGPSDisplay.setActive(getString(R.string.OutOfService));
        			myGPSDisplay.updateLastInfo();
    			}
    			break;
    		case LocationProvider.TEMPORARILY_UNAVAILABLE:
    			GPSActive = false;
    			if (myGPSDisplay != null) {
    				myGPSDisplay.setActive(getString(R.string.TempUnavail));
    				myGPSDisplay.setSatsSeenDefault();
    				myGPSDisplay.setSatsLockedDefault();
    				myGPSDisplay.updateLastInfo();
    			}
    			break;
    		case LocationProvider.AVAILABLE:
    			GPSActive = true;
    			if (myGPSDisplay != null) {
    				myGPSDisplay.setActive(getString(R.string.Available));
    				myGPSDisplay.updateLastInfo();
    			}
    			break;
    		}
    	}
    }

}
