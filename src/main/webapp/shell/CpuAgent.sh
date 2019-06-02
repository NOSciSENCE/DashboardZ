#!/bin/bash

. /etc/profile


function __readINI(){
 
 	INIFILE=$1; SECTION=$2; ITEM=$3
 
 	_readIni=`awk -F '=' '/['$SECTION']/{a=1}a==1&&$1~/'$ITEM'/{print $2;exit}' $INIFILE`
 
	echo ${_readIni}
} 

cpu_used_percent=( $( __readINI bound.ini BOUND cpu_used_percent ))


#===============Path===============
DataPath="/opt/dashboardz/sysdata"

AlarmPath="/opt/dashboardz/alarm"

#crontab 0 * * * * (sh?) x.sh  每小时第0分
#        * * * * *       每分钟
# 

Date=`date +"%y-%m-%d %H:%M"`
#CPU
CpuIdle=`vmstat 1 5 |sed -n '3,$p'|awk '{x = x + $15} END {print x/5}' |awk -F. '{print $1}'`  
Cpu=`echo "100-$CpuIdle" | bc`   


#======================OUTPUT===================

result=$Date" "$Cpu";"
echo $result >>$DataPath"/log"
if [ "$Cpu" -ge "$cpu_used_percent" ];then
	echo "cpualarm "$Date" "$Cpu";">>$Alarm"/cpualarm"
fi
