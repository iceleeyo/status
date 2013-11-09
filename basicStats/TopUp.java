package basicStats;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.DBResultSet;
import util.MoDBRW;

public class TopUp {
	static Logger logger = Logger.getLogger(OAproKPI.class);
	static String dbReadUrls = null;
	static String dbDriver = null;
	static String db1286 = null;
	static MoDBRW dbClient1286 = null;
	static MoDBRW dbClient167 = null;
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			db1286 = serverConf.getString("db086");
			
			dbClient1286 = new MoDBRW(db1286,dbDriver);
			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}

	}
	
	public static void StartStats(Date fTime,Date tTime){
		int j = 0, k = 1;
		long day = ((tTime.getTime() - fTime.getTime()) / 1000 / 60 / 60 / 24);
		for (; j < day; j++, k++) {
			//
			Calendar fromWhen = Calendar.getInstance();
			fromWhen.setTime(fTime);
			GregorianCalendar gc = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));
			GregorianCalendar gc2 = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));

			gc.add(Calendar.DATE, j);
			Date fromTime = (Date) gc.getTime();
			//
			gc2.add(Calendar.DATE, k);
			Date toTime = (Date) gc2.getTime();
			
			deleteAddValue(fromTime);
			setAddValue(0,fromTime,toTime);
			setAddValue(500,fromTime,toTime);
			setAddValue(1000,fromTime,toTime);
			setAddValue(2000,fromTime,toTime);
			
			int oneDayTopUp = getTopUpAmount(fromTime, 1);
//			int oneDayCount = getTopUpCount(fromTime, 1);
			int oneDayUsers = getTopUpUsers(fromTime, 1);
			
//			Object[] dbArgs = new Object[] {0,oneDayCount,oneDayUsers,fTime};
//			dbClient167.execSQLUpdate("insert into AddValueUser(money,times,users,atime) values(?,?,?,?)",
//							dbArgs);
			
			OAproKPI.setKpi("TopUpAmount",oneDayTopUp, fromTime);
			OAproKPI.setKpi("TopUpAmount30",getTopUpAmount(fromTime,30), fromTime);
			OAproKPI.setKpi("TopUpUsers",oneDayUsers, fromTime);
			OAproKPI.setKpi("TopUpUsers30",getTopUpUsers(fromTime,30), fromTime);
		}
	}
	
	public static void deleteAddValue(Date fTime){
		try {
			Object[] dbArgs = new Object[] {fTime};
			dbClient167.execSQLUpdate("delete from AddValueUser where atime = ?",dbArgs);
		} catch (Exception e) {
			logger.error("deleteAddValue" , e);
		}
		
	}
	
	public static void setAddValue(int money,Date fTime,Date tTime){
		try {
			int amount = getAddMoney(money,fTime,tTime);
			int user = getAddUser(money,fTime,tTime);
			if(amount>0){
				Object[] dbArgs = new Object[] {money/100,amount,user,fTime};
				dbClient167.execSQLUpdate("insert into AddValueUser(money,times,users,atime) values(?,?,?,?)",
								dbArgs);
			}
		} catch (Exception e) {
			logger.error("setAddValue" , e);
		}
	}
	
	public static int getAddMoney(int money,Date fTime,Date tTime){
		int result = 0;
		try {
			DBResultSet rs = null;
			if(money == 0){
				Object[] dbArgs = new Object[] {fTime,tTime};
				rs = dbClient1286.execSQLQuery(
							"select count(*) as amount from callbackstc where date>=? and date<?",
							dbArgs);
			}else{
				Object[] dbArgs = new Object[] {fTime,tTime,money};
				rs = dbClient1286.execSQLQuery(
							"select count(*) as amount from callbackstc where date>=? and date<? and amount=?",
							dbArgs);
			}
			if(rs.next()){
				result = rs.getInt("amount");
			}else{
				result = 0;
			}
		} catch (Exception e) {
			logger.error("getAddMoney" , e);
		}
		return result;
	}
	
	public static int getAddUser(int money,Date fTime,Date tTime){
		Set<Integer> set = new HashSet<Integer>();
		try {
			//Addvalue
			DBResultSet rs = null;
			if(money==0){
				Object[] dbArgs = new Object[] {fTime,tTime};
				rs = dbClient1286.execSQLQuery(
							"select distinct monet_id from callbackstc where date>=? and date<?",
							dbArgs);
			}else{
				Object[] dbArgs = new Object[] {fTime,tTime,money};
				rs = dbClient1286.execSQLQuery(
							"select distinct monet_id from callbackstc where date>=? and date<? and amount=?",
							dbArgs);
			}
			while(rs.next()){
				set.add(rs.getInt("monet_id"));
			}
		} catch (Exception e) {
			logger.error("getAddUser" , e);
		}
		return set.size();
	}
	
	public static int getTopUpAmount(Date fTime,int last){
		Date tTime = new Date();
		Calendar fromWhen = Calendar.getInstance();
		fromWhen.setTime(fTime);
		GregorianCalendar gc = new GregorianCalendar(fromWhen
				.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
				.get(Calendar.DAY_OF_MONTH));
		gc.add(Calendar.DATE, 1);
		tTime = (Date) gc.getTime();
		if(last==1){
			gc.add(Calendar.DATE, -1);	
			fTime = (Date) gc.getTime();
		}else if(last==30){
			gc.add(Calendar.DATE, -15);
			gc.add(Calendar.DATE, -15);	
			fTime = (Date) gc.getTime();
		}
		int result = 0;
		try {
			DBResultSet rs = null;
			Object[] dbArgs = new Object[] {fTime,tTime};
			rs = dbClient1286.execSQLQuery(
						"select amount/100 as amount from callbackstc where date>=? and date<?",
						dbArgs);
			while(rs.next()){
				result += rs.getInt("amount");
			}
		} catch (Exception e) {
			logger.error("getAddMoney" , e);
		}
		return result;
	}
	
	public static int getTopUpCount(Date fTime,int last){
		Date tTime = new Date();
		Calendar fromWhen = Calendar.getInstance();
		fromWhen.setTime(fTime);
		GregorianCalendar gc = new GregorianCalendar(fromWhen
				.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
				.get(Calendar.DAY_OF_MONTH));
		gc.add(Calendar.DATE, 1);
		tTime = (Date) gc.getTime();
		if(last==1){
			gc.add(Calendar.DATE, -1);	
			fTime = (Date) gc.getTime();
		}else if(last==30){
			gc.add(Calendar.DATE, -15);
			gc.add(Calendar.DATE, -15);	
			fTime = (Date) gc.getTime();
		}
		int result = 0;
		try {
			DBResultSet rs = null;
			Object[] dbArgs = new Object[] {fTime,tTime};
			rs = dbClient1286.execSQLQuery(
						"select count(*) amount from callbackstc where date>=? and date<?",
						dbArgs);
			while(rs.next()){
				result = rs.getInt("amount");
			}
		} catch (Exception e) {
			logger.error("getAddMoney" , e);
		}
		return result;
	}
	
	public static int getTopUpUsers(Date fTime,int last){
		Date tTime = new Date();
		Calendar fromWhen = Calendar.getInstance();
		fromWhen.setTime(fTime);
		GregorianCalendar gc = new GregorianCalendar(fromWhen
				.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
				.get(Calendar.DAY_OF_MONTH));
		gc.add(Calendar.DATE, 1);
		tTime = (Date) gc.getTime();
		if(last==1){
			gc.add(Calendar.DATE, -1);	
			fTime = (Date) gc.getTime();
		}else if(last==30){
			gc.add(Calendar.DATE, -15);
			gc.add(Calendar.DATE, -15);	
			fTime = (Date) gc.getTime();
		}
		int result = 0;
		try {
			DBResultSet rs = null;
			Object[] dbArgs = new Object[] {fTime,tTime};
			rs = dbClient1286.execSQLQuery(
						"select count(distinct monet_id) as amount from callbackstc where date>=? and date<?",
						dbArgs);
			if(rs.next()){
				result = rs.getInt("amount");
			}
		} catch (Exception e) {
			logger.error("getTopUpUsers" , e);
		}
		return result;
	}
}
