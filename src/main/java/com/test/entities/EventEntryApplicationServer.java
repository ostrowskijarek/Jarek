package com.test.entities;

import javax.persistence.Entity;

@Entity
public class EventEntryApplicationServer extends EventEntry {
	private String type;
	private String host;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
}
