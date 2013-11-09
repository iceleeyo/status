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

public class PromotionChannel {
	static Logger logger = Logger.getLogger(PromotionChannel.class);
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient167 = null;
	static String downloadnginx = null;
	final static String oauid = "OaDownUID";
	
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");

			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
			
			downloadnginx = serverConf.getString("downloadnginx");
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	public static String GetDateToString(Date date){
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		return df.format(date);
	}
	
	public static String getChannel(String log){
		if(log.contains("from=")){
			String temp = log.substring(log.indexOf("from=")+5);
			if(temp.contains(" ")&&temp.indexOf(" ")<25){
				return temp.substring(0,temp.indexOf(" ")).toLowerCase();
			}else if(temp.contains("&")&&temp.indexOf("&")<25){
				return temp.substring(0,temp.indexOf("&")).toLowerCase();
			}
		}
		return null;
	}
	
	public static String DateToString(Date date){
		String datestring = "";
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		datestring = df.format(date);
		return datestring;
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
	
	public static void StartStats(Date date){
		Map<String,Integer> countMap = new HashMap<String,Integer>();
		Map<String,Set<String>> userMap = new HashMap<String,Set<String>>();
		Map<String,Integer> countDMap = new HashMap<String,Integer>();
		Map<String,Set<String>> userDMap = new HashMap<String,Set<String>>();
		String uid = "";
		
		Map<String,String> uidSource = new HashMap<String,String>();
		String path = downloadnginx + "oapro.shabik.sa" + DateToString(date)+".log";
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
							
							String status = "";
							String string[] = temp.split(" ");
							if(string.length>3){
								status = string[string.length-3];
							}
							
							if(!status.equals("")&&!status.equals("403")){
								if((temp.contains("GET /")||(temp.contains("/Download/OAPRO-SA")))&&temp.contains("from=")){
									String name = getChannel(temp);
									if(countMap.containsKey(name)){
										countMap.put(name,countMap.get(name)+1);
									}else{
										countMap.put(name,1);
									}
									if(userMap.containsKey(name)){
										Set<String> set = userMap.get(name);
										set.add(uid);
										userMap.put(name, set);
									}else{
										Set<String> set = new HashSet<String>();
										set.add(uid);
										userMap.put(name, set);
									}
									uidSource.put(uid, name);
								}else{
									
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
							if(uidSource.containsKey(uid)){
								String status = "";
								String string[] = temp.split(" ");
								if(string.length>3){
									status = string[string.length-3];
								}
								
								if(!status.equals("")&&!status.equals("403")){
									if(temp.contains("/Download/OAPRO-SA")){
									String name = uidSource.get(uid);
										if(countDMap.containsKey(name)){
											countDMap.put(name,countDMap.get(name)+1);
										}else{
											countDMap.put(name,1);
										}
										if(userDMap.containsKey(name)){
											Set<String> set = userDMap.get(name);
											set.add(uid);
											userDMap.put(name, set);
										}else{
											Set<String> set = new HashSet<String>();
											set.add(uid);
											userDMap.put(name, set);
										}
									}else{
											
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
		
		Set<String> keySet = countMap.keySet();
		for(String key:keySet){
			int dcount = 0;
			if(countDMap.containsKey(key)){
				dcount = countDMap.get(key);
			}else{
				
			}
			int dusers = 0;
			if(userDMap.containsKey(key)){
				dusers = userDMap.get(key).size();
			}else{
				
			}
			
			if(userMap.containsKey(key)){
				SetData(key, countMap.get(key), userMap.get(key).size(), dcount, dusers,date);
			}
		}
	}
	
	static public void SetData(String key,int count,int users,int dcount,int dusers,Date date) {
		try {
			Object[] dbArgs = new Object[] {key,count,users,dcount,dusers,date};
			dbClient167.execSQLUpdate(
							"insert into promotionchannel values (?,?,?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setKpi with key=" + key + " count=" + count + " users=" + users, e);
		}
		logger.info("kpi="+key+",count="+count+",users="+users);
	}
	
	public static void main(String[] args) {
//		StartStats(new Date());
		System.out.println(getChannel("[23/Jan/2013:00:01:40 +0800] GET /?from=shabik360 HTTP/1.1 84.235.91.76 - \"BlackBerry8520/5.0.0.681 Profile/MIDP-2.1 Configuration/CLDC-1.1 VendorID/609\" OaDownUID=iywrbftby14gdfgvvvvdb5d1; __utmz=183150692.1358270236.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utma=183150692.1005297167.1358270236.1358270236.1358270236.1 - 200 1278 0.004"));
	}

}
