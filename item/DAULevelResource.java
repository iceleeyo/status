package item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.MoDBRW;
import util.MyUtil;

public class DAULevelResource {
	static final Logger logger = Logger.getLogger(DAULevelResource.class);
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
			oaLog = serverConf.getString("OALog");
			
			dbClient1286 = new MoDBRW(db1286,dbDriver);
			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	public static void StartStats(Date date){
		//the log location
		ArrayList<Integer> testList = MyUtil.getTestAccount();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String path = oaLog + "trade.log."+ simpleDateFormat.format(date);
		BufferedReader br = null;
		String temp = null;
		String tempPath = "";
		Map<Integer,Map<String,Map<String,Map<Integer,Integer>>>> inLevelMap = new HashMap<Integer,Map<String,Map<String,Map<Integer,Integer>>>>();
		Map<Integer,Map<String,Map<String,Map<Integer,Integer>>>> outLevelMap = new HashMap<Integer,Map<String,Map<String,Map<Integer,Integer>>>>();
//		Map<String,Map<Integer,Integer>> inAllUserMap = new HashMap<String,Map<Integer,Integer>>();
//		Map<String,Map<Integer,Integer>> outAllUserMap = new HashMap<String,Map<Integer,Integer>>();
		
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
						if(temp.contains("addGold")||temp.contains("addPearl")){
							String levelString = MyUtil.getValueOfKey(temp, "level",",");
							if(MyUtil.isMonetid(levelString)){
								int level = Integer.parseInt(levelString);
								String resource = "";
								if(temp.contains("addGold")){
									resource = "Gold";
								}else if(temp.contains("addPearl")){
									resource = "Pearl";
								}
								String action = MyUtil.getValueOfKey(temp, "from",",");
								int monetid = Integer.parseInt(MyUtil.getValueOfKey(temp, "monetId",","));
								if(!testList.contains(monetid)){
									String amountString = MyUtil.getValueOfKey(temp,"num",",");
									if(amountString.contains("-")){
										amountString = amountString.replace("-","");
									}
									if(MyUtil.isMonetid(amountString)){
										int amount = Integer.parseInt(amountString) ;
	//									//all 
	//									if(inAllUserMap.containsKey(action)){
	//										Map<Integer,Integer> monetidMap = inAllUserMap.get(action);
	//										if(monetidMap.containsKey(monetid)){
	//											monetidMap.put(monetid,monetidMap.get(monetid)+amount);
	//										}else{
	//											monetidMap.put(monetid,amount);
	//										}
	//										inAllUserMap.put(action,monetidMap);
	//									}else{
	//										Map<Integer,Integer> monetidMap = new HashMap<Integer,Integer>();
	//										monetidMap.put(monetid,amount);
	//										inAllUserMap.put(action,monetidMap);
	//									}
										//set map
										for(int i=0;i<2;i++){
											if(i==1){
												level = 0;
											}
											if(inLevelMap.containsKey(level)){
												Map<String,Map<String,Map<Integer,Integer>>> resourceMap = inLevelMap.get(level);
												if(resourceMap.containsKey(resource)){
													Map<String,Map<Integer,Integer>> typeMap = resourceMap.get(resource);
													if(typeMap.containsKey(action)){
														Map<Integer,Integer> monetidMap = typeMap.get(action);
														if(monetidMap.containsKey(monetid)){
															monetidMap.put(monetid,monetidMap.get(monetid)+amount);
														}else{
															monetidMap.put(monetid,amount);
														}
														typeMap.put(action,monetidMap);
														resourceMap.put(resource, typeMap);
														inLevelMap.put(level,resourceMap);
													}else{
														Map<Integer,Integer> monetidMap = new HashMap<Integer,Integer>();
														monetidMap.put(monetid,amount);
														typeMap.put(action,monetidMap);
														resourceMap.put(resource, typeMap);
														inLevelMap.put(level,resourceMap);
													}
												}else{
													Map<String,Map<Integer,Integer>> typeMap = new HashMap<String,Map<Integer,Integer>>();
													Map<Integer,Integer> monetidMap = new HashMap<Integer,Integer>();
													monetidMap.put(monetid,amount);
													typeMap.put(action,monetidMap);
													resourceMap.put(resource, typeMap);
													inLevelMap.put(level,resourceMap);
												}
											}else{
												Map<String,Map<Integer,Integer>> typeMap = new HashMap<String,Map<Integer,Integer>>();
												Map<Integer,Integer> monetidMap = new HashMap<Integer,Integer>();
												monetidMap.put(monetid,amount);
												typeMap.put(action, monetidMap);
												Map<String,Map<String,Map<Integer,Integer>>> resourceMap = new HashMap<String,Map<String,Map<Integer,Integer>>>();
												resourceMap.put(resource, typeMap);
												inLevelMap.put(level,resourceMap);
											}
										}
									}else{
										logger.error(temp);
									}
								}
							}
						}
						
						if(temp.contains("subGold")||temp.contains("subPearl")){
							String levelString = MyUtil.getValueOfKey(temp, "level",",");
							if(MyUtil.isMonetid(levelString)){
								int level = Integer.parseInt(levelString);
								String resource = "";
								if(temp.contains("subGold")){
									resource = "Gold";
								}else if(temp.contains("subPearl")){
									resource = "Pearl";
								}
								String action = MyUtil.getValueOfKey(temp, "to",",");
								int monetid = Integer.parseInt(MyUtil.getValueOfKey(temp, "monetId",","));
								if(!testList.contains(monetid)){
									String amountString = MyUtil.getValueOfKey(temp,"num",",");
									if(amountString.contains("-")){
										amountString = amountString.replace("-","");
									}
									if(MyUtil.isMonetid(amountString)){
										int amount = Integer.parseInt(amountString) ;
	//									//all 
	//									if(outAllUserMap.containsKey(action)){
	//										Map<Integer,Integer> monetidMap = outAllUserMap.get(action);
	//										if(monetidMap.containsKey(monetid)){
	//											monetidMap.put(monetid,monetidMap.get(monetid)+amount);
	//										}else{
	//											monetidMap.put(monetid,amount);
	//										}
	//										outAllUserMap.put(action,monetidMap);
	//									}else{
	//										Map<Integer,Integer> monetidMap = new HashMap<Integer,Integer>();
	//										monetidMap.put(monetid,amount);
	//										outAllUserMap.put(action,monetidMap);
	//									}
										//set map
										for(int i=0;i<2;i++){
											if(i==1){
												level = 0;
											}
											if(outLevelMap.containsKey(level)){
												Map<String,Map<String,Map<Integer,Integer>>> resourceMap = outLevelMap.get(level);
												if(resourceMap.containsKey(resource)){
													Map<String,Map<Integer,Integer>> typeMap = resourceMap.get(resource);
													if(typeMap.containsKey(action)){
														Map<Integer,Integer> monetidMap = typeMap.get(action);
														if(monetidMap.containsKey(monetid)){
															monetidMap.put(monetid,monetidMap.get(monetid)+amount);
														}else{
															monetidMap.put(monetid,amount);
														}
														typeMap.put(action,monetidMap);
														resourceMap.put(resource, typeMap);
														outLevelMap.put(level,resourceMap);
													}else{
														Map<Integer,Integer> monetidMap = new HashMap<Integer,Integer>();
														monetidMap.put(monetid,amount);
														typeMap.put(action,monetidMap);
														resourceMap.put(resource, typeMap);
														outLevelMap.put(level,resourceMap);
													}
												}else{
													Map<String,Map<Integer,Integer>> typeMap = new HashMap<String,Map<Integer,Integer>>();
													Map<Integer,Integer> monetidMap = new HashMap<Integer,Integer>();
													monetidMap.put(monetid,amount);
													typeMap.put(action,monetidMap);
													resourceMap.put(resource, typeMap);
													outLevelMap.put(level,resourceMap);
												}
											}else{
												Map<String,Map<Integer,Integer>> typeMap = new HashMap<String,Map<Integer,Integer>>();
												Map<Integer,Integer> monetidMap = new HashMap<Integer,Integer>();
												monetidMap.put(monetid,amount);
												typeMap.put(action, monetidMap);
												Map<String,Map<String,Map<Integer,Integer>>> resourceMap = new HashMap<String,Map<String,Map<Integer,Integer>>>();
												resourceMap.put(resource, typeMap);
												outLevelMap.put(level,resourceMap);
											}
										}
									}else{
										logger.error(temp);
									}
								}
							}
						}
						temp = br.readLine();
					}
				}
			}catch(Exception e){
				logger.error("",e);
			}
		}
		
		//output
		Set<Integer> levelSet = inLevelMap.keySet();
		for(int level:levelSet){
			Map<String,Map<String,Map<Integer,Integer>>> resourceMap = inLevelMap.get(level);
			Set<String> resourceSet = resourceMap.keySet();
			for(String resource:resourceSet){
				Map<String,Map<Integer,Integer>> typeMap = resourceMap.get(resource);
				Set<String> typeSet = typeMap.keySet();
				for(String type:typeSet){
					List<Integer> list = new ArrayList<Integer>();
					Map<Integer,Integer> map = typeMap.get(type);
					if(!map.isEmpty()){
						Set<Integer> monetidSet = map.keySet();
						for(int monetid:monetidSet){
							list.add(map.get(monetid));
						}
						if(list!=null&&list.size()>0){
							long total = 0;
							for(int amount:list){
								total += amount;
							}
							Collections.sort(list);
							long mid = 0;
							if(list.size()%2==0){
								mid = (list.get(list.size()/2)+list.get(list.size()/2-1))/2;
							}else{
								mid = list.get(list.size()/2);
							}
							setData(resource,"In",type,level, total, list.size(),total*1.0/list.size(), list.get(list.size()-1), mid, list.get(0), date);
							System.out.println("Level="+level+",Resource="+resource+",Type="+type+",Amount="+total
									+",Average="+total*1.0/list.size()+",Max="+list.get(list.size()-1)+",Mid="+mid+",Min="+list.get(0));
						}
						
					}
				}
			}
		}
		
		levelSet = outLevelMap.keySet();
		for(int level:levelSet){
			Map<String,Map<String,Map<Integer,Integer>>> resourceMap = outLevelMap.get(level);
			Set<String> resourceSet = resourceMap.keySet();
			for(String resource:resourceSet){
				Map<String,Map<Integer,Integer>> typeMap = resourceMap.get(resource);
				Set<String> typeSet = typeMap.keySet();
				for(String type:typeSet){
					List<Integer> list = new ArrayList<Integer>();
					Map<Integer,Integer> map = typeMap.get(type);
					if(!map.isEmpty()){
						Set<Integer> monetidSet = map.keySet();
						for(int monetid:monetidSet){
							list.add(map.get(monetid));
						}
						if(list!=null&&list.size()>0){
							long total = 0;
							for(int amount:list){
								total += amount;
							}
							Collections.sort(list);
							long mid = 0;
							if(list.size()%2==0){
								mid = (list.get(list.size()/2)+list.get(list.size()/2-1))/2;
							}else{
								mid = list.get(list.size()/2);
							}
							setData(resource,"Out",type,level, total,list.size(),total*1.0/list.size(), list.get(list.size()-1), mid, list.get(0), date);
							System.out.println("Level="+level+",Resource="+resource+",Type="+type+",Amount="+total
									+",Average="+total*1.0/list.size()+",Max="+list.get(list.size()-1)+",Mid="+mid+",Min="+list.get(0));
						}
					}
				}
			}
		}
		
	}

	static public void setData(String resource,String action,String type,int level,long total,int users,double average,long max,long mid,long min,Date date) {
		try {
			Object[] dbArgs = new Object[] {resource,action,type,level,total,users,average,max,mid,min,date};
			dbClient167.execSQLUpdate(
							"insert into daulevelresource(resource,action,type,level,amount,users,averageS,maxS,midS,minS,stime)values (?,?,?,?,?,?,?,?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setData" , e);
		}
	}
}
