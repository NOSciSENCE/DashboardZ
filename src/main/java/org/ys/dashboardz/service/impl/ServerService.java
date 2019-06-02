package org.ys.dashboardz.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ys.dashboardz.bean.Alarm;
import org.ys.dashboardz.bean.IptablesRule;
import org.ys.dashboardz.bean.LinuxProcess;
import org.ys.dashboardz.bean.Server;
import org.ys.dashboardz.cache.ServerCache;
import org.ys.dashboardz.mapper.ServerMapper;
import org.ys.dashboardz.service.IServerService;
import org.ys.dashboardz.util.LinuxUtil;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

@Service
@Transactional
public class ServerService implements IServerService {
	@Resource
	private ServerMapper serverMapper;

	@Override
	@Transactional
	public int add(Server server) {
		// TODO Auto-generated method stub
		serverMapper.add(server);
		return 0;
	}

	@Override
	@Transactional
	public int delById(int id) {
		// TODO Auto-generated method stub
		serverMapper.delById(id);
		return 0;
	}

	@Override
	@Transactional
	public List<Server> findByOwner(int ownerid) {
		List<Server> list = serverMapper.findByOwner(ownerid);
		return list;
	}

	@Override
	public Map<String, String> scanServerInfo(String ip, String rootpwd) {
		Map<String, String> serverInfo = null;
		try {
			// 先行判断缓存中是否存在
			// if has cache

			// else
			serverInfo = LinuxUtil.getServerInfo(ip, rootpwd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// LOG
		}
		return serverInfo;
	}

	@Override
	public Map<String, List<String>> scanServerData(String ip, String rootpwd) {
		// TODO Auto-generated method stub
		Map<String, List<String>> serverData = null;
		try {

			// 先行判断缓存中是否存在
			// if has cache

			// else
			serverData = LinuxUtil.getServerData(ip, rootpwd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serverData;
	}

	@Override
	public Map<String, List<IptablesRule>> scanIptables(String ip, String rootpwd) {
		Map<String, List<IptablesRule>> iptables = new HashMap<String, List<IptablesRule>>();
		try {
			Map<String, String> result = null;
			result = LinuxUtil.getIptables(ip, rootpwd);
			iptables.put("INPUT", addRuleList(result.get("INPUT")));
			iptables.put("FORWARD", addRuleList(result.get("FORWARD")));
			iptables.put("OUTPUT", addRuleList(result.get("OUTPUT")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return iptables;
	}

	private static List<IptablesRule> addRuleList(String data) {
		List<IptablesRule> list = new ArrayList<IptablesRule>();
		if (data == null)
			return null;
		String[] input = data.split("\n");
		for (int i = 0; i < input.length; ++i) {
			String[] str = input[i].split("\\s+");
			if (str.length == 3) {
				list.add(new IptablesRule(str[0], str[1].substring(1), str[2].substring(0, str[2].length() - 1)));
			} else if (str.length == 5) {
				if (str[0].equals("target"))
					continue;
				list.add(new IptablesRule(str[0], str[1], str[2], str[3], str[4]));
			} else if (str.length == 7) {
				list.add(new IptablesRule(str[0], str[1], str[2], str[3], str[4], str[5] + str[6]));
			}
		}
		return list;
	}

	public String getServerTime(String ip, String pwd) {
		Connection conn = null;
		Session session = null;
		String time = null;
		try {
			conn = LinuxUtil.connect(ip, pwd);
			session = LinuxUtil.openSession(conn);
			time = LinuxUtil.exec("date", session).get("out");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			LinuxUtil.closeSession(session);
			LinuxUtil.disconnect(conn);
		}

		return time.trim();
	}

	@Override
	public List<Alarm> getAlarm(String ip, String rootpwd) {
		try {
			return LinuxUtil.getAlarm(ip, rootpwd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deployServer(String ip, String rootpwd) {
		// TODO Auto-generated method stub

		// mkdir /opt/dashboardz
		// ftp Agent.sh
		// crontab Agent.sh
		// LinuxUtil.exec(cmd, session);

	}

	@Override
	public Map<String, String> getRealTimeData(String ip, String rootpwd) {
		Connection conn = null;
		Session session = null;
		String data = null;
		Map<String, String> result = new HashMap<String, String>();
		try {
			conn = LinuxUtil.connect(ip, rootpwd);
			session = LinuxUtil.openSession(conn);
			data = LinuxUtil.exec("sh " + ServerCache.RTS, session).get("out");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			LinuxUtil.closeSession(session);
			LinuxUtil.disconnect(conn);
		}
		String datas[] = data.split(" ");
		// $bu" "$ca" "$used" "$total" "$free" "$mem" "$size" "$used" "$avail" "$useper"
		// "$Cpu
		if (true) {
			result.put("buff", datas[0] );
			result.put("cached", datas[1] );
			result.put("us", datas[2] );
			result.put("total", datas[3]);
			result.put("free", datas[4] );
			result.put("mem", datas[5] + "%");
			result.put("size", datas[6] );
			result.put("used", datas[7]);
			result.put("avail", datas[8] );
			result.put("usedper", datas[9] + "%");
			result.put("cpu", datas[10] + "%");
		}
		return result;
	}

	@Override
	public List<LinuxProcess> getProcess(String ip, String rootpwd) {
		Connection conn = null;
		Session session = null;
		String data = null;
		List<LinuxProcess> result = new ArrayList<LinuxProcess>();
		try {
			conn = LinuxUtil.connect(ip, rootpwd);
			session = LinuxUtil.openSession(conn);
			data = LinuxUtil.exec("ps ax -o user,%mem,%cpu,group,comm,nice,pid,stat", session).get("out");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			LinuxUtil.closeSession(session);
			LinuxUtil.disconnect(conn);
		}
		String datas[] = data.split("\n");
		for(int i=1; i<datas.length ; ++i) {
			String tmp[] = datas[i].split("\\s+");
			if(tmp.length>=8) {
				result.add(new LinuxProcess(tmp[0],tmp[1],tmp[2],tmp[3],tmp[4],tmp[5],tmp[6],tmp[7]));
			}
		}
		
		
		return result;
	}
}
