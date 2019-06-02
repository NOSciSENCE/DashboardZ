package org.ys.dashboardz.service;

import java.util.List;
import java.util.Map;

import org.ys.dashboardz.bean.Alarm;
import org.ys.dashboardz.bean.IptablesRule;
import org.ys.dashboardz.bean.LinuxProcess;
import org.ys.dashboardz.bean.Server;

public interface IServerService {
	int add(Server server);
	
	int delById(int id);
	
	List<Server> findByOwner(int ownerid);
	
	Map<String,String> scanServerInfo(String ip, String rootpwd);
	
	Map<String, List<String>> scanServerData(String ip, String rootpwd);
	
	Map<String, List<IptablesRule>> scanIptables(String ip, String rootpwd);
	
	String getServerTime(String ip, String pwd);

	List<Alarm> getAlarm(String ip, String rootpwd);
	
	void deployServer(String ip, String rootpwd);

	Map<String, String> getRealTimeData(String ip, String rootpwd);

	List<LinuxProcess> getProcess(String ip, String rootpwd);
}
