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


#disk
#size=`df / -hP| awk 'NR==2{print $2}'`
#used=`df / -hP| awk 'NR==2{print $3}'`
#avail=`df / -hP| awk 'NR==2{print $4}'`
useper=`df / -hP| awk 'NR==2{print $5}'`
useper=${useper%?}


#======================OUTPUT===================

result=$Date" "$useper";"
echo $result >>$DataPath"/log"

if [ "$mem" -ge "$bound_disk_used_percent" ];then
	echo "diskalarm "$Date" "$useper";">>$Alarm"/diskalarm"
fi
