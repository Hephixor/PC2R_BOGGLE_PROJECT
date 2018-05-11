package main.messages;

import java.io.Serializable;

public class User implements Serializable {

	String name;
	String picture;
	Status status;
	int score;

	public int getScore() {
		return this.score;
	}

	public void setScore(int s) {
		this.score = s ; 
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


}
