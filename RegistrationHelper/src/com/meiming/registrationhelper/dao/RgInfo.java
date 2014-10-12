package com.meiming.registrationhelper.dao;

public class RgInfo {
	private int id;
	private String date;
	private int period;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}

	@Override
	public String toString() {
		return "RgInfo [id=" + id + ", date=" + date + ", period=" + period
				+ "]";
	}
	
}
