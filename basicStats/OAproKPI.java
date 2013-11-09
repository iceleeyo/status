package basicStats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.DBResultSet;
import util.MoDBRW;
import util.MyUtil;

public class OAproKPI {
	static Logger logger = Logger.getLogger(OAproKPI.class);
	static String dbWriteUrl = null;
	static String protalDbUrl = null;
	static String dbDriver = null;
	static MoDBRW dbClient = null;
	static String serverDbUrl = null;
	static MoDBRW serverDb = null;
	static MoDBRW protalDb = null;
	static String idLog = null;
	static String ccuLog = null;
	static SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			
			dbWriteUrl = serverConf.getString("dbWriteUrl");
			dbDriver = serverConf.getString("dbDriver");
			protalDbUrl = serverConf.getString("dbReadUrls");
			serverDbUrl = serverConf.getString("db086");
			
			idLog = serverConf.getString("idlog");
			ccuLog = serverConf.getString("ccuLog");
			
			serverDb = new MoDBRW(serverDbUrl,dbDriver);
			dbClient = new MoDBRW(dbWriteUrl,dbDriver);
			protalDb = new MoDBRW(protalDbUrl,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	public static void countFisherAmount(Date date){
		int amount = 0;
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient.execSQLQuery("select count(*) as amount from fisher",dbArgs);
			if (rs.next()) {
				amount = rs.getInt("amount");
			}
			
		} catch (Exception e) {
			logger.error("countFisherAmount ", e);
		}
		
		try {
			Object[] dbArgs = new Object[] {"FisherAmount",amount,date};
			protalDb.execSQLUpdate("insert into kpi(oakey,oavalue,sdate) values(?,?,?)",dbArgs);
		} catch (Exception e) {
			logger.error("setKpi with key=" + "FisherAmount" + " value=" + amount+ " sdate=" + date, e);
		}
	}
	
	public static double getOneDaySales(Date date){
		double sales = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = protalDb.execSQLQuery("select sum(credit) as amount from selling where stime=?",dbArgs);
			if (rs.next()) {
				sales = rs.getDouble("amount");
			}
		} catch (Exception e) {
			logger.error("getOneDaySales ", e);
		}
		return sales;
	}
	
	static public void setKpi(String key,double value,Date sdate) {
		try {
			Object[] dbArgs = new Object[] {key,value,sdate};
			protalDb.execSQLUpdate("insert into kpi(oakey,oavalue,sdate) values(?,?,?)",dbArgs);
		} catch (Exception e) {
			logger.error("setKpi with key=" + key + " value=" + value + " sdate=" + sdate, e);
		}
		logger.info("key="+key+",value="+value+",date"+sdate);
	}
	
	public static void StartStats(Date date){
		logger.info("KPI start "+new Date());
		//fisher amount
		countFisherAmount(date);
		
		//new user
		int newUserReg = 0;
		newUserReg = getNewUser(date);
		setKpi("newUserReg",newUserReg,date);
		
		//new user
		int newUserDB = 0;
		newUserDB = getNewUserFromDB(date);
		setKpi("newUserDB",newUserDB,date);
		
		//DAU
		int dailyUserAmount = 0;
		dailyUserAmount = getDailyUserAmount(date);
		
		//selling
		double selling = 0.0;
		selling = getOneDaySales(date);
		setKpi("DailySelling",selling,date);
		
		//在线人数
		//the log location
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String path = ccuLog + simpleDateFormat.format(date)+".txt";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			String temp = null;
			temp = br.readLine();
			int users = 0;
			int maxUsers = 0;
			int num = 0;
			long usersTotal = 0;
			while(temp!=null){
				users = getAvrgUsers(temp);
				if(users>0){
					usersTotal += users;
					if(users>maxUsers){
						maxUsers = users;
					}
					num++;
				}
				temp = br.readLine();
			}
			
			//set 平均在线
			double avrgUsers = 0.0;
			if(num==0.0){
				
			}else{
				avrgUsers = usersTotal/num;
			}
			setKpi("avrgUsers",avrgUsers, date);
			
			//set 最高在线
			setKpi("maxUsers",maxUsers,date);
			
			//set 用户在线时长
			double times = 0.0;
			if(dailyUserAmount==0.0){
				
			}else{
				times = avrgUsers*24/dailyUserAmount;
			}
			setKpi("userTime",times,date);
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//MAU
		int mau = 0;
		mau = getMAU(date);
		setKpi("MAU",mau,date);
		
		// 月活跃用户
		//int macu = getMACU(date);
		//setKpi("MACU",macu,date);
		// 日活跃用户
		//int dacu = getDACU(date);
		//setKpi("DACU",dacu,date);
		
		//set 渗透率
		double permeability = 0.0;
		double topupMonth = getMonthlyTopUpUsers(date);
		permeability = topupMonth/mau;
		setKpi("Permeability",permeability,date);
		
		//arpu
		double arpu = 0.0;
		double topup30 = getMonthlyTopUpAmount(date);
		if(mau==0){
		}else{
			arpu = topup30/mau;
		}
		setKpi("ARPU",arpu, date);
		
		//arppu
		double arppu = 0.0;
		if(topupMonth==0){
			
		}else{
			arppu = topup30/topupMonth;
		}
		setKpi("ARPPU",arppu, date);
		
		logger.info("KPI end "+new Date());
	}
	
	static public int getNewUser(Date date){
		int amount = 0;
		Set<String> idSet = new HashSet<String>();
		String path = idLog+MyUtil.DateToString(date)+"_newoaNewUser.txt";
		if(new File(path).isFile()){
			idSet = getIdList(path,idSet);
		}
		amount = idSet.size();
		return amount;
	}
	
	static public int getTotalMAU(Date date){
		int amount = 0;
		Set<String> idSet = new HashSet<String>();
		for(int i=0;i<30;i++){
			Calendar fromWhen = Calendar.getInstance();
			fromWhen.setTime(date);
			GregorianCalendar gc = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));
			if(i==0){
				
			}else{
				gc.add(Calendar.DATE, -1);
				date = (Date) gc.getTime();
			}
			String path = idLog+MyUtil.DateToString(date)+"_newoa.txt";
			if(new File(path).isFile()){
				idSet = getIdList(path,idSet);
			}
		}
		amount = idSet.size();
		return amount;
	}
	
	public static int getAvrgUsers(String temp){
		int users = 0;
		if(temp.contains(",")){
			int start = 0;
			if(temp.indexOf(",") != -1){
				start = temp.indexOf(",") + 1;
				temp = temp.substring(start);
				Pattern pattern = Pattern.compile("[0-9]{1,}");
		        Matcher matcher = pattern.matcher((CharSequence)temp);
		        if(matcher.matches()){
		        	users = Integer.parseInt(temp);
		        }
			}
		}
		return users;
	}
	
	public static int getNewDailyUserAmount(Date date){
		int amount = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = protalDb.execSQLQuery(
							"select count(*) as amount from new_user_behavior where cdate=?",
							dbArgs);
			if (rs.next()) {
				amount = rs.getInt("amount");
			}
		} catch (Exception e) {
			logger.error("getNewDailyUserAmount ", e);
		}
		return amount;
	}
	
	public static int getDailyUserAmount(Date date){
		int amount = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = protalDb.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='DAU')",
							dbArgs);
			if (rs.next()) {
				amount = rs.getInt("oavalue");
			}
		} catch (Exception e) {
			logger.error("getDailyUserAmount ", e);
		}
		return amount;
	}
	
	public static double getDailyTopUpAmount(Date date){
		double amount = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = protalDb.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='TopUpAmount')",
							dbArgs);
			if (rs.next()) {
				amount = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
			logger.error("getDailyTopUpAmount ", e);
		}
		return amount;
	}
	
	public static double getDailyTopUpUsers(Date date){
		double amount = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = protalDb.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='TopUpUsers')",
							dbArgs);
			if (rs.next()) {
				amount = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
			logger.error("getDailyPaymentUser ", e);
		}
		return amount;
	}
	
	public static double getMonthlyTopUpAmount(Date date){
		double amount = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = protalDb.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='TopUpAmount30')",
							dbArgs);
			if (rs.next()) {
				amount = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
			logger.error("getMonthlyTopUpUsers ", e);
		}
		return amount;
	}
	
	public static double getMonthlyTopUpUsers(Date date){
		double amount = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = protalDb.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='TopUpUsers30')",
							dbArgs);
			if (rs.next()) {
				amount = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
			logger.error("getMonthlyTopUpUsers ", e);
		}
		return amount;
	}
	
//	public static double getDailySelling(Date date){
//		double amount = 0;
//		try {
//			Object[] dbArgs = new Object[] {date};
//			DBResultSet rs = dbClient167.execSQLQuery(
//							"select sum(credit) as amount from selling where stime=?",
//							dbArgs);
//			if (rs.next()) {
//				amount = rs.getDouble("amount");
//			}
//		} catch (Exception e) {
//			logger.error("getDailyUserAmount ", e);
//		}
//		
//		try {
//			Object[] dbArgs = new Object[] {date};
//			DBResultSet rs = dbClient167.execSQLQuery(
//							"select sum(oabalance) as amount from selling_oabalance where stime=?",
//							dbArgs);
//			if (rs.next()) {
//				amount -= rs.getDouble("amount");
//			}
//		} catch (Exception e) {
//			logger.error("getDailyUserAmount ", e);
//		}
//		return amount;
//	}
	
//	public static double getPermeability(Date date,int all){
//		double amount = 0;
//		double pay = 0;
//		Date date1 = new Date(date.getTime()-1000*3600*24*15);
//		Date date2 = new Date(date1.getTime()-1000*3600*24*15);
//
//		
//		Date date3 = new Date(date.getTime()+1000*3600*24);
//		Date date4 = new Date(date2.getTime()+1000*3600*24);
//		try {
//			Object[] dbArgs = new Object[] {date4,date3};
//			DBResultSet rs = dbClient1286.execSQLQuery(
//							"select count(distinct user_id) as amount from payment where time_payment>=? and time_payment<? and app_name='ocean age'",
//							dbArgs);
//			if (rs.next()) {
//				pay = rs.getDouble("amount");
////				System.out.println(pay);
//			}
//		} catch (Exception e) {
//			logger.error("getPermeability ", e);
//		}
//		
//		if(all!=0){
//			amount = pay/all;
//		}
//		
//		return amount;
//	}
	
	public static int getNewUserFromDB(Date date){
		Date date2 = new Date(date.getTime()+1000*3600*24);
		try {
			Object[] dbArgs = new Object[] {date,date2};
			DBResultSet rs = serverDb.execSQLQuery(
							"select count(*) as amount from fisher where newuserflag>=? and newuserflag<?",
							dbArgs);
			if (rs.next()) {
				return rs.getInt("amount");
			}
		} catch (Exception e) {
			logger.error("getNewUserFromDB ", e);
		}
		return 0;
	}
	
	static public int getMAU(Date date){
		int amount = 0;
		Set<String> idSet = new HashSet<String>();
		for(int i=0;i<30;i++){
			Calendar fromWhen = Calendar.getInstance();
			fromWhen.setTime(date);
			GregorianCalendar gc = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));
			if(i==0){
				
			}else{
				gc.add(Calendar.DATE, -1);
				date = (Date) gc.getTime();
			}
			String path = "/home/mozat/morange/OceanAgeDailyMonetidList/"+MyUtil.DateToString(date)+"_newoa.txt";
			if((new File(path)).isFile()){
				idSet = getIdList(path,idSet);
			}
		}
		amount = idSet.size();
		return amount;
	}
	
	
	/*static public int getDACU(Date date){
		try {
			DBResultSet rs = serverDb.execSQLQuery("select count(distinct monetid) amount from user_event where server_date < dateadd(dd,1, ?) and server_date >=  dateadd(dd,-30, ?)",new Object[]{sdf.format(date)});
			if (rs.next()) {
				return rs.getInt("amount");
			}else{
				return 0;
			}
		} catch (Exception e) {
			logger.error("getDACU ", e);
			return 0;
		}
	}*/
	
	/*static public int getMACU(Date date){
		try {
			DBResultSet rs = serverDb.execSQLQuery("select count(distinct monetid) amount from user_event where server_date < dateadd(dd,1, ?) and server_date >=  ?",new Object[]{sdf.format(date),sdf.format(date)});
			if (rs.next()) {
				return rs.getInt("amount");
			}else{
				return 0;
			}
		} catch (Exception e) {
			logger.error("getMACU ", e);
			return 0;
		}
	}*/
	

	public static Set<String> getIdList(String path,Set<String> idSet){
		BufferedReader br;
//		Set<String> idSet = new java.util.HashSet<String>();
		try {
			br = new BufferedReader(new FileReader(path));
			String temp = null;
			temp = br.readLine();
			while (temp != null) {
				idSet.add(temp);
				temp = br.readLine();
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return idSet;
	}
	
	
	/*public static void main(String args[]){
		Date date = new Date();
		try {
			MoDBRW testDb = new MoDBRW("jdbc:sqlserver://192.168.50.14:41188;databaseName=OceanAge_n3;user=mozone;password=morangerunmozone","com.microsoft.sqlserver.jdbc.SQLServerDriver");
			DBResultSet rs = testDb.execSQLQuery("select count(distinct monetid) amount from user_event where server_date < dateadd(dd,1, ?) and server_date >=  dateadd(dd,-30, ?)",new Object[]{sdf.format(date),sdf.format(date)});
			if (rs.next()) {
				System.out.println(rs.getInt("amount"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
