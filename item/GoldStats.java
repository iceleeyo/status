package item;

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

import util.DBResultSet;
import util.MoDBRW;
import util.MyUtil;

public class GoldStats {
	static final Logger logger = Logger.getLogger(GoldStats.class);
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient = null;
	static MoDBRW dbClient81 = null;
	static String dbWriteUrl = null;
	static String oaLog = null;
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration(
					"system.properties"));
			Configuration serverConf = settings.subset("service");

			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			dbWriteUrl = serverConf.getString("dbWriteUrl");
			oaLog = serverConf.getString("OALog");

			dbClient = new MoDBRW(dbReadUrls,dbDriver);
			dbClient81 = new MoDBRW(dbWriteUrl,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	/*
	 * 读取trade.log  获得金币数据
	 */
	public static void countGoldProduce(Date date){
		//from new log
		String path = oaLog + "trade.log."+GetDateToString(date);
		ArrayList<Integer> testList = MyUtil.getTestAccount();
		String tempPath = "";
		BufferedReader br = null;
		
		Map<String,Long> inMap = new HashMap<String,Long>();
		Map<String,Long> outMap = new HashMap<String,Long>();
		for(int index=0;index<24;index++){
			if(index<10){
				tempPath = path +"-0"+index;
			}else{
				tempPath = path +"-"+index;
			}
			try {
				if(new File(tempPath).isFile()){
					System.out.println("file is " + tempPath);
					br = new BufferedReader(new FileReader(tempPath));
					String log = br.readLine();
					while(log!=null){
						if(log.contains("action=addGold")){
							for(int i : testList){
								if(log.contains("monetid="+i+",")){
									System.out.println(log + "--" + i);
									continue;
								}
							}
							String from = MyUtil.getValueOfKey(log,"from",",");
							long num = Long.parseLong(MyUtil.getValueOfKey(log,"num",","));
							num = Math.abs(num);
							if(inMap.containsKey(from)){
								inMap.put(from, inMap.get(from)+num);
							}else{
								inMap.put(from,num);
							}
						}else if(log.contains("action=subGold")){
							for(int i : testList){
								if(log.contains("monetid="+i+",")){
									System.out.println(log + "--" + i);
									continue;
								}
							}
							String to = MyUtil.getValueOfKey(log,"to",",");
							long num = Long.parseLong(MyUtil.getValueOfKey(log,"num",","));
							num = Math.abs(num);
							if(outMap.containsKey(to)){
								outMap.put(to, outMap.get(to)+num);
							}else{
								outMap.put(to,num);
							}
						}
						log = br.readLine();
					}
				}else{
					System.out.println(path + " is not exist!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Set<String> set = inMap.keySet();
		for(String name:set){
			setGoldStats("in", name, inMap.get(name), date);
		}
		set = outMap.keySet();
		for(String name:set){
			setGoldStats("out", name, outMap.get(name), date);
		}
	}
	
	/*
	 * 在trade.log 读取数据获取珍珠数目
	 */
	public static void countPearlProduce(Date date){
		//from new log
		String path = oaLog + "trade.log."+GetDateToString(date);
		ArrayList<Integer> testList = MyUtil.getTestAccount();
		String tempPath = "";
		BufferedReader br = null;
		
		Map<String,Double> inMap = new HashMap<String,Double>();
		Map<String,Double> outMap = new HashMap<String,Double>();
		
		for(int index=0;index<24;index++){
			if(index<10){
				tempPath = path +"-0"+index;
			}else{
				tempPath = path +"-"+index;
			}
			try {
				if(new File(tempPath).isFile()){
					System.out.println("file is " + tempPath);
					br = new BufferedReader(new FileReader(tempPath));
					String log = br.readLine();
					while(log!=null){
						if(log.contains("action=addPearl")){
							for(int i : testList){
								if(log.contains("monetid="+i+",")){
									System.out.println(log + "--" + i);
									continue;
								}
							}
							//拿到数据源  from=finishGuide.19,  里面的值:finishGuide.19
							String from = MyUtil.getValueOfKey(log,"from",",");
							double num = Double.parseDouble(MyUtil.getValueOfKey(log,"num",","));
							num = Math.abs(num);
							if(inMap.containsKey(from)){
								inMap.put(from, inMap.get(from)+num);
							}else{
								inMap.put(from,num);
							}
						}else if(log.contains("action=subPearl")){
							for(int i : testList){
								if(log.contains("monetid="+i+",")){
									System.out.println(log + "--" + i);
									continue;
								}
							}
							String to = MyUtil.getValueOfKey(log,"to",",");
							double num = Double.parseDouble(MyUtil.getValueOfKey(log,"num",","));
							num = Math.abs(num);
							if(outMap.containsKey(to)){
								outMap.put(to, outMap.get(to)+num);
							}else{
								outMap.put(to,num);
							}
						}
						log = br.readLine();
					}
				}else{
					System.out.println(path + " is not exist!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Set<String> set = inMap.keySet();
		for(String name:set){
			setPearlStats("in", name, inMap.get(name), date);
		}
		set = outMap.keySet();
		for(String name:set){
			setPearlStats("out", name, outMap.get(name), date);
		}
	}
	
	/*
	 * 读取trade.log 数据,获得蓝宝石数据
	 */
	public static void countSapphireProduce(Date date){
		//from new log
		String path = oaLog + "trade.log."+GetDateToString(date);
		ArrayList<Integer> testList = MyUtil.getTestAccount();
		String tempPath = "";
		BufferedReader br = null;
		
		Map<String,Double> inMap = new HashMap<String,Double>();
		Map<String,Double> outMap = new HashMap<String,Double>();
		
		for(int index=0;index<24;index++){
			if(index<10){
				tempPath = path +"-0"+index;
			}else{
				tempPath = path +"-"+index;
			}
			try {
				if(new File(tempPath).isFile()){
					//System.out.println("file is " + tempPath);
					br = new BufferedReader(new FileReader(tempPath));
					String log = br.readLine();
					while(log!=null){
						//去掉小号
						
						if(log.contains("action=addCredit")){
							for(int i : testList){
								if(log.contains("monetid="+i+",")){
									System.out.println(log + "--" + i);
									continue;
								}
							}
							String from = MyUtil.getValueOfKey(log,"from",",");
							double num = Double.parseDouble(MyUtil.getValueOfKey(log,"num",","));
							num = Math.abs(num);
							if(inMap.containsKey(from)){
								inMap.put(from, inMap.get(from)+num);
							}else{
								inMap.put(from,num);
							}
						}else if(log.contains("action=subCredit")){
							for(int i : testList){
								if(log.contains("monetid="+i+",")){
									System.out.println(log + "--" + i);
									continue;
								}
							}
							String to = MyUtil.getValueOfKey(log,"to",",");
							double num = Double.parseDouble(MyUtil.getValueOfKey(log,"num",","));
							num = Math.abs(num);
							if(outMap.containsKey(to)){
								outMap.put(to, outMap.get(to)+num);
							}else{
								outMap.put(to,num);
							}
						}
						log = br.readLine();
					}
				}else{
					System.out.println(path + " is not exist!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Set<String> set = inMap.keySet();
		for(String name:set){
			System.out.println(":in:"+name+":"+inMap.get(name));
			setCreditStats("in", name, inMap.get(name), date);
		}
		set = outMap.keySet();
		for(String name:set){
			System.out.println(":out:"+name+":"+outMap.get(name));
			setCreditStats("out", name, outMap.get(name), date);
		}
	}
	
	public static void setAllGold(Date date){
		ArrayList<Integer> testList = MyUtil.getTestAccount();
		StringBuffer ids = new StringBuffer();
		for(int monetid : testList){
			ids.append(monetid);
			ids.append(",");
		}
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs =  dbClient81.execSQLQuery(
							"select sum(money) as amount from fisher where monetid not in ("+ids.substring(0,ids.length()-1)+")",
							dbArgs);
			while(rs.next()){
				GoldStats.setGoldStats("all","all",rs.getLong("amount"), date);
			}
		} catch (Exception e) {
			logger.error("getTribeLevel", e);
		}
	}
	
	public static void setAllCredit(Date date){
		ArrayList<Integer> testList = MyUtil.getTestAccount();
		StringBuffer ids = new StringBuffer();
		for(int monetid : testList){
			ids.append(monetid);
			ids.append(",");
		}
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs =  dbClient81.execSQLQuery(
							"select sum(balance) as amount from profile where monetid not in ("+ids.substring(0,ids.length()-1)+")",
							dbArgs);
			while(rs.next()){
				GoldStats.setCreditStats("all","all",rs.getLong("amount"), date);
			}
		} catch (Exception e) {
			logger.error("getTribeLevel", e);
		}
	}

	public static void setAllPearl(Date date){
		ArrayList<Integer> testList = MyUtil.getTestAccount();
		StringBuffer ids = new StringBuffer();
		for(int monetid : testList){
			ids.append(monetid);
			ids.append(",");
		}
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs =  dbClient81.execSQLQuery(
							"select sum(count) as amount from gem where typeid=1 and ownerid not in ("+ids.substring(0,ids.length()-1)+")",
							dbArgs);
			while(rs.next()){
				GoldStats.setPearlStats("all","all",rs.getLong("amount"), date);
			}
		} catch (Exception e) {
			logger.error("getTribeLevel", e);
		}
	}
	
	
	public static long getNum(String log,String flag){
		long number = -1;
		String temp = "";
		if(log.contains(flag)){
			int start = 0;
			int end = 0;
			if(log.indexOf(flag) != -1){
				start = log.indexOf(flag) + flag.length()+1;
				log = log.substring(start);
				if(log.indexOf(",") != -1){
					end = log.indexOf(",");
					try{
						temp = log.substring(0,end);
						number = Long.parseLong(temp);
					}catch(Exception e){
//						System.out.println(temp);
					}
				}
			}
		}
		return number;
	}
	
	public static String GetDateToString(Date date){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}
	
	static public void setGoldStats(String type,String item,long amount,Date date){
		try {
			Object[] dbArgs = new Object[] {type,item,amount,date};
			dbClient.execSQLUpdate(
							"insert into GoldStats(type,item,amount,gtime) values (?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setGoldStats with " + " fromTime=" + date, e);
		}
		logger.info("type="+type+",item="+item+",amount="+amount);
	}
	
	static public void setPearlStats(String type,String item,double amount,Date date){
		try {
			Object[] dbArgs = new Object[] {type,item,amount,date};
			dbClient.execSQLUpdate(
							"insert into pearlStats(type,item,amount,gtime) values (?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setPearlStats with " + " fromTime=" + date, e);
		}
		logger.info("type="+type+",item="+item+",amount="+amount);
	}
	
	static public void setCreditStats(String type,String item,double amount,Date date){
		try {
			Object[] dbArgs = new Object[] {type,item,amount,date};
			dbClient.execSQLUpdate(
							"insert into creditStats(type,item,amount,gtime) values (?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setCreditStats with " + " fromTime=" + date, e);
		}
	}
	public static void main(String[] args) {
		Date date = MyUtil.StringToDate(args[0]);
		countSapphireProduce(date);
		setAllCredit(date);
	}
}
