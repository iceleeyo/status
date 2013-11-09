package user;

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
import basicStats.OAproKPI;

public class NewUserGuide {
	static Logger logger = Logger.getLogger(OAproKPI.class);
	static String dbWriteUrl = null;
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient = null;
	static MoDBRW dbClient167 = null;
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			dbWriteUrl = serverConf.getString("dbWriteUrl");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			
			dbClient = new MoDBRW(dbWriteUrl,dbDriver);
			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	public static Map<Integer,Integer> getNewUserGuideMap(Date fromTime,Date toTime){
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		try {
			Object[] dbArgs = new Object[] {fromTime,toTime};
			DBResultSet rs = dbClient.execSQLQuery(
							"select step,count(*) as amount from guideinfisher where monet_id in (select monetid from fisher where newuserflag>=? and newuserflag<?) group by step",
							dbArgs);
			while(rs.next()) {
				map.put(rs.getInt("step"),rs.getInt("amount"));
			}
		} catch (Exception e) {
			logger.error("getNewUserGuideMap ", e);
		}
		return map;
	}
	
	public static void setNewUserGuide(int days,int guide,int amount,Date date){
		try {
			Object[] dbArgs = new Object[] {days,guide,amount,date};
			dbClient167.execSQLUpdate(
							"insert into newUserGuide(days,guide,amount,gdate) values(?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("getLastMonetid ", e);
		}
	}
	
	public static void StartStats(Date fromTime){
		Date toTime = new Date(fromTime.getTime()+1000*3600*24);
		Map<Integer,Integer> map = getNewUserGuideMap(fromTime, toTime);
		Set<Integer> set = map.keySet();
		for(int guide:set){
			setNewUserGuide(1, guide,map.get(guide),fromTime);
		}
	}
}
