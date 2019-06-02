package org.ys.dashboardz.bean;

public class Server {
	private int id;
	private String ip;
	private String name;
	private String rootpwd;
	private int ownerid;
	private String status;
	
	public Server() {
		
	}
	public Server(String ip, String usr, String pwd) {
		this.ip = ip;
		this.name = usr;
		this.rootpwd = pwd;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOwnerid() {
		return ownerid;
	}
	public void setOwnerid(int ownerid) {
		this.ownerid = ownerid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRootpwd() {
		return rootpwd;
	}
	public void setRootpwd(String rootpwd) {
		this.rootpwd = rootpwd;
	}
	
	@Override
	public String toString() {
		return "Server [id=" + id + ", ip=" + ip + ", name=" + name + ", rootpwd=" + rootpwd + ", ownerid=" + ownerid
				+ ", status=" + status + "]";
	}
	
}
