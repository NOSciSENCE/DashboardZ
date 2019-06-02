package org.ys.dashboardz;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.ys.dashboardz.bean.IptablesRule;
import org.ys.dashboardz.bean.LinuxProcess;
import org.ys.dashboardz.cache.ServerCache;
import org.ys.dashboardz.service.IServerService;
import org.ys.dashboardz.service.impl.ServerService;
import org.ys.dashboardz.util.LinuxUtil;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class LinuxUtilTest {
	String ip = "47.103.0.91";
	String rootpwd = "WINys.2008";
	IServerService serverService = new ServerService();
	@Ignore
	public void  execTest() throws IOException {
		String ip = "47.103.0.91";
		String rootpwd = "WINys.2008";
		Map<String, String> ret;
		
		Connection conn = LinuxUtil.connect(ip, rootpwd);
		Session session = LinuxUtil.openSession(conn);
		
		System.out.println("Connected");
		ret = LinuxUtil.exec("ls /", session);
		System.out.println("Executed" + ret);
		
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		
		Assert.assertFalse(ret.get("out") == "");
	}
	@Ignore
	public void timeTest()throws IOException {
		System.out.println(serverService.getServerTime(ip, rootpwd));
	}
	
	@Test
	public void infoTest()throws IOException{
		//model name -> cpuÐÍºÅ
		//cpu cores ->ºËÊý
		System.out.print(LinuxUtil.getServerInfo(ip, rootpwd).toString());
		
	}
	
	@Ignore
	public void dataTest() throws IOException {
		
		System.out.println(LinuxUtil.getServerData(ip, rootpwd).toString());
	}
	
	
	
	@Ignore
	public void iptablesTest() throws IOException {
		Map<String, List<IptablesRule>> parts = serverService.scanIptables(ip, rootpwd);
		for (Entry<String, List<IptablesRule>> entry : parts.entrySet() ) {
			System.out.println( "Key = " + entry.getKey() + ", Size = "+entry.getValue().size() );
			if(entry.getValue()!=null)
			for(int i=0 ; i<entry.getValue().size();++i) {
				System.out.println(entry.getValue().get(i).toString());
			
			}
		}
	}
	@Test
	public void RTDTest() throws IOException {
		Map<String, String> parts = serverService.getRealTimeData(ip, rootpwd);
		for (Entry<String, String> entry : parts.entrySet() ) {
			System.out.println( "Key = " + entry.getKey() + ", Size = "+entry.getValue() );
			
		}
	}
	
	@Ignore
	public void processTest()throws IOException{
		List<LinuxProcess> result = serverService.getProcess(ip, rootpwd);
		for (int i=0; i<result.size();++i) {
			System.out.println(result.get(i).toString());
		}
	}
	
}
