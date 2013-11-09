package tempStats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import util.DBResultSet;
import util.MoDBRW;
import util.MyUtil;

public class TempNewUserStats {
	static Logger logger = Logger.getLogger(TempNewUserStats.class);
	static String dbWriteUrl = null;
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient = null;
	static String db1286 = null;
	static MoDBRW dbClient1286 = null;
	static MoDBRW dbClient167 = null;
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			dbWriteUrl = serverConf.getString("dbWriteUrl");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			db1286 = serverConf.getString("db086");
			
			dbClient1286 = new MoDBRW(db1286,dbDriver);
			dbClient = new MoDBRW(dbWriteUrl,dbDriver);
			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	/*	
	static public List<Integer> getNewUserList(Date date){
		List<Integer> idList = new ArrayList<Integer>();
		String path = "/home/vivas/morange/OceanAgeDailyMonetidList/"+MyUtil.DateToString(date)+"_newoaNewUser.txt";
		if(new File(path).isFile()){
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(path));
				String temp = null;
				temp = br.readLine();
				while (temp != null) {
					if(MyUtil.isMonetid(temp)){
						idList.add(Integer.parseInt(temp));
					}
					temp = br.readLine();
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return idList;
	}
	
	public static void StartStats(){
		Date fTime = new Date();
		Date tTime = new Date();
		
		Calendar fromWhen = Calendar.getInstance();
		fromWhen.setTime(fTime);
		GregorianCalendar gc = new GregorianCalendar(fromWhen
				.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
				.get(Calendar.DAY_OF_MONTH));
		gc.add(Calendar.DATE, -1);
		tTime = (Date) gc.getTime();	
		gc.add(Calendar.DATE, -4);
		fTime = (Date) gc.getTime();
		
		//save
		List<Integer> saveNewUserList = new ArrayList<Integer>();
		//lost
		List<Integer> lostNewUserList = new ArrayList<Integer>();
		
		logger.info(":Date:Amount:save:lost:saveP:lostP");
		long flag =  (tTime.getTime()-fTime.getTime())/1000/3600/24;
		for(long temp=0;temp<flag;temp++){
			fromWhen = Calendar.getInstance();
			fromWhen.setTime(fTime);
			gc = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));
			gc.add(Calendar.DATE, 1);
			tTime = (Date) gc.getTime();
			gc.add(Calendar.DATE, -1);
			fTime = (Date) gc.getTime();
			
			List<Integer> list1 = getNewUserList(fTime);
			List<Integer> list2 = getNewUserList(tTime);
			
			int saveCount = 0;
			int lostCount = 0;
			
			
			for(int monetid : list1){
				if(list2.contains(monetid)){
					saveCount++;
					if(!saveNewUserList.contains(monetid)){
						saveNewUserList.add(monetid);
					}
				}else{
					lostCount++;
					if(!lostNewUserList.contains(monetid)){
						lostNewUserList.add(monetid);
					}
				}
			}
			
			logger.info(":"+MyUtil.DateToString(fTime)+":"+list1.size()+":"+saveCount+":"+lostCount+":"+saveCount*1.0/list1.size()+":"+lostCount*1.0/list1.size());
			
			fTime = tTime;
		}
	}
*/
	public static List<Integer> getNewUserList(Date date,Date date2){
		List<Integer> list = new ArrayList<Integer>();
		try {
			Object[] dbArgs = new Object[] {date,date2};
			DBResultSet rs = dbClient.execSQLQuery(
							"select monetid from fisher where newuserflag>=? and newuserflag<?",dbArgs);
			while(rs.next()) {
				list.add(rs.getInt("monetid"));
			}
		} catch (Exception e) {
			logger.error("getNewUserList ", e);
		}
		return list;
	}
	
	static public int getActionOwner(String log) {
		if(log!=null){
			if(log.length()>0){
				if(log.indexOf("[") != -1){
					log = log.substring(0, log.indexOf("["));
					log = log.substring(log.lastIndexOf("-")+1,log.length()-1);
					if(MyUtil.isMonetid(log.trim())){
						return Integer.parseInt(log.trim());
					}
				}
			}
		}
		return 0;
	}
	
	static public String getActionName(String log) {
		if(log!=null){
			if(log.length()>0){
				if(log.indexOf("[") != -1){
					log = log.substring(log.indexOf("[")+1,log.length()-1);
					log = log.substring(0,log.indexOf("]"));
				}
			}
		}
		return log;
	}
	
	static public void StartStats(Date date){
		//the log location
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String path = "/home/mozat/morange/OA_NewServer/deployment/OceanAgelogs/newOA_Request.log."+ simpleDateFormat.format(date);
		
		List<Integer> newUserList = getNewUserList(date,new Date(date.getTime()+1000*3600*24));
		
		Map<String,Integer> actionCountMap = new HashMap<String,Integer>();
		Map<String,Set<Integer>> actionUserMap = new HashMap<String,Set<Integer>>();
		
		BufferedReader br = null;
		try {
			String temp = null;
			String tempPath = "";
			int monetid = 0;
			String action = "";
			
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
						monetid = getActionOwner(temp);
						action = getActionName(temp);
						if(newUserList.contains(monetid)){
//							System.out.println(monetid);
//							System.out.println(action);
							if(actionCountMap.containsKey(action)){
								actionCountMap.put(action,actionCountMap.get(action)+1);
							}else{
								actionCountMap.put(action,1);
							}
							
							if(actionUserMap.containsKey(action)){
								Set<Integer> set = actionUserMap.get(action);
								set.add(monetid);
								actionUserMap.put(action,set);
							}else{
								Set<Integer> set = new HashSet<Integer>();
								set.add(monetid);
								actionUserMap.put(action,set);
							}
						}
						temp = br.readLine();
					}
				}
			}
			if(br!=null){
				br.close();
			}
			
			Set<String> set = actionCountMap.keySet();
			logger.info(":ActionName:Amount:User");
			for(String actionName:set){
				logger.info(":"+actionName+":"+actionCountMap.get(actionName)+":"+actionUserMap.get(actionName).size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 
	 public static void main(String[] args) {
		 String temp = "INFO 23 Apr 00:04:01,744 -(EventLog.java:71) - 774     [PersonalMonster.SelectShipFromPersonalMonsterAction]time = 3 ms";
		 System.out.println(getActionOwner(temp));
	 }
}
