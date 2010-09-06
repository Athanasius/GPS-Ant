package org.miggy.android.gpsant;

import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import org.miggy.android.gpsant.GPSReader;

public class GPSSatsView extends Activity {
//////////////////////////////////////////////////////////////////////////////////////////
// Class globals
//////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private GPSReader myGPSReader;
    private boolean GPSReaderIsBound = false;
    
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

}
