#!/bin/bash 
#for i in {1..1}
#do 
cd  ~/morange/oaProStat/OAProStats_Middle_East_Saudi 
date1=`date -d "-0day" +%Y-%m-%d` 
date=`date -d "-7day" +%Y-%m-%d` 
java -cp ./lib/poi-scratchpad-3.7-20101029.jar:./lib/poi-ooxml-schemas-3.7-20101029.jar:./lib/poi-ooxml-3.7-20101029.jar:./lib/poi-examples-3.7-20101029.jar:./lib/poi-3.7-20101029.jar:./lib/commons-collections-3.1.jar:./lib/commons-configuration-1.1.jar:./lib/commons-dbcp-1.2.1.jar:./lib/commons-lang-2.1.jar:./lib/commons-logging.jar:./lib/commons-pool-1.2.jar:./lib/log4j-1.2.9.jar:./lib/sqljdbc.jar:./lib/ezmorph-1.0.4.jar:./lib/commons-beanutils.jar:./lib/json-lib-2.2-jdk15.jar:./lib/commons-email-1.3.jar:./lib/mail.jar: both.calc.Report $date $date1 
#done
