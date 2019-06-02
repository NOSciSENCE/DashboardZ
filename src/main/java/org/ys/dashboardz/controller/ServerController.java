package org.ys.dashboardz.controller;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ys.dashboardz.bean.Admin;
import org.ys.dashboardz.bean.Alarm;
import org.ys.dashboardz.bean.IptablesRule;
import org.ys.dashboardz.bean.LinuxProcess;
import org.ys.dashboardz.bean.Msg;
import org.ys.dashboardz.bean.Server;
import org.ys.dashboardz.service.IServerService;


@Controller
@RequestMapping("/server")
public class ServerController {
	@Resource
	IServerService serverService;
	
	@RequestMapping("/add")
	@ResponseBody
	public Msg add(String ip, String name, String rootpwd, HttpSession session) {
		Msg msg = new Msg();
		//install something on linux
		//get more settings about the server
		Admin admin= (Admin)session.getAttribute("admin");
		
		//access for create
		
		Server server = new Server(); 
		server.setIp(ip);
		server.setName(name);
		server.setRootpwd(rootpwd);
		server.setOwnerid(admin.getId());
		server.setStatus("running");
		serverService.add(server);
		msg.success();
		return msg;
	}
	
	@RequestMapping("remove")
	@ResponseBody
	public Msg remove(String id, HttpSession session) {
		int serverId;
		Msg msg = new Msg();
		
		try {
			serverId = Integer.parseInt(id);
		}catch(Exception e) {
			msg.fail();
			return msg;
		}
		msg.success();
		serverService.delById(serverId);
		session.removeAttribute("server");
		return msg;
	}
	
	//选择同时鉴权
	@RequestMapping("select")
	@ResponseBody
	public Msg select(String id, HttpSession session) {
		Admin admin = (Admin)session.getAttribute("admin");
		Msg msg = new Msg();
		msg.fail();
		if(admin == null)
			return null;
		List<Server> serverList = serverService.findByOwner(admin.getId());
		if(id == null||id.isEmpty())
			return null;
		int idnum = Integer.parseInt(id);
		for (Iterator iter = serverList.iterator(); iter.hasNext();  ) {
			Server tmp = (Server) iter.next();
			if(tmp.getId()==idnum) {
				session.setAttribute("server", tmp);
				msg.success();
				return msg;
			}
		}
		return msg;
	}
	
	@RequestMapping("list")
	@ResponseBody
	public List<Server> list(HttpSession session) {
		Admin admin = (Admin)session.getAttribute("admin");
		if(admin == null)
			return null;
		List<Server>list = serverService.findByOwner(admin.getId());
		return list;
	}
	
	//定时记录的数据
	@RequestMapping("data")
	@ResponseBody
	public Map<String, List<String>> data(HttpSession session) {
		Server server = (Server)session.getAttribute("server");
		if (server==null) {
			return null;
		}
		String ip = server.getIp();
		String pwd = server.getRootpwd();
		Map<String, List<String>> data = serverService.scanServerData(ip, pwd);
		return data;
	}
	
	@RequestMapping("info")
	@ResponseBody
	public Map<String, String> info(HttpSession session) {
		//若用session同时间打开多个会有问题
		Server server = (Server)session.getAttribute("server");
		if (server==null) {
			return null;
		}
		Map<String, String> info = serverService.scanServerInfo(server.getIp(), server.getRootpwd());
		
		return info;
	}
	

	
	
	@RequestMapping("alarm")
	@ResponseBody
	public List<Alarm> getAlarm(HttpSession session){
		Server server = (Server)session.getAttribute("server");
		if (server==null) {
			return null;
		}
		List<Alarm> alarmList = serverService.getAlarm(server.getIp(), server.getRootpwd());
		
		return alarmList;
	}
	
	@RequestMapping("time")
	@ResponseBody
	public Msg getTime(HttpSession session) {
		Msg msg = new Msg();
		Server server = (Server)session.getAttribute("server");
		if (server==null) {
			return null;
		}
		msg.success();
		Map<String, Object> extendMap = new HashMap<String, Object>();
		extendMap.put("time", serverService.getServerTime(server.getIp(), server.getRootpwd()));
		msg.setExtend(extendMap);
		return msg;
	}
	@RequestMapping("iptable")
	@ResponseBody
	public Msg getIptables(HttpSession session) {
		Msg msg = new Msg();
		Server server = (Server)session.getAttribute("server");
		if (server==null) {
			return null;
		}
		msg.success();
		Map<String, List<IptablesRule>> map = serverService.scanIptables(server.getIp(), server.getRootpwd());
		if(map != null) {
			msg.addExtend("iptable", map);
		}
		return msg;
	}
	
	@RequestMapping("realtimedata")
	@ResponseBody
	public Msg getRealTimeData(HttpSession session) {
		Msg msg = new Msg();
		Map<String, String> result = null;
		Server server = (Server)session.getAttribute("server");
		if (server==null) {
			return null;
		}
		msg.success();
		result = serverService.getRealTimeData(server.getIp(), server.getRootpwd());
		msg.addExtend("data", result);
		return msg;
	}
	
	
	@RequestMapping("process")
	@ResponseBody
	public List<LinuxProcess> getProcess(HttpSession session){
		List<LinuxProcess> result = null;
		Server server = (Server)session.getAttribute("server");
		if (server==null) {
			return null;
		}
		result = serverService.getProcess(server.getIp(), server.getRootpwd());
		return result;
	}
}
