package org.ys.dashboardz.bean;

public class LinuxProcess {
	private String user;
	private String mem;
	private String cpu;
	private String group;
	private String COMMAND;
	private String NI;  //nice ,优先级
	private String pid;
	private String stat; //进程状态
	
	
	public LinuxProcess(String user, String mem, String cpu, String group, String cOMMAND, String nI, String pid,
			String stat) {
		super();
		this.user = user;
		this.mem = mem;
		this.cpu = cpu;
		this.group = group;
		COMMAND = cOMMAND;
		NI = nI;
		this.pid = pid;
		this.stat = stat;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getMem() {
		return mem;
	}
	public void setMem(String mem) {
		this.mem = mem;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getCOMMAND() {
		return COMMAND;
	}
	public void setCOMMAND(String cOMMAND) {
		COMMAND = cOMMAND;
	}
	public String getNI() {
		return NI;
	}
	public void setNI(String nI) {
		NI = nI;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getSTAT() {
		return stat;
	}
	public void setSTAT(String stat) {
		this.stat = stat;
	}
	@Override
	public String toString() {
		return "LinuxProcess [user=" + user + ", mem=" + mem + ", cpu=" + cpu + ", group=" + group + ", COMMAND="
				+ COMMAND + ", NI=" + NI + ", pid=" + pid + ", stat=" + stat + "]";
	}
	
	
}
