package com.dant.entity;
import java.io.Serializable;

import org.json.*;

import com.dant.util.KeyGeneratorUtil;
@SuppressWarnings("serial")
public class Session implements Serializable {

	private String sessionId;
	private String idUser;
	private float latitude;
	private float longitude;
	private long time;

	public Session(String idUser, String rawData) {
		if (idUser.length() == 8) {
			if (rawData.length() == 0) {
				this.idUser = idUser;
				this.sessionId = KeyGeneratorUtil.generateKey(4);
				this.latitude = 0;
				this.longitude = 0;
				this.time = 0;
			} else {
				String[] array = rawData.split(";");

				if (array.length == 4) {
					this.idUser = idUser;
					this.sessionId = array[0];
					this.longitude = Float.parseFloat(array[1]);
					this.latitude = Float.parseFloat(array[2]);
					this.time = Long.parseLong(array[3]);
				}
			}
		}
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getIdUser(){
		return this.idUser;
	}

	@Override
	public String toString() {
		return this.sessionId + ";" + this.longitude + ";" + this.latitude + ";" + this.time;
	}
}
