#!/bin/sh 
date=`date  -d "-1day" +%Y%m%d`
cd /tmp 
> $date
rm /home/mozat/morange/oaProStat/channel/buzz.txt.$date* 
echo 'delete /home/mozat/morange/oaProStat/channel/buzz.txt.'$date
cd /mnt/192.168.1.174/oapro.buzz.com.eg

lists=`ls oapro.buzz.com.eg$date*.log` 
for log in $lists
do
	a=`echo $log | awk -F ' ' '{print substr($1,18,8)}'`
	grep 'GET / ' $log | awk -F ' ' 'BEGIN{ORS=" "}{print $4;for(i=1;i<NF;i++){if(substr($i,1,6)=="OaDown") print $i;} print "\n"}' >> /tmp/$a 	
	grep 'GET /?from=' $log | awk -F ' ' 'BEGIN{ORS=" "}{print $4;for(i=1;i<NF;i++){if(substr($i,1,6)=="OaDown") print $i;} print "\n"}' >> /tmp/$a 	
	pv=`cat /tmp/$a	| wc -l`
	upv=`cat /tmp/$a | grep UID |wc -l`
	uuv=`cat /tmp/$a | grep UID | sort -u | wc -l`
	if test $upv != '0'
	then 
		uv=$(($pv*$uuv/$upv)) 
	else 
		uv=$pv	
	fi
	echo "insert into webSiteData (oakey,oavalue,ddate) values ('buzzall_PV',"$pv,"'"$a"')"  >>/home/mozat/morange/oaProStat/channel/buzz.txt.$a
	echo "insert into webSiteData (oakey,oavalue,ddate) values ('buzzall_UV',"$uv,"'"$a"')"  >>/home/mozat/morange/oaProStat/channel/buzz.txt.$a
	allFrom=`grep '/?from=' /tmp/$a | awk -F ' ' '{print substr($1,8)}' | grep -v '&' | grep -v '/' | sort -u ` 
	for from in $allFrom 
	do
		if test $from != ''
		then
			fpv=`grep $from /tmp/$a | wc -l`
			fupv=`grep $from /tmp/$a |grep UID |  wc -l`
			fuuv=`grep $from /tmp/$a |grep UID | sort -u | wc -l`
		        if test $fupv != '0'
     		        then
                		fuv=$(($fpv*$fuuv/$fupv))
     			else
               			fuv=$fpv
		        fi
			 
		fi
		echo "insert into webSiteData (oakey,oavalue,ddate) values ('buzz"$from"_PV',"$fpv,"'"$a"')"  >>/home/mozat/morange/oaProStat/channel/buzz.txt.$a
		echo "insert into webSiteData (oakey,oavalue,ddate) values ('buzz"$from"_UV',"$fuv,"'"$a"')"  >>/home/mozat/morange/oaProStat/channel/buzz.txt.$a
	done
	noFrompv=`grep '/ ' /tmp/$a | wc -l`
	noFromupv=`grep '/ ' /tmp/$a |grep UID |  wc -l`
	noFromuuv=`grep '/ ' /tmp/$a |grep UID | sort -u|  wc -l`
	if test $noFromupv != '0'
	then
		noFromuv=$(($noFrompv*$noFromuuv/$noFromupv))
	else 
		noFromuv=$noFrompv
	fi
	echo "insert into webSiteData (oakey,oavalue,ddate) values ('buzznoFrom_PV',"$noFrompv,"'"$a"')"  >>/home/mozat/morange/oaProStat/channel/buzz.txt.$a
	echo "insert into webSiteData (oakey,oavalue,ddate) values ('buzznoFrom_UV',"$noFromuv,"'"$a"')"  >>/home/mozat/morange/oaProStat/channel/buzz.txt.$a
	
done 
cd /tmp 
rm $date* 
echo 'delete temp /tmp/'$date'*'

cd /home/mozat/morange/oaProStat/channel
sqlfnames=`ls buzz.txt.$date*`

cd /home/mozat/morange/oaProStat/OAProStats_Middle_East_Saudi 
for sqlfname in $sqlfnames 
do
java -cp ./lib/poi-scratchpad-3.7-20101029.jar:./lib/poi-ooxml-schemas-3.7-20101029.jar:./lib/poi-ooxml-3.7-20101029.jar:./lib/poi-examples-3.7-20101029.jar:./lib/poi-3.7-20101029.jar:./lib/commons-collections-3.1.jar:./lib/commons-configuration-1.1.jar:./lib/commons-dbcp-1.2.1.jar:./lib/commons-lang-2.1.jar:./lib/commons-logging.jar:./lib/commons-pool-1.2.jar:./lib/log4j-1.2.9.jar:./lib/sqljdbc.jar:./lib/ezmorph-1.0.4.jar:./lib/commons-beanutils.jar:./lib/json-lib-2.2-jdk15.jar:./lib/commons-email-1.3.jar:./lib/mail.jar: main.ExecSql /home/mozat/morange/oaProStat/channel/${sqlfname} 
done 
