package basicStats;

import java.util.Date;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.DBResultSet;
import util.MoDBRW;

public class TribeStats {
	static final Logger logger = Logger.getLogger(TribeStats.class);
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
	
	public static void setAllTribeLevel(Date date){
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs =  dbClient81.execSQLQuery(
							"select tribelevel,count(*) as amount from tribe group by tribelevel",
							dbArgs);
			while(rs.next()){
				setTribeStats(rs.getInt("tribelevel"),rs.getInt("amount"), date);
			}
		} catch (Exception e) {
			logger.error("getTribeLevel", e);
		}
	}
	
	static public void setTribeStats(int level,int amount,Date date){
		try {
			Object[] dbArgs = new Object[] {level,amount,date};
			dbClient.execSQLUpdate(
							"insert into TribeInfoStats(level,amount,ttime) values (?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setTribeStats with " + " date=" + date, e);
		}
	}
	
	/*
	 * 按照家族等级分类，每个等级升级时间的平均值、中位数、最小值
	 */
	
//	public static void setTribeLevelTime(Date date) {
//		try {
//			Object[] dbArgs = new Object[] {};
//			DBResultSet rs =  dbClient81.execSQLQuery(
//							"select tribelevel,count(*) as amount from tribe group by tribelevel",
//							dbArgs);
//			while(rs.next()){
//				setTribeLevelTime(rs.getInt("tribelevel"),rs.getInt("amount"), date);
//			}
//		} catch (Exception e) {
//			logger.error("getTribeLevel", e);
//		}
//	}
//	static public void setTribeLevelTime(int level,int amount,Date date){
//		try {
//			Object[] dbArgs = new Object[] {level,amount,date};
//			dbClient.execSQLUpdate(
//							"insert into TribeInfoStats(level,amount,ttime) values (?,?,?)",
//							dbArgs);
//		} catch (Exception e) {
//			logger.error("setTribeStats with " + " date=" + date, e);
//		}
//	}
	
	
	/*
	 * 家族贡献值       珍珠捐献总贡献值        打家族怪总贡献
	 */
		public static void setTribeContribute(Date date) {
			try {
				Object[] dbArgs = new Object[] {};
				DBResultSet rs =  dbClient81.execSQLQuery(
								"select tribelevel,count(*) as amount from tribe group by tribelevel",
								dbArgs);
				while(rs.next()){
					setTribeContribute(rs.getInt("tribelevel"),rs.getInt("amount"), date);
				}
			} catch (Exception e) {
				logger.error("getTribeLevel", e);
				}
			}
		static public void setTribeContribute(int level,int amount,Date date){
			try {
				Object[] dbArgs = new Object[] {level,amount,date};
				dbClient.execSQLUpdate(
								"insert into TribeInfoStats(level,amount,ttime) values (?,?,?)",
								dbArgs);
			} catch (Exception e) {
				logger.error("setTribeStats with " + " date=" + date, e);
			}
		}
	}
