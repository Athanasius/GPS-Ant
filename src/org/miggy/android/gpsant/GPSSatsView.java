package org.miggy.android.gpsant;

import java.util.Vector;

import android.os.Bundle;
import android.os.IBinder;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.widget.Toast;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import org.miggy.android.gpsant.GPSReader;
import org.miggy.android.gpsant.GPSSat;

public class GPSSatsView extends Activity {
//////////////////////////////////////////////////////////////////////////////////////////
// Class globals
//////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private GPSReader myGPSReader;
    private boolean GPSReaderIsBound = false;
    Vector<GPSSat> mySats = new Vector<GPSSat>(32, 4);
    private static int SatTextSize = 12;
//////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////
// Startup code
//////////////////////////////////////////////////////////////////////////////////////////
 	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.sats);
        GPSReaderIsBound = bindService(new Intent(GPSSatsView.this, GPSReader.class), GPSReaderConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection GPSReaderConnection = new ServiceConnection() {
    	public void onServiceConnected(ComponentName className, IBinder service) {
        	myGPSReader = ((GPSReader.LocalBinder)service).getService(GPSSatsView.this);
         	
         	Toast.makeText(GPSSatsView.this, R.string.GPSReader_service_connected,
                     Toast.LENGTH_SHORT).show();
        }
         
        public void onServiceDisconnected(ComponentName className) {
         	myGPSReader = null;
         	
         	Toast.makeText(GPSSatsView.this, R.string.GPSReader_service_disconnected,
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

    public void SatData(int prn, float azimuth, float elevation, float snr, boolean almanac, boolean ephemeris, boolean used) {
    	GPSSat mySat = null;
    	for (int s = 0; s < mySats.size(); s++) {
    		if (((GPSSat) mySats.elementAt(s)).prn == prn) {
    			mySat = (GPSSat) mySats.elementAt(s);
    	    	mySat.setData(prn, azimuth, elevation, snr, almanac, ephemeris, used);
    			break;
    		}
    	}
    	if (mySat == null) {
    		mySat = new GPSSat();
    		mySat.setData(prn, azimuth, elevation, snr, almanac, ephemeris, used);
			mySat.row = new TableRow(this);
			mySat.row.addView(mySat.ID = new TextView(this));
			mySat.ID.setGravity(Gravity.LEFT);
			mySat.ID.setTextSize(SatTextSize);
			mySat.row.addView(mySat.Azimuth = new TextView(this));
			mySat.Azimuth.setGravity(Gravity.RIGHT);
			mySat.Azimuth.setTextSize(SatTextSize);
			mySat.row.addView(mySat.Elevation = new TextView(this));
			mySat.Elevation.setGravity(Gravity.RIGHT);
			mySat.Elevation.setTextSize(SatTextSize);
			mySat.row.addView(mySat.SNR = new TextView(this));
			mySat.SNR.setGravity(Gravity.CENTER_HORIZONTAL);
			mySat.SNR.setTextSize(SatTextSize);
			mySat.row.addView(mySat.Almanac = new TextView(this));
			mySat.Almanac.setGravity(Gravity.CENTER_HORIZONTAL);
			mySat.Almanac.setTextSize(SatTextSize);
			mySat.row.addView(mySat.Ephemeris = new TextView(this));
			mySat.Ephemeris.setGravity(Gravity.CENTER_HORIZONTAL);
			mySat.Ephemeris.setTextSize(SatTextSize);
			((TableLayout)findViewById(R.id.SatsLayout)).addView(mySat.row);
			mySats.addElement(mySat);
    	}
		mySat.ID.setText(String.format("%d", prn));
		mySat.Azimuth.setText(String.format("%5.1f", azimuth));
		mySat.Elevation.setText(String.format("%5.1f", elevation));
		mySat.SNR.setText(String.format("%2.1f", snr));
		mySat.Almanac.setText(almanac ? "Y" : "N");
		mySat.Ephemeris.setText(ephemeris ? "Y" : "N");
		if (mySat.used) {
			mySat.row.setBackgroundColor(this.getResources().getColor(R.color.ColorSatUsed));
		} else {
			mySat.row.setBackgroundColor(this.getResources().getColor(R.color.ColorSatUnused));			
		}
    }

}
