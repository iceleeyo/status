package basicStats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.DBResultSet;
import util.MoDBRW;
import util.MyUtil;

public class BusinessModel {
	static Logger logger = Logger.getLogger(BusinessModel.class);
	static String dbWriteUrl = null;
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient = null;
	static String db1286 = null;
	static MoDBRW dbClient1286 = null;
	static MoDBRW dbClient167 = null;
	static String idLog = null;
	static String ccuLog = null;
	
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			dbWriteUrl = serverConf.getString("dbWriteUrl");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			db1286 = serverConf.getString("db086");
			idLog = serverConf.getString("idlog");
			ccuLog = serverConf.getString("ccuLog");
			
			dbClient1286 = new MoDBRW(db1286,dbDriver);
			dbClient = new MoDBRW(dbWriteUrl,dbDriver);
			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	/**
	 * 获得某个文件的id列表.(文件格式必须固定)
	 * @param path
	 * @return
	 */
	public static Set<Integer> getIdList(String path){
		BufferedReader br;
		Set<Integer> idList = new HashSet<Integer>();
		try {
			if((new File(path)).isFile()){
				br = new BufferedReader(new FileReader(path));
				String temp = null;
				temp = br.readLine();
				while (temp != null) {
					if(MyUtil.isMonetid(temp)){
						if(!idList.contains(Integer.parseInt(temp))){
							idList.add(Integer.parseInt(temp));
						}
					}
					temp = br.readLine();
				}
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return idList;
	}
	
	/**
	 * 获取这个时间段的登录用户。例如20130515,20130522将会获得20130515-20130521这七天的不重复登录用户的集合
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Set<Integer> getLoginIDSet(Date date1,Date date2){
		Set<Integer> loginIDSet = new HashSet<Integer>();
		long dateNum = (date2.getTime()-date1.getTime())/1000/3600/24;
		System.out.println("daycount="+dateNum);
		for(int i=0;i<dateNum;i++){
			Date date = new Date(date1.getTime()+(long)i*1000*3600*24);
//			System.out.println(date);
			Set<Integer> tempSet = getIdList(idLog+MyUtil.DateToString(date)+"_newoa.txt");
			for(int monetid:tempSet){
				loginIDSet.add(monetid);
			}
		}
		//+"and monetid not in(7504,81849,2235,540,71859,83054,551,668,69837,62392)"
		//+"and monetid not in (4164,7001,7803,83885,13793,28268,105158,105529,4164)"
		loginIDSet.remove(7504);
		loginIDSet.remove(81849);
		loginIDSet.remove(2235);
		loginIDSet.remove(540);
		loginIDSet.remove(71859);
		loginIDSet.remove(83054);
		loginIDSet.remove(551);
		loginIDSet.remove(668);
		loginIDSet.remove(69837);
		loginIDSet.remove(62392);
		
		loginIDSet.remove(4164);
		loginIDSet.remove(7001);
		loginIDSet.remove(7803);
		loginIDSet.remove(83885);
		loginIDSet.remove(13793);
		loginIDSet.remove(28268);
		loginIDSet.remove(105158);
		loginIDSet.remove(105529);
		loginIDSet.remove(4164);
		return loginIDSet;
	}
	
	/**
	 * 将传入的monetid集合按照平台分类
	 * @param idSet
	 * @return
	 */
	public static Map<String,Set<Integer>> getPlatformActiveUserMap(Set<Integer> idSet){
		Map<String,Set<Integer>> platformMap = new HashMap<String,Set<Integer>>();
		StringBuffer sb = new StringBuffer();
		for(int monetid:idSet){
			sb.append(monetid);
			sb.append(",");
		}
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient167.execSQLQuery(
							"select platform,monetid from all_users where monetid in ("+sb.substring(0,sb.length()-1)+") ", 
							//+"and monetid not in(7504,81849,2235,540,71859,83054,551,668,69837,62392)",
							dbArgs);
			while(rs.next()) {
				String platform = rs.getString("platform");
				int monetid = rs.getInt("monetid");
				if(platformMap.containsKey(platform)){
					Set<Integer> set = platformMap.get(platform);
					set.add(monetid);
					platformMap.put(platform, set);
				}else{
					Set<Integer> set = new HashSet<Integer>();
					set.add(monetid);
					platformMap.put(platform, set);
				}
			}
		} catch (Exception e) {
			logger.error("getNewUserFromDB ", e);
		}
		return platformMap;
	}
	
	
	public static Map<String,Set<Integer>> getPlatformActiveUserMap2(Set<Integer> idSet){
		Map<String,Set<Integer>> platformMap = new HashMap<String,Set<Integer>>();
		StringBuffer sb = new StringBuffer();
		for(int monetid:idSet){
			sb.append(monetid);
			sb.append(",");
		}
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient167.execSQLQuery(
							"select platform,monetid from all_users", 
							//+"and monetid not in(7504,81849,2235,540,71859,83054,551,668,69837,62392)",
							dbArgs);
			while(rs.next()) {
				String platform = rs.getString("platform");
				int monetid = rs.getInt("monetid");
				if(idSet.contains(monetid)){
					if(platformMap.containsKey(platform)){
						Set<Integer> set = platformMap.get(platform);
						set.add(monetid);
						platformMap.put(platform, set);
					}else{
						Set<Integer> set = new HashSet<Integer>();
						set.add(monetid);
						platformMap.put(platform, set);
					}
				}
			}
		} catch (Exception e) {
			logger.error("getNewUserFromDB ", e);
		}
		return platformMap;
	}
	
	
	/**
	 * 获得所给monetid集合,在这段时间内的充值金额
	 * @param idSet
	 * @param fTime
	 * @param tTime
	 * @return
	 */
	public static int getTopUpAmount(Set<Integer> idSet,Date fTime,Date tTime){
		int result = 0;
		StringBuffer sb = new StringBuffer();
		for(int monetid:idSet){
			sb.append(monetid);
			sb.append(",");
		}
		try {
			DBResultSet rs = null;
			Object[] dbArgs = new Object[] {fTime,tTime};
//			System.out.println(fTime);
//			System.out.println(tTime);
			rs = dbClient1286.execSQLQuery(
						"select sum(amount)/100 as amount from callbackstc where date>=? and date<? and monet_id in ("+sb.substring(0,sb.length()-1)+")",
						dbArgs);
			if(rs.next()){
				result = rs.getInt("amount");
			}else{
				result = 0;
			}
		} catch (Exception e) {
			logger.error("getTopUpAmount" , e);
		}
		return result;
	}
	
	
	
	public static int getTopUpAmount2(Set<Integer> idSet,Date fTime,Date tTime){
		int result = 0;
		StringBuffer sb = new StringBuffer();
		for(int monetid:idSet){
			sb.append(monetid);
			sb.append(",");
		}
		try {
			DBResultSet rs = null;
			Object[] dbArgs = new Object[] {fTime,tTime};
//			System.out.println(fTime);
//			System.out.println(tTime);
			rs = dbClient1286.execSQLQuery("select sum(amount)/100 as amount,monet_id monetid from callbackstc where date>=? and date<? group by monet_id",dbArgs);
			while (rs.next()){
				int monetid = rs.getInt("monetid");
				if(idSet.contains(monetid)){
					result += rs.getInt("amount");
				}
			}
		} catch (Exception e) {
			logger.error("getTopUpAmount" , e);
		}
		return result;
	}
	
	/**
	 * 获得所给monetid集合,在这段时间内的充值 人数
	 * @param idSet
	 * @param fTime
	 * @param tTime
	 * @return
	 */
	public static Set<Integer> getTopUpUser(Set<Integer> idSet,Date fTime,Date tTime){
		Set<Integer> set = new HashSet<Integer>();
		StringBuffer sb = new StringBuffer();
		for(int monetid:idSet){
			sb.append(monetid);
			sb.append(",");
		}
		try {
			DBResultSet rs = null;
			Object[] dbArgs = new Object[] {fTime,tTime};
//			System.out.println(fTime);
//			System.out.println(tTime);
			rs = dbClient1286.execSQLQuery(
						"select distinct monet_id from callbackstc where date>=? and date<? and monet_id in ("+sb.substring(0,sb.length()-1)+")",
						dbArgs);
			while(rs.next()){
				set.add(rs.getInt("monet_id"));
			}
		} catch (Exception e) {
			logger.error("getTopUpUser" , e);
		}
		return set;
	}
	
	/**
	 * 获得给定用户集在这段时间内注册的用户集合
	 * @param idSet
	 * @param fTime
	 * @param tTime
	 * @return
	 */
	public static Set<Integer> getNewUserSet(Set<Integer> idSet,Date fTime,Date tTime){
		Set<Integer> set = new HashSet<Integer>();
		try {
			DBResultSet rs = null;
			Object[] dbArgs = new Object[] {fTime,tTime};
			rs = dbClient.execSQLQuery(
						"select monetid from fisher where newuserflag>=? and newuserflag<?",
						dbArgs);
//			int c1 = 0;
//			int c2 = 0;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
//				c1 ++;
				if(idSet.contains(monetid)){
					set.add(rs.getInt("monetid"));
//					c2++;
				}
			}
//			System.out.println(c1+"///"+c2+"---------------------------------------");
		} catch (Exception e) {
			logger.error("getNewUserSet" , e);
		}
		return set;
	}
	
	
	public static Set<Integer> getNewUserSet2(Set<Integer> idSet,Date fTime,Date tTime){
		Set<Integer> set = new HashSet<Integer>();
		try {
			DBResultSet rs = null;
			Object[] dbArgs = new Object[] {fTime,tTime};
			rs = dbClient.execSQLQuery(
						"select monetid from fisher where newuserflag>=? and newuserflag<?",
						dbArgs);
//			int c1 = 0;
//			int c2 = 0;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
//				c1 ++;
				if(idSet.contains(monetid)){
					set.add(rs.getInt("monetid"));
//					c2++;
				}
			}
//			System.out.println(c1+"///"+c2+"---------------------------------------");
		} catch (Exception e) {
			logger.error("getNewUserSet" , e);
		}
		return idSet;
	}
	
	/**
	 * 获得给定用户集不在这段时间内注册的用户集合
	 * @param idSet
	 * @param fTime
	 * @param tTime
	 * @return
	 */
	public static Set<Integer> getOldUserSet(Set<Integer> idSet,Date fTime,Date tTime){
		Set<Integer> result =  new HashSet<Integer>();
		for(int monetid:idSet){
			result.add(monetid);
		}
		try {
			DBResultSet rs = null;
			Object[] dbArgs = new Object[] {fTime,tTime};
			rs = dbClient.execSQLQuery(
						"select monetid from fisher where newuserflag>=? and newuserflag<?",
						dbArgs);
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(result.contains(monetid)){
					result.remove(rs.getInt("monetid"));
				}
			}
		} catch (Exception e) {
			logger.error("getNewUserSet" , e);
		}
		return result;
	}
	
	
	public static Set<Integer> getOldUserSet2(Set<Integer> idSet,Date fTime,Date tTime){
		Set<Integer> result =  new HashSet<Integer>();
		return result;
	}
	
	/**
	 * 根据给定的用户集合,查询在给定时间段和付费分段标准下的各付费段充值总额
	 * @param idSet
	 * @param fTime
	 * @param tTime
	 * @param high
	 * @param mid
	 * @return
	 */
	public static Map<String,Integer> getTopupLevelAmount(Set<Integer> idSet,Date fTime,Date tTime,int high,int mid){
		Map<String,Integer> levelMap = new HashMap<String,Integer>();
		StringBuffer sb = new StringBuffer();
		for(int monetid:idSet){
			sb.append(monetid);
			sb.append(",");
		}
		try {
			DBResultSet rs = null;
			Object[] dbArgs = new Object[] {fTime,tTime};
			rs = dbClient1286.execSQLQuery(
						"select monet_id,sum(amount)/100 as amount from callbackstc where date>=? and date<? and monet_id in ("+sb.substring(0,sb.length()-1)+") group by monet_id",
						dbArgs);
			while(rs.next()){
				int money = rs.getInt("amount");
				if(money>high){
					if(levelMap.containsKey("HighTopUp")){
						levelMap.put("HighTopUp", levelMap.get("HighTopUp")+money);
					}else{
						levelMap.put("HighTopUp", money);
					}
				}else if(money>mid){
					if(levelMap.containsKey("MidTopUp")){
						levelMap.put("MidTopUp", levelMap.get("MidTopUp")+money);
					}else{
						levelMap.put("MidTopUp", money);
					}
				}else{
					if(levelMap.containsKey("LowTopUp")){
						levelMap.put("LowTopUp", levelMap.get("LowTopUp")+money);
					}else{
						levelMap.put("LowTopUp", money);
					}
				}
			}
		} catch (Exception e) {
			logger.error("getTopUpAmount" , e);
		}
		return levelMap;
	}
	
	/**
	 * 根据给定的用户集合,查询在给定时间段和付费分段标准下的各付费段充值人数
	 * @param idSet
	 * @param fTime
	 * @param tTime
	 * @param high
	 * @param mid
	 * @return
	 */
	public static Map<String,Set<Integer>> getTopupLevelUser(Set<Integer> idSet,Date fTime,Date tTime,int high,int mid){
		Map<String,Set<Integer>> levelMap = new HashMap<String,Set<Integer>>();
		StringBuffer sb = new StringBuffer();
		for(int monetid:idSet){
			sb.append(monetid);
			sb.append(",");
		}
		try {
			DBResultSet rs = null;
			Object[] dbArgs = new Object[] {fTime,tTime};
			rs = dbClient1286.execSQLQuery(
						"select monet_id,sum(amount)/100 as amount from callbackstc where date>=? and date<? and monet_id in ("+sb.substring(0,sb.length()-1)+") group by monet_id",
						dbArgs);
			while(rs.next()){
				int money = rs.getInt("amount");
				int monetid = rs.getInt("monet_id");
				if(money>high){
					if(levelMap.containsKey("HighTopUp")){
						Set<Integer> set = levelMap.get("HighTopUp");
						set.add(monetid);
						levelMap.put("HighTopUp",set);
					}else{
						Set<Integer> set = new HashSet<Integer>();
						set.add(monetid);
						levelMap.put("HighTopUp", set);
					}
				}else if(money>mid){
					if(levelMap.containsKey("MidTopUp")){
						Set<Integer> set = levelMap.get("MidTopUp");
						set.add(monetid);
						levelMap.put("MidTopUp",set);
					}else{
						Set<Integer> set = new HashSet<Integer>();
						set.add(monetid);
						levelMap.put("MidTopUp", set);
					}
				}else{
					if(levelMap.containsKey("LowTopUp")){
						Set<Integer> set = levelMap.get("LowTopUp");
						set.add(monetid);
						levelMap.put("LowTopUp",set);
					}else{
						Set<Integer> set = new HashSet<Integer>();
						set.add(monetid);
						levelMap.put("LowTopUp", set);
					}
				}
			}
		} catch (Exception e) {
			logger.error("getTopUpAmount" , e);
		}
		return levelMap;
	}
	
	/**
	 * 获取两个集合的相同部分
	 * @param set1
	 * @param set2
	 * @return
	 */
	public static Set<Integer> getCommonSet(Set<Integer> set1,Set<Integer> set2){
		Set<Integer> result = new HashSet<Integer>();
		for(int monetid:set1){
			if(set2.contains(monetid)){
				result.add(monetid);
			}
		}
		return result;
	}
	
	///////////////////////////test
	public static void test(Date fTime,Date tTime,int high,int mid){
//		Logger logger = Logger.getLogger(BusinessModel.class);
//		Date fTime = new Date();
//		Date tTime = new Date();
//		if(args.length == 2){
//			fTime = MyUtil.StringToDate(args[0]);
//			tTime = MyUtil.StringToDate(args[1]);
//		}
//		if(args.length == 0){
//			Calendar fromWhen = Calendar.getInstance();
//			fromWhen.setTime(fTime);
//			GregorianCalendar gc = new GregorianCalendar(fromWhen
//					.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen
//					.get(Calendar.DAY_OF_MONTH));
//			tTime = (Date) gc.getTime();
//			gc.add(Calendar.DATE, -1);
//			fTime = (Date) gc.getTime();
//		}
		
		/////////////
		System.out.println(fTime);
		System.out.println(tTime);
		Date dateBefore = new Date(fTime.getTime()-(tTime.getTime()-fTime.getTime())) ;
		System.out.println(dateBefore);
		
		DecimalFormat df = new DecimalFormat(".00");
		Set<Integer> idSet = getLoginIDSet(fTime, tTime);
		//temp
//		Set<Integer> newUserAllSet = getNewUserSet(idSet, fTime, tTime);
//		System.out.println("////////////"+newUserAllSet.size());
		System.out.println("期间活跃用户数为 ="+idSet.size());
		Set<Integer> lastIdSet = getLoginIDSet(dateBefore, fTime);
		
		Map<String,Set<Integer>> platformMap = getPlatformActiveUserMap2(idSet);
		platformMap.put("All", idSet);
		Map<String,Set<Integer>> lastPlatformMap = getPlatformActiveUserMap2(lastIdSet);
		lastPlatformMap.put("All", lastIdSet);
		
		Set<String> platformSet = platformMap.keySet();
		System.out.println("手机平台:活跃用户数:新用户数:旧用户数:付费用户数:总收入:ARPU:ARPPU:渗透率:" +
				"高付费段收入:高付费段人数:高付费段人均收入:中付费段收入:中付费段人数:中付费段人均收入:低付费段收入:低付费段人数:低付费段人均收入:" +
				"上期活跃用户数:上期新用户:上期旧用户:上期活跃在这周有停留:上期新用户这周有停留:上期旧用户这周有停留:" +
				"上期活跃用户停留率:上期新用户停留率:上期旧用户停留率:新用户充值金额:新用户充值人数:新用户渗透率:旧用户充值金额:旧用户充值人数:旧用户渗透率"
		);
		for(String platform:platformSet){
			//这期活跃用户(机型)
			Set<Integer> activeUserSet = platformMap.get(platform);
			int activeUser = activeUserSet.size();
			//上期活跃用户(机型)
			Set<Integer> lastActiveUserSet = lastPlatformMap.get(platform);
			//充值金额
			int topupAmount = getTopUpAmount2(activeUserSet, fTime, tTime);
			//充值用户
			Set<Integer> topupUserSet = getTopUpUser(activeUserSet, fTime, tTime);
			int topupUser = topupUserSet.size();
			//这期新用户
			Set<Integer> newUserSet = getNewUserSet(activeUserSet, fTime, tTime);
			int newUser = newUserSet.size();
			int topupAmountNewUser = getTopUpAmount2(newUserSet, fTime, tTime);
			Set<Integer> topupUserNewSet = getTopUpUser(newUserSet, fTime, tTime);
			int topupNewUser = topupUserNewSet.size();
			
			//这期旧用户
			Set<Integer> oldUserSet = getOldUserSet(activeUserSet, fTime, tTime);
			int oldUser = oldUserSet.size();
			int topupAmountOldUser = getTopUpAmount2(oldUserSet, fTime, tTime);
			Set<Integer> topupUserOldSet = getTopUpUser(oldUserSet, fTime, tTime);
			int topupOldUser = topupUserOldSet.size();
			
			
			//分付费段充值金额
			Map<String,Integer> topUpAmountMap = getTopupLevelAmount(topupUserSet, fTime, tTime, high, mid);
			//分付费段充值人数
			Map<String,Set<Integer>> topUpUserMap = getTopupLevelUser(topupUserSet, fTime, tTime, high, mid);
			//上期新用户
			Set<Integer> lastNewUserSet = getNewUserSet(lastActiveUserSet, dateBefore, fTime);
			int lastNewUser = lastNewUserSet.size();
			//这期旧用户
			Set<Integer> lastOldUserSet = getOldUserSet(lastActiveUserSet, dateBefore, fTime);
			int lastOldUser = lastOldUserSet.size();
			//上期活跃这期停留
			Set<Integer> lastActiveStaySet = getCommonSet(lastActiveUserSet, activeUserSet);
			int lastActiveStay = lastActiveStaySet.size();
			//上期活跃新用户这期停留
			Set<Integer> lastActiveNewStaySet = getCommonSet(lastNewUserSet, activeUserSet);
			int lastActiveNewStay = lastActiveNewStaySet.size();
			//上期活跃旧用户这期停留
			Set<Integer> lastActiveOldStaySet = getCommonSet(lastOldUserSet, activeUserSet);
			int lastActiveOldStay = lastActiveOldStaySet.size();
			
			System.out.println(platform+":"+activeUser+":"+newUser+":"+oldUser+":"+topupUser+":"+topupAmount+":"+df.format(topupAmount*1.0/activeUser)+":"+df.format(topupAmount*1.0/topupUser)
					+":"+df.format(topupUser*1.0/activeUser)+":"+topUpAmountMap.get("HighTopUp")+":"+topUpUserMap.get("HighTopUp").size()+":"+df.format(topUpAmountMap.get("HighTopUp")*1.0/topUpUserMap.get("HighTopUp").size())
					+":"+topUpAmountMap.get("MidTopUp")+":"+topUpUserMap.get("MidTopUp").size()+":"+df.format(topUpAmountMap.get("MidTopUp")*1.0/topUpUserMap.get("MidTopUp").size())
					+":"+topUpAmountMap.get("LowTopUp")+":"+topUpUserMap.get("LowTopUp").size()+":"+df.format(topUpAmountMap.get("LowTopUp")*1.0/topUpUserMap.get("LowTopUp").size())
					+":"+lastActiveUserSet.size()+":"+lastNewUser+":"+lastOldUser+":"+lastActiveStay+":"+lastActiveNewStay+":"+lastActiveOldStay+":"+df.format(lastActiveStay*1.0/lastActiveUserSet.size())
					+":"+df.format(lastActiveNewStay*1.0/lastNewUser)+":"+df.format(lastActiveOldStay*1.0/lastOldUser)
					+":"+topupAmountNewUser+":"+topupNewUser+":"+df.format(topupNewUser*1.0/newUser)
					+":"+topupAmountOldUser+":"+topupOldUser+":"+df.format(topupOldUser*1.0/oldUser)
			);
		}
	}
}
