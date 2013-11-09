package download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.MoDBRW;
import util.MyUtil;

public class DownloadFileForResumeBrokenDownload {
	static Logger logger = Logger.getLogger(DownloadFileForResumeBrokenDownload.class);
	final static String oauid = "OaDownUID";
	static String downloadnginx = null;
	
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient167 = null;
	
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			downloadnginx = serverConf.getString("downloadnginx");

			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	public static String DateToString(Date date){
		String datestring = "";
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		datestring = df.format(date);
		return datestring;
	}
	
	public static int getHttpStatus(String log){
		int status = 0;
		String string[] = log.split(" ");
		if(string.length>4){
			if(MyUtil.isMonetid(string[string.length-4])){
				status = Integer.parseInt(string[string.length-4]);
			}else{
				
			}
		}
		return status;
	}
	
	public static int getNeedTransfer(String log){
		int status = 0;
		String string[] = log.split(" ");
		if(string.length>4
				&&MyUtil.isMonetid(string[string.length-1])
				){
			status = Integer.parseInt(string[string.length-1]);
		}
		return status;
	}
	
	public static int getActuallyTransfer(String log){
		int status = 0;
		String string[] = log.split(" ");
		if(string.length>4
				&&MyUtil.isMonetid(string[string.length-3])
				){
			status = Integer.parseInt(string[string.length-3]);
		}
		return status;
	}
	
	static public String getUID(String log) {
		String key = oauid;
		boolean flag = false;
		log = log.substring(log.indexOf(key)+key.length()+1);
		if(log.indexOf(";") != -1){
			log = log.substring(0,log.indexOf(";"));
			flag = true;
		}
		if(!flag){
			if(log.indexOf(" ") != -1){
				log = log.substring(0,log.indexOf(" "));
				flag = true;
			}
		}
		if(log.length()<50){
			return log;
		}
		return null;
	}

	public static void StartStatsAndroid(Date date,String sign){
//		//update file size
//		DownloadWebSite.UpdateDownloadFileSize(DownloadWebSite.getFileUrlMap("data\\FileURL.txt"));
		
//		Map<String,Double> fileSize = DownloadWebSite.getFileSizeMap();
//		System.out.println("fileSizeMap = "+fileSize.size());
		
		String uid = "";
		String fileName = "";
		
		Map<String,Map<String,Integer>> countMap = new HashMap<String,Map<String,Integer>>();
		Map<String,Map<String,Set<String>>> userMap = new HashMap<String,Map<String,Set<String>>>();
		
		//wait 206
		Set<String> uid206Set = new HashSet<String>();
		Map<String,Set<String>> uid206Map = new HashMap<String,Set<String>>();
		
		//all
		Map<String,Set<String>> allMap = new HashMap<String,Set<String>>();
		
		//all 206 and success
		Map<String,Set<String>> all206Map = new HashMap<String,Set<String>>();
		Map<String,Set<String>> success206Map = new HashMap<String,Set<String>>();

		String path = downloadnginx + "oapro.shabik.sa" + DateToString(date)+".log";
		if(new File(path).isFile()){
			try {
				BufferedReader br = new BufferedReader(new FileReader(path));
				String temp = br.readLine();
				while(temp!=null){
					if(temp.contains(oauid)){
						if((temp.contains("/Download/OAPRO-SA")||(temp.contains("/Download/OAPRO-SA")))
								&&temp.contains(sign)
								&&(getHttpStatus(temp)==200 || getHttpStatus(temp)==206)
								&&temp.contains(oauid)){
							uid = getUID(temp);
//							System.out.println("UID="+uid);
							//get download file name
							fileName = MyUtil.getValueOfKey(temp, "/Download/OAPRO-SA"," ");
							if(fileName==null){
								fileName = MyUtil.getValueOfKey(temp, "/Download/OAPRO-SA"," ");
							}
							if(fileName.contains("?")){
								fileName = fileName.substring(0,fileName.indexOf("?"));
							}
							//get download size
//							if(fileSize.containsKey(fileName)){
								//if it is download success
								if(getHttpStatus(temp)==200){
									//all download user
									if(allMap.containsKey(fileName)){
										Set<String> set = allMap.get(fileName);
										set.add(uid);
										allMap.put(fileName, set);
									}else{
										Set<String> set = new HashSet<String>();
										set.add(uid);
										allMap.put(fileName, set);
									}
									if(getNeedTransfer(temp)==getActuallyTransfer(temp)){
										// download success
										if(countMap.containsKey(fileName)){
											//count
											Map<String,Integer> resultCountMap = countMap.get(fileName);
											if(resultCountMap.containsKey("Success")){
												resultCountMap.put("Success",resultCountMap.get("Success")+1);
											}else{resultCountMap.put("Success",1);
												
											}
											countMap.put(fileName, resultCountMap);
											
											//user
											Map<String,Set<String>> resultUserMap = userMap.get(fileName);
											Set<String> resultUserSet = new HashSet<String>();
											if(resultUserMap.containsKey("Success")){
												resultUserSet = resultUserMap.get("Success");
											}else{
												
											}
											resultUserSet.add(uid);
											resultUserMap.put("Success", resultUserSet);
											userMap.put(fileName, resultUserMap);
											
										}else{
											//count
											Map<String,Integer> resultCountMap = new HashMap<String,Integer>();
											resultCountMap.put("Success", 1);
											countMap.put(fileName, resultCountMap);
											
											//user 
											Set<String> resultUserSet = new HashSet<String>();
											resultUserSet.add(uid);
											Map<String,Set<String>> resultUserMap = new HashMap<String,Set<String>>();
											resultUserMap.put("Success", resultUserSet);
											userMap.put(fileName, resultUserMap);
										}
									}else{
										if(uid206Set.contains(uid)){
											// download fail wait 206 but 200
											if(countMap.containsKey(fileName)){
												//count
												Map<String,Integer> resultCountMap = countMap.get(fileName);
												if(resultCountMap.containsKey("Fail")){
													resultCountMap.put("Fail",resultCountMap.get("Fail")+1);
												}else{
													resultCountMap.put("Fail",1);
												}
												countMap.put(fileName, resultCountMap);
												
												//user
												Map<String,Set<String>> resultUserMap = userMap.get(fileName);
												Set<String> resultUserSet = new HashSet<String>();
												if(resultUserMap.containsKey("Fail")){
													resultUserSet = resultUserMap.get("Fail");
												}else{
													
												}
												resultUserSet.add(uid);
												resultUserMap.put("Fail", resultUserSet);
												userMap.put(fileName, resultUserMap);
											}else{
												//count
												Map<String,Integer> resultCountMap = new HashMap<String,Integer>();
												resultCountMap.put("Fail", 1);
												countMap.put(fileName, resultCountMap);
												
												//user 
												Set<String> resultUserSet = new HashSet<String>();
												resultUserSet.add(uid);
												Map<String,Set<String>> resultUserMap = new HashMap<String,Set<String>>();
												resultUserMap.put("Fail", resultUserSet);
												userMap.put(fileName, resultUserMap);
											}
										}else{
											//download fail or wait to resume broken download
											uid206Set.add(uid);
											if(uid206Map.containsKey(fileName)){
												Set<String> uidSet = uid206Map.get(fileName);
												uidSet.add(uid);
												uid206Map.put(fileName, uidSet);
											}else{
												Set<String> uidSet = new HashSet<String>();
												uidSet.add(uid);
												uid206Map.put(fileName, uidSet);
											}
										}
									}
								}else if(getHttpStatus(temp)==206){
									if(all206Map.containsKey(fileName)){
										Set<String> set = all206Map.get(fileName);
										set.add(uid);
										all206Map.put(fileName, set);
									}else{
										Set<String> set = new HashSet<String>();
										set.add(uid);
										all206Map.put(fileName, set);
									}
									//all download user
									if(allMap.containsKey(fileName)){
										Set<String> set = allMap.get(fileName);
										set.add(uid);
										allMap.put(fileName, set);
									}else{
										Set<String> set = new HashSet<String>();
										set.add(uid);
										allMap.put(fileName, set);
									}
									if(uid206Set.contains(uid)){
										if(getNeedTransfer(temp)==getActuallyTransfer(temp)){
											if(success206Map.containsKey(fileName)){
												Set<String> set = success206Map.get(fileName);
												set.add(uid);
												success206Map.put(fileName, set);
											}else{
												Set<String> set = new HashSet<String>();
												set.add(uid);
												success206Map.put(fileName, set);
											}
											//success
											if(countMap.containsKey(fileName)){
												//count
												Map<String,Integer> resultCountMap = countMap.get(fileName);
												if(resultCountMap.containsKey("Success")){
													resultCountMap.put("Success",resultCountMap.get("Success")+1);
												}else{
													resultCountMap.put("Success",1);
												}
												countMap.put(fileName, resultCountMap);
												
												//user
												Map<String,Set<String>> resultUserMap = userMap.get(fileName);
												Set<String> resultUserSet = new HashSet<String>();
												if(resultUserMap.containsKey("Success")){
													resultUserSet = resultUserMap.get("Success");
												}else{
													
												}
												resultUserSet.add(uid);
												resultUserMap.put("Success", resultUserSet);
												userMap.put(fileName, resultUserMap);
											}else{
												//count
												Map<String,Integer> resultCountMap = new HashMap<String,Integer>();
												resultCountMap.put("Success", 1);
												countMap.put(fileName, resultCountMap);
												
												//user 
												Set<String> resultUserSet = new HashSet<String>();
												resultUserSet.add(uid);
												Map<String,Set<String>> resultUserMap = new HashMap<String,Set<String>>();
												resultUserMap.put("Success", resultUserSet);
												userMap.put(fileName, resultUserMap);
											}
											//remove
											uid206Set.remove(uid);
											System.out.print("E0-----------------------------------------------------------");
											if(uid206Map==null){
												System.out.print("E1-----------------------------------------------------------");
											}else{
												if(uid206Map.get(fileName)==null){
													System.out.print("E2---------------------------------------------------------"+fileName);
												}else{
													if(uid206Map.get(fileName).contains(uid)
															&&uid206Map.get(fileName).size()==1){
														uid206Map.remove(fileName);
													}else if(uid206Map.get(fileName).contains(uid)&&uid206Map.get(fileName).size()>1){
														Set<String> uidSet = uid206Map.get(fileName);
														uid206Set.remove(uid);
														uid206Map.put(fileName, uidSet);
													}
													
												}
												
											}
											
										}else{
											//fail do nothing
										}
									}else{
										//??????????
									}
								}
//							}
						}else{
//							System.out.println(temp.contains("/Download/OAPRO-SA")+","+temp.contains(sign)+","+(getHttpStatus(temp)==200 || getHttpStatus(temp)==206)+","+temp.contains(oauid));
						}
					}
					temp = br.readLine();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		//file end . 206
		Set<String> failFile = uid206Map.keySet();
		for(String file:failFile){
			if(countMap.containsKey(file)){
				// fail count
				Map<String,Integer> map = countMap.get(file);
				if(map.containsKey("Fail")){
					map.put("Fail",uid206Map.get(file).size()+map.get("Fail"));
				}else{
					map.put("Fail",uid206Map.get(file).size());
				}
				countMap.put(file,map);
				//fail users
				Map<String,Set<String>> failMap = userMap.get(file);
				Set<String> uidSet = new HashSet<String>();
				if(failMap.containsKey("Fail")){
					uidSet = failMap.get("Fail");
				}else{
					
				}
				for(String id:uid206Map.get(file)){
					uidSet.add(id);
				}
				failMap.put("Fail",uidSet);
				userMap.put(file, failMap);
			}else{
				// fail count
				Map<String,Integer> map = new HashMap<String,Integer>();
				map.put("Fail",uid206Map.get(file).size());
				countMap.put(file,map);
				//fail users
				Map<String,Set<String>> failMap = new HashMap<String,Set<String>>();
				failMap.put("Fail",uid206Map.get(file));
				userMap.put(file, failMap);
			}
		}
		
		
		///////////// output
		Set<String> fileSet = countMap.keySet();
		logger.info(":fileName:allCount:allUser:SuccessCount:SuccessUser:FailCount:FailUser:success206:all206");
		for(String file:fileSet){
			int successCount = 0;
			int successUser = 0;
			int failCount = 0;
			int failUser = 0;
			int alluser = 0;
			int all206 = 0;
			int success206 = 0;
			if(countMap.containsKey(file)){
				if(countMap.get(file).containsKey("Success")){
					successCount = countMap.get(file).get("Success");
				}
			}
			if(userMap.containsKey(file)){
				if(userMap.get(file).containsKey("Success")){
					successUser = userMap.get(file).get("Success").size();
				}
			}
			if(countMap.containsKey(file)){
				if(countMap.get(file).containsKey("Fail")){
					failCount = countMap.get(file).get("Fail");
				}
			}
			if(userMap.containsKey(file)){
				if(userMap.get(file).containsKey("Fail")){
					failUser = userMap.get(file).get("Fail").size();
				}
			}
			if(allMap.containsKey(file)){
				alluser = allMap.get(file).size();
			}
			if(all206Map.containsKey(file)){
				if(all206Map.get(file)!=null){
					all206 = all206Map.get(file).size();
				}
			}
			if(success206Map.containsKey(file)){
				if(success206Map.get(file)!=null){
					success206 = success206Map.get(file).size();
				}
			}
			logger.info(":"+file+":"+(successCount+failCount)+":"+alluser+":"+successCount
					+":"+successUser+":"+failCount
					+":"+failUser+":"+success206+":"+all206);
			
			setData("Download", file,(successCount+failCount)*1.0, date);
			setData("DownloadUser", file,alluser*1.0, date);
			setData("DownloadSuccessCount", file,successCount*1.0, date);
			setData("DownloadSuccessUser", file,successUser*1.0, date);
		}
	}
	
	public static void StartStats(Date date){
		StartStatsAndroid(date,".apk");
		StartStatsAndroid(date,".jar");
	}
	
	public static void setData(String key,String item,Double value,Date date){
		try {
			Object[] dbArgs = new Object[] {key,item,value,date};
			dbClient167.execSQLUpdate(
							"insert into downloadStats(dlkey,item,amount,ddate) values(?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setData ", e);
		}
		logger.info("key="+key+",item="+item+",value="+value+",date="+MyUtil.DateToString(date));
	}
	
	public static void main(String[] args) {
		StartStats(new Date());
	}
}
