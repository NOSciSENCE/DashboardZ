#!/bin/bash

. /etc/profile


#===============create needed directory===================
DataPath="/opt/dashboardz/sysdata"
AlarmPath="/opt/dashboardz/alarm"
ScriptPath="/opt/dashboardz/script"
if [ ! -d $ScriptPath  ];then
  mkdir -p $ScriptPath
fi
if [ ! -d $DataPath  ];then
  mkdir -p $DataPath
  touch $DataPath"/cpulog"
  touch $DataPath"/memlog"
  touch $DataPath"/disklog"
fi
if [ ! -d $AlarmPath  ];then
  mkdir -p $AlarmPath
  touch $AlarmPath"/cpualarm"
  touch $AlarmPath"/memalarm"
  touch $AlarmPath"/diskalarm"
fi

#=============Log=======================
if [ ! -f $DataPath"/cpulog" ];then
  touch $DataPath"/cpulog"
fi	
if [ ! -f $DataPath"/memlog" ];then
  touch $DataPath"/memlog"
fi	
if [ ! -f $DataPath"/disklog" ];then
  touch $DataPath"/disklog"
fi	
#==============Alarm=====================
if [ ! -f $AlarmPath"/cpualarm" ];then
  touch $AlarmPath"/cpualarm"
fi
if [ ! -f $AlarmPath"/memalarm" ];then
  touch $AlarmPath"/memalarm"
fi
if [ ! -f $AlarmPath"/diskalarm" ];then
  touch $AlarmPath"/diskalarm"
fi


#==============Crontab====================

echo "* * * * * /opt/dashboardz/script/CpuAgent.sh">> /var/spool/cron/root
echo "* * * * * /opt/dashboardz/script/DiskAgent.sh">> /var/spool/cron/root
echo "* * * * * /opt/dashboardz/script/MemAgent.sh">> /var/spool/cron/root
