package com.dant.entity;

import java.io.Serializable;

public class Position implements Serializable{
	float longitude,latitude;
	long timeSinceLastUpdate;
	
	public Position(float longi, float lat, long time){
		longitude=longi;
		latitude=lat;
		timeSinceLastUpdate=time;
	}
	
	public float getLongitude(){
		return longitude;
	}
	
	public float getLatitude(){
		return latitude;
	}

	public long getTimeSinceLastUpdate(){
		return timeSinceLastUpdate;
	}
}
