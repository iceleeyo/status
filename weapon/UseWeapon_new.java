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

public class UseWeapon_new {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	private static Map<String,Integer> getMapFromCmd(String cmd){
		BufferedReader br = null;
		try{
			Map<String,Integer> resultMap = new HashMap<String, Integer>();
				String []cmdArray = new String[]{ "/bin/sh", "-c", cmd};
				
				System.out.println("startProcess:"+cmd);
				Process process = Runtime.getRuntime().exec(cmdArray);
				
				InputStream  in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();  
	            while (result != null) {
	            	String a [] = result.split(",");
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
	
	public static void WeaponInAndOut(Date date) {
		
		String fileUrl = "/home/mozat/morange/oapro/OceanAgelogs/trace.log." + sdf.format(date) ;
		String temp = "*|  awk -F ':' '{arr[$6]+=1}END{for(key in arr) print key \",\" arr[key]}'" ;
		String temp2 = "*|  awk -F ':' '{print $6}' |grep -E '2|3'|  awk '{arr[$1]+=1}END{for(key in arr) print key \",\" arr[key]}'" ;
		
		String out_attackFisher_cmd = "grep action=AttackFisher " + fileUrl + temp ;
		Map<String,Integer> attackFisherMap  = getMapFromCmd(out_attackFisher_cmd);
		String out_attackPersonalMonster_cmd = "grep action=attackPersonalMonster " + fileUrl + temp2 ;
		Map<String,Integer> attackPersonMap  = getMapFromCmd(out_attackPersonalMonster_cmd);
		
		Map<String,Integer> inWeaponByC  = getInWeaponByCredit(date);
		
		String in_gold_cmd = "grep action=BuyWeaponByGold " + fileUrl + temp;
		Map<String,Integer> inWeaponByG  = getMapFromCmd(in_gold_cmd);
		
		String in_mission_cmd = "grep action=getMissionReward " + fileUrl +"* | grep RewardItem=/3"+ temp;
		Map<String,Integer> inWeaponByM  = getMapFromCmd(in_mission_cmd);
		
		String in_FreeLucky_cmd = "grep action=LuckyDrawFree " + fileUrl + "*| grep Item=/Weapon:" + temp;
		Map<String,Integer> inWeaponByF  = getMapFromCmd(in_FreeLucky_cmd);
		
		String in_creditLucky_cmd = "grep action=LuckyDrawByCredit " + fileUrl + "*| grep Item=/Weapon:" + temp;
		Map<String,Integer> inWeaponByB  = getMapFromCmd(in_creditLucky_cmd);
		
		String out_tribeMonter_cmd = "grep action=attackTribeMonster /home/mozat/morange/oapro/OceanAgelogs/action.log."+sdf.format(date) +"*  | awk -F '[=,]' '{arr[$13]+=1}END{for(key in arr) print key \",\" arr[key]}' " ;
		Map<String,Integer> outWeaponByT  = getMapFromCmd(out_tribeMonter_cmd);
		
		insertDB(attackFisherMap,date,"out","attackFisher");
		insertDB(attackPersonMap,date,"out","attackPerson");
		insertDB(inWeaponByC,date,"in","Credit");
		insertDB(inWeaponByG,date,"in","Gold");
		
		insertDB(inWeaponByM,date,"in","Mission");
		insertDB(inWeaponByF,date,"in","FreeLucky");
		insertDB(inWeaponByB,date,"in","CreditLucky");
		insertDB(outWeaponByT,date,"out","tribe");
		
		System.out.println(attackFisherMap);
		System.out.println(attackPersonMap);
		System.out.println(inWeaponByC);
		System.out.println(inWeaponByG);
		System.out.println(inWeaponByM);
		System.out.println(inWeaponByF);
		System.out.println(inWeaponByB);
		System.out.println(outWeaponByT);
		
	}
	
	private static Map<String,Integer> getInWeaponByCredit(Date date){
		String fileUrl = "/home/mozat/morange/oapro/OceanAgelogs/trace.log." + sdf.format(date) ;
		String in_credit_cmd = "grep action=BuyWeaponByC "+fileUrl+"* | awk -F '[:/]' '{arr[$15]+=1}END{for(key in arr) print key \",\" arr[key]}' " ;
		Map<String,String> weaponinfo  = getWeaponInfoFromShopitem();
		Map<String,Integer> map = getMapFromCmd(in_credit_cmd);
		Map<String,Integer> map2 = new HashMap<String, Integer>();
		for(String s : map.keySet()){
			String str = weaponinfo.get(s);
			String[] a = str.split("_");
			int amount = Integer.parseInt(a[0]);
			map2.put(a[1], amount*map.get(s));
		}
		return map2;
	}
	
	private static Map<String,String> getWeaponInfoFromShopitem(){
		String sql = "select itemid,itemtypeid,itemAmount from shopitem where itemtype='weapon'";
		DBResultSet ds = ConfigUtil.myConfigDb.execSQL(sql, new Object[]{});
		Map<String,String> map = new HashMap<String, String>();
		while(ds.next()){
			try {
				int itemid = ds.getInt("itemid");
				int amount = ds.getInt("itemAmount");
				int itemtypeid = ds.getInt("itemtypeid");
				map.put(itemid+"", amount + "_" + itemtypeid);
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
