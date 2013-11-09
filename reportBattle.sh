#!/bin/bash
for i in {1..1} 
do
cd /home/mozat/morange/oapro/OceanAgelogs 
date=`date -d "-${i}day" +%Y-%m-%d`

sqlfile="/home/mozat/morange/oaProStat/channel/reportbattle.txt.$date" 
> $sqlfile 
rNum=`grep action=reportBattle action.log.$date* | wc -l`
rPerson=`grep action=reportBattle action.log.$date* |awk -F ',' '{print substr($8,14)}' |  sort -u | wc -l`

rSuccessNum=`grep action=reportBattle action.log.$date* | grep result=1| wc -l`
rSuccessPerson=`grep action=reportBattle action.log.$date* | grep result=1|awk -F ',' '{print substr($8,14)}' |  sort -u | wc -l` 

rFailNum=`grep action=reportBattle action.log.$date* | grep result=0| wc -l`
rFailPerson=`grep action=reportBattle action.log.$date* | grep result=0|awk -F ',' '{print substr($8,14)}' |  sort -u | wc -l` 

rReward=`grep action=arenaRankingReward action.log.$date* |  awk -F ',' '{print substr($2,35)}'  |sort -u | wc -l ` 

rBuyNum=`grep action=reportBattle item.log.$date* | grep itemType=ArenaChallenge | wc -l` 
rBuyPerson=`grep action=reportBattle item.log.$date* | grep itemType=ArenaChallenge | awk -F ',' '{print substr($2,34)}' | sort -u | wc -l` 
rBuyAmount=`grep action=reportBattle item.log.$date* | grep itemType=ArenaChallenge | awk -F ',' '{print substr($NF,7)}'  | awk '{sum+=$1}END{print sum}'` 

rList=` grep action=reportBattle action.log.$date* | awk -F ',' '{print $7}' | sort -u`
other=''
for r in $rList 
do 
	num=`grep action=reportBattle action.log.$date* |grep -w $r |awk -F ',' '{print substr($8,14)}' |  sort -u | wc -l`
	t=`echo ${r:12}':'$num','`
	other=$other$t
done 

echo "insert into reportBattle values('$date',$rNum,$rPerson,$rSuccessNum,$rSuccessPerson,$rFailNum,$rFailPerson,$rReward,$rBuyNum,$rBuyPerson,$rBuyAmount,'$other')" >> $sqlfile 
  
> /tmp/pet$date
grep action=reportBattle action.log.$date* | awk -F ',' '{print substr($2,35),$4}'   >>  /tmp/pet$date  
allN=`cat /tmp/pet$date | wc -l `
allP=`cat /tmp/pet$date |awk -F ' ' '{print $1}' | sort -u|wc -l` 
PList=`cat /tmp/pet$date |awk -F ' ' '{print $1}' | sort -u`
> /tmp/p$date
for p in $PList 
do
	grep -w $p /tmp/pet$date  | wc -l >> /tmp/p$date 
done 

allMax=`cat /tmp/p$date | awk 'BEGIN{max=0}{if(max < $1) max = $1}END{print max}'`
allMin=`cat /tmp/p$date | awk 'BEGIN{min=1000}{if(min > $1) min = $1}END{print min}'`
allsum=`cat /tmp/p$date | awk '{sum+=$1}END{print sum}'`
allavg=`echo $allsum/$allP | bc`
alln=`echo  $allP/2+1 | bc`  
allmid=`cat /tmp/p$date | sort -n | head -$alln |tail -1`
echo "insert into petdarelevelaction values('$date',0,$allN,$allP,$allMax,$allMin,$allavg,$allmid);"  >> $sqlfile
levelList=`cat /tmp/pet$date | awk -F ' ' '{print $2}' | sort -u`
for level in $levelList 
do
	LPerson=`grep -w $level /tmp/pet$date |awk -F ' ' '{print $1}'| sort -u | wc -l `
	LNum=`grep -w $level /tmp/pet$date | wc -l` 
	> /tmp/$level
	monetidList=`grep -w $level  /tmp/pet$date | awk -F ' ' '{print $1}' | sort -u`
	for person in $monetidList 
	do
		grep -w $level /tmp/pet$date | grep -w $person | wc -l  >> /tmp/$level 
	done
	Lmax=`cat /tmp/$level | awk 'BEGIN{max=0}{if(max < $1) max = $1}END{print max}'`
	Lmin=`cat /tmp/$level | awk 'BEGIN{min=1000}{if(min > $1) min = $1}END{print min}'`
	Lsum=`cat /tmp/$level | awk '{sum+=$1}END{print sum}'` 
	Lavg=`echo $Lsum/$LPerson | bc`
	Ln=`echo  $LPerson/2+1 | bc` 
	Lmid=`cat /tmp/$level | sort -n | head -$Ln |tail -1`
	echo "insert into petdarelevelaction values('$date',${level:9},$LNum,$LPerson,$Lmax,$Lmin,$Lavg,$Lmid);"  >> $sqlfile
done 

> /tmp/startStone$date
grep action=addStarStone  trade.log.$date* | awk -F ',' '{print substr($4,5),substr($5,6)}' >> /tmp/startStone$date
fromList=`cat /tmp/startStone$date | awk -F ' ' '{print $2}'| sort -u `
for f in $fromList
do
	sum=`grep -w $f /tmp/startStone$date  | awk -F ' ' '{print $1}' | awk '{sum+=$1}END{print sum}' `
	echo "insert into starstonefrom values('$date','$f',$sum)" >> $sqlfile 
done

> /tmp/expPet$date
grep action=addPetExp trade.log.$date* | awk -F ',' '{print substr($5,5),substr($6,6)}' >> /tmp/expPet$date 
fromList=`cat /tmp/expPet$date | awk -F ' ' '{print $2}'| sort -u `
for f in $fromList
do
	sum=`grep -w $f /tmp/expPet$date | awk -F ' ' '{print $1}' | awk '{sum+=$1}END{print sum}' `
	echo "insert into petExpFrom values('$date','$f',$sum)" >> $sqlfile
done



cd /home/mozat/morange/oaProStat/OAProStats_Middle_East_Saudi 
java -cp ./lib/poi-scratchpad-3.7-20101029.jar:./lib/poi-ooxml-schemas-3.7-20101029.jar:./lib/poi-ooxml-3.7-20101029.jar:./lib/poi-examples-3.7-20101029.jar:./lib/poi-3.7-20101029.jar:./lib/commons-collections-3.1.jar:./lib/commons-configuration-1.1.jar:./lib/commons-dbcp-1.2.1.jar:./lib/commons-lang-2.1.jar:./lib/commons-logging.jar:./lib/commons-pool-1.2.jar:./lib/log4j-1.2.9.jar:./lib/sqljdbc.jar:./lib/ezmorph-1.0.4.jar:./lib/commons-beanutils.jar:./lib/json-lib-2.2-jdk15.jar:./lib/commons-email-1.3.jar:./lib/mail.jar: main.ExecSql $sqlfile  

done
