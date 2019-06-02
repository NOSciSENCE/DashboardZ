package org.ys.dashboardz.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerCache {
	
	public static final String BASE_PATH = "/opt/dashboardz";
	
	public static final String SCRIPT_PATH = BASE_PATH +"/script";
	public static final String RTS = SCRIPT_PATH + "/DetailInfo.sh";
	
	public static final String DATA_PATH = BASE_PATH + "/sysdata/log";
	public static final String CPU_DATA_PATH = BASE_PATH + "/sysdata/cpulog";
	public static final String MEM_DATA_PATH = BASE_PATH + "/sysdata/memlog";
	public static final String DISK_DATA_PATH = BASE_PATH + "/sysdata/disklog";
	
	public static final String ALARM_PATH = BASE_PATH + "/alarm/log";
	public static final String CPU_ALARM_PATH = BASE_PATH + "/sysdata/cpualarm";
	public static final String MEM_ALARM_PATH = BASE_PATH + "/sysdata/memalarm";
	public static final String DISK_ALARM_PATH = BASE_PATH + "/sysdata/diskalarm";
	
	
	public static final String ENCODING = "utf-8";
	
	//<serverId, dataList>
	public static Map<Integer, Map<String, List<String>>> serverDataCache = new HashMap<Integer, Map<String, List<String>>>();
}
