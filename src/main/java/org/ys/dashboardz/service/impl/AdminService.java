package org.ys.dashboardz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ys.dashboardz.bean.Admin;
import org.ys.dashboardz.mapper.AdminMapper;
import org.ys.dashboardz.service.IAdminService;

@Service
@Transactional
public class AdminService implements IAdminService{
	@Resource
	private AdminMapper adminMapper;
	
	@Transactional
	public Admin login(String name, String pwd)
	{
		Admin admin = adminMapper.findByName(name);
		if(pwd.equals(admin.getPwd()))
			return admin;
		return null;
	}
	
	@Transactional
	public int regist(String name, String pwd, int access, String email, String phone) {
		Admin admin = new Admin(0, name, pwd, access, email, phone);
		int result = adminMapper.add(admin);
		return result;
	}
	
}
