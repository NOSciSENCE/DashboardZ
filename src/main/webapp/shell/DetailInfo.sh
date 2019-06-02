#!/bin/bash

. /etc/profile


function __readINI(){
 
 	INIFILE=$1; SECTION=$2; ITEM=$3
 
 	_readIni=`awk -F '=' '/['$SECTION']/{a=1}a==1&&$1~/'$ITEM'/{print $2;exit}' $INIFILE`
 
	echo ${_readIni}
} 

disk_used_percent=( $( __readINI bound.ini BOUND disk_used_percent ))

#===============Path===============
DataPath="/opt/dashboardz/sysdata"

AlarmPath="/opt/dashboardz/alarm"



#================OUTPUT==================
#Mem
# MB
#total:总物理内存的大小。
#used:已使用内存。
#free:可用内存。
#Shared:多个进程共享的内存总额。
#Buffers/cached:磁盘缓存的大小。
bu=`free -m| awk 'NR==2{print $6}'`
ca=`free -m| awk 'NR==2{print $7}'`
us=`free -m| awk 'NR==2{print $3}'`
total=`free -m| awk 'NR==2{print $2}'`
free=`free -m | grep Mem | awk '{print $4}'`  
mem=`expr "scale=2;($used-$bu-$ca)/$total" |bc -l | cut -d. -f2`

#disk
size=`df / -hPm| awk 'NR==2{print $2}'`
used=`df / -hPm| awk 'NR==2{print $3}'`
avail=`df / -hPm| awk 'NR==2{print $4}'`
useper=`df / -hPm| awk 'NR==2{print $5}'`
useper=${useper%?}

CpuIdle=`vmstat 1 5 |sed -n '3,$p'|awk '{x = x + $15} END {print x/5}' |awk -F. '{print $1}'`  
Cpu=`echo "100-$CpuIdle" | bc`   


echo $bu" "$ca" "$us" "$total" "$free" "$mem" "$size" "$used" "$avail" "$useper" "$Cpu