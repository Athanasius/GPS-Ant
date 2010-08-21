package org.miggy.android.gpsant;

import android.app.Activity;
import android.os.Bundle;
import android.location.GpsStatus;
// import android.location.GpsStatus.Listener;

public class GPSStatus extends Activity implements GpsStatus.Listener {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    }

	public void onGpsStatusChanged(int event) {
		// TODO Auto-generated method stub
		switch (event) {
		case GpsStatus.GPS_EVENT_STARTED:
			break;
		case GpsStatus.GPS_EVENT_STOPPED:
			break;
		case GpsStatus.GPS_EVENT_FIRST_FIX:
			break;
		case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
			break;
		}
	}
	
}
