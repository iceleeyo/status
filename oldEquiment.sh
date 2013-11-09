#!/bin/bash 
for i in {1..1} 
do 
cd /home/mozat/morange/oapro/OceanAgelogs 
date=`date -d "-${i}day" +%Y-%m-%d`
useLan=`grep action=subCredit  trade.log.$date* | awk -F ',' '{if($6="to=ReinforceEquipment"){print substr($4,5)}}'  | awk '{sum+=$1}END{print sum}' ` 
usePearl=`grep action=subPearl  trade.log.$date* | awk -F ',' '{if($6="to=ReinforceEquipment"){print substr($4,5)}}'  | awk '{sum+=$1}END{print sum}' ` 
EBuyNum=`grep BuyEquipmentByCredit trace.log.$date* | wc -l` 
EBuyAmount=`grep BuyEquipmentByCredit trace.log.$date* | awk -F ',' '{print substr($NF,11)}' |  awk '{sum+=$1}END{print sum}'`  
EBuyPerson=`grep BuyEquipmentByCredit trace.log.$date* | awk -F ',' '{print substr($2,36)}' | sort -u | wc -l`

CEBuyNum=`grep BuyEquipmentByGold  trace.log.$date* | wc -l` 
CEBuyAmount=`grep BuyEquipmentByGold  trace.log.$date* | awk -F ',' '{print substr($NF,9)}' |  awk '{sum+=$1}END{print sum}'`  
CEBuyPerson=`grep BuyEquipmentByGold  trace.log.$date* | awk -F ',' '{print substr($2,36)}' | sort -u | wc -l` 

#addCByE=`grep action=addGold trade.log.$date* |awk -F ',' '{if($5=="from=SellEquipment")print substr($4,5)}' |awk '{sum+=$1}END{print sum}'`  

> /home/mozat/morange/oaProStat/channel/butitem$date
echo "insert into creditStats(type,item,amount,gtime) values('out','ReinforceEquipment',$useLan,'$date') " >> /home/mozat/morange/oaProStat/channel/butitem$date
echo "insert into PearlStats(type,item,amount,gtime) values('out','ReinforceEquipment',$usePearl,'$date') " >> /home/mozat/morange/oaProStat/channel/butitem$date 

echo "insert into Buyitem(type,usetype,num,person,amount,edate) values('Equipment','Credit',$EBuyNum,$EBuyPerson,$EBuyAmount,'$date')"  >> /home/mozat/morange/oaProStat/channel/butitem$date 
echo "insert into Buyitem(type,usetype,num,person,amount,edate) values('Equipment','Gold',$CEBuyNum,$CEBuyPerson,$CEBuyAmount,'$date')"  >> /home/mozat/morange/oaProStat/channel/butitem$date

#usenum=`grep action=subReinforceStone  trade.log.$date* | awk -F ',' '{print substr($4,6)}' | awk '{sum+=$1}END{print sum}' ` 
#addnum=`grep action=addReinforceStone  trade.log.$date* | awk -F ',' '{print substr($4,6)}' | awk '{sum+=$1}END{print sum}' ` 

buynum1=`grep action=BuyReinforceStoneByCredit trace.log.$date* |grep Stone:2  | wc -l| awk '{print $1*500}'` 
buynum2=`grep action=BuyReinforceStoneByCredit trace.log.$date* |grep Stone:1  | wc -l| awk '{print $1*50}'` 
buynum=`expr $buynum1 + $buynum2 `  
buyPerson=`grep action=BuyReinforceStoneByCredit trace.log.$date* |awk -F ',' '{print substr($2,36)}' | sort -u | wc -l`
buyAmount=`grep action=BuyReinforceStoneByCredit trace.log.$date* | awk -F ',' '{print substr($NF,11)}' | awk '{sum+=$1}END{print sum}'` 

echo "insert into Buyitem(type,usetype,num,person,amount,edate) values('ReinforceStone','Credit',$buynum,$buyPerson,$buyAmount,'$date')"  >> /home/mozat/morange/oaProStat/channel/butitem$date





cd /home/mozat/morange/oaProStat/OAProStats_Middle_East_Saudi 
java -cp ./lib/poi-scratchpad-3.7-20101029.jar:./lib/poi-ooxml-schemas-3.7-20101029.jar:./lib/poi-ooxml-3.7-20101029.jar:./lib/poi-examples-3.7-20101029.jar:./lib/poi-3.7-20101029.jar:./lib/commons-collections-3.1.jar:./lib/commons-configuration-1.1.jar:./lib/commons-dbcp-1.2.1.jar:./lib/commons-lang-2.1.jar:./lib/commons-logging.jar:./lib/commons-pool-1.2.jar:./lib/log4j-1.2.9.jar:./lib/sqljdbc.jar:./lib/ezmorph-1.0.4.jar:./lib/commons-beanutils.jar:./lib/json-lib-2.2-jdk15.jar:./lib/commons-email-1.3.jar:./lib/mail.jar: main.ExecSql /home/mozat/morange/oaProStat/channel/butitem$date 

done
