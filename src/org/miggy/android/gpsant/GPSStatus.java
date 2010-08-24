//////////////////////////////////////////////////////////////////////////////////////////
// Class:GPSStatus
//
// Simply implements Location.GpsStatus.Listener to receive GPS provider updates.
// Then passes this information upstream to a GPSReader instance for handling.
//
// NB: The constructor that passes in a GPSReader should be used, so this class instance
//     can talk back to it.
//////////////////////////////////////////////////////////////////////////////////////////


package org.miggy.android.gpsant;

import android.location.GpsStatus;
import android.location.GpsSatellite;

public class GPSStatus implements GpsStatus.Listener {
	GPSReader myGpsReader = null;
	// Global for this so we can pass it for use in getGpsStatus() and not require a new one created/destroyed each time
	GpsStatus myGpsStatus = null;
	
	public GPSStatus() {
	} GPSStatus(GPSReader gpsReader) {
		myGpsReader = gpsReader;
	}
	
	public void onGpsStatusChanged(int event) {
		switch (event) {
		case GpsStatus.GPS_EVENT_STARTED:
			// Seems to be called when the GPS system wakes up from sleep (due to large minTime passed to requestLocationUpdates())
			myGpsReader.setActive(myGpsReader.getString(R.string.Started));
			break;
		case GpsStatus.GPS_EVENT_STOPPED:
			// Seems to be called when the GPS system sleeps (due to large minTime passed to requestLocationUpdates())
			myGpsReader.setActive(myGpsReader.getString(R.string.Stopped));
			myGpsReader.setSatsSeenDefault();
			myGpsReader.setSatsLockedDefault();
			break;
		case GpsStatus.GPS_EVENT_FIRST_FIX:
			myGpsReader.setActive(myGpsReader.getString(R.string.FirstFix));
			break;
		case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
			myGpsStatus = myGpsReader.locationManager.getGpsStatus(myGpsStatus);
			int seen = 0;
			int usedInFix = 0;
			for (GpsSatellite sat : myGpsStatus.getSatellites()) {
				seen++;
				if (sat.usedInFix()) {
					usedInFix++;
				}
			}
			myGpsReader.setSatsSeen(String.valueOf(seen));
			myGpsReader.setSatsLocked(String.valueOf(usedInFix));
			break;
		}
	}
	
}
