package weapon;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import both.basic.ConfigUtil;
import util.DBResultSet;

public class UseWeapon {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static Map<String,Integer> getOutWeaponByAttack(Date date,String key){
		Date beginDate = date ;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,+1);//把日期往后增加一天.整数往后推,负数往前移动
		Date endDate =calendar.getTime(); //这个时间就是日期往后推一天的结果
		Map<String,Integer> map = new HashMap<String,Integer>();
		String sql = "select weaponid ,count(weaponid) num from ( " + 
					"select substring(msg,charindex('n:',msg)+2,2) weaponid from user_event "+ 
					"where msg like ? "+
					"and server_date between  ? and ? "+ 
					") a group by weaponid ";
		DBResultSet ds = ConfigUtil.myServerDb.execSQL(sql, new Object[]{key,beginDate,endDate});
		while(ds.next()){
			try {
				map.put(ds.getString("weaponid"), ds.getInt("num"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	private static Map<String,Integer> getInWeaponByGold(Date date){
		Date begin = date ;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,+1);//把日期往后增加一天.整数往后推,负数往前移动
		Date end =calendar.getTime(); //这个时间就是日期往后推一天的结果
		Map<String,Integer> map = new HashMap<String,Integer>();
		String sql = "select weaponid ,count(weaponid) num from ( " + 
					"select substring(msg,charindex(':',msg)+1,2) weaponid from user_event "+ 
					"where msg like 'action=buyWeaponByG%' "+
					"and server_date between ? and ? "+ 
					") a group by weaponid ";
		DBResultSet ds = ConfigUtil.myServerDb.execSQL(sql, new Object[]{begin,end});
		while(ds.next()){
			try {
				map.put(ds.getString("weaponid"), ds.getInt("num"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	private static Map<String,Integer> getInWeaponByMission(Date date){
		Date begin = date ;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,+1);//把日期往后增加一天.整数往后推,负数往前移动
		Date end =calendar.getTime(); //这个时间就是日期往后推一天的结果
		Map<String,Integer> map = new HashMap<String,Integer>();
		String sql = "select weaponid ,count(weaponid) num from ( " + 
				"select  SUBSTRING(msg, CHARINDEX(':', msg)+1, CHARINDEX('_/', msg)-CHARINDEX(':', msg)-4) weaponid  from user_event "+ 
				"WHERE msg LIKE 'action=getMissionReward%' "+
				"and msg like '%RewardItem=/3%' "+
				"and server_date between ? and ? "+ 
				") a group by weaponid ";
		DBResultSet ds = ConfigUtil.myServerDb.execSQL(sql, new Object[]{begin,end});
		while(ds.next()){
			try {
				map.put(ds.getString("weaponid"), ds.getInt("num"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	private static Map<String,Integer> getInWeaponByLuckyFree(Date date){
		Date begin = date ;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,+1);//把日期往后增加一天.整数往后推,负数往前移动
		Date end =calendar.getTime(); //这个时间就是日期往后推一天的结果
		Map<String,Integer> map = new HashMap<String,Integer>();
		String sql = "select weaponid ,count(weaponid) num from ( " + 
				"SELECT  SUBSTRING(msg, CHARINDEX(':', msg)+1, len(msg)-CHARINDEX(':', msg)-3) weaponid  from " +
				"user_event " + 
				"WHERE msg like 'action=luckyDrawF%' " + 
				"and msg like '%weapon%' "+
				"and server_date between ? and ? "+ 
				") a group by weaponid ";
		DBResultSet ds = ConfigUtil.myServerDb.execSQL(sql, new Object[]{begin,end});
		while(ds.next()){
			try {
				map.put(ds.getString("weaponid"), ds.getInt("num"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
			
	private static Map<String,Integer> getOutWeaponByTribe(Date stime){
		BufferedReader br = null;
		String date = sdf.format(stime);
		try{
			Map<String,Integer> resultMap = new HashMap<String, Integer>();
			String fileUrl = null;
			fileUrl = "/home/mozat/morange/oapro/OceanAgelogs/action.log.";
			
			
				//grep action=attackTribeMonster  action.log.2013-09-26* | awk -F '[=,]' '{arr[$13]+=1}END{for(key in arr)print key,arr[key]}'
				String []cmdArray = new String[]{ "/bin/sh", "-c", "grep action=attackTribeMonster "+fileUrl+date+"*"+" |awk -F '[=,]' '{arr[$13]+=1}END{for(key in arr)print key,arr[key]}'"};
				
				System.out.println("---------------------------startProcess:"+"grep action=attackTribeMonster "+fileUrl+date+"*"+"|awk -F '[=,]' '{arr[$13]+=1}END{for(key in arr)print key,arr[key]}'");
				Process process = Runtime.getRuntime().exec(cmdArray);
				
				//process.waitFor();  
				InputStream  in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();  
	            while (result != null) {
	            	String a [] = result.split(" ");
	            	if(a.length==2){
	            		resultMap.put(a[0], new Integer(a[1]));
	            	}
	                result = read.readLine();
	            }
			return resultMap;
		}catch (Exception e){
			e.printStackTrace();
			return new HashMap<String, Integer>();
		}finally{
			try {
				if(br!=null)br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}
	private static Map<String,Integer> getInWeaponByLuckyByCredit(Date date){
		Date begin = date ;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,+1);//把日期往后增加一天.整数往后推,负数往前移动
		Date end =calendar.getTime(); //这个时间就是日期往后推一天的结果
		Map<String,Integer> map = new HashMap<String,Integer>();
		String sql = "select weaponid ,count(weaponid) num from ( " + 
				"SELECT  SUBSTRING(msg, CHARINDEX(':', msg)+1, len(msg)-CHARINDEX(':', msg)-13) weaponid  from " +
				"user_event " + 
				"WHERE msg like 'action=luckyDrawB%' " + 
				"and msg like '%weapon%' "+
				"and server_date between ? and ? "+ 
				") a group by weaponid ";
		DBResultSet ds = ConfigUtil.myServerDb.execSQL(sql, new Object[]{begin,end});
		while(ds.next()){
			try {
				map.put(ds.getString("weaponid"), ds.getInt("num"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	public static void WeaponInAndOut(Date date) {
		Map<String,Integer> attackFisherMap  = getOutWeaponByAttack(date,"action=attackF%");
		Map<String,Integer> attackPersonMap  = getOutWeaponByAttack(date,"action=attackP%");
		Map<String,Integer> inWeaponByC  = getInWeaponByCredit(date);
		Map<String,Integer> inWeaponByG  = getInWeaponByGold(date);
		Map<String,Integer> inWeaponByM  = getInWeaponByMission(date);
		Map<String,Integer> inWeaponByF  = getInWeaponByLuckyFree(date);
		Map<String,Integer> inWeaponByB  = getInWeaponByLuckyByCredit(date);
		Map<String,Integer> outWeaponByT  = getOutWeaponByTribe(date);
		insertDB(attackFisherMap,date,"out","attackFisher");
		insertDB(attackPersonMap,date,"out","attackPerson");
		insertDB(inWeaponByC,date,"in","Credit");
		insertDB(inWeaponByG,date,"in","Gold");
		
		insertDB(inWeaponByM,date,"in","Mission");
		insertDB(inWeaponByF,date,"in","FreeLucky");
		insertDB(inWeaponByB,date,"in","CreditLucky");
		insertDB(outWeaponByT,date,"out","tribe");
		
		/*System.out.println(attackFisherMap);
		System.out.println(attackPersonMap);
		System.out.println(inWeaponByC);
		System.out.println(inWeaponByG);
		System.out.println(inWeaponByM);
		System.out.println(inWeaponByF);
		System.out.println(inWeaponByB);
		System.out.println(outWeaponByT);*/
		
	}
	
	private static Map<String,Integer> getInWeaponByCredit(Date date){
		Date begin = date ;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,+1);//把日期往后增加一天.整数往后推,负数往前移动
		Date end =calendar.getTime(); //这个时间就是日期往后推一天的结果
		Map<String,Integer> map = new HashMap<String,Integer>();
		Map<String,Integer> weaponinfo  = getWeaponInfoFromShopitem();
		String sql  = "select substring(weaponid,1,charindex(':',weaponid)-1) weaponid ,itemid from " +  
					    "( "+ 
						"select  substring(msg,charindex(':',msg)+1,3) weaponid,  " +  
						"		  substring(msg,charindex('.',msg)+3,charindex('/,',msg)-charindex('.',msg)-3) itemid " +
						"from user_event "+
						"where msg like 'action=buyWeaponByC%' " + 
						"and server_date between ? and ? " +
						") a " ;
		DBResultSet ds = ConfigUtil.myServerDb.execSQL(sql, new Object[]{begin,end});
		while(ds.next()){
			try {
				String weaponid = ds.getString("weaponid");
				String itemid = ds.getString("itemid");
				int amount = weaponinfo.get(itemid);
				if(map.containsKey(weaponid)){
					map.put(weaponid,map.get(weaponid)+amount);
				}else{
					map.put(weaponid,amount);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	private static Map<String,Integer> getWeaponInfoFromShopitem(){
		String sql = "select itemid,itemAmount from shopitem where itemtype='weapon'";
		DBResultSet ds = ConfigUtil.myConfigDb.execSQL(sql, new Object[]{});
		Map<String,Integer> map = new HashMap<String, Integer>();
		while(ds.next()){
			try {
				int itemid = ds.getInt("itemid");
				int amount = ds.getInt("itemAmount");
				map.put(itemid+"", amount);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	private static void insertDB(Map<String,Integer> map , Date dataTime ,String type ,String from) {
		String s1 = "INSERT INTO weaponStatus([type],[from],[wdate],[weaponid],[amount]) VALUES(?,?,?,?,?)";
		for(String s: map.keySet()){
			int weaponid = Integer.parseInt(s);
			int amount = map.get(s);
			try {
				ConfigUtil.myPortalDb.execSQLUpdate(s1, new Object[]{type,from,dataTime,weaponid,amount});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		try {
			Date date = sdf.parse(args[0]);
			WeaponInAndOut(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
