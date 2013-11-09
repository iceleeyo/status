package download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import download.DownloadWebsite;
import util.MoDBRW;
import util.MyUtil;

public class DownloadFile {
	
	static Logger logger = Logger.getLogger(DownloadFile.class);
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient167 = null;
	final static String oauid = "OaDownUID";
	static String downloadnginx = null;
	
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
/*	
	public static void StartStatsJ2ME(Date date){
		//update file size
		DownloadWebsite.UpdateDownloadFileSize(DownloadWebsite.getFileUrlMap("data\\FileURL.txt"));
		
		Map<String,Double> fileSize = DownloadWebsite.getFileSizeMap();
		System.out.println("fileSizeMap = "+fileSize.size());
		
		String uid = "";
		String fileName = "";
		int size = 0;
		
		Map<String,Integer> fileCountMap = new HashMap<String,Integer>();
		Map<String,Set<String>> fileUserMap = new HashMap<String,Set<String>>();
		Map<String,Integer> successCountMap = new HashMap<String,Integer>();
		Map<String,Set<String>> successUserMap = new HashMap<String,Set<String>>();
		Map<String,ArrayList<Double>> failSizeMap = new HashMap<String,ArrayList<Double>>();

		String path = downloadnginx + "oapro.shabik.sa"+DateToString(date)+".log";
		if(new File(path).isFile()){
			try {
				BufferedReader br = new BufferedReader(new FileReader(path));
				String temp = br.readLine();
				while(temp!=null){
					if(temp.contains(oauid)){
						if(temp.contains("/Download/OAPRO-SA")
								&&temp.contains("/OceanAge_Pro.jar")
								&&getHttpStatus(temp)==200
								&&temp.contains(oauid)){
							
							uid = DownloadWebsite.getUID125(temp);
							fileName = MyUtil.getValueOfKey(temp, "/Download/OAPRO-SA"," ");
							if(fileName.contains("?")){
								fileName = fileName.substring(0,fileName.indexOf("?"));
							}
							
							//each file download count
							if(fileCountMap.containsKey(fileName)){
								fileCountMap.put(fileName,fileCountMap.get(fileName)+1);
							}else{
								fileCountMap.put(fileName,1);
							}
							//each file download users
							if(fileUserMap.containsKey(fileName)){
								Set<String> userSet = fileUserMap.get(fileName);
								userSet.add(uid);
								fileUserMap.put(fileName, userSet);
							}else{
								Set<String> userSet = new HashSet<String>();
								userSet.add(uid);
								fileUserMap.put(fileName, userSet);
							}
							
							if(fileSize.containsKey(fileName)){
								size = getTransferedSize(temp);
								try{
									if(size==fileSize.get(fileName)){
										//each file success download count
										if(successCountMap.containsKey(fileName)){
											successCountMap.put(fileName,successCountMap.get(fileName)+1);
										}else{
											successCountMap.put(fileName,1);
										}
										//each file success download users
										if(successUserMap.containsKey(fileName)){
											Set<String> userSet = successUserMap.get(fileName);
											userSet.add(uid);
											successUserMap.put(fileName, userSet);
										}else{
											Set<String> userSet = new HashSet<String>();
											userSet.add(uid);
											successUserMap.put(fileName, userSet);
										}
									}else if(size<fileSize.get(fileName)){
										if(failSizeMap.containsKey(fileName)){
											ArrayList<Double> list = failSizeMap.get(fileName);
											list.add(size*1.0);
											failSizeMap.put(fileName, list);
										}else{
											ArrayList<Double> list = new ArrayList<Double>();
											list.add(size*1.0);
											failSizeMap.put(fileName, list);
										}
									}
								}catch(Exception e){
									e.printStackTrace();
									System.out.println(fileName);
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
		
		//output
		Set<String> fileNameSet = fileCountMap.keySet();
		logger.info(":"+"File"+":DownloadCount"+":DownloadUsers"+":SuccessCount"+":SuccessUser"+":RealSize"+":FailMidSize");
		for(String filename:fileNameSet){
			Set<String> userSet = fileUserMap.get(filename);
			ArrayList<Double> list = failSizeMap.get(filename);
			System.out.println("filename = " + filename);
			Collections.sort(list);

			//success count
			double successCount = 0;
			if(successCountMap.containsKey(filename)){
				successCount = successCountMap.get(filename)*1.0;
			}
			//success users
			double successUsers = 0;
			if(successUserMap.containsKey(filename)){
				Set<String> successUserSet = successUserMap.get(filename);
				if(!successUserSet.isEmpty()){
					successUsers = successUserSet.size()*1.0;
				}
			}
			
			setData("Download", filename,fileCountMap.get(filename)*1.0, date);
			setData("DownloadUser", filename,userSet.size()*1.0, date);
			setData("DownloadSuccessCount", filename,successCount, date);
			setData("DownloadSuccessUser", filename,successUsers, date);
			
//			logger.info(":"+filename+":"+fileCountMap.get(filename)+":"+userSet.size()
//					+":"+successCountMap.get(filename)+":"+successCount+":"+fileSize.get(filename)+":"+LogPraser.getMid(list));
//			System.out.println(filename+" SuccessRate="+String.format("%.2f", successCountMap.get(filename)*1.0/fileCountMap.get(filename))+",FailMid="+String.format("%.2f", LogPraser.getMid(list)*1.0/fileSize.get(filename)));
		}
	}
	
	public static void StartStatsAndroid(Date date){
		//update file size
		DownloadWebsite.UpdateDownloadFileSize(DownloadWebsite.getFileUrlMap("data/FileURL.txt"));
		
		Map<String,Double> fileSize = DownloadWebsite.getFileSizeMap();
		System.out.println("fileSizeMap = "+fileSize.size());
		
		String uid = "";
		String fileName = "";
		int size = 0;
		
		Map<String,Integer> fileCountMap = new HashMap<String,Integer>();
		Map<String,Set<String>> fileUserMap = new HashMap<String,Set<String>>();
		Map<String,Integer> successCountMap = new HashMap<String,Integer>();
		Map<String,Set<String>> successUserMap = new HashMap<String,Set<String>>();
		Map<String,ArrayList<Double>> failSizeMap = new HashMap<String,ArrayList<Double>>();

		String path = "\\\\192.168.1.174\\oapro.shabik.sa\\oapro.shabik.sa.log";
		if(new File(path).isFile()){
			try {
				BufferedReader br = new BufferedReader(new FileReader(path));
				String temp = br.readLine();
				while(temp!=null){
					if(temp.contains(oauid)){
						if(temp.contains("/Download/OAPRO-SA")
								&&temp.contains("/oapro.apk")
								&&getHttpStatus(temp)==200
								&&temp.contains(oauid)){
							
							uid = DownloadWebsite.getUID125(temp);
							fileName = MyUtil.getValueOfKey(temp, "/Download/OAPRO-SA"," ");
							if(fileName.contains("?")){
								fileName = fileName.substring(0,fileName.indexOf("?"));
							}
							
							//each file download count
							if(fileCountMap.containsKey(fileName)){
								fileCountMap.put(fileName,fileCountMap.get(fileName)+1);
							}else{
								fileCountMap.put(fileName,1);
							}
							//each file download users
							if(fileUserMap.containsKey(fileName)){
								Set<String> userSet = fileUserMap.get(fileName);
								userSet.add(uid);
								fileUserMap.put(fileName, userSet);
							}else{
								Set<String> userSet = new HashSet<String>();
								userSet.add(uid);
								fileUserMap.put(fileName, userSet);
							}
							
							if(fileSize.containsKey(fileName)){
								size = getTransferedSize(temp);
								try{
									if(size==fileSize.get(fileName)){
										//each file success download count
										if(successCountMap.containsKey(fileName)){
											successCountMap.put(fileName,successCountMap.get(fileName)+1);
										}else{
											successCountMap.put(fileName,1);
										}
										//each file success download users
										if(successUserMap.containsKey(fileName)){
											Set<String> userSet = successUserMap.get(fileName);
											userSet.add(uid);
											successUserMap.put(fileName, userSet);
										}else{
											Set<String> userSet = new HashSet<String>();
											userSet.add(uid);
											successUserMap.put(fileName, userSet);
										}
									}else if(size<fileSize.get(fileName)){
										if(failSizeMap.containsKey(fileName)){
											ArrayList<Double> list = failSizeMap.get(fileName);
											list.add(size*1.0);
											failSizeMap.put(fileName, list);
										}else{
											ArrayList<Double> list = new ArrayList<Double>();
											list.add(size*1.0);
											failSizeMap.put(fileName, list);
										}
									}
								}catch(Exception e){
									e.printStackTrace();
									System.out.println(fileName);
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
		
		//output
		Set<String> fileNameSet = fileCountMap.keySet();
		logger.info(":"+"File"+":DownloadCount"+":DownloadUsers"+":SuccessCount"+":SuccessUser"+":RealSize"+":FailMidSize");
		for(String filename:fileNameSet){
			Set<String> userSet = fileUserMap.get(filename);
			ArrayList<Double> list = failSizeMap.get(filename);
			System.out.println("filename = " + filename);
			Collections.sort(list);

			//success count
			double successCount = 0;
			if(successCountMap.containsKey(filename)){
				successCount = successCountMap.get(filename)*1.0;
			}
			//success users
			double successUsers = 0;
			if(successUserMap.containsKey(filename)){
				Set<String> successUserSet = successUserMap.get(filename);
				if(!successUserSet.isEmpty()){
					successUsers = successUserSet.size()*1.0;
				}
			}
			
			setData("Download", filename,fileCountMap.get(filename)*1.0, date);
			setData("DownloadUser", filename,userSet.size()*1.0, date);
			setData("DownloadSuccessCount", filename,successCount, date);
			setData("DownloadSuccessUser", filename,successUsers, date);
			
//			logger.info(":"+filename+":"+fileCountMap.get(filename)+":"+userSet.size()
//					+":"+successCountMap.get(filename)+":"+successCount+":"+fileSize.get(filename)+":"+LogPraser.getMid(list));
			System.out.println(filename+" SuccessRate="+String.format("%.2f", successCountMap.get(filename)*1.0/fileCountMap.get(filename))+",FailMid="+String.format("%.2f", MyUtil.getMid(list)*1.0/fileSize.get(filename)));
		}
	}
*/	
	public static void StartStats(Date date){
		StartStatsBB(date);
	}
	
	public static void StartStatsBB(Date date){
		//update file size
		DownloadWebsite.UpdateDownloadFileSize(DownloadWebsite.getFileUrlMap("data/FileURL.txt"));
		Map<String,String> lastFileMap = getBBLastFile(date);
		//Map<String,Double> fileSize = DownloadWebsite.getFileSizeMap();
		
		String uid = "";
		String fileName = "";
		String fileType = "";
		
		Map<String,Integer> fileCountMap = new HashMap<String,Integer>();
		Map<String,Set<String>> fileUserMap = new HashMap<String,Set<String>>();
		Map<String,Integer> successCountMap = new HashMap<String,Integer>();
		Map<String,Set<String>> successUserMap = new HashMap<String,Set<String>>();
		
		Map<String,Integer> allCodMap = new HashMap<String,Integer>();

		String path = downloadnginx + "oapro.shabik.sa"+DateToString(date)+".log";
		if(new File(path).isFile()){
			try {
				BufferedReader br = new BufferedReader(new FileReader(path));
				String temp = br.readLine();
				while(temp!=null){
					if(temp.contains(oauid)){
						if((temp.contains("/Download/OAPRO-SA")||(temp.contains("/Download/OAPRO-SA")))
								&&(temp.contains(".jad")||temp.contains(".cod"))
//								&&getHttpStatus(temp)==200
								&&temp.contains(oauid)){
							
							uid = DownloadWebsite.getUID125(temp);
							if(temp.contains(".jad")){
								fileName = MyUtil.getValueOfKey(temp, "/Download/OAPRO-SA"," ");
								
								//each file download count
								if(fileCountMap.containsKey(fileName)){
									fileCountMap.put(fileName,fileCountMap.get(fileName)+1);
								}else{
									fileCountMap.put(fileName,1);
								}
								//each file download users
								if(fileUserMap.containsKey(fileName)){
									Set<String> userSet = fileUserMap.get(fileName);
									userSet.add(uid);
									fileUserMap.put(fileName, userSet);
								}else{
									Set<String> userSet = new HashSet<String>();
									userSet.add(uid);
									fileUserMap.put(fileName, userSet);
								}
							}
							if(temp.contains(".cod")){
								fileName = MyUtil.getValueOfKey(temp, "/Download/OAPRO-SA"," ");
								if(fileName==null){
									fileName = MyUtil.getValueOfKey(temp, "/Download/OAPRO-SA"," ");
								}
								if(allCodMap.containsKey(fileName)){
									
								}else{
									
								}
								fileType = MyUtil.getValueOfKey(temp, "/Download/OAPRO-SA","/");
								if(fileType==null){
									fileType = MyUtil.getValueOfKey(temp, "/Download/OAPRO-SA","/");
								}
//								System.out.println(fileName);
//								System.out.println(fileType);
//								System.out.println(lastFileMap.get(fileType));
								if(fileName.equals(lastFileMap.get(fileType))){
									fileName = fileName.substring(0,fileName.indexOf("OceanAge_Pro-")+12)+".jad";
									//each file success download count
									if(successCountMap.containsKey(fileName)){
										successCountMap.put(fileName,successCountMap.get(fileName)+1);
									}else{
										successCountMap.put(fileName,1);
									}
									//each file success download users
									if(successUserMap.containsKey(fileName)){
										Set<String> userSet = successUserMap.get(fileName);
										userSet.add(uid);
										successUserMap.put(fileName, userSet);
									}else{
										Set<String> userSet = new HashSet<String>();
										userSet.add(uid);
										successUserMap.put(fileName, userSet);
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
		
		//output
		Set<String> fileNameSet = fileCountMap.keySet();
		logger.info(":"+"File"+":DownloadCount"+":DownloadUsers"+":SuccessCount"+":SuccessUser");
		for(String filename:fileNameSet){
			Set<String> userSet = fileUserMap.get(filename);
			
			Set<String> successUserSet = new HashSet<String>();
			if(successUserMap.containsKey(filename)){
				successUserSet = successUserMap.get(filename);
			}
			
			int successCount = 0;
			
			if(successCountMap.containsKey(filename)){
				successCount = successCountMap.get(filename);
			}
			setData("Download", filename,fileCountMap.get(filename)*1.0, date);
			setData("DownloadUser", filename,userSet.size()*1.0, date);
			setData("DownloadSuccessCount", filename,successCount*1.0, date);
			setData("DownloadSuccessUser", filename,successUserSet.size()*1.0, date);
			logger.info(":"+filename+":"+fileCountMap.get(filename)+":"+userSet.size()
					+":"+successCount+":"+successUserSet.size());
		}
	}
	
	public static int getHttpStatus(String log){
		int status = 0;
		String string[] = log.split(" ");
		if(string.length>4){
			status = Integer.parseInt(string[string.length-4]);
		}
		return status;
	}
	
	public static int getTransferedSize(String log){
		String[] temp = log.split(" ");
		if(MyUtil.isMonetid(temp[temp.length-2].trim())){
			return Integer.parseInt(temp[temp.length-2].trim());
		}
		return -1;
	}
	
	public static Map<String,String> getBBLastFile(Date date){
		Map<String,List<String>> allFileMap = new HashMap<String,List<String>>();
		Map<String,String> lastFileMap = new HashMap<String,String>();
		String fileName = "";
		String fileType = "";
		String path = "\\\\192.168.1.174\\oapro.shabik.sa\\oapro.shabik.sa"+DateToString(date)+".log";
		if(new File(path).isFile()){
			try {
				BufferedReader br = new BufferedReader(new FileReader(path));
				String temp = br.readLine();
				while(temp!=null){
					if(temp.contains(".cod")){
						fileName = MyUtil.getValueOfKey(temp, "/Download/OAPRO-SA"," ");
						fileType = MyUtil.getValueOfKey(temp, "/Download/OAPRO-SA","/");
						if(allFileMap.containsKey(fileType)){
							List<String> list = allFileMap.get(fileType);
							if(!list.contains(fileName)){
								list.add(fileName);
							}
							allFileMap.put(fileType, list);
						}else{
							List<String> list = new ArrayList<String>();
							list.add(fileName);
							allFileMap.put(fileType, list);
						}
					}
					temp = br.readLine();
				}
			}catch(Exception e){
				
			}
		}
		
		//
		Set<String> fileTypeSet = allFileMap.keySet();
		for(String filetype:fileTypeSet){
			List<String> list = allFileMap.get(filetype);
			Collections.sort(list);
			for(int i=0;i<list.size();i++){
				System.out.println(":"+filetype+":"+list.get(i));
			}
			lastFileMap.put(filetype, list.get(list.size()-2));
		}
		return lastFileMap;
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
	
	public static void main(String[] args){
		String log = "[31/Jan/2013:00:00:21 +0800] GET /Download/OA/bb-320x240/OceanAge-14.cod HTTP/1.1 84.235.92.64 - \"BlackBerry8520/5.0.0.681 Profile/MIDP-2.1 Configuration/CLDC-1.1 VendorID/600\" uid=wKgHylEJQBpsl1GMAxD0Ag==; OaDownUID=5qsthrvkot5xl4b0wucqofrr - 200 56480 0.026";
		System.out.println(MyUtil.getValueOfKey(log, "/Download/OA"," "));
		System.out.println(getHttpStatus(log));
	}
}
