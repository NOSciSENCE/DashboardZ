package org.ys.dashboardz.bean;

public class IptablesRule {
	// iptables -A INPUT -p tcp  --dport 22 -j ACCEPT
	private String target;
	private String prot;
	private String opt;
	private String source;
	private String destination;
	private String moreinfo;
	//ACCEPT     all  --  anywhere             192.168.0.22
	//ACCEPT     tcp  --  one.one.one.one      2.2.2.2              tcp dpt:ssh
	//DROP       all  --  3.3.3.3              4.4.4.4
	
	public IptablesRule(String target, String prot, String opt) {
		this.target = target;
		this.prot = prot;
		this.opt = opt;
	}

	public IptablesRule(String target, String prot, String opt, String source, String destination) {
		this.target = target;
		this.prot = prot;
		this.opt = opt;
		this.source = source;
		this.destination = destination;
	}
	
	public IptablesRule(String target, String prot, String opt, String source, String destination, String moreinfo) {
		this.target = target;
		this.prot = prot;
		this.opt = opt;
		this.source = source;
		this.destination = destination;
		this.moreinfo = moreinfo;
	}
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getProt() {
		return prot;
	}
	public void setProt(String prot) {
		this.prot = prot;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getMoreinfo() {
		return moreinfo;
	}
	public void setMoreinfo(String moreinfo) {
		this.moreinfo = moreinfo;
	}

	@Override
	public String toString() {
		return "IptablesRule [target=" + target + ", prot=" + prot + ", opt=" + opt + ", source=" + source
				+ ", destination=" + destination + ", moreinfo=" + moreinfo + "]";
	}
	
	
	
}

