package org.ys.dashboardz.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.ys.dashboardz.bean.Alarm;
import org.ys.dashboardz.bean.IptablesRule;
import org.ys.dashboardz.cache.ServerCache;



import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class LinuxUtil {

	private static final int TIME_OUT = 1000 * 5;

	private LinuxUtil() {

	}

	public static Connection connect(String ip, String rootpwd) throws IOException {
		Connection conn = new Connection(ip);
		conn.connect();
		if (conn.authenticateWithPassword("root", rootpwd)) {
			return conn;
		}
		return null;
	}

	public static Session openSession(Connection conn) throws IOException {
		Session session = conn.openSession();
		// session.requestPTY("vt100", 80, 24, 640, 480, null);
		session.requestPTY("bash");
		// session.startShell();
		return session;
	}

	public static Map<String, String> exec(String cmd, Session session) {
		InputStream out = null;
		InputStream err = null;
		String outStr = "";
		String errStr = "";
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			session.execCommand(cmd);
			out = new StreamGobbler(session.getStdout());
			outStr = processStream(out, "UTF-8");
			err = new StreamGobbler(session.getStderr());
			errStr = processStream(err, "UTF-8");
			// System.out.println("1");
			// System.out.println(outStr);
			// System.out.println(errStr);
			//session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
			session.waitForCondition(ChannelCondition.TIMEOUT, TIME_OUT);
			resultMap.put("out", outStr);
			resultMap.put("err", errStr);
		} catch (IOException e) {
			// do log
			System.out.println("error:" + e);
		} finally {
			IOUtils.closeQuietly(err);
			IOUtils.closeQuietly(out);
		}

		return resultMap;

	}

	private static String processStream(InputStream in, String charset) throws IOException {
		byte[] buf = new byte[1024];
		StringBuilder sb = new StringBuilder();
		while (in.read(buf) != -1) {
			sb.append(new String(buf, charset));
		}
		return sb.toString();
	}

	public static int closeSession(Session session) {
		session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
		int result = session.getExitStatus();
		return result;
	}

	public static void disconnect(Connection conn) {
		conn.close();
	}
	
	
	
	
	private static int getLines(String ip, String pwd, String filePath) throws IOException {
		Connection conn = LinuxUtil.connect(ip, pwd);
		Session session = LinuxUtil.openSession(conn);
		int lines = 10;
		String result = LinuxUtil.exec("awk '{print NR}' "+ filePath +" |tail -n1", session).get("out");
		try{
			lines = Integer.parseInt(result.trim());
		}catch(Exception e) {
			lines = 10;
		}
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		return lines;
	}
	
	// 实时数据
	public static Map<String, List<String>> getServerData(String ip, String pwd) throws IOException {
		Map<String, List<String>> serverData = new HashMap<String, List<String>>();
		int lineNum = getLines(ip, pwd, ServerCache.DATA_PATH);
		Connection conn = LinuxUtil.connect(ip, pwd);
		Session session = LinuxUtil.openSession(conn);
		String result = null;
		if (lineNum >= 480) {
			result = LinuxUtil.exec("cat " + ServerCache.DATA_PATH + " | tail -n 480", session).get("out");
		}
		else {
			result = LinuxUtil.exec("cat " + ServerCache.DATA_PATH + " | tail -n "+lineNum, session).get("out");
		}
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		String[] dataBlock = result.split(";");
		List<String> timeList = new ArrayList<String>(480);
		List<String> cpuList = new ArrayList<String>(480);
		List<String> memList = new ArrayList<String>(480);
		//System.out.println(result);
		List<String> diskList = new ArrayList<String>();
		for (int i = 0; i < dataBlock.length; ++i) {
			String[] data = dataBlock[i].split(" ");
			if (data.length == 5) {
				//timeList.add(data[0] + " " + data[1]);
				timeList.add(data[1]);
				cpuList.add(data[2]);
				memList.add(data[3]);
				diskList.add(data[4]);
			}
		}
		serverData.put("time", timeList);
		serverData.put("cpu", cpuList);
		serverData.put("mem", memList);
		serverData.put("disk", diskList);
		return serverData;
	}

	// 固定数据
	public static Map<String, String> getServerInfo(String ip, String pwd) throws IOException {
		Map<String, String> serverInfo = new HashMap<String, String>();
		Connection conn = LinuxUtil.connect(ip, pwd);
		Session session = LinuxUtil.openSession(conn);
		String str = LinuxUtil.exec("cat /proc/cpuinfo | grep \"physical id\" | uniq | wc -l", session).get("out");
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		//String[] list = str.split("\n");
		serverInfo.put("cpuNum",str.trim());
		
		conn = LinuxUtil.connect(ip, pwd);
		session = LinuxUtil.openSession(conn);
		str = LinuxUtil.exec("cat /proc/cpuinfo | grep \"cpu cores\" | uniq", session).get("out");
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		serverInfo.put("coreNum",str.split(":")[1].trim());
		
		conn = LinuxUtil.connect(ip, pwd);
		session = LinuxUtil.openSession(conn);
		str = LinuxUtil.exec("cat /proc/cpuinfo | grep 'model name' |uniq", session).get("out");
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		serverInfo.put("coreType",str.split(":")[1].trim());
		
		conn = LinuxUtil.connect(ip, pwd);
		session = LinuxUtil.openSession(conn);
		str = LinuxUtil.exec("cat /proc/meminfo | grep MemTotal", session).get("out");
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		serverInfo.put("memTotal", str.split(":")[1].trim());
		
		
		conn = LinuxUtil.connect(ip, pwd);
		session = LinuxUtil.openSession(conn);
		str = LinuxUtil.exec("df / -hP| awk 'NR==2{print $2}'", session).get("out");
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		serverInfo.put("diskTotal", str.trim());
		
		conn = LinuxUtil.connect(ip, pwd);
		session = LinuxUtil.openSession(conn);
		str = LinuxUtil.exec("lsb_release -a|grep \"Description:\"", session).get("out");
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		serverInfo.put("linuxVersion", str.split(":")[1].trim());
		
		return serverInfo;
	}
	
	
	//iptables
	public static Map<String, String> getIptables(String ip, String pwd) throws IOException {
		Connection conn = LinuxUtil.connect(ip, pwd);
		Session session = LinuxUtil.openSession(conn);
		String str = LinuxUtil.exec("iptables -L -n", session).get("out");
		Map<String, String> policy = new HashMap<String, String>();
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		String[] parts = str.split("Chain ");
		for(int i = 1; i<parts.length; ++i) {
			if(i==1) {
				policy.put("INPUT", parts[i].trim());
			}else if(i==2) {
				policy.put("FORWARD", parts[i].trim());
			}else if(i==3) {
				policy.put("OUTPUT", parts[i].trim());
			}else {
				policy.put("OTHER",parts[i].trim());
			}
			
		}
		
		return policy;
	}
	
	public static String[] addIpatables(String ip, String pwd, IptablesRule rule) throws IOException {
		Connection conn = LinuxUtil.connect(ip, pwd);
		Session session = LinuxUtil.openSession(conn);
		String str = LinuxUtil.exec("iptables -L", session).get("out");
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		String[] parts = str.split("Chain");
		
		return parts;
	}
	
	//remove all
	public static void removeAllIpatables(String ip, String pwd) throws IOException {
		Connection conn = LinuxUtil.connect(ip, pwd);
		Session session = LinuxUtil.openSession(conn);
		String str = LinuxUtil.exec("iptables -F", session).get("out");
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
	}
	
	//port  netstat -lntp    查看所有监听端口
	//Active Internet connections (only servers)
	//Proto Recv-Q Send-Q Local Address           Foreign Address         State       PID/Program name
	//tcp        0      0 0.0.0.0:22              0.0.0.0:*               LISTEN      30105/sshd
	
	
	public static List<String> getPort(String ip, String pwd) throws IOException {
		Connection conn = LinuxUtil.connect(ip, pwd);
		Session session = LinuxUtil.openSession(conn);
		String str = LinuxUtil.exec("netstat -lntp", session).get("out");
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		String[] lines = str.split("\n");
		List<String> ports = Arrays.asList(lines);
		return ports;
	}
	//rpm -qa  所有安装软件包
	
	public static List<String> getRPM(String ip, String pwd) throws IOException {
		Connection conn = LinuxUtil.connect(ip, pwd);
		Session session = LinuxUtil.openSession(conn);
		String str = LinuxUtil.exec("rpm -qa", session).get("out");
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		String[] lines = str.split("\n");
		List<String> rpm = Arrays.asList(lines);
		return rpm;
	}
	
	
	public static List<Alarm> getAlarm(String ip, String pwd) throws IOException{
		Connection conn = LinuxUtil.connect(ip, pwd);
		Session session = LinuxUtil.openSession(conn);
		String alarmStr = LinuxUtil.exec("cat "+ServerCache.ALARM_PATH, session).get("out");
		LinuxUtil.closeSession(session);
		LinuxUtil.disconnect(conn);
		String[] lines = alarmStr.split("\n");
		List<Alarm> alarmList = new ArrayList<Alarm>();
		for(int i=0; i<lines.length; ++i) {
			String tmp[] = lines[i].split(" ");
			if(tmp.length == 3) {
				alarmList.add(new Alarm(tmp[0], tmp[1], tmp[2]));
			}
		}
		return alarmList;
	}
}
