package org.ys.dashboardz.service;

import org.ys.dashboardz.bean.Admin;

public interface IAdminService {
	Admin login(String name,String password);
}
