package org.ys.dashboardz.mapper;

import java.util.List;

import org.ys.dashboardz.bean.Server;

public interface ServerMapper {
	int add(Server server);
	
	List<Server> findByOwner(int ownerid);
	
	int delById(int id);
	
}
