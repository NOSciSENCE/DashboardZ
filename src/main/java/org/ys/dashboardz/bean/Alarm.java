package org.ys.dashboardz.bean;

public class Alarm {
	private int id;
	private String time;
	private String type;
	private String rate;
	private String more;
	
	
	public Alarm(String time, String type, String rate) {
		super();
		this.time = time;
		this.type = type;
		this.rate = rate;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getMore() {
		return more;
	}
	public void setMore(String more) {
		this.more = more;
	}
	
}
