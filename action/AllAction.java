package action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.MoDBRW;

public class AllAction {
	static Logger logger = Logger.getLogger(AllAction.class);
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient167 = null;
	final static String oauid = "OaDownUID";
	static String oaLog = null;
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			oaLog = serverConf.getString("OALog");

			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	
	public static void StartStats(Date date){
		logger.info("All action start "+new Date());
		GetDailyActionShipyardLevel(date);
		GetDailyActionPetLevel(date);
		logger.info("All action end "+new Date());
	}
	
	public static int getMonetid(String log){
		int monetid = 0;
		if(log.contains("monetId")){
			log = log.substring(log.indexOf("monetId")+"monetId=".length());
			if(log.contains(",")){
				log = log.substring(0,log.indexOf(","));
			}
			monetid = Integer.parseInt(log);
		}
		return monetid;
	}
	
	public static int getShipyardLevel(String log){
		int level = 0;
		if(log.contains("shipyardLevel")){
			log = log.substring(log.indexOf("shipyardLevel")+"shipyardLevel=".length());
			if(log.contains(",")){
				log = log.substring(0,log.indexOf(","));
			}
			level = Integer.parseInt(log);
		}
		return level;
	}
	
	public static int getPetLevel(String log){
		int level = 0;
		if(log.contains("petLevel")){
			log = log.substring(log.indexOf("petLevel")+"petLevel=".length());
			if(log.contains(",")){
				log = log.substring(0,log.indexOf(","));
			}
			level = Integer.parseInt(log);
		}
		return level;
	}
	
	public static int getAmount(String log){
		int amount = -1;
		if(log.contains("amount")){
			log = log.substring(log.indexOf("amount")+"amount=".length());
			if(log.contains(",")){
				log = log.substring(0,log.indexOf(","));
			}
			amount = Integer.parseInt(log);
		}
		return amount;
	}
	
	public static String getAction(String log){
		String action = "";
		if(log.contains("action")){
			log = log.substring(log.indexOf("action")+"action=".length());
			if(log.contains(",")){
				log = log.substring(0,log.indexOf(","));
			}
			action = log;
		}
		return action;
	}
	
	public static void GetDailyActionShipyardLevel(Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String path = oaLog + "action.log."+ simpleDateFormat.format(date);
		String tempPath = "";
		BufferedReader br = null;
		Map<Integer,Map<Integer,Map<String,Integer>>> monetidCountMap = new HashMap<Integer,Map<Integer,Map<String,Integer>>>();
		Map<Integer,Map<Integer,Map<String,Integer>>> monetidAmountMap = new HashMap<Integer,Map<Integer,Map<String,Integer>>>();
		
		for(int index=0;index<24;index++){
			if(index<10){
				tempPath = path +"-0"+index;
			}else{
				tempPath = path +"-"+index;
			}
			try {
				if(new File(tempPath).isFile()){
					br = new BufferedReader(new FileReader(tempPath));
					String log = br.readLine();
					while(log!=null){
						if(log.contains("")){
							int monetid = getMonetid(log);
							int level = getShipyardLevel(log);
							String action = getAction(log).toLowerCase();
							int amount = getAmount(log);
							
							// to get value
							if(monetidCountMap.containsKey(monetid)){
								Map<Integer,Map<String,Integer>> levelCountMap = monetidCountMap.get(monetid);
								if(levelCountMap.containsKey(level)){
									Map<String,Integer> countMap = levelCountMap.get(level);
									if(countMap.containsKey(action)){
										countMap.put(action, 1+countMap.get(action));
										levelCountMap.put(level,countMap);
										monetidCountMap.put(monetid,levelCountMap);
									}else{
										countMap.put(action, 1);
										levelCountMap.put(level,countMap);
										monetidCountMap.put(monetid,levelCountMap);
									}
								}else{
									Map<String,Integer> countMap = new HashMap<String,Integer>();
									countMap.put(action, 1);
									levelCountMap.put(level,countMap);
									monetidCountMap.put(monetid,levelCountMap);
								}
							}else{
								Map<String,Integer> countMap = new HashMap<String,Integer>();
								countMap.put(action, 1);
								Map<Integer,Map<String,Integer>> levelCountMap = new HashMap<Integer,Map<String,Integer>>();
								levelCountMap.put(level, countMap);
								monetidCountMap.put(monetid,levelCountMap);
							}
							
							if(amount>-1){
								if(monetidAmountMap.containsKey(monetid)){
									Map<Integer,Map<String,Integer>> levelAmountMap = monetidAmountMap.get(monetid);
									if(levelAmountMap.containsKey(level)){
										Map<String,Integer> amountMap = levelAmountMap.get(level);
										if(amountMap.containsKey(action)){
											amountMap.put(action, amount+amountMap.get(action));
											levelAmountMap.put(level,amountMap);
											monetidAmountMap.put(monetid,levelAmountMap);
										}else{
											amountMap.put(action,amount);
											levelAmountMap.put(level,amountMap);
											monetidAmountMap.put(monetid,levelAmountMap);
										}
									}else{
										Map<String,Integer> amountMap = new HashMap<String,Integer>();
										amountMap.put(action, amount);
										levelAmountMap.put(level,amountMap);
										monetidAmountMap.put(monetid,levelAmountMap);
									}
								}else{
									Map<String,Integer> amountMap = new HashMap<String,Integer>();
									amountMap.put(action,amount);
									Map<Integer,Map<String,Integer>> levelAmountMap = new HashMap<Integer,Map<String,Integer>>();
									levelAmountMap.put(level, amountMap);
									monetidAmountMap.put(monetid,levelAmountMap);
								}
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
		//output
		if(monetidCountMap!=null){
			Set<Integer> monetidSet = monetidCountMap.keySet();
			for(int monetid:monetidSet){
				Map<Integer,Map<String,Integer>> levelMap = monetidCountMap.get(monetid);
				if(levelMap!=null){
					Set<Integer> levelSet = levelMap.keySet();
					for(int level:levelSet){
						Map<String,Integer> actionMap = levelMap.get(level);
						Set<String> actionSet = actionMap.keySet();
						for(String action:actionSet){
//							System.out.println("monetid="+monetid+",level="+level+",action="+action+",count="+actionMap.get(action));
							setDataShipyardLevel(monetid, level, action, "count",actionMap.get(action),date);
						}
					}	
				}
			}
			for(int monetid:monetidSet){
				Map<Integer,Map<String,Integer>> levelMap = monetidAmountMap.get(monetid);
				if(levelMap!=null){
					Set<Integer> levelSet = levelMap.keySet();
					for(int level:levelSet){
						Map<String,Integer> actionMap = levelMap.get(level);
						Set<String> actionSet = actionMap.keySet();
						for(String action:actionSet){
	//						System.out.println("monetid="+monetid+",level="+level+",action="+action+",amount="+actionMap.get(action));
							setDataShipyardLevel(monetid, level, action, "amount",actionMap.get(action),new Date());
						}
					}
				}
			}
		}
		
	}
	
	public static void GetDailyActionPetLevel(Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String path = oaLog + "action.log."+ simpleDateFormat.format(date);
		String tempPath = "";
		BufferedReader br = null;
		Map<Integer,Map<Integer,Map<String,Integer>>> monetidCountMap = new HashMap<Integer,Map<Integer,Map<String,Integer>>>();
		Map<Integer,Map<Integer,Map<String,Integer>>> monetidAmountMap = new HashMap<Integer,Map<Integer,Map<String,Integer>>>();
		
		for(int index=0;index<24;index++){
			if(index<10){
				tempPath = path +"-0"+index;
			}else{
				tempPath = path +"-"+index;
			}
			try {
				if(new File(tempPath).isFile()){
					br = new BufferedReader(new FileReader(tempPath));
					String log = br.readLine();
					while(log!=null){
						if(log.contains("")){
							int monetid = getMonetid(log);
							int level = getPetLevel(log);
							String action = getAction(log).toLowerCase();
							int amount = getAmount(log);
							
							// to get value
							if(monetidCountMap.containsKey(monetid)){
								Map<Integer,Map<String,Integer>> levelCountMap = monetidCountMap.get(monetid);
								if(levelCountMap.containsKey(level)){
									Map<String,Integer> countMap = levelCountMap.get(level);
									if(countMap.containsKey(action)){
										countMap.put(action, 1+countMap.get(action));
										levelCountMap.put(level,countMap);
										monetidCountMap.put(monetid,levelCountMap);
									}else{
										countMap.put(action, 1);
										levelCountMap.put(level,countMap);
										monetidCountMap.put(monetid,levelCountMap);
									}
								}else{
									Map<String,Integer> countMap = new HashMap<String,Integer>();
									countMap.put(action, 1);
									levelCountMap.put(level,countMap);
									monetidCountMap.put(monetid,levelCountMap);
								}
							}else{
								Map<String,Integer> countMap = new HashMap<String,Integer>();
								countMap.put(action, 1);
								Map<Integer,Map<String,Integer>> levelCountMap = new HashMap<Integer,Map<String,Integer>>();
								levelCountMap.put(level, countMap);
								monetidCountMap.put(monetid,levelCountMap);
							}
							
							if(amount>-1){
								if(monetidAmountMap.containsKey(monetid)){
									Map<Integer,Map<String,Integer>> levelAmountMap = monetidAmountMap.get(monetid);
									if(levelAmountMap.containsKey(level)){
										Map<String,Integer> amountMap = levelAmountMap.get(level);
										if(amountMap.containsKey(action)){
											amountMap.put(action, amount+amountMap.get(action));
											levelAmountMap.put(level,amountMap);
											monetidAmountMap.put(monetid,levelAmountMap);
										}else{
											amountMap.put(action,amount);
											levelAmountMap.put(level,amountMap);
											monetidAmountMap.put(monetid,levelAmountMap);
										}
									}else{
										Map<String,Integer> amountMap = new HashMap<String,Integer>();
										amountMap.put(action, amount);
										levelAmountMap.put(level,amountMap);
										monetidAmountMap.put(monetid,levelAmountMap);
									}
								}else{
									Map<String,Integer> amountMap = new HashMap<String,Integer>();
									amountMap.put(action,amount);
									Map<Integer,Map<String,Integer>> levelAmountMap = new HashMap<Integer,Map<String,Integer>>();
									levelAmountMap.put(level, amountMap);
									monetidAmountMap.put(monetid,levelAmountMap);
								}
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
		//output
		if(monetidCountMap!=null){
			Set<Integer> monetidSet = monetidCountMap.keySet();
			for(int monetid:monetidSet){
				Map<Integer,Map<String,Integer>> levelMap = monetidCountMap.get(monetid);
				if(levelMap!=null){
					Set<Integer> levelSet = levelMap.keySet();
					for(int level:levelSet){
						Map<String,Integer> actionMap = levelMap.get(level);
						Set<String> actionSet = actionMap.keySet();
						for(String action:actionSet){
//							System.out.println("monetid="+monetid+",level="+level+",action="+action+",count="+actionMap.get(action));
							setDataPetLevel(monetid, level, action, "count",actionMap.get(action),date);
						}
					}	
				}
			}
			for(int monetid:monetidSet){
				Map<Integer,Map<String,Integer>> levelMap = monetidAmountMap.get(monetid);
				if(levelMap!=null){
					Set<Integer> levelSet = levelMap.keySet();
					for(int level:levelSet){
						Map<String,Integer> actionMap = levelMap.get(level);
						Set<String> actionSet = actionMap.keySet();
						for(String action:actionSet){
	//						System.out.println("monetid="+monetid+",level="+level+",action="+action+",amount="+actionMap.get(action));
							setDataPetLevel(monetid, level, action, "amount",actionMap.get(action),new Date());
						}
					}
				}
			}
		}
		
	}
	
	public static void setDataShipyardLevel(int monetid,int level,String action,String type,double amount,Date date){
		try {
			Object[] dbArgs = new Object[] {monetid,level,action,type,amount,date};
			dbClient167.execSQLUpdate(
							"insert into UserLevelAction(monetid,level,action,type,amount,atime) values(?,?,?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setData ", e);
		}
//		logger.info("monetid="+monetid+",level="+level+",action="+action+",type="+type+",amount="+amount+",date="+date);
	}
	
	public static void setDataPetLevel(int monetid,int level,String action,String type,double amount,Date date){
		try {
			Object[] dbArgs = new Object[] {monetid,level,action,type,amount,date};
			dbClient167.execSQLUpdate(
							"insert into UserPetLevelAction(monetid,level,action,type,amount,atime) values(?,?,?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setData ", e);
		}
//		logger.info("monetid="+monetid+",level="+level+",action="+action+",type="+type+",amount="+amount+",date="+date);
	}
	
	public static void main(String args[]){
	}
}
