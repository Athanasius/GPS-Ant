package org.miggy.android.gpsant;

import android.location.GpsStatus;
import android.location.GpsSatellite;

import org.miggy.android.gpsant.GPSDisplay;


public class GPSStatus implements GpsStatus.Listener {
	GPSDisplay myGpsDisplay = null;
	GpsStatus myGpsStatus = null;
	
	public GPSStatus() {
		// TODO Auto-generated constructor stub
	} GPSStatus(GPSDisplay gpsDisplay) {
		myGpsDisplay = gpsDisplay;
	}
	
	public void onGpsStatusChanged(int event) {
		// TODO Auto-generated method stub
		switch (event) {
		case GpsStatus.GPS_EVENT_STARTED:
			myGpsDisplay.setActive("Started");
			break;
		case GpsStatus.GPS_EVENT_STOPPED:
			myGpsDisplay.setActive("Stopped");
			myGpsDisplay.setSatsSeenDefault();
			myGpsDisplay.setSatsLockedDefault();
			break;
		case GpsStatus.GPS_EVENT_FIRST_FIX:
			myGpsDisplay.setActive("First fix");
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
