package org.miggy.android.gpsant;

import android.location.GpsStatus;
import android.location.GpsSatellite;

import org.miggy.android.gpsant.GPSDisplay;


public class GPSStatus implements GpsStatus.Listener {
	GPSDisplay myGpsDisplay = null;
	GpsStatus myGpsStatus = null;
	
	public GPSStatus() {
	} GPSStatus(GPSDisplay gpsDisplay) {
		myGpsDisplay = gpsDisplay;
	}
	
	public void onGpsStatusChanged(int event) {
		switch (event) {
		case GpsStatus.GPS_EVENT_STARTED:
			// Seems to be called when the GPS system wakes up from sleep (due to large minTime passed to requestLocationUpdates())
			myGpsDisplay.setActive(myGpsDisplay.getString(R.string.Started));
			break;
		case GpsStatus.GPS_EVENT_STOPPED:
			// Seems to be called when the GPS system sleeps (due to large minTime passed to requestLocationUpdates())
			myGpsDisplay.setActive(myGpsDisplay.getString(R.string.Stopped));
			myGpsDisplay.setSatsSeenDefault();
			myGpsDisplay.setSatsLockedDefault();
			break;
		case GpsStatus.GPS_EVENT_FIRST_FIX:
			myGpsDisplay.setActive(myGpsDisplay.getString(R.string.FirstFix));
			break;
		case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
			myGpsStatus = myGpsDisplay.locationManager.getGpsStatus(myGpsStatus);
			int seen = 0;
			int usedInFix = 0;
			for (GpsSatellite sat : myGpsStatus.getSatellites()) {
				seen++;
				if (sat.usedInFix()) {
					usedInFix++;
				}
			}
			myGpsDisplay.setSatsSeen(String.valueOf(seen));
			myGpsDisplay.setSatsLocked(String.valueOf(usedInFix));
			break;
		}
	}
	
}
