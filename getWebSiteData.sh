#/bin/bash
cd /home/mozat/morange/oaProStat/OAProStats_Middle_East_Saudi
date=`date -d "-1 days" '+%Y%m%d'`
date2=`date -d "-1 days" '+%F'`
#date='20130701'
#date2='2013-07-01'
sqlfname=/home/mozat/morange/oaProStat/channel/sql.txt.${date2}
sfname=/home/mozat/morange/oaProStat/OAProStats_Middle_East_Saudi/data/WebSiteSource.txt
fname=/mnt/192.168.1.174/oapro.shabik.sa/oapro.shabik.sa${date}.log
tempfname=/home/mozat/morange/oaProStat/OAProStats_Middle_East_Saudi/temp.sh
> ${sqlfname}

allPv=`grep -E 'GET / '\|'GET /\?' ${fname}|wc -l`
echo $allPv
#echo "insert into webSiteData (oakey,oavalue,ddate) values ('all_PV' $allPv, '${date2}');">> ${sqlfname}
allPvUid=`grep -E 'GET / '\|'GET /\?' ${fname}|grep OaDownUID|wc -l`
echo $allPvUid
allPvUUid=`grep -E 'GET / '\|'GET /\?' ${fname}|grep OaDownUID|awk -F ':' '{print $1";;;;;;;",$0}'|awk -F 'OaDownUID=' '{print $1,";;;;;;;"$2}'|awk  -F ';;;;;;;' '{print $1,$3}'|awk '{print $1,$2}'|awk -F ';' '{print $1}'|awk '{print $2}'|sort -b|uniq -c|wc -l`
echo $allPvUUid
#echo "insert into webSiteData (oakey,oavalue,ddate) values ('all_UV',cast($allPv as float)*$allPvUUid/$allPvUid,'${date2}');">>${sqlfname};

notePv=0
notePvUid=0
notePvUUid=0
node=`awk '{print $1}' $sfname`
for i in $node
do
        echo $i
        echo 'grep -E '\''GET /\\?from='$i' '\'\\\|\''GET /'\\\?from=$i\&\'' '${fname} > ${tempfname}
        iPv=`sh ${tempfname}|wc -l`
        echo $iPv
        iPvUid=`sh ${tempfname}|grep OaDownUID|wc -l`
        echo $iPvUid
        iPvUUid=`sh ${tempfname}|grep OaDownUID|awk -F ':' '{print $1";;;;;;;",$0}'|awk -F 'OaDownUID=' '{print $1,";;;;;;;"$2}'|awk  -F ';;;;;;;' '{print $1,$3}'|awk '{print $1,$2}'|awk -F ';' '{print $1}'|awk '{print $2}'|sort -b|uniq -c|wc -l`
        echo $iPvUUid
        echo "insert into webSiteData (oakey,oavalue,ddate) values ('$i'+'_PV',$iPv, '${date2}');">>${sqlfname}
        echo "insert into webSiteData (oakey,oavalue,ddate) values ('$i'+'_UV',round(cast($iPv as float)*$iPvUUid/$iPvUid,0),'${date2}');">>${sqlfname}
        notePv=`expr $notePv + $iPv`
        notePvUid=`expr $notePvUid + $iPvUid`
        notePvUUid=`expr $notePvUUid + $iPvUUid`
done
echo '-----------------------'
echo $notePv
echo $notePvUid
echo $notePvUUid

noFromPv=`grep 'GET / ' ${fname}|wc -l`
noFromPvUid=`grep 'GET / ' ${fname}|grep OaDownUID|wc -l`
noFromPvUUid=`grep 'GET / ' ${fname}|grep OaDownUID|awk -F ':' '{print $1";;;;;;;",$0}'|awk -F 'OaDownUID=' '{print $1,";;;;;;;"$2}'|awk  -F ';;;;;;;' '{print $1,$3}'|awk '{print $1,$2}'|awk -F ';' '{print $1}'|awk '{print $2}'|sort -b|uniq -c|wc -l`
echo '----------------------'
echo $noFromPv
echo $noFromPvUid
echo $noFromPvUUid 

notePv=`expr $notePv + $noFromPv`
notePvUid=`expr $notePvUid + $noFromPvUid`
notePvUUid=`expr $notePvUUid + $noFromPvUUid`

echo '-----------------------'
echo $notePv
echo $notePvUid
echo $notePvUUid


echo "insert into webSiteData (oakey,oavalue,ddate) values ('all_PV',$allPv, '${date2}');">> ${sqlfname}
echo "insert into webSiteData (oakey,oavalue,ddate) values ('all_UV',round(cast($allPv as float)*$notePvUUid/$notePvUid,0),'${date2}');">>${sqlfname};

echo "insert into webSiteData (oakey,oavalue,ddate) values ('noFrom_PV',$noFromPv, '${date2}');">> ${sqlfname}
echo "insert into webSiteData (oakey,oavalue,ddate) values ('noFrom_UV',round(cast($noFromPv as float)*$notePvUUid/$notePvUid,0),'${date2}');">>${sqlfname};

echo "insert into webSiteData (oakey,oavalue,ddate) values ('other_PV',$allPv-$notePv, '${date2}');">> ${sqlfname}
echo "insert into webSiteData (oakey,oavalue,ddate) values ('other_UV',round((cast($allPv as float)-cast($notePv as float))*$notePvUUid/$notePvUid,0),'${date2}');">>${sqlfname};


java -cp ./lib/poi-scratchpad-3.7-20101029.jar:./lib/poi-ooxml-schemas-3.7-20101029.jar:./lib/poi-ooxml-3.7-20101029.jar:./lib/poi-examples-3.7-20101029.jar:./lib/poi-3.7-20101029.jar:./lib/commons-collections-3.1.jar:./lib/commons-configuration-1.1.jar:./lib/commons-dbcp-1.2.1.jar:./lib/commons-lang-2.1.jar:./lib/commons-logging.jar:./lib/commons-pool-1.2.jar:./lib/log4j-1.2.9.jar:./lib/sqljdbc.jar:./lib/ezmorph-1.0.4.jar:./lib/commons-beanutils.jar:./lib/json-lib-2.2-jdk15.jar:./lib/commons-email-1.3.jar:./lib/mail.jar: main.ExecSql ${sqlfname} 
