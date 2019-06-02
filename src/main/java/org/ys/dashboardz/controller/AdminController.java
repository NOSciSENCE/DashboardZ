package org.ys.dashboardz.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ys.dashboardz.bean.Admin;
import org.ys.dashboardz.bean.Msg;
import org.ys.dashboardz.service.IAdminService;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private static int DEFAULT_ACCESS = 1;
	@Resource
	IAdminService adminService;
	
	@RequestMapping("/login")
	@ResponseBody//返回数据使用
	public Msg login(String name, String password, HttpSession session) {
		if(session.getAttribute("admin") == null) {
			Admin admin = adminService.login(name, password);
			//放入session
			session.setAttribute("admin", admin);
		}
		Msg msg = new Msg("successfully login", "index");
		msg.success();
		return msg;
	}
	
	@RequestMapping("/check")
	@ResponseBody
	public Msg check(HttpSession session) {
		Admin admin = (Admin)session.getAttribute("admin");
		Msg msg = new Msg("false","");
		if(admin != null) {
			msg.success();
			msg.setMsg("true");
			msg.addExtend("admin", admin);
			return msg;
		}
		msg.fail();
		return msg;
	}

	@RequestMapping("/logout")
	public Msg logout(HttpSession session) {
		session.invalidate();
		Msg msg = new Msg("successfully logout", "index");
		msg.success();
		return msg;
	}
	
	@RequestMapping("/register")
	@ResponseBody
	public Msg register(String name, String password, String email, String phone) {
		Admin admin = new Admin(0, name, password, DEFAULT_ACCESS, email, phone);
		//adminService.add(admin);
		Msg msg = new Msg("successfully registered", "index");
		return msg;
	}

}