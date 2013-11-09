package basicStats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

public class ItemUsing {
	
	static final Logger logger = Logger.getLogger(ItemUsing.class);
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient167 = null;
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

			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	static public void StartStats(Date fromTime){
		StatsFromItemLog(fromTime);
		StatsFromTrace(fromTime);
	}
	
	static public void StatsFromItemLog(Date fromTime){
		logger.info("Item using start "+new Date());
		ArrayList<Integer> testList = MyUtil.getTestAccount();
		//the log location
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String path = oaLog + "item.log."+ simpleDateFormat.format(fromTime);
//		path = "c:\\item.log";
		String tempPath = "";
		Map<String,String> nameMap = NewLogUtil.getNameMapItemLog();
		BufferedReader br = null;
		try {
			String temp = null;
			String monetid = "";

			java.util.Map<String,Map<String,Integer>> itemUsing = new java.util.HashMap<String,Map<String,Integer>>();
			java.util.Map<String,Integer> amountMap = new java.util.HashMap<String,Integer>();
			java.util.Map<String,Integer> userMap = new java.util.HashMap<String,Integer>();
			java.util.Map<String,Integer> maxMap = new java.util.HashMap<String,Integer>();
			
			java.util.Map<String,Map<String,Integer>> attackMonsterItemUsing = new java.util.HashMap<String,Map<String,Integer>>();
			java.util.Map<String,Integer> attackMonsterAmountMap = new java.util.HashMap<String,Integer>();
			java.util.Map<String,Integer> attackMonsterUserMap = new java.util.HashMap<String,Integer>();
			java.util.Map<String,Integer> attackMonsterMaxMap = new java.util.HashMap<String,Integer>();
			
			java.util.Map<String,Map<String,Integer>> attackWorldMonsterItemUsing = new java.util.HashMap<String,Map<String,Integer>>();
			java.util.Map<String,Integer> attackWorldMonsterAmountMap = new java.util.HashMap<String,Integer>();
			java.util.Map<String,Integer> attackWorldMonsterUserMap = new java.util.HashMap<String,Integer>();
			java.util.Map<String,Integer> attackWorldMonsterMaxMap = new java.util.HashMap<String,Integer>();
			
			String item1 = "";
			
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
						if(temp.contains("method=Use,")
								&&(temp.contains("action=attackWorldMonster,")||temp.contains("action=useSpeaker")||temp.contains("action=attackMonster,")||temp.contains("action=attack,")||temp.contains("action=useWeapon,")
										||temp.contains("action=useCrew")||temp.contains("action=useAvatar"))){
							monetid = NewLogUtil.getFileMonetId(temp);
							if(MyUtil.isMonetid(monetid)){
								int monetidd = Integer.parseInt(monetid);
								if(!testList.contains(monetidd)){
									item1 = NewLogUtil.getItemNameId(temp);
									if(item1.equals("")){
										System.out.println(temp);
									}
									if(itemUsing.containsKey(item1)){
										if(!itemUsing.get(item1).containsKey(monetid)){
											itemUsing.get(item1).put(monetid,0);
											userMap.put(item1,userMap.get(item1)+1);
										}
										itemUsing.get(item1).put(monetid,itemUsing.get(item1).get(monetid)+1);
										amountMap.put(item1,amountMap.get(item1)+1);
										if(itemUsing.get(item1).get(monetid)>maxMap.get(item1)){
											maxMap.put(item1, itemUsing.get(item1).get(monetid));
										}
									}else{
										itemUsing.put(item1,new HashMap<String,Integer>());
										itemUsing.get(item1).put(monetid,1);
										amountMap.put(item1,1);
										userMap.put(item1,1);
										maxMap.put(item1,1);
									}
								}
							}
						}
						
						if(temp.contains("method=Use,")&&temp.contains("action=attackWorldMonster,")
								&&!MyUtil.isTestAccount(monetid)){
							item1 = NewLogUtil.getItemName(temp);
							if(item1.equals("")){
								System.out.println(temp);
							}
							if(MyUtil.isMonetid(monetid)){
								int monetidd = Integer.parseInt(monetid);
								if(!testList.contains(monetidd)){
									if(attackWorldMonsterItemUsing.containsKey(item1)){
										if(!attackWorldMonsterItemUsing.get(item1).containsKey(monetid)){
											attackWorldMonsterItemUsing.get(item1).put(monetid,0);
											attackWorldMonsterUserMap.put(item1,attackWorldMonsterUserMap.get(item1)+1);
										}
										attackWorldMonsterItemUsing.get(item1).put(monetid,attackWorldMonsterItemUsing.get(item1).get(monetid)+1);
										attackWorldMonsterAmountMap.put(item1,attackWorldMonsterAmountMap.get(item1)+1);
										if(attackWorldMonsterItemUsing.get(item1).get(monetid)>attackWorldMonsterMaxMap.get(item1)){
											attackWorldMonsterMaxMap.put(item1, attackWorldMonsterItemUsing.get(item1).get(monetid));
										}
									}else{
										attackWorldMonsterItemUsing.put(item1,new HashMap<String,Integer>());
										attackWorldMonsterItemUsing.get(item1).put(monetid,1);
										attackWorldMonsterAmountMap.put(item1,1);
										attackWorldMonsterUserMap.put(item1,1);
										attackWorldMonsterMaxMap.put(item1,1);
									}
								}
							}
						}
						
						if(temp.contains("method=Use,")&&temp.contains("action=attackMonster,")&&!MyUtil.isTestAccount(monetid)){
							item1 = NewLogUtil.getItemName(temp);
							if(item1.equals("")){
								System.out.println(temp);
							}
							if(MyUtil.isMonetid(monetid)){
							int monetidd = Integer.parseInt(monetid);
							if(!testList.contains(monetidd)){
								if(attackMonsterItemUsing.containsKey(item1)){
									if(!attackMonsterItemUsing.get(item1).containsKey(monetid)){
										attackMonsterItemUsing.get(item1).put(monetid,0);
										attackMonsterUserMap.put(item1,attackMonsterUserMap.get(item1)+1);
									}
									attackMonsterItemUsing.get(item1).put(monetid,attackMonsterItemUsing.get(item1).get(monetid)+1);
									attackMonsterAmountMap.put(item1,attackMonsterAmountMap.get(item1)+1);
									if(attackMonsterItemUsing.get(item1).get(monetid)>attackMonsterMaxMap.get(item1)){
										attackMonsterMaxMap.put(item1, attackMonsterItemUsing.get(item1).get(monetid));
									}
								}else{
									attackMonsterItemUsing.put(item1,new HashMap<String,Integer>());
									attackMonsterItemUsing.get(item1).put(monetid,1);
									attackMonsterAmountMap.put(item1,1);
									attackMonsterUserMap.put(item1,1);
									attackMonsterMaxMap.put(item1,1);
								}
							}
						}
					}
					temp = br.readLine();
				}
			}
		}
			try {
				if(br!=null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
//			deleteUsing(fromTime);
			Set<String> key = amountMap.keySet();
			for(String s:key){
				if(nameMap.containsKey(s)){
					setUsingFromA(nameMap.get(s), amountMap.get(s),userMap.get(s),maxMap.get(s),0,"all",fromTime);
				}
			}
			key = attackMonsterAmountMap.keySet();
			for(String s:key){
				if(nameMap.containsKey(s)){
					setUsingFromA(nameMap.get(s), attackMonsterAmountMap.get(s),attackMonsterUserMap.get(s),attackMonsterMaxMap.get(s),0,"attackMonster",fromTime);
				}
			}
			key = attackWorldMonsterAmountMap.keySet();
			for(String s:key){
				if(nameMap.containsKey(s)){
					setUsingFromA(nameMap.get(s), attackWorldMonsterAmountMap.get(s),attackWorldMonsterUserMap.get(s),attackWorldMonsterMaxMap.get(s),0,"attackWorldMonster",fromTime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Item using end "+new Date());
	}
	
	static public void StatsFromTrace(Date fromTime){
		ArrayList<Integer> testList = MyUtil.getTestAccount();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String path = oaLog + "trace.log."+ simpleDateFormat.format(fromTime);
		String tempPath = "";
		Map<String,String> nameMap = NewLogUtil.getNameMap();
		BufferedReader br = null;
		try {
			String temp = null;
			String monetid = "";

			java.util.Map<String,Map<String,Integer>> itemUsing = new java.util.HashMap<String,Map<String,Integer>>();
			java.util.Map<String,Integer> amountMap = new java.util.HashMap<String,Integer>();
			java.util.Map<String,Integer> userMap = new java.util.HashMap<String,Integer>();
			java.util.Map<String,Integer> maxMap = new java.util.HashMap<String,Integer>();
			
			String item1 = "";
			
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
						if(
								(temp.contains("action=Defence,")&&temp.contains("IsSuccess=success")) ||
								(temp.contains("action=UseCard")&&temp.contains("Card="))
						
						){
							monetid = NewLogUtil.getFileMonetId(temp);
							if(MyUtil.isMonetid(monetid)){
								int monetidd = Integer.parseInt(monetid);
								if(!testList.contains(monetidd)){
									item1 = NewLogUtil.getDefenseNameTraceLog(temp);
									if(item1.equals("")){
										System.out.println(temp);
									}
									if(itemUsing.containsKey(item1)){
										if(!itemUsing.get(item1).containsKey(monetid)){
											itemUsing.get(item1).put(monetid,0);
											userMap.put(item1,userMap.get(item1)+1);
										}
										itemUsing.get(item1).put(monetid,itemUsing.get(item1).get(monetid)+1);
										amountMap.put(item1,amountMap.get(item1)+1);
										if(itemUsing.get(item1).get(monetid)>maxMap.get(item1)){
											maxMap.put(item1, itemUsing.get(item1).get(monetid));
										}
									}else{
										itemUsing.put(item1,new HashMap<String,Integer>());
										itemUsing.get(item1).put(monetid,1);
										amountMap.put(item1,1);
										userMap.put(item1,1);
										maxMap.put(item1,1);
									}
								}
							}
						}
						temp = br.readLine();
					}
				}
			}
			
			try {
				if(br!=null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Set<String> key = amountMap.keySet();
			for(String s:key){
				if(nameMap.containsKey(s)){
					setUsingFromA(nameMap.get(s), amountMap.get(s),userMap.get(s),maxMap.get(s),0,"all",fromTime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static public void deleteUsing(Date fromTime) {
		try {
			Date toTime = new Date(fromTime.getTime()+1*24*3600*1000);
			Object[] dbArgs2 = new Object[] {fromTime,toTime};
			dbClient167.execSQLUpdate("delete from using where utime>=? and utime<?", dbArgs2);
			
		} catch (Exception e) {
			logger.error("deleteUsing with fromTime=" + fromTime , e);
		}

	}
	
	static public void setUsingFromA(String item,int usingAmount,int totalUser,int maxUser,int minUser,String type,Date fromTime) {
		try {

			Object[] dbArgs = new Object[] {item,usingAmount,totalUser,maxUser,minUser,type,fromTime};
			dbClient167.execSQLUpdate(
							"insert into using(iname,amount,totalUser,maxUser,minUser,type,utime)values (?,?,?,?,?,?,?)",
							dbArgs);
			
		} catch (Exception e) {
			logger.error("setSellingFromAtoB with item=" + item + " amount=" + usingAmount
					+ " fromTime=" + fromTime, e);
		}
		logger.info("item="+item+",amount="+usingAmount+",users="+totalUser+",maxUser="+maxUser+",minUser="+minUser+",Date="+fromTime);
	}
	
	public static void main(String args[]){
		String temp = "INFO 27 May 01:02:59,203 -(TraceLog.java:96) - monetId=13277,action=UseCard,Card=/Card:2:1:1855/,FishId=92439";
		System.out.println(NewLogUtil.getDefenseNameTraceLog(temp));
	}
}
