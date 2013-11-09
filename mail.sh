cd /home/mozat/morange/oaProStat/OAProStats_Middle_East_Saudi
date=`date -d "-1 days" '+%F'`
logfile=/home/mozat/morange/oaProStat/log/log.${date}
server1Dau=/home/mozat/morange/oaProStat/dau/server1Dau.${date}
server2Dau=/home/mozat/morange/oaProStat/dau/server2Dau.${date}
server3Dau=/home/mozat/morange/oaProStat/dau/server3Dau.${date}
server4Dau=/home/mozat/morange/oaProStat/dau/server4Dau.${date}

server1fname=/mnt/192.168.1.101/mologin/morange.log.${date}*
server2fname=/mnt/192.168.1.106/morange.log.${date}*
server3fname=/mnt/192.168.1.110/morange.log.${date}*
server4fname=/mnt/192.168.1.115/morange.log.${date}*

server1item=/home/mozat/morange/oapro/OceanAgelogs/item.log.${date}*
server2item=/mnt/192.168.1.107/oapro/OceanAgelogs/item.log.${date}*
server3item=/mnt/192.168.1.112/oapro/OceanAgelogs/item.log.${date}*
server4item=/mnt/192.168.1.116/oapro/OceanAgelogs/item.log.${date}*


server1Pau=/home/mozat/morange/oaProStat/dau/server1Pau.${date}
server2Pau=/home/mozat/morange/oaProStat/dau/server2Pau.${date}
server3Pau=/home/mozat/morange/oaProStat/dau/server3Pau.${date}
server4Pau=/home/mozat/morange/oaProStat/dau/server4Pau.${date}


echo ${server1fname}
grep 'login result: result=0' ${server1fname}|awk -F '[=,]' '{print $5}'|sort -b|uniq -c|awk '{print $2}' > ${server1Dau}

echo ${server2fname}
grep 'login result: result=0' ${server2fname}|awk -F '[=,]' '{print $5}'|sort -b|uniq -c|awk '{print $2}' > ${server2Dau}

echo ${server3fname}
grep 'login result: result=0' ${server3fname}|awk -F '[=,]' '{print $5}'|sort -b|uniq -c|awk '{print $2}' > ${server3Dau}

echo ${server4fname}
grep 'login result: result=0' ${server4fname}|awk -F '[=,]' '{print $5}'|sort -b|uniq -c|awk '{print $2}' > ${server4Dau}

dau=`sort -b /home/mozat/morange/oaProStat/dau/*Dau.${date}|uniq -c|wc -l`
echo ${dau}
echo 'finish get dau' >> ${logfile}

> /home/mozat/morange/oaProStat/dau/mau

for i in $(seq 1 30 )
do
        fname3=/home/mozat/morange/oaProStat/dau/*Dau.`date -d "-$i days" '+%F'`
        echo 'cp '${fname3}
        cat ${fname3} >> /home/mozat/morange/oaProStat/dau/mau
done

mau=`sort -u  /home/mozat/morange/oaProStat/dau/mau|wc -l`
echo ${mau}

hau=`sort -u  /home/mozat/morange/oaProStat/dau/*Dau.*|wc -l`
echo ${hau}

echo 'finish get mau hau' >> ${logfile}

withdraw1=`grep method=BuyByCredit ${server1item}|awk -F '[=,]' '{print $NF}'|awk '{sum +=$1};END {print sum}'`
echo ${withdraw1}
grep method=BuyByCredit ${server1item}|awk -F '[=,]' '{print $3}'|sort -b|uniq -c|awk '{print $2}' > ${server1Pau}

withdraw2=`grep method=BuyByCredit ${server2item}|awk -F '[=,]' '{print $NF}'|awk '{sum +=$1};END {print sum}'`
echo ${withdraw2}
grep method=BuyByCredit ${server2item}|awk -F '[=,]' '{print $3}'|sort -b|uniq -c|awk '{print $2}' > ${server2Pau}

withdraw3=`grep method=BuyByCredit ${server3item}|awk -F '[=,]' '{print $NF}'|awk '{sum +=$1};END {print sum}'`
echo ${withdraw3}
grep method=BuyByCredit ${server3item}|awk -F '[=,]' '{print $3}'|sort -b|uniq -c|awk '{print $2}' > ${server3Pau}

withdraw4=`grep method=BuyByCredit ${server4item}|awk -F '[=,]' '{print $NF}'|awk '{sum +=$1};END {print sum}'`
echo ${withdraw4}
grep method=BuyByCredit ${server4item}|awk -F '[=,]' '{print $3}'|sort -b|uniq -c|awk '{print $2}' > ${server4Pau}


withdraw=`expr $withdraw1 + $withdraw2`
echo ${withdraw}
withdraw=`expr $withdraw + $withdraw3`
echo ${withdraw}
withdraw=`expr $withdraw + $withdraw4`
echo ${withdraw}

pau=`sort -u /home/mozat/morange/oaProStat/dau/*Pau.${date}|wc -l`
echo ${pau}

fname6=/mnt/192.168.1.101/Tomcat/stat.log.${date}*
> /home/mozat/morange/oaProStat/dau/cau.${date}

grep CreateUser ${fname6}|awk -F '[=,]' '{print $3}' > /home/mozat/morange/oaProStat/dau/cau.${date}
cau=`sort -b /home/mozat/morange/oaProStat/dau/cau.${date}|uniq -c|wc -l`

clau=0
node=`cat /home/mozat/morange/oaProStat/dau/cau.${date}`
for i in $node
do
        tcount=`grep $i /home/mozat/morange/oaProStat/dau/*Dau.${date}|wc -l`
        #echo $tcount
        if [ $tcount -gt 0 ]
        then
             clau=`expr $clau + 1`        	
        fi
done
echo 'before call java' >> ${logfile}
java -cp ./lib/poi-scratchpad-3.7-20101029.jar:./lib/poi-ooxml-schemas-3.7-20101029.jar:./lib/poi-ooxml-3.7-20101029.jar:./lib/poi-examples-3.7-20101029.jar:./lib/poi-3.7-20101029.jar:./lib/commons-collections-3.1.jar:./lib/commons-configuration-1.1.jar:./lib/commons-dbcp-1.2.1.jar:./lib/commons-lang-2.1.jar:./lib/commons-logging.jar:./lib/commons-pool-1.2.jar:./lib/log4j-1.2.9.jar:./lib/sqljdbc.jar:./lib/ezmorph-1.0.4.jar:./lib/commons-beanutils.jar:./lib/json-lib-2.2-jdk15.jar:./lib/commons-email-1.3.jar:./lib/mail.jar: main.Mail $dau $mau $hau $pau $withdraw $cau $clau
