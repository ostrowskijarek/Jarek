package com.test.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.test.enums.EntryState;

@Entity
public class EventEntry {

	@Id
	protected String id;
	@Transient
	protected EntryState state;
	@Transient
	protected long timestamp;
	protected int duration;
	protected boolean alert;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EntryState getState() {
		return state;
	}

	public void setState(EntryState state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "id: "+id+", state: "+state+", timestamp: "+timestamp;
	}
}
