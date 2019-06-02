package org.ys.dashboardz.mapper;

import org.apache.ibatis.annotations.Param;
import org.ys.dashboardz.bean.Admin;

public interface AdminMapper {
	int add(Admin admin);
	
	Admin findByName(String name);
	
	int delById(@Param("id") Integer... id);
	
}
