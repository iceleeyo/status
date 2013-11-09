date=`date -d "-1 hours" '+%F-%H'`
date2=`date -d "-1 hours" '+%F'`
#date=$1
#date2=$2
server1fname=/home/mozat/morange/oapro/OceanAgelogs/trace.log.${date}*
sqlfile=/home/mozat/morange/oaProStat/item/new_pet.txt.${date}
> ${sqlfile}

node=`grep UpgradePetLevel ${server1fname}|awk -F '[,= ]' '{print $5"'\'',"$10","$NF}'`

for i in $node
do
        echo 'insert into petUpgrade_new (utime,monetid,petlevel) values ('"'"$date2' '$i")" >> ${sqlfile}
done

cd /home/mozat/morange/oaProStat/OAProStats_Middle_East_Saudi

java -cp ./lib/poi-scratchpad-3.7-20101029.jar:./lib/poi-ooxml-schemas-3.7-20101029.jar:./lib/poi-ooxml-3.7-20101029.jar:./lib/poi-examples-3.7-20101029.jar:./lib/poi-3.7-20101029.jar:./lib/commons-collections-3.1.jar:./lib/commons-configuration-1.1.jar:./lib/commons-dbcp-1.2.1.jar:./lib/commons-lang-2.1.jar:./lib/commons-logging.jar:./lib/commons-pool-1.2.jar:./lib/log4j-1.2.9.jar:./lib/sqljdbc.jar:./lib/ezmorph-1.0.4.jar:./lib/commons-beanutils.jar:./lib/json-lib-2.2-jdk15.jar:./lib/commons-email-1.3.jar:./lib/mail.jar: main.ExecSql ${sqlfile}
