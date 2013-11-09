#!/bin/bash 
for j in {1..1} 
do 
#转到log文件的目录下
cd /home/mozat/morange/oapro/OceanAgelogs  
#取得昨天的日期 例如2013-09-24
tdate=`date -d "-${j}days" +%Y-%m-%d`
#保存sql语句的文件
sqlfile=/home/mozat/morange/oaProStat/channel/tribe_$tdate.sql.txt 
> $sqlfile
filename=''
#循环取得过去24小时的log文件的名字
for i in {1..24}
do 
date=`date -d "-${i}hours" +%Y-%m-%d-%H` 
f=`echo "action.log.$date"`
filename=`echo "$filename $f"`
done
#家族打怪的概括数据
createAmount=`grep  action=createTribeMonster  $filename | awk -F '[=,]' '{print $13}' | sort -u |  wc -l `
killAmount=`grep  action=killTribeMonster   $filename | grep -c curHp=0 ` 
accackAmount=`grep action=tribeMonsterSelectPosition $filename | grep -cv oldPosition=,`
personAmount=`grep action=tribeMonsterSelectPosition $filename | grep -v oldPosition=, | awk -F '[=,]' '{print $3}' | sort -u | wc -l` 
tribeAmount=`grep action=tribeMonsterSelectPosition $filename  | awk -F '[=,]' '{print $13}' | sort -u | wc -l` 
allAmount=`grep action=tribeMonsterSelectPosition $filename | grep  oldPosition=,|  awk -F '[=,]' '{print $3}' | sort -u | wc -l` 
weiguanAmount=`expr $allAmount - $personAmount ` 
echo "insert into tribeSum(createMonter,killMonter,tribeNum,person,num,wnum,date,dsum)values($createAmount,$killAmount,$tribeAmount,$personAmount,$accackAmount,$weiguanAmount,'$tdate',1)" >> $sqlfile
#打怪人数分布
attList=`grep action=tribeMonsterSelectPosition $filename | grep -v oldPosition=,|awk -F '[=,]' '{print $13,$3}' |sort -u | awk '{arr[$1]+=1}END{ for (key in arr) printf("%s\n",  arr[key])}' | awk '{arr[$1]+=1}END{ for (key in arr) printf("%s,%s\n",key, arr[key])}'`
for att in $attList 
do
 echo "insert into tribeAttackAndweiguan(num,amount,type,date,dsum) values($att,'attack','$tdate',1)" >> $sqlfile
done 
#围观人数分布
> /tmp/alltride_$tdate.temp
grep action=tribeMonsterSelectPosition $filename | grep  oldPosition=,|awk -F '[=,]' '{print $13,$3}' | sort -u| awk '{arr[$1]+=1}END{ for (key in arr) printf("%s,%s\n",key,arr[key])}' >> /tmp/alltride_$tdate.temp 
> /tmp/attacktride_$tdate.temp
grep action=tribeMonsterSelectPosition $filename | grep -v  oldPosition=,|awk -F '[=,]' '{print $13,$3}'|sort -u | awk '{arr[$1]+=1}END{ for (key in arr) printf("%s,%s\n",key,arr[key])}' >> /tmp/attacktride_$tdate.temp 
tlist=`cat /tmp/alltride_$tdate.temp | awk -F ',' '{print $1}'` 
> /tmp/wnum.temp.txt
for t in tlist 
do
	anum=`grep $t /tmp/alltride_$tdate.temp | awk -F ',' '{print $2}'`
	tnum=`grep $t /tmp/attacktride_$tdate.temp | awk -F ',' '{print $2}'`
	wnum=`expr $anum-$tnum` 
	echo $wnum  >> /tmp/wnum.temp.txt 
done 

wgList=`cat /tmp/wnum.temp.txt|awk '{arr[$1]+=1}END{ for (key in arr) printf("%s,%s\n",key, arr[key])}'`
#for att in $wgList
#do
# echo "insert into tribeAttackAndweiguan(num,amount,type,date,dsum) values($att,'weiguan','$tdate',1)" >> $sqlfile
#done 





filename=''
#循环取得过去72小时的log文件的名字
for i in {1..72}
do 
date=`date -d "-${i}hours" +%Y-%m-%d-%H` 
f=`echo "action.log.$date"`
filename=`echo "$filename $f"`
done
#家族打怪的概括数据
createAmount=`grep action=createTribeMonster  $filename | awk -F '[=,]' '{print $13}' | sort -u |wc -l`
killAmount=`grep  action=killTribeMonster    $filename | grep -c curHp=0 ` 
accackAmount=`grep action=tribeMonsterSelectPosition $filename | grep -cv oldPosition=,`
personAmount=`grep action=tribeMonsterSelectPosition $filename | grep -v oldPosition=, | awk -F '[=,]' '{print $3}' | sort -u | wc -l` 
tribeAmount=`grep action=tribeMonsterSelectPosition $filename  | awk -F '[=,]' '{print $13}' | sort -u | wc -l` 
allAmount=`grep action=tribeMonsterSelectPosition $filename | grep  oldPosition=,|  awk -F '[=,]' '{print $3}' | sort -u | wc -l` 
weiguanAmount=`expr $allAmount - $personAmount ` 
echo "insert into tribeSum(createMonter,killMonter,tribeNum,person,num,wnum,date,dsum)values($createAmount,$killAmount,$tribeAmount,$personAmount,$accackAmount,$weiguanAmount,'$tdate',3)" >> $sqlfile
#打怪人数分布
attList=`grep action=tribeMonsterSelectPosition $filename | grep -v oldPosition=,|awk -F '[=,]' '{print $13,$3}' |sort -u| awk '{arr[$1]+=1}END{ for (key in arr) printf("%s\n",  arr[key])}' | awk '{arr[$1]+=1}END{ for (key in arr) printf("%s,%s\n",key, arr[key])}'`
for att in $attList 
do
 echo "insert into tribeAttackAndweiguan(num,amount,type,date,dsum) values($att,'attack','$tdate',3)" >> $sqlfile
done 
#围观人数分布
> /tmp/alltride_$tdate.temp
grep action=tribeMonsterSelectPosition $filename | grep  oldPosition=,|awk -F '[=,]' '{print $13,$3}' |sort -u| awk '{arr[$1]+=1}END{ for (key in arr) printf("%s,%s\n",key,arr[key])}' >> /tmp/alltride_$tdate.temp 
> /tmp/attacktride_$tdate.temp
grep action=tribeMonsterSelectPosition $filename | grep -v  oldPosition=,|awk -F '[=,]' '{print $13,$3}' |sort -u| awk '{arr[$1]+=1}END{ for (key in arr) printf("%s,%s\n",key,arr[key])}' >> /tmp/attacktride_$tdate.temp 
tlist=`cat /tmp/alltride_$tdate.temp | awk -F ',' '{print $1}'` 
> /tmp/wnum.temp.txt
for t in tlist 
do
	anum=`grep $t /tmp/alltride_$tdate.temp | awk -F ',' '{print $2}'`
	tnum=`grep $t /tmp/attacktride_$tdate.temp | awk -F ',' '{print $2}'`
	wnum=`expr $anum-$tnum` 
	echo $wnum  >> /tmp/wnum.temp.txt 
done 

wgList=`cat /tmp/wnum.temp.txt|awk '{arr[$1]+=1}END{ for (key in arr) printf("%s,%s\n",key, arr[key])}'`
#for att in $wgList
#do
 #echo "insert into tribeAttackAndweiguan(num,amount,type,date,dsum) values($att,'weiguan','$tdate',3)" >> $sqlfile
#done 





#转到程序目录
cd /home/mozat/morange/oaProStat/OAProStats_Middle_East_Saudi 
#执行sq语句,入库
java -cp ./lib/poi-scratchpad-3.7-20101029.jar:./lib/poi-ooxml-schemas-3.7-20101029.jar:./lib/poi-ooxml-3.7-20101029.jar:./lib/poi-examples-3.7-20101029.jar:./lib/poi-3.7-20101029.jar:./lib/commons-collections-3.1.jar:./lib/commons-configuration-1.1.jar:./lib/commons-dbcp-1.2.1.jar:./lib/commons-lang-2.1.jar:./lib/commons-logging.jar:./lib/commons-pool-1.2.jar:./lib/log4j-1.2.9.jar:./lib/sqljdbc.jar:./lib/ezmorph-1.0.4.jar:./lib/commons-beanutils.jar:./lib/json-lib-2.2-jdk15.jar:./lib/commons-email-1.3.jar:./lib/mail.jar: main.ExecSql $sqlfile  

done
