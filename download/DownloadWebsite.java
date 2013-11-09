package download;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

public class DownloadWebsite {
	static Logger logger = Logger.getLogger(DownloadWebsite.class);
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient167 = null;
	final static String oauid = "OaDownUID";
	static String downloadWebsite = null;
	static String downloadnginx = null;
	static String matchphone = null;
	
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			downloadWebsite = serverConf.getString("downloadWebsite");
			downloadnginx = serverConf.getString("downloadnginx");
			matchphone = serverConf.getString("matchphone");

			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	static public String getUID125(String log) {
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
	
	static public String getUID175(String log) {
		String key = oauid;
		String sign = ",";
		if(log!=null&&key!=null){
			if(log.length()>0&&key.length()>0){
				if(log.contains(key)){
					int start = 0;
					int end = 0;
					start = log.indexOf(key) + key.length()+1;
					log = log.substring(start);
					if(log.indexOf(sign) != -1){
						end = log.indexOf(sign);
						log = log.substring(0,end);
					}
					if(log.contains("==")){
						return null;
					}
					return log.replace("\t","");
				}
			}
		}
		return null;
	}
	
//	public static String getProducer(String phone){
//		//BB
//		Pattern bbp1 = Pattern.compile("[Bb][Ll][Aa][Cc][Kk][Bb][Ee][Rr][Rr][Yy].{1,}");
//		Matcher bbm1 = bbp1.matcher(phone);
//		
//		//HTC
//		Pattern htcp1 = Pattern.compile("[Hh][Tt][Cc]/.{1,}");
//		Matcher htcm1 = htcp1.matcher(phone);
//		
//		//samsung
//		Pattern samsungp1 = Pattern.compile("[sS][aA][mM][sS][uU][nN][gG].{1,}");
//		Matcher samsungm1 = samsungp1.matcher(phone);
//		
//		//SonyEricsson
//		Pattern sonyEricssonp1 = Pattern.compile("[Ss][Oo][Nn][Yy][Ee][Rr][Ii][Cc][Ss][Ss][Oo][Nn].{1,}");
//		Matcher sonyEricssonm1 = sonyEricssonp1.matcher(phone);
//		
//		//IOS
//		Pattern iosp1 = Pattern.compile("[Ii][Pp][Hh][Oo][Nn][Ee].{1,}");
//		Matcher iosm1 = iosp1.matcher(phone);
//		
//		//nokia
//		Pattern nokiap1 = Pattern.compile("[Nn][oO][Kk][iI][Aa].{1,}");
//		Matcher nokiam1 = nokiap1.matcher(phone);
//		
//		if(bbm1.matches()){
//			return "BlackBerry";
//		}else if(htcm1.matches()){
//			return "HTC";
//		}else if(samsungm1.matches()){
//			return "Samsung";
//		}else if(nokiam1.matches()){
//			return "Nokia";
//		}else if(iosm1.matches()){
//			return "IOS";
//		}else if(sonyEricssonm1.matches()){
//			return "SonyEricsson";
//		}else{
//			return "Other";
//		}
//	}
	
	public static String getAgent(String log){
		if(log.contains(" - ")){
			log = log.substring(log.indexOf(" - ")+3);
			if(log.contains("\"")){
				log = log.substring(log.indexOf("\"")+1);
				if(log.contains("\"")){
					log = log.substring(0,log.indexOf("\""));
					return log;
				}
			}
		}else{
			
		}
		return null;
	}
	
	public static void StatsMathchAndIsWap(String path,Date date){
		Map<String,Double> mapMatch = new HashMap<String,Double>();
		Map<String,Double> mapIsWap = new HashMap<String,Double>();
		Map<PhoneOS,Integer> matchFailMap = new HashMap<PhoneOS,Integer>();
		
		Set<String> UIDSetMatch = new HashSet<String>();
		Set<String> UIDSetIsWap = new HashSet<String>();
		
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(matchphone + "notMatch-"+MyUtil.DateToString(date)+".txt"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String uid = "";
		if(path!=null){
			if(path.length()>0){
				if(new File(path).isFile()){
					try {
						BufferedReader br = new BufferedReader(new FileReader(path));
						String temp = br.readLine();
						while(temp!=null){
							if(temp.contains(oauid)){
								uid = getUID175(temp);
							}

							if(temp.contains("Action=Match")&&uid!=null&&uid.length()>5){
								if(UIDSetMatch.add(uid)){
									if(temp.contains("Action=MatchFail")){
										PhoneInfo info = PhoneInfo.Parse(temp.substring(temp.indexOf("UserAgent=")+"UserAgent=".length()));
										if(info.os.toString().equals("Unspecified")){
											output.write("matchFail:"+info.os+":"+info.model+":"+temp.substring(temp.indexOf("UserAgent=")+"UserAgent=".length()));
											output.write("\r\n");
										}
										if(matchFailMap.containsKey(info.os)){
											matchFailMap.put(info.os, matchFailMap.get(info.os)+1);
										}else{
											matchFailMap.put(info.os,1);
										}
										if(temp.contains("OS=BlackBerry")||temp.contains("OS=Symbian")||temp.contains("OS=J2ME")){
											output.write("matchFail:"+info.os+":"+info.model+":"+temp.substring(temp.indexOf("UserAgent=")+"UserAgent=".length()));
											output.write("\r\n");
//											logger.info(":matchFail:"+info.os+":"+info.model+":"+temp.substring(temp.indexOf("UserAgent=")+"UserAgent=".length()));
											if(mapMatch.containsKey("Fail")){
												mapMatch.put("Fail", mapMatch.get("Fail")+1);
											}else{
												mapMatch.put("Fail",1.0);
											}
										}else{
											if(mapMatch.containsKey("Other")){
												mapMatch.put("Other", mapMatch.get("Other")+1);
											}else{
												mapMatch.put("Other",1.0);
											}
										}
										
									}else if(temp.contains("Action=MatchSuccess")){
										if(mapMatch.containsKey("Success")){
											mapMatch.put("Success", mapMatch.get("Success")+1);
										}else{
											mapMatch.put("Success",1.0);
										}
									}
								}
							}
							
							if(temp.contains("IsWap")&&uid!=null&&uid.length()>5){
								if(UIDSetIsWap.add(uid)){
									if(temp.contains("IsWap=False")){
										if(mapIsWap.containsKey("False")){
											mapIsWap.put("False", mapIsWap.get("False")+1);
										}else{
											mapIsWap.put("False",1.0);
										}
									}else if(temp.contains("IsWap=True")){
										if(mapIsWap.containsKey("True")){
											mapIsWap.put("True", mapIsWap.get("True")+1);
										}else{
											mapIsWap.put("True",1.0);
										}
									}
								}
							}
							temp = br.readLine();
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					logger.info(path+" not found!");
				}
			}else{
				logger.info(path+" is null!");
			}
		}else{
			logger.info(path+" is null!");
		}
		
		Set<String> agents = mapIsWap.keySet();
		for(String agent:agents){
			setData("IsWap", agent, mapIsWap.get(agent), date);
		}
		
		agents = mapMatch.keySet();
		for(String agent:agents){
			setData("IsMatched", agent, mapMatch.get(agent), date);
		}
		
		//KPI phoneMacthRate
		double failNum = 0;
		if(mapMatch.containsKey("Success")&&mapMatch.get("Success")>0){
			if(mapMatch.containsKey("Fail")){
				failNum = mapMatch.get("Fail");
			}else{
				failNum = 0;
			}
			setKpi("phoneMatchRate",mapMatch.get("Success")/(failNum+mapMatch.get("Success")), date);
		}
		
		Set<PhoneOS> set = matchFailMap.keySet();
		for(PhoneOS tempp:set){
			try {
				output.write("matchFail:"+tempp+":"+matchFailMap.get(tempp));
				output.write("\r\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void StatsDownloadPhone(String path,Date date){
		Map<PhoneOS,Double> mapOS = new HashMap<PhoneOS,Double>();
		Map<String,Double> mapDownload = new HashMap<String,Double>();
		Map<String,Double> mapVisit = new HashMap<String,Double>();
		Map<String,PhoneOS> mapModelOS = new HashMap<String,PhoneOS>();
		
		Set<String> UIDSetOS = new HashSet<String>();
		Set<String> UIDSetDownload = new HashSet<String>();
		Set<String> UIDSetVisit = new HashSet<String>();
		
		String model = "";
		String uid = "";
		PhoneOS os;
		if(path!=null){
			if(path.length()>0){
				if(new File(path).isFile()){
					try {
						BufferedReader br = new BufferedReader(new FileReader(path));
						String temp = br.readLine();
						while(temp!=null){
							if(temp.contains(oauid)){
								uid = getUID125(temp);
							}
							String agent = "";
							agent = getAgent(temp);
							
							if(agent!=null){
								PhoneInfo info = PhoneInfo.Parse(agent);
								
								//∑√Œ 
								if(UIDSetVisit.add(uid)){
									if(info.model==null||info.model.equals("")){
										model = "NULL";
									}else{
										model = info.model; 
									}
									if(mapVisit.containsKey(model)){
										mapVisit.put(model, mapVisit.get(model)+1);
										mapModelOS.put(info.model, info.os);
									}else{
										mapVisit.put(model, 1.0);
										mapModelOS.put(info.model, info.os);
									}
								}
								
								//œ¬‘ÿ
								if(temp.contains("/Download/OAPRO-SA")){
									if(info.model==null||info.model.equals("")){
										model = "NULL";
									}else{
										model = info.model; 
									}
									if(UIDSetDownload.add(uid)){
										if(mapDownload.containsKey(model)){
											mapDownload.put(model, mapDownload.get(model)+1);
											mapModelOS.put(info.model, info.os);
										}else{
											mapDownload.put(model, 1.0);
											mapModelOS.put(info.model, info.os);
										}
									}
								}
								
								if(UIDSetOS.add(uid)){
									if(info.os==null||info.os.equals(PhoneOS.Unspecified)){
										os = PhoneOS.Unspecified;
									}else{
										os = info.os; 
									}
									if(mapOS.containsKey(os)){
										mapOS.put(os, mapOS.get(os)+1);
									}else{
										mapOS.put(os, 1.0);
									}
								}
							}
							
							
							temp = br.readLine();
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					logger.info("path not found");
				}
			}else{
				logger.info("paht is null");
			}
		}else{
			logger.info("paht is null");
		}
		Set<String> agents = mapDownload.keySet();
		for(String agent:agents){
			setDownloadPhone("Brand", agent,mapModelOS.get(agent), mapDownload.get(agent), date);
		}
		
		agents = mapVisit.keySet();
		for(String agent:agents){
			setDownloadPhone("Model", agent,mapModelOS.get(agent), mapVisit.get(agent), date);
		}
		
		Set<PhoneOS> osSet = mapOS.keySet();
		for(PhoneOS oss:osSet){
			setData("OS", oss.toString(), mapOS.get(oss), date);
		}
		
		setData("KPI","UV",UIDSetVisit.size()*1.0, date);
		setData("KPI","downloadUV",UIDSetDownload.size()*1.0, date);
	}
	
	public static String GetDateToString(Date date){
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		return df.format(date);
	}
	
	public static String GetDateToStringYyMmDd(Date date){
		SimpleDateFormat df=new SimpleDateFormat("yyMMdd");
		return df.format(date);
	}
	
	public static void UpdateDownloadFileSize(Map<String,String> urlMap){
		URL url;
		Map<String, Integer> temp = new HashMap<String, Integer>();
			Set<String> set = urlMap.keySet();
			for(String key:set){
				try{
					url = new URL(urlMap.get(key));
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();  
					InputStream stream = urlConnection.getInputStream();
					
					byte[] tempBytes = new byte[8192];
					int totalLength = 0;
					int bytesRead = stream.read(tempBytes);
					while(bytesRead > 0)
					{
						totalLength += bytesRead;
						bytesRead = stream.read(tempBytes);
					}
					temp.put(key,totalLength);
					System.out.println("$%^&*((*&$%&*)((()(((("+key+" "+totalLength);
				}catch(Exception e){
					e.printStackTrace();
				}    
			}
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("data/FileSize.txt"));
				for(String key:set){
					bw.write(key+" "+temp.get(key));
					bw.write("\r\n");
				}
				bw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	/*
	 * Url File:
	 * Key Url
	 * j2me-240x320 http://oa.shabik.sa/Download/OA/j2me-240x320/jmilk.jar
	 * 
	 * Length Log File:
	 * Key Length	  
	 * j2me-240x320 9999999
	 */
	
	public static Map<String,String> getFileUrlMap(String filePath){
		if(filePath!=null){
			if(filePath.length()>0){
				if(new File(filePath).isFile()){
					try {
						Map<String,String> map = new HashMap<String,String>();
						BufferedReader br = new BufferedReader(new FileReader(filePath));
						String temp = br.readLine();
						while(temp!=null){
							String[] sizes = temp.split(" ");
							if(sizes!=null&&sizes.length==2){
								System.out.println(sizes[1]);
								map.put(sizes[0],sizes[1]);
							}
							temp = br.readLine();
						}
						return map;
					}catch(Exception e){
						
					}
				}else{
					System.out.print("File not find");
				}
			}
		}
		return null;
	}
	
	public static Map<String,Double> getFileSizeMap(){
		String filePath = "data/FileSize.txt";
		if(filePath!=null){
			if(filePath.length()>0){
				if(new File(filePath).isFile()){
					try {
						Map<String,Double> map = new HashMap<String,Double>();
						BufferedReader br = new BufferedReader(new FileReader(filePath));
						String temp = br.readLine();
						while(temp!=null){
							String[] sizes = temp.split(" ");
							if(sizes!=null&&sizes.length==2){
								if(sizes[1].contains("null")){
									if(sizes[0].contains("bb")){
										map.put(sizes[0]+"/OceanAge_Pro.jad",99999999.0);
									}else if(sizes[0].contains("j2me")){
										map.put(sizes[0]+"/OceanAge_Pro.jar",99999999.0);
									}else{
										map.put(sizes[0]+"/oapro.apk",99999999.0);
									}
								}else{
									if(sizes[0].contains("bb")){
										map.put(sizes[0]+"/OceanAge_Pro.jad",Double.parseDouble(sizes[1]));
									}else if(sizes[0].contains("j2me")){
										map.put(sizes[0]+"/OceanAge_Pro.jar",Double.parseDouble(sizes[1]));
									}else{
										map.put(sizes[0]+"/oapro.apk",Double.parseDouble(sizes[1]));
									}
								}
							}
							temp = br.readLine();
						}
						return map;
					}catch(Exception e){
						
					}
				}else{
					System.out.print("File not find");
				}
			}
		}
		return null;
	}
	
	public static Double getTransferedSize(String log){
		String[] temp = log.split(" ");
		if(MyUtil.isMonetid(temp[temp.length-2].trim())){
			return Double.parseDouble(temp[temp.length-2].trim());
		}
		return -1.0;
	}
	
	public static void StatsDownloadFile(String path,Date date){
		String uid = "";
		String fileName = "";
		Double size = -1.0;
		
		Map<String,Double> fileSize = getFileSizeMap();
		System.out.println("fileSizeMap = "+fileSize.size());
		Set<String> names = fileSize.keySet();
		for(String name:names){
			logger.info(":"+name+":"+fileSize.get(name));
		}
		
		Map<String,Integer> allFileCount = new HashMap<String,Integer>();
		Map<String,Set<String>> allFileUser = new HashMap<String,Set<String>>();
		
		Map<String,Integer> allFileSuccessCount = new HashMap<String,Integer>();
		Map<String,Set<String>> allFileSuccessUser = new HashMap<String,Set<String>>();
		
		if(path!=null){
			if(path.length()>0){
				if(new File(path).isFile()){
					try {
						BufferedReader br = new BufferedReader(new FileReader(path));
						String temp = br.readLine();
						while(temp!=null){
							if(temp.contains(oauid)){
								uid = getUID125(temp);
							}

							if(temp.contains("/Download/OAPRO-SA")&&(temp.contains(".jar")||temp.contains(".jad"))&&temp.contains("200")&&temp.contains(oauid)){
								fileName = MyUtil.getValueOfKey(temp, "/Download/OAPRO-SA"," ");
								if(fileName!=null&&fileName.length()>0){
									//add count
									if(allFileCount.containsKey(fileName)){
										allFileCount.put(fileName, allFileCount.get(fileName)+1);
									}else{
										allFileCount.put(fileName, 1);
									}
									
									if(allFileUser.containsKey(fileName)){
										Set<String> userSet = allFileUser.get(fileName);
										userSet.add(uid);
										allFileUser.put(fileName,userSet);
									}else{
										Set<String> userSet = new HashSet<String>();
										userSet.add(uid);
										allFileUser.put(fileName,userSet);
									}
								
									size = getTransferedSize(temp);
									if(size>=0){
										if(size>fileSize.get(fileName)*0.99){
											//add count
											if(allFileSuccessCount.containsKey(fileName)){
												allFileSuccessCount.put(fileName, allFileSuccessCount.get(fileName)+1);
											}else{
												allFileSuccessCount.put(fileName, 1);
											}
											
											if(allFileSuccessUser.containsKey(fileName)){
												Set<String> userSet = allFileSuccessUser.get(fileName);
												userSet.add(uid);
												allFileSuccessUser.put(fileName,userSet);
											}else{
												Set<String> userSet = new HashSet<String>();
												userSet.add(uid);
												allFileSuccessUser.put(fileName,userSet);
											}
										}
									}
								}
							}
							
							temp = br.readLine();
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		Set<String> set = allFileCount.keySet();
		System.out.println("set size = "+set.size());
		for(String filename:set){
			setData("Download", filename,Double.parseDouble(allFileCount.get(filename).toString()), date);
			logger.info("AllCount"+":"+filename+":"+allFileCount.get(filename));
		}
		
		set = allFileUser.keySet();
		for(String filename:set){
			setData("DownloadUser", filename,Double.parseDouble(allFileUser.get(filename).size()+""), date);
			logger.info("AllUser"+":"+filename+":"+allFileUser.get(filename).size());
		}
		
		set = allFileSuccessCount.keySet();
		for(String filename:set){
			setData("DownloadSuccessCount", filename,Double.parseDouble(allFileSuccessCount.get(filename).toString()), date);
			logger.info("SuccessCount"+":"+filename+":"+allFileSuccessCount.get(filename));
		}
		
		set = allFileSuccessUser.keySet();
		for(String filename:set){
			setData("DownloadSuccessUser", filename,Double.parseDouble(allFileSuccessUser.get(filename).size()+""), date);
			logger.info("SuccessUser"+":"+filename+":"+allFileSuccessUser.get(filename).size());
		}
		
		set = allFileCount.keySet();
		int allCount = 0;
		int allUser = 0;
		int successCount = 0;
		int successUser = 0;
		for(String filename:set){
			if(allFileCount.containsKey(filename)){
				allCount = allFileCount.get(filename);
			}else{
				allCount = 0;
			}
			if(allFileUser.containsKey(filename)&&allFileUser.get(filename).size()>0){
				allUser = allFileUser.get(filename).size();
			}else{
				allUser = 0;
			}
			if(allFileSuccessCount.containsKey(filename)){
				successCount = allFileSuccessCount.get(filename);
			}else{
				successCount = 0;
			}
			if(allFileSuccessUser.containsKey(filename)&&allFileSuccessUser.get(filename).size()>0){
				successUser = allFileSuccessUser.get(filename).size();
			}else{
				successUser = 0;
			}
			logger.info("ALLLLLLLLL"+":"+filename+":"+allCount+":"+allUser+":"+successCount+":"+successUser);
		}
	}
	
	static public void setKpi(String key,double value,Date sdate) {
		try {
			Object[] dbArgs = new Object[] {key,value,sdate};
			dbClient167.execSQLUpdate(
							"insert into kpi(oakey,oavalue,sdate) values(?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setKpi with key=" + key + " value=" + value + " sdate=" + sdate, e);
		}
		logger.info("kpi="+key+",value="+value+",date="+sdate);
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
	
	public static void setDownloadPhone(String key,String item,PhoneOS phoneOS,Double value,Date date){
		try {
			Object[] dbArgs = new Object[] {key,item,phoneOS.toString(),value,date};
			dbClient167.execSQLUpdate(
							"insert into downloadphone(dlkey,item,os,amount,ddate) values(?,?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setData ", e);
		}
		logger.info("key="+key+",item="+item+",value="+value+",date="+MyUtil.DateToString(date));
	}
	
	public static void startStats(Date date){
		logger.info("Download stats start "+new Date());
		UpdateDownloadFileSize(getFileUrlMap("data/FileURL.txt"));
		
		String path = downloadWebsite + "access.log" + GetDateToString(date);
		StatsMathchAndIsWap(path, date);
 
		path = downloadnginx + "oapro.shabik.sa"+GetDateToString(date)+".log";
		//StatsDownloadFile(path, date);
		StatsDownloadPhone(path,date);
		logger.info("Download stats end "+new Date());
	}
	
	public static void main(String args[]){
		String path = "c:\\1.txt";
		StatsDownloadPhone(path,new Date());
	}
}
