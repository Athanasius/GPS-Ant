package org.miggy.android.gpsant;

import android.widget.TableRow;
import android.widget.TextView;

public class GPSSat {
	int prn;
	float azimuth;
	float elevation;
	float snr;
	boolean almanac;
	boolean ephemeris;
	boolean used;

	TableRow row;
	TextView ID;
	TextView Azimuth;
	TextView Elevation;
	TextView SNR;
	TextView Almanac;
	TextView Ephemeris;
	
	public GPSSat() {
	}
	
	public void setData(int n_prn, float n_azimuth, float n_elevation, float n_snr, boolean n_almanac, boolean n_ephemeris, boolean n_used) {
		prn = n_prn;
		azimuth = n_azimuth;
		elevation = n_elevation;
		snr = n_snr;
		almanac = n_almanac;
		ephemeris = n_ephemeris;
		used = n_used;
	}
}
