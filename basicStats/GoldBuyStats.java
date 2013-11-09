package basicStats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.*;

public class GoldBuyStats {
	static final Logger logger = Logger.getLogger(GoldBuyStats.class);
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient = null;
	static String oaLog = null;
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration(
					"system.properties"));
			Configuration serverConf = settings.subset("service");

			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			oaLog = serverConf.getString("OALog");

			dbClient = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	static public void StartStats(Date fromTime){
		logger.info("Gold sales start "+new Date());
		ArrayList<Integer> testList = MyUtil.getTestAccount();
		//the log location
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String path = oaLog + "item.log."+ simpleDateFormat.format(fromTime);
//		path = "c:\\item.log";
		Map<String,String> nameMap = NewLogUtil.getNameMapItemLog();
		BufferedReader br = null;
		try {
			String temp = null;
			String tempPath = "";
			String monetid = "";
			String item = "";
			java.util.Map<String,Map<String,Integer>> itemSell = new java.util.HashMap<String,Map<String,Integer>>();
			java.util.Map<String,Integer> amountMap = new java.util.HashMap<String,Integer>();
			java.util.Map<String,Integer> userMap = new java.util.HashMap<String,Integer>();
			java.util.Map<String,Integer> maxMap = new java.util.HashMap<String,Integer>();
			java.util.Map<String,Long> goldMap = new HashMap<String,Long>();
			
			for(int index=0;index<24;index++){
				if(index<10){
					tempPath = path +"-0"+index;
				}else{
					tempPath = path +"-"+index;
				}
				if((new File(tempPath)).exists()){
					br = new BufferedReader(new FileReader(tempPath));
					temp = br.readLine();
					while(temp!=null){
						if(temp.contains("BuyByGold")){
							monetid = NewLogUtil.getFileMonetId(temp);
							if(MyUtil.isMonetid(monetid)){
								int monetidd = Integer.parseInt(monetid);
								if(!testList.contains(monetidd)){
									item = NewLogUtil.getItemNameId(temp);
									if(item.equals("")){
										System.out.println(temp);
									}
								
									if(itemSell.containsKey(item)){
										if(!itemSell.get(item).containsKey(monetid)){
											itemSell.get(item).put(monetid,0);
											userMap.put(item,userMap.get(item)+1);
										}
										itemSell.get(item).put(monetid,itemSell.get(item).get(monetid)+1);
										amountMap.put(item,amountMap.get(item)+1);
										goldMap.put(item,goldMap.get(item)+Integer.parseInt(NewLogUtil.getPrice(temp)));
										if(itemSell.get(item).get(monetid)>maxMap.get(item)){
											maxMap.put(item, itemSell.get(item).get(monetid));
										}
									}else{
										itemSell.put(item,new HashMap<String,Integer>());
										itemSell.get(item).put(monetid,1);
										amountMap.put(item,1);
										goldMap.put(item,Long.parseLong((NewLogUtil.getPrice(temp))));
										userMap.put(item,1);
										maxMap.put(item,1);
									}
								}
							}
						}
						temp = br.readLine();
					}
				}
			}
//			deleteSellingGold(fromTime);
			Set<String> key = amountMap.keySet();
			for(String s:key){
				if(nameMap.containsKey(s)){
					setSellingGoldFromA(nameMap.get(s), amountMap.get(s),userMap.get(s),maxMap.get(s),0,goldMap.get(s), fromTime);
				}
			}
			if(br!=null){
				br.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Gold sales end "+new Date());
	}
	
	static public void deleteSellingGold(Date fromTime){
		try {

			Date toTime = new Date(fromTime.getTime()+1*24*3600*1000);
			Object[] dbArgs2 = new Object[] {fromTime,toTime};
			dbClient.execSQLUpdate("delete from selling_gold where gtime>=? and gtime<?", dbArgs2);
			
		} catch (Exception e) {
			logger.error("deleteSellingGold with fromTime" + fromTime , e);
		}
	}
	
	static public void setSellingGoldFromA(String item,int sellingAmount,int totalUser,int maxUser,int minUser,long gold,Date fromTime){
		try {

			Object[] dbArgs = new Object[] {item,sellingAmount,totalUser,maxUser,minUser,gold,fromTime};
			dbClient.execSQLUpdate(
							"insert into selling_gold(iname,amount,totalUser,maxUser,minUser,gold,gtime)values (?,?,?,?,?,?,?)",
							dbArgs);
			
		} catch (Exception e) {
			logger.error("setSellingGoldFromAtoB with item=" + item + " amount=" + sellingAmount
					+ " fromTime=" + fromTime, e);
		}
		logger.info("item="+item+",amount="+sellingAmount+",totalUser="+totalUser+",maxUser="+maxUser+",minUser="+minUser+",gold="+gold+",date="+fromTime);
	}
	
	public static void main(String args[]){
		StartStats(new Date());
	}
}
