package org.ys.dashboardz.bean;

import java.util.HashMap;
import java.util.Map;

public class Msg {
	
	private int code;   //
	private String msg;
	private String redirect;
	private Map<String, Object> extend;
	
	public Msg() {
		this.extend = new HashMap<String, Object>();
	}
	
	public Msg(String msg, String redirect) {
		this.msg = msg;
		this.redirect = redirect;
		this.extend = new HashMap<String, Object>();
		
	}
	
	public void success() {
		this.code = 100;
	}
	public void fail() {
		this.code = 200;
	}
	
	public void addExtend(String key, Object object) {
		this.getExtend().put(key, object);
	}
	

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public Map<String, Object> getExtend() {
		return extend;
	}

	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}
	
	
}
