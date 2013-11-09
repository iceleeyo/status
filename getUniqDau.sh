date=`date -d "-1 days" '+%F'`
#date=$1
server1Url=/home/mozat/morange/OceanAgeDailyMonetidList/${date}_newoa5-5.txt
server2Url=/mnt/192.168.1.107/OceanAgeDailyMonetidList/${date}_newoa5-5.txt
resultfname=/home/mozat/morange/OceanAgeDailyMonetidList/${date}_uniq_newoa5-5.txt

> ${resultfname}
clau=0
node=`cat ${server1Url}`
for i in $node
do
        tcount=`grep -w $i ${server2Url}|wc -l`
        #echo $tcount"|"$i
        if [ $tcount -eq 0 ]
        then
             clau=`expr $clau + 1`
	     echo $i >> ${resultfname}
        fi
done
echo ${clau}
