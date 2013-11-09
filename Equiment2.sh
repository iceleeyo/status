#!/bin/bash 
for i in {1..1} 
do 
cd /home/mozat/morange/oapro/OceanAgelogs 
date=`date -d "-${i}day" +%Y-%m-%d`
> /tmp/equiment$date

grep  BuyEquipmentByCredit trace.log.$date* | awk -F ',' 'BEGIN{ORS=" "}{ print  substr($2,28);split($4,A,":");print substr(A[4],1,length(A[4])-1);print substr($NF,11);print "\n"}' >> /tmp/equiment$date 
EBuyNum=`cat /tmp/equiment$date | wc -l` 
EBuyAmount=`cat /tmp/equiment$date | awk -F ' ' '{print $3}' |  awk 'BEGIN{sum=0}{sum+=$1}END{print sum}'`  
EBuyPerson=`cat /tmp/equiment$date | awk -F ' ' '{print $1}' | sort -u | wc -l` 

> /home/mozat/morange/oaProStat/channel/selling_equiment$date 

echo "insert into selling_equiment(type,usetype,itemid,num,person,amount,edate) values('Equipment','Credit',9999,$EBuyNum,$EBuyPerson,$EBuyAmount,'$date')"  >> /home/mozat/morange/oaProStat/channel/selling_equiment$date 

itemlist=`cat /tmp/equiment$date| awk -F ' ' '{print $2}' | sort -u `
for item in $itemlist
do
	> /tmp/equiment_${item}_${date} 
	grep -w $item /tmp/equiment$date >> /tmp/equiment_${item}_${date} 
	n=`cat /tmp/equiment_${item}_${date} | wc -l` 
	p=`cat /tmp/equiment_${item}_${date} | awk -F ' ' '{print $1}' | sort -u |wc -l` 
	a=`cat /tmp/equiment_${item}_${date} | awk -F ' ' '{print $3}' |  awk 'BEGIN{sum=0}{sum+=$1}END{print sum}'`  
	echo "insert into selling_equiment(type,usetype,itemid,num,person,amount,edate) values('Equipment','Credit',$item,$n,$p,$a,'$date')"  >> /home/mozat/morange/oaProStat/channel/selling_equiment$date 
done


> /tmp/equiment_gold$date
 grep  BuyEquipmentByGold trace.log.$date* | awk -F ',' '{split($4,A,":");print substr($2,28),substr(A[4],1,length(A[4]-1)),substr($NF,9)}' >> /tmp/equiment_gold$date
CEBuyNum=`cat /tmp/equiment_gold$date | wc -l`         
CEBuyAmount=`cat /tmp/equiment_gold$date | awk -F ' ' '{print $3}' |  awk '{sum+=$1}END{print sum}'`                           
CEBuyPerson=`cat /tmp/equiment_gold$date | awk -F ' ' '{print $1}' | sort -u | wc -l`
echo "insert into selling_equiment(type,usetype,itemid,num,person,amount,edate) values('Equipment','Gold',9999,$CEBuyNum,$CEBuyPerson,$CEBuyAmount,'$date')"  >> /home/mozat/morange/oaProStat/channel/selling_equiment$date 

item_gold_list=`cat /tmp/equiment_gold$date|awk -F ' ' '{print $2}' | sort -u` 
for item in $item_gold_list 
do
	> /tmp/gold_equiment_${item}_${date} 
	grep -w $item /tmp/equiment_gold$date >> /tmp/gold_equiment_${item}_${date} 
	gn=`cat /tmp/gold_equiment_${item}_${date} |  wc -l` 
	gp=`cat /tmp/gold_equiment_${item}_${date} | awk -F ' ' '{print $1}' | sort -u |wc -l`
	ga=`cat /tmp/gold_equiment_${item}_${date} | awk -F ' ' '{print $3}' |  awk 'BEGIN{sum=0}{sum+=$1}END{print sum}'`
	echo "insert into selling_equiment(type,usetype,itemid,num,person,amount,edate) values('Equipment','Gold',$item,$gn,$gp,$ga,'$date')"  >> /home/mozat/morange/oaProStat/channel/selling_equiment$date
done

xiaohao=`cat /home/mozat/morange/oaProStat/OAProStats_Middle_East_Saudi/SubsidiaryAccounts.txt | head -1`
buynum1_1=`grep action=BuyReinforceStoneByCredit trace.log.$date* |grep -Ev $xiaohao |grep Stone:1  |grep :601| wc -l` 
buynum1_2=`grep action=BuyReinforceStoneByCredit trace.log.$date* |grep -Ev $xiaohao |grep Stone:1  |grep :602| wc -l` 
buynum2_1=`grep action=BuyReinforceStoneByCredit trace.log.$date* |grep -Ev $xiaohao |grep Stone:2  |grep :603| wc -l` 
buynum2_2=`grep action=BuyReinforceStoneByCredit trace.log.$date* |grep -Ev $xiaohao |grep Stone:2  |grep :604| wc -l` 
buynum3_1=`grep action=BuyReinforceStoneByCredit trace.log.$date* |grep -Ev $xiaohao |grep Stone:3  |grep :605| wc -l` 
buynum3_2=`grep action=BuyReinforceStoneByCredit trace.log.$date* |grep -Ev $xiaohao |grep Stone:3  |grep :606| wc -l` 
#buynum=`expr $buynum1_1 + $buynum1_2 + $buynum2_1 + $buynum2_2 + $buynum3_1 + $buynum3_2`  
buynum=`grep action=BuyReinforceStoneByCredit trace.log.$date* |grep -Ev $xiaohao | wc -l`
buyPerson=`grep action=BuyReinforceStoneByCredit trace.log.$date* |grep -Ev $xiaohao |awk -F ',' '{print substr($2,36)}' | sort -u | wc -l`
buyAmount=`grep action=BuyReinforceStoneByCredit trace.log.$date* |grep -Ev $xiaohao | awk -F ',' '{print substr($NF,11)}' | awk 'BEGIN{sum=0}{sum+=$1}END{print sum}'`  

p1_1=`grep action=BuyReinforceStoneByCredit trace.log.$date* | grep Stone:1 |grep :601 |grep -Ev $xiaohao|awk -F ',' '{print substr($2,36)}' | sort -u | wc -l`
p1_2=`grep action=BuyReinforceStoneByCredit trace.log.$date* | grep Stone:1 |grep :602 |grep -Ev $xiaohao |awk -F ',' '{print substr($2,36)}' | sort -u | wc -l`
a1_1=`grep action=BuyReinforceStoneByCredit trace.log.$date* | grep Stone:1 |grep :601 |grep -Ev $xiaohao|awk -F ',' '{print substr($NF,11)}' | awk 'BEGIN{sum=0}{sum+=$1}END{print sum}'`
a1_2=`grep action=BuyReinforceStoneByCredit trace.log.$date* | grep Stone:1 |grep :602 |grep -Ev $xiaohao|awk -F ',' '{print substr($NF,11)}' | awk 'BEGIN{sum=0}{sum+=$1}END{print sum}'`

p2_1=`grep action=BuyReinforceStoneByCredit trace.log.$date* | grep Stone:2  |grep :603 |grep -Ev $xiaohao|awk -F ',' '{print substr($2,36)}' | sort -u | wc -l`
p2_2=`grep action=BuyReinforceStoneByCredit trace.log.$date* | grep Stone:2  |grep :604 |grep -Ev $xiaohao|awk -F ',' '{print substr($2,36)}' | sort -u | wc -l`
a2_1=`grep action=BuyReinforceStoneByCredit trace.log.$date* | grep Stone:2  |grep :603 |grep -Ev $xiaohao|awk -F ',' '{print substr($NF,11)}' | awk 'BEGIN{sum=0}{sum+=$1}END{print sum}'`
a2_2=`grep action=BuyReinforceStoneByCredit trace.log.$date* | grep Stone:2  |grep :604 |grep -Ev $xiaohao|awk -F ',' '{print substr($NF,11)}' | awk 'BEGIN{sum=0}{sum+=$1}END{print sum}'`
p3_1=`grep action=BuyReinforceStoneByCredit trace.log.$date* | grep Stone:3  |grep :605 |grep -Ev $xiaohao|awk -F ',' '{print substr($2,36)}' | sort -u | wc -l`
p3_2=`grep action=BuyReinforceStoneByCredit trace.log.$date* | grep Stone:3  |grep :606 |grep -Ev $xiaohao|awk -F ',' '{print substr($2,36)}' | sort -u | wc -l`
a3_1=`grep action=BuyReinforceStoneByCredit trace.log.$date* | grep Stone:3  |grep :605 |grep -Ev $xiaohao|awk -F ',' '{print substr($NF,11)}' | awk 'BEGIN{sum=0}{sum+=$1}END{print sum}'`
a3_2=`grep action=BuyReinforceStoneByCredit trace.log.$date* | grep Stone:3  |grep :606 |grep -Ev $xiaohao|awk -F ',' '{print substr($NF,11)}' | awk 'BEGIN{sum=0}{sum+=$1}END{print sum}'`

echo "insert into selling_equiment(type,usetype,itemid,num,person,amount,edate) values('ReinforceStone','Credit',9999,$buynum,$buyPerson,$buyAmount,'$date')"  >> /home/mozat/morange/oaProStat/channel/selling_equiment$date
echo "insert into selling_equiment(type,usetype,itemid,num,person,amount,edate) values('ReinforceStone','Credit',1,$buynum1_1,$p1_1,$a1_1,'$date')"  >> /home/mozat/morange/oaProStat/channel/selling_equiment$date
echo "insert into selling_equiment(type,usetype,itemid,num,person,amount,edate) values('ReinforceStone','Credit',2,$buynum1_2,$p1_2,$a1_2,'$date')"  >> /home/mozat/morange/oaProStat/channel/selling_equiment$date
echo "insert into selling_equiment(type,usetype,itemid,num,person,amount,edate) values('ReinforceStone','Credit',3,$buynum2_1,$p2_1,$a2_1,'$date')"  >> /home/mozat/morange/oaProStat/channel/selling_equiment$date
echo "insert into selling_equiment(type,usetype,itemid,num,person,amount,edate) values('ReinforceStone','Credit',4,$buynum2_2,$p2_2,$a2_2,'$date')"  >> /home/mozat/morange/oaProStat/channel/selling_equiment$date
echo "insert into selling_equiment(type,usetype,itemid,num,person,amount,edate) values('ReinforceStone','Credit',5,$buynum3_1,$p3_1,$a3_1,'$date')"  >> /home/mozat/morange/oaProStat/channel/selling_equiment$date
echo "insert into selling_equiment(type,usetype,itemid,num,person,amount,edate) values('ReinforceStone','Credit',6,$buynum3_2,$p3_2,$a3_2,'$date')"  >> /home/mozat/morange/oaProStat/channel/selling_equiment$date

cd /home/mozat/morange/oaProStat/OAProStats_Middle_East_Saudi 
java -cp ./lib/poi-scratchpad-3.7-20101029.jar:./lib/poi-ooxml-schemas-3.7-20101029.jar:./lib/poi-ooxml-3.7-20101029.jar:./lib/poi-examples-3.7-20101029.jar:./lib/poi-3.7-20101029.jar:./lib/commons-collections-3.1.jar:./lib/commons-configuration-1.1.jar:./lib/commons-dbcp-1.2.1.jar:./lib/commons-lang-2.1.jar:./lib/commons-logging.jar:./lib/commons-pool-1.2.jar:./lib/log4j-1.2.9.jar:./lib/sqljdbc.jar:./lib/ezmorph-1.0.4.jar:./lib/commons-beanutils.jar:./lib/json-lib-2.2-jdk15.jar:./lib/commons-email-1.3.jar:./lib/mail.jar: main.ExecSql /home/mozat/morange/oaProStat/channel/selling_equiment$date 
done




cd /home/mozat/morange/oaProStat/OAProStats_Middle_East_Saudi 
java -cp ./lib/poi-scratchpad-3.7-20101029.jar:./lib/poi-ooxml-schemas-3.7-20101029.jar:./lib/poi-ooxml-3.7-20101029.jar:./lib/poi-examples-3.7-20101029.jar:./lib/poi-3.7-20101029.jar:./lib/commons-collections-3.1.jar:./lib/commons-configuration-1.1.jar:./lib/commons-dbcp-1.2.1.jar:./lib/commons-lang-2.1.jar:./lib/commons-logging.jar:./lib/commons-pool-1.2.jar:./lib/log4j-1.2.9.jar:./lib/sqljdbc.jar:./lib/ezmorph-1.0.4.jar:./lib/commons-beanutils.jar:./lib/json-lib-2.2-jdk15.jar:./lib/commons-email-1.3.jar:./lib/mail.jar: petEquiment.PetEquiment  
