package main;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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

public class DailyReportEmail {
	static Logger logger = Logger.getLogger(DailyReportEmail.class);
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient = null;
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration(
					"system.properties"));
			Configuration serverConf = settings.subset("service");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			dbClient = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
		}
	}
	
	public static void StartSend(){
		logger.info("To send Email!");
		StringBuffer bf = new StringBuffer();
		
		bf.append("<HEAD>");
		bf.append("<STYLE TYPE=\"TEXT/CSS\">");
		
		bf.append("div.table {float:left;position:relative;width:614px;margin:0 300px 37px 0;}");
		bf.append("table.listing {text-align: center;font-family: Arial;font-weight: normal;font-size: 11px;width: 900px;background-color:#fafafa;border: 1px #AE0000 solid;border-collapse: collapse;border-spacing: 0px;}");
		bf.append("table.listing td,");
		bf.append("table.listing th{border: 1px #AE0000 solid;padding:3px;}");
		
		
		bf.append("</STYLE>");
		bf.append("</HEAD>");
		
		bf.append("<h1>");
		bf.append("OA Pro Daily Report");
		bf.append("<h1>");
		//start
		bf.append("<table>");
		
		Date toTime = new Date();
		Date fromTime = new Date();
		Calendar fromWhen = Calendar.getInstance();
		fromWhen.setTime(fromTime);
		GregorianCalendar gc = new GregorianCalendar(fromWhen
				.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
				.get(Calendar.DAY_OF_MONTH));
		gc.add(Calendar.DATE, -1);
		toTime = (Date) gc.getTime();
		gc.add(Calendar.DATE, -15);
		gc.add(Calendar.DATE, -15);
		fromTime = (Date) gc.getTime();
		
		//New Users
		Map<Date,Double> newuserMap = new HashMap<Date,Double>();
		Map<Date,Integer> newuserExceptionMap = new HashMap<Date,Integer>();
		//DAU
		Map<Date,Double> dauMap = new HashMap<Date,Double>();
		Map<Date,Integer> dauExceptionMap = new HashMap<Date,Integer>();
		//
		Map<Date,Double> sellingMap = new HashMap<Date,Double>();
		Map<Date,Integer> sellingExceptionMap = new HashMap<Date,Integer>();
		//
		Map<Date,Double> addvalueMap = new HashMap<Date,Double>();
		Map<Date,Integer> addvalueExceptionMap = new HashMap<Date,Integer>();
		//
		{
			long j;	
			j = (toTime.getTime()-(new Date()).getTime())/(1000*60*60*24);
			long flag = (fromTime.getTime()-(new Date()).getTime())/(1000*60*60*24);
			for (; j >= flag; j--) {
				Date fTime = getDate((int)j);
				
				addvalueMap.put(fTime,DailyReportEmail.getDailyAddValue(fTime));
				sellingMap.put(fTime,DailyReportEmail.getDailySelling(fTime));
				dauMap.put(fTime,DailyReportEmail.getDAU(fTime));
				newuserMap.put(fTime,DailyReportEmail.getNewFisher(fTime));
			}
			addvalueExceptionMap = checkExceptionValue(addvalueMap);
			sellingExceptionMap = checkExceptionValue(sellingMap);
			dauExceptionMap = checkExceptionValue(dauMap);
			newuserExceptionMap = checkExceptionValue(newuserMap);
		}	
		bf.append("</table>");
		bf.append("<table>");
		{
			toTime = new Date();
			fromTime = new Date();
			fromWhen = Calendar.getInstance();
			fromWhen.setTime(fromTime);
			gc = new GregorianCalendar(fromWhen
					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
					.get(Calendar.DAY_OF_MONTH));
			gc.add(Calendar.DATE, -1);
			toTime = (Date) gc.getTime();
			gc.add(Calendar.DATE, -3);
			fromTime = (Date) gc.getTime();
		
			long j;	
			bf.append("<div class=\"table\">");
			bf.append("<table class=\"listing\" cellpadding=\"0\" cellspacing=\"0\">");

			bf.append("<tr class=\"first\" width=\"177\">");
			bf.append("<th nowrap='nowrap'>");
			bf.append("日期");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("DAU");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("新用户数");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("MAU");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("历史用户");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("销售(蓝宝石)");
			bf.append("</th>");
						
			bf.append("<th nowrap='nowrap'>");
			bf.append("付费人数");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("日充值人数");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("月充值人数");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("日充值金额");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("月充值金额");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("ARPU");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("ARPPU");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("平均在线人数");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("最高在线人数");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("用户在线时长");
			bf.append("</th>");
			
			bf.append("<th nowrap='nowrap'>");
			bf.append("渗透率");
			bf.append("</th>");
			
			bf.append("</tr>");
	
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

			j = (toTime.getTime()-(new Date()).getTime())/(1000*60*60*24);
			DecimalFormat dfdouble = new DecimalFormat("0.00");
			DecimalFormat dfint = new DecimalFormat("0");
			long flag = (fromTime.getTime()-(new Date()).getTime())/(1000*60*60*24);
			for (; j >= flag; j--) {
				if(-j%2 == 1){
					bf.append("<tr >");
				}else{
					bf.append("<tr class=\"bg\">");
				}

				Date fTime = getDate((int)j);
				{				
					bf.append("<td>");
					bf.append(sdf.format(fTime));
					bf.append("</td>");
				
					bf.append("<td>");
					if(dauExceptionMap.get(fTime)==1){
						bf.append("<font color=\"#FF0000\">");
						bf.append(dauMap.get(fTime));
						bf.append("</font>");
					}else if(dauExceptionMap.get(fTime)==2){
						bf.append("<font color=\"#00FF00\">");
						bf.append(dauMap.get(fTime));
						bf.append("</font>");
					}else{
						bf.append(dauMap.get(fTime));
					}
					bf.append("</td>");
					
					bf.append("<td>");
					if(newuserExceptionMap.get(fTime)==1){
						bf.append("<font color=\"#FF0000\">");
						bf.append(newuserMap.get(fTime));
						bf.append("</font>");
					}else if(newuserExceptionMap.get(fTime)==2){
						bf.append("<font color=\"#00FF00\">");
						bf.append(newuserMap.get(fTime));
						bf.append("</font>");
					}else{
						bf.append(newuserMap.get(fTime));
					}
					bf.append("</td>");
					
					bf.append("<td>");
					bf.append(DailyReportEmail.getMAU(fTime));
					bf.append("</td>");
					
					bf.append("<td>");
					bf.append(dfint.format(DailyReportEmail.getFisherAmount(fTime)));
					bf.append("</td>");
					
					bf.append("<td>");
					if(sellingExceptionMap.get(fTime)==1){
						bf.append("<font color=\"#FF0000\">");
						bf.append(sellingMap.get(fTime));
						bf.append("</font>");
					}else if(sellingExceptionMap.get(fTime)==2){
						bf.append("<font color=\"#00FF00\">");
						bf.append(sellingMap.get(fTime));
						bf.append("</font>");
					}else{
						bf.append(sellingMap.get(fTime));
					}
					bf.append("</td>");
					
					bf.append("<td>");
					bf.append(dfint.format(DailyReportEmail.getPaymentUser(fTime)));
					bf.append("</td>");
					
					bf.append("<td>");
					bf.append(dfint.format(DailyReportEmail.getDailyAddvalueUser(fTime)));
					bf.append("</td>");
					
					bf.append("<td>");
					bf.append(dfint.format(DailyReportEmail.getMonthlyAddvalueUser(fTime)));
					bf.append("</td>");
					
					bf.append("<td>");
					if(addvalueExceptionMap.get(fTime)==1){
						bf.append("<font color=\"#FF0000\">");
						bf.append(addvalueMap.get(fTime));
						bf.append("</font>");
					}else if(addvalueExceptionMap.get(fTime)==2){
						bf.append("<font color=\"#00FF00\">");
						bf.append(addvalueMap.get(fTime));
						bf.append("</font>");
					}else{
						bf.append(addvalueMap.get(fTime));
					}
					bf.append("</td>");
					
					bf.append("<td>");
					bf.append(dfint.format(DailyReportEmail.getMonthlyAddValue(fTime)));
					bf.append("</td>");
					
					bf.append("<td>");
					bf.append(DailyReportEmail.getARPU(fTime));
					bf.append("</td>");
					
					bf.append("<td>");
					bf.append(DailyReportEmail.getARPPU(fTime));
					bf.append("</td>");
					
					bf.append("<td>");
					bf.append(dfint.format(DailyReportEmail.getAvrgUsers(fTime)));
					bf.append("</td>");
					
					bf.append("<td>");
					bf.append(dfint.format(DailyReportEmail.getMaxUsers(fTime)));
					bf.append("</td>");
					
					bf.append("<td>");
					bf.append(dfdouble.format(DailyReportEmail.getUsersTime(fTime)));
					bf.append("</td>");
					
					bf.append("<td class=\"last\">");
					bf.append(DailyReportEmail.getPermeability(fTime));
					bf.append("</td>");
				}

				bf.append("</tr>");

			}
			bf.append("</table>");
			bf.append("</div>");
			
		}
		
		bf.append("<br>");
		bf.append("<br>");
		
		MyUtil.sendEmail("Daily Report - "+MyUtil.DateToString(new Date(fromTime.getTime()+1000*3600*24*3)), bf.toString());
		logger.info("Finish Sending!");
	}
	
	public static double getDailyAddValue(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='TopUpAmount')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static double getMonthlyAddValue(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='TopUpAmount30')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static double getDailySelling(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='DailySelling')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static double getDAU(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='DAU')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static double getNewFisher(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"select oavalue from kpi where oakey='newUserDB' and sdate=?",
							dbArgs);
			if(rs.next()) {
				result = rs.getInt("oavalue") ;
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static Map<Date,Integer> checkExceptionValue(Map<Date,Double> source){
		Map<Date,Integer> result = new HashMap<Date,Integer>();
		List<Double> valueList = new ArrayList<Double>();
		double _up4;
		double _up4_real;
		int _up4_maybe;
		double _down4;
		double _down4_real;
		int _down4_maybe;
		double _mid;
		double _mid_real;
		int _mid_maybe;
		
		double highException = 0;
		double lowException = 0;
		
		Set<Date> dates = source.keySet();
		for(Date date:dates){
			valueList.add(source.get(date));
		}
		
		DecimalFormat df = new DecimalFormat("0");
		
		Collections.sort(valueList);
		
		_down4_real = (valueList.size()+1.0)/4-1;
		_down4_maybe = Integer.parseInt(df.format(Math.floor(_down4_real)));
		
		_mid_real = (valueList.size()+1.0)/2-1;
		_mid_maybe = Integer.parseInt(df.format(Math.floor(_mid_real)));
		
		_up4_real = 3*(valueList.size()+1.0)/4-1;
		_up4_maybe = Integer.parseInt(df.format(Math.floor(_up4_real)));
		
		_up4 = valueList.get(_up4_maybe)+(valueList.get(_up4_maybe+1)-valueList.get(_up4_maybe))*(_up4_real-_up4_maybe);
		_mid = valueList.get(_mid_maybe)+(valueList.get(_mid_maybe+1)-valueList.get(_mid_maybe))*(_mid_real-_mid_maybe);
		_down4 = valueList.get(_down4_maybe)+(valueList.get(_down4_maybe+1)-valueList.get(_down4_maybe))*(_down4_real-_down4_maybe);
		
		highException = _mid + (_up4-_mid)*1.5;
		lowException = _mid + (_down4-_mid)*1.5;
		
		double tempValue;
		for(Date date:dates){
			tempValue = source.get(date);
			if(tempValue>=highException){
				result.put(date, 1);
			}else if(tempValue<=lowException){
				result.put(date, 2);
			}else{
				result.put(date, 0);
			}
		}
		
		return result;
	}
	
	public static double getMAU(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='MAU')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static double getFisherAmount(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='FisherAmount')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static double getAvrgUsers(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='avrgUsers')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static double getMaxUsers(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='maxUsers')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static double getUsersTime(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='userTime')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static double getDailyAddvalueUser(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='TopUpUsers')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static double getMonthlyAddvalueUser(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='TopUpUsers30')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static double getPaymentUser(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='PaymentUser')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static double getARPU(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='ARPU')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static double getARPPU(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='ARPPU')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static Map<String, Float> getSellingMap(Date date){
		Map<String, Float> sellingMap = new HashMap<String,Float>();
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"select * from selling where stime=?",
							dbArgs);
			while(rs.next()) {
				sellingMap.put(rs.getString("iname"),rs.getFloat("credit"));
			}
		} catch (Exception e) {
		}
		return sellingMap;
	}
	
	public static double getPermeability(Date date){
		double result = 0;
		try {
			Object[] dbArgs = new Object[] {date};
			DBResultSet rs = dbClient.execSQLQuery(
							"SELECT oavalue FROM kpi WHERE (sdate = ? and oakey='Permeability')",
							dbArgs);
			if(rs.next()) {
				result = rs.getDouble("oavalue");
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	static public Date getDate(int dex){
		Date date = new Date();
		Calendar rightNow = Calendar.getInstance();
		GregorianCalendar gc = new GregorianCalendar(rightNow
				.get(Calendar.YEAR), rightNow.get(Calendar.MONTH),
				rightNow.get(Calendar.DAY_OF_MONTH));
		gc.add(Calendar.DATE, dex);
		date = (Date) gc.getTime();	
		return date;
	}
}
