package basicStats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.*;

public class myCreditStats {
	
	static final Logger logger = Logger.getLogger(myCreditStats.class);
	static String dbReadUrls = null;
	static String dbDriver = null;
	static String db1286 = null;
	static MoDBRW dbClient1286 = null;
	static MoDBRW dbClient167 = null;
	static String oaLog = null;
	
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration(
					"system.properties"));
			Configuration serverConf = settings.subset("service");
			db1286 = serverConf.getString("db086");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			dbClient1286 = new MoDBRW(db1286,dbDriver);
			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
			oaLog = serverConf.getString("OALog");
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	
	
	static public void StartStats2(Date fromTime){
		logger.info("Credit sales start "+new Date());
		ArrayList<Integer> testList = MyUtil.getTestAccount();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String path = oaLog + "trace.log."+ simpleDateFormat.format(fromTime);
		Map<String,String> nameMap = NewLogUtil.getNameMapItemLog();
		BufferedReader br = null;
		String temp = null;
		String tempPath = "";
		String monetid = "";
		String item = "";
		java.util.Map<String,Map<String,Integer>> itemSell = new java.util.HashMap<String,Map<String,Integer>>();
		java.util.Map<String,Integer> amountMap = new java.util.HashMap<String,Integer>();
		java.util.Map<String,Integer> userMap = new java.util.HashMap<String,Integer>();
		java.util.Map<String,Integer> maxMap = new java.util.HashMap<String,Integer>();
		java.util.Map<String,Double> CreditMap = new java.util.HashMap<String,Double>();
		Set<String> paymentUser = new HashSet<String>();
		
		for(int index=0;index<24;index++){
			if(index<10){
				tempPath = path +"-0"+index;
			}else{
				tempPath = path +"-"+index;
			}
			try {
				if((new File(tempPath)).exists()){
					br = new BufferedReader(new FileReader(tempPath));
					temp = br.readLine();
					while(temp!=null){
						if(temp.contains("BuyEquipmentByCredit")){
							monetid = MyUtil.getValueOfKey(temp,"monetId",",");
							if(MyUtil.isMonetid(monetid)){
								int monetidd = Integer.parseInt(monetid);
								if(!testList.contains(monetidd)){
									paymentUser.add(monetid);
									item = getItemNameId(temp);
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
										CreditMap.put(item,CreditMap.get(item)+Double.parseDouble(getItemPrice(temp)));
										if(itemSell.get(item).get(monetid)>maxMap.get(item)){
											maxMap.put(item, itemSell.get(item).get(monetid));
										}
									}else{
										itemSell.put(item,new HashMap<String,Integer>());
										itemSell.get(item).put(monetid,1);
										amountMap.put(item,1);
										userMap.put(item,1);
										maxMap.put(item,1);
										CreditMap.put(item,Double.parseDouble(getItemPrice(temp)));
									}
								}
							}
						}
						temp = br.readLine();
					}
				}else{
					System.out.println(tempPath + " not find");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}	
			Set<String> key = amountMap.keySet();
			for(String s:key){
				if(nameMap.containsKey(s)){
					//setSellingFromA(nameMap.get(s), amountMap.get(s),userMap.get(s),maxMap.get(s),0,CreditMap.get(s),fromTime);
					System.out.println(nameMap.get(s) +"," + amountMap.get(s)+"," +userMap.get(s)+"," +maxMap.get(s)+"," +0+"," +CreditMap.get(s)+"," +fromTime);
				}
			}
			//OAproKPI.setKpi("PaymentUser", paymentUser.size(), fromTime);
			try {
				if(br!=null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			logger.info("Credit sales end "+new Date());
	}
	
	static public double CreditSelling(Date fromTime,Date toTime){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {fromTime,toTime};
			DBResultSet rs = dbClient1286.execSQLQuery(
							"select sum(money) as amount from payment where app_name='ocean age' and time_payment>=? and time_payment<? and user_id>50000",
							dbArgs);
			if(rs.next()){
				result = rs.getDouble("amount");
			}
		} catch (Exception e) {
			logger.error("CreditSelling with result=" + result, e);
		}
		return result;
	}
	
	static public void deleteSelling(Date fromTime){
		Date toTime = new Date(fromTime.getTime()+1*24*3600*1000);
		Object[] dbArgs2 = new Object[] {fromTime,toTime};
		try {
			dbClient167.execSQLUpdate("delete from selling where stime>=? and stime<?", dbArgs2);
		} catch (Exception e) {
			logger.error("deleteSelling fromTime=" + fromTime + " toTime=" + toTime, e);
		}
	}
	
	static public void setSellingFromA(String item,int sellingAmount,int totalUser,int maxUser,int minUser,Double credit,Date fromTime) {
		try {

			Object[] dbArgs = new Object[] {item,sellingAmount,totalUser,maxUser,minUser,credit,fromTime};
			dbClient167.execSQLUpdate(
							"insert into selling(iname,amount,totalUser,maxUser,minUser,credit,stime)values (?,?,?,?,?,?,?)",
							dbArgs);
			
		} catch (Exception e) {
			logger.error("setSellingFromAtoB with item=" + item + " amount=" + sellingAmount
					+ " fromTime=" + fromTime, e);
		}
		System.out.println("Item="+item+",sales="+sellingAmount+",Users="+totalUser+",Max="+maxUser+",Min="+minUser+",Credit="+credit+",Date="+fromTime);
	}
	
	static public String getItemNameId(String temp) {
		String id = "";
		if(temp.contains("BuyItem=")){
			int start = 0;
			int end = 0;
			if(temp.indexOf("BuyItem=") != -1){
				start = temp.indexOf("item=") + 19;
				id = temp.substring(start);
				if(id.indexOf("/") != -1){
					end = id.indexOf(",");
					id = id.substring(0,end).split(":")[3];
				}
			}
		}
		return id;
	}
	static public String getItemPrice(String temp) {
		String price = "";
		if(temp.contains("BuyItem=")){
			int start = 0;
			int end = 0;
			if(temp.indexOf("BuyItem=") != -1){
				start = temp.indexOf("item=") + 19;
				price = temp.substring(start);
				if(price.indexOf("/") != -1){
					end = price.indexOf(",");
					price = price.substring(0,end).split(":")[2];
				}
			}
		}
		return price;
	}
	
	public static void main(String args[]){
		StartStats2(new Date());
	}
}
