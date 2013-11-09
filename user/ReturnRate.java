package user;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.DBResultSet;
import util.DBUtil;
import util.MoDBRW;
import util.MyUtil;

public class ReturnRate {
	static Logger logger = Logger.getLogger(ReturnRate.class);
	static String dbReadUrls = null;
	static String dbWriteUrl = null;
	static String dbDriver = null;
	static MoDBRW dbClient = null;
	static MoDBRW dbClient167 = null;
	static String idLog = null;
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbWriteUrl = serverConf.getString("dbWriteUrl");
			
			dbDriver = serverConf.getString("dbDriver");
			idLog = serverConf.getString("idlog");

			dbClient = new MoDBRW(dbWriteUrl,dbDriver);
			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	public static Map<Integer,Integer> getUserShipyardLevel(ArrayList<Integer> monetidList){
		if(monetidList!=null){
			if(monetidList.size()>0){
				StringBuffer sb = new StringBuffer();
				for(int monetid:monetidList){
					sb.append(monetid);
					sb.append(",");
				}
				String monetids = sb.substring(0,sb.length()-1);
				
				try {
					Map<Integer,Integer> monetidLevel = new HashMap<Integer,Integer>();
					Object[] dbArgs = new Object[] {};
					DBResultSet rs = dbClient.execSQLQuery("select monetid,buildingLevel from building where monetid in ("+monetids+") and buildingType=1",dbArgs);
					while(rs.next()) {
						monetidLevel.put(rs.getInt("monetid"),rs.getInt("buildingLevel"));
					}
					return monetidLevel;
				} catch (Exception e) {
					logger.error("getOneDaySales ", e);
				}
			}
		}
		return null;
	}
	
	static public double getRate(ArrayList<Integer> thatDay,ArrayList<Integer> yesterday) {
		if(!yesterday.isEmpty()&&!thatDay.isEmpty()){
			int amount = 0;
			for(int monetid:thatDay){
				if(yesterday.contains(monetid)){
					amount++;
				}
			}
			return amount*1.0/thatDay.size();
		}
		return 0;
	}
	
	public static Map<Integer,Integer> getLevelCount(Map<Integer,Integer> monetidMap){
		if(monetidMap!=null){
			if(!monetidMap.isEmpty()){
				Map<Integer,Integer> levelMap = new HashMap<Integer,Integer>();
				Set<Integer> monetidSet = monetidMap.keySet();
				for(int monetid:monetidSet){
					int level = monetidMap.get(monetid);
					if(levelMap.containsKey(level)){
						levelMap.put(level,levelMap.get(level)+1);
					}else{
						levelMap.put(level,1);
					}
				}
				return levelMap;
			}
		}
		return null;
	}
	
	
	static public void setReturnRate(int index,Date fromTime,double rate,String rateType,Date date) {
		try {
			Object[] dbArgs = new Object[] {fromTime,index,rate,rateType,date};
			dbClient167.execSQLUpdate(
							"insert into newOAReturnRate(rTime,dateNum,dateRate,rateType,statsTime) values(?,?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setReturnRate ", e);
		}
		logger.info("Index="+index+",Date="+MyUtil.DateToString(fromTime)+",rate="+rate+",date="+MyUtil.DateToString(date));
	}
	
	static public void setLevelReturnRate(int index,Date fromTime,int shipyardLevel,double rate,int levelCount,String rateType,Date date) {
		try {
			Object[] dbArgs = new Object[] {fromTime,index,shipyardLevel,rate,levelCount,rateType,date};
			dbClient167.execSQLUpdate(
							"insert into levelReturnRate(rTime,dateNum,shipyardLevel,dateRate,levelCount,rateType,statsTime) values(?,?,?,?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setLevelReturnRate ", e);
		}
		logger.info("Index="+index+",Date="+MyUtil.DateToString(fromTime)+",rate="+rate+",date="+MyUtil.DateToString(date));
	}
	
	public static void StartStats(Date date){
		//all user return rate
		ArrayList<Integer> yesterDay = MyUtil.getIdList(idLog+MyUtil.DateToString(date)+"_newoa.txt");
		for(int i=0;i<31;i++){
			double rate = -1;
			Calendar fromWhen = Calendar.getInstance();
			fromWhen.setTime(date);
			GregorianCalendar gc = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));
			for(int j=1;j<=i;j++){
				gc.add(Calendar.DATE, -1);
			}
			Date toTime = (Date) gc.getTime();
			if(new File(idLog+MyUtil.DateToString(toTime)+"_newoa.txt").isFile()){
				ArrayList<Integer> thatDay = MyUtil.getIdList(idLog+MyUtil.DateToString(toTime)+"_newoa.txt");
				rate = getRate(thatDay,yesterDay);
//				System.out.println("thatDay="+MyUtil.DateToString(toTime)+",yesterday="+MyUtil.DateToString(date)+",rate="+rate);
				setReturnRate(i,toTime,rate,"all",date);
			}
		}
		
		//new user return rate
		for(int i=0;i<31;i++){
			double rate = -1;
			Calendar fromWhen = Calendar.getInstance();
			fromWhen.setTime(date);
			GregorianCalendar gc = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));
			for(int j=1;j<=i;j++){
				gc.add(Calendar.DATE, -1);
			}
			Date toTime = (Date) gc.getTime();
			if(new File(idLog+MyUtil.DateToString(toTime)+"_newoaNewUser.txt").isFile()){
//				ArrayList<Integer> thatDay = MyUtil.getIdList(idLog+MyUtil.DateToString(toTime)+"_newoaNewUser.txt");
				ArrayList<Integer> thatDay = DBUtil.getNewUserList(toTime, new Date(toTime.getTime()+1000*3600*24));
				rate = getRate(thatDay,yesterDay);
				setReturnRate(i,toTime,rate,"new",date);
			}
		}
	}
	
	public static Map<Integer,Double> getEachShipyardLevelReturnRate(Map<Integer,Integer> thatDayMap,ArrayList<Integer> yesterdayList){
		if(thatDayMap!=null){
			Set<Integer> monetidSet = thatDayMap.keySet();
			if(monetidSet!=null&&monetidSet.size()>0){
				if(yesterdayList!=null&&yesterdayList.size()>0){
					Map<Integer,Integer> allMap = new HashMap<Integer,Integer>();
					Map<Integer,Integer> stayMap = new HashMap<Integer,Integer>();
					for(int monetid:monetidSet){
						int level = thatDayMap.get(monetid);
						if(allMap.containsKey(level)){
							allMap.put(level,allMap.get(level)+1);
						}else{
							allMap.put(level,1);
						}
						if(yesterdayList.contains(monetid)){
							if(stayMap.containsKey(level)){
								stayMap.put(level,stayMap.get(level)+1);
							}else{
								stayMap.put(level,1);
							}
						}
					}
					
					Map<Integer,Double> levelMap = new HashMap<Integer,Double>();
					Set<Integer> levelSet = allMap.keySet();
					for(int level:levelSet){
						int stay = 0;
						if(stayMap.containsKey(level)){
							stay = stayMap.get(level);
						}
						levelMap.put(level,stay*1.0/allMap.get(level));
					}
					return levelMap;
				}
			}
		}
		return null;
	}
	
	public static void getEachShipyardLevelReturnRate(Date date){
		ArrayList<Integer> yesterDay = MyUtil.getIdList(idLog+MyUtil.DateToString(date)+"_newoa.txt");
		for(int i=0;i<31;i++){
			Calendar fromWhen = Calendar.getInstance();
			fromWhen.setTime(date);
			GregorianCalendar gc = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));
			for(int j=1;j<=i;j++){
				gc.add(Calendar.DATE, -1);
			}
			Date toTime = (Date) gc.getTime();
			if(new File(idLog+MyUtil.DateToString(toTime)+"_newoa.txt").isFile()){
				ArrayList<Integer> thatDay = MyUtil.getIdList(idLog+MyUtil.DateToString(toTime)+"_newoa.txt");
				Map<Integer,Integer> monetidMap = getUserShipyardLevel(thatDay);
				Map<Integer,Double> levelReturn = getEachShipyardLevelReturnRate(monetidMap, yesterDay);
				Map<Integer,Integer> monetidLevel = getLevelCount(monetidMap);
				if(levelReturn!=null){
					Set<Integer> levelSet = levelReturn.keySet();
					for(int level:levelSet){
						//set
						setLevelReturnRate(i,toTime,level,levelReturn.get(level),monetidLevel.get(level),"all",date);
					}
				}
			}
		}
	}
}
