#!/bin/bash

. /etc/profile


function __readINI(){
 
 	INIFILE=$1; SECTION=$2; ITEM=$3
 
 	_readIni=`awk -F '=' '/['$SECTION']/{a=1}a==1&&$1~/'$ITEM'/{print $2;exit}' $INIFILE`
 
	echo ${_readIni}
} 

mem_used_percent=( $( __readINI bound.ini BOUND mem_used_percent ))

#===============Path===============
DataPath="/opt/dashboardz/sysdata"

AlarmPath="/opt/dashboardz/alarm"


#Mem
bu=`free | awk 'NR==2{print $6}'`
ca=`free | awk 'NR==2{print $7}'`
us=`free | awk 'NR==2{print $3}'`
total=`free | awk 'NR==2{print $2}'`
free=`free -m | grep Mem | awk '{print $4}'`  
mem=`expr "scale=2;($us-$bu-$ca)/$total" |bc -l | cut -d. -f2`
#mem_total=`awk '/MemTotal/{total=$2}/MemFree/{free=$2}END{print (total-free)/1024}'  /proc/meminfo`
#mem_usage=`echo ${mem_total} | awk '{print $1*100/16240}'`

#======================OUTPUT===================

result=$Date" "$mem";"
echo $result >>$DataPath"/log"

if [ "$mem" -ge "$bound_mem_used_percent" ];then
	echo "memalarm "$Date" "$mem";">>$Alarm"/memalarm"
fi