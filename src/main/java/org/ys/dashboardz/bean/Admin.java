package org.ys.dashboardz.bean;

public class Admin {
	private int id;
	private String name;
	private String pwd;
	private int access;
	private String email;
	private String phone;
	
	
	public Admin() {
		
	}
	public Admin(int id, String name, String pwd, int access, String email, String phone) {
		this.id = id;
		this.name = name;
		this.pwd = pwd;
		this.access = access;
		this.email = email;
		this.phone = phone;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getAccess() {
		return access;
	}
	public void setAccess(int access) {
		this.access = access;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString() {
		return "Admin [id=" + id + ", name=" + name + ", pwd=" + pwd + ", access=" + access + ", email=" + email
				+ ", phone=" + phone + "]";
	}
	
	
}
