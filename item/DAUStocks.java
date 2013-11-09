package item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import util.NewLogUtil;

public class DAUStocks {
	static Logger logger = Logger.getLogger(DAUStocks.class);
	static String dbWriteUrl = null;
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient1286 = null;
	static MoDBRW dbClient167 = null;
	static String idlog = null;
	static java.util.Map<String,Integer> map = new java.util.HashMap<String,Integer>();
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			dbWriteUrl = serverConf.getString("dbWriteUrl");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			idlog = serverConf.getString("idlog");

			dbClient1286 = new MoDBRW(dbWriteUrl,dbDriver);
			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}

	public static void StartStats(Date date){
		//get that day users list
		String path = idlog+MyUtil.DateToString(date)+"_newoa.txt";
		ArrayList<Integer> userList = MyUtil.getIdList(path);
		//get monetids string
		StringBuffer sb = new StringBuffer();
		for(int monetid:userList){
			sb.append(monetid);
			sb.append(",");
		}
		if(sb!=null&&sb.length()>0){
			String monetids = sb.substring(0,sb.length()-1);
			
			Map<Integer,Map<Integer,Integer>> weaponTotalMap = new HashMap<Integer,Map<Integer,Integer>>();
			Map<Integer,Map<Integer,Integer>> crewTotalMap = new HashMap<Integer,Map<Integer,Integer>>();
			Map<Integer,Map<Integer,Integer>> speakerTotalMap = new HashMap<Integer,Map<Integer,Integer>>();
			
			//stats weapon stocks
			weaponTotalMap = getWeaponStocksTotal(monetids);
			//stats crew stocks
			crewTotalMap = getCrewStocksTotal(monetids);
			//stats speaker stocks
			speakerTotalMap = getSpeakerStocksTotal(monetids);
			
			//get shop item name
			Map<String,String> nameMap = NewLogUtil.getNameMap();
			
			//output
			//weapon
			Set<Integer> weaponSet = weaponTotalMap.keySet();
			for(int typeid:weaponSet){
				Map<Integer,Integer> owneridMap = weaponTotalMap.get(typeid);
				Set<Integer> owneridSet = owneridMap.keySet();
				List<Integer> weaponList = new ArrayList<Integer>();
				int total = 0;
				int max = 0;
				int maxOwnerid = 0;
				int mid = 0;
				for(int ownerid:owneridSet){
					if(nameMap.containsKey("Weapon"+typeid)){
						weaponList.add(owneridMap.get(ownerid));
						total += owneridMap.get(ownerid);
						if(owneridMap.get(ownerid)>max){
							max = owneridMap.get(ownerid);
							maxOwnerid = ownerid;
						}
//						System.out.println("Name="+nameMap.get("Weapon"+typeid)+",Ownerid="+ownerid+",Amount="+owneridMap.get(ownerid));
					}else{
						System.out.println("Cannot find name.");
					}
				}
				if(weaponList!=null&&weaponList.size()>0){
					Collections.sort(weaponList);
					if(weaponList.size()%2==0){
						mid = (weaponList.get(weaponList.size()/2)+weaponList.get(weaponList.size()/2-1))/2;
					}else{
						mid = weaponList.get(weaponList.size()/2);
					}
					System.out.println("Name="+nameMap.get("Weapon"+typeid)+",Ownerid="+maxOwnerid+",Total="+total+",Average="+total*1.0/weaponList.size()+",Mid="+mid+",Max="+max+",Min="+weaponList.get(0));
					setStocks(nameMap.get("Weapon"+typeid), total, total*1.0/weaponList.size(), mid, max, weaponList.get(0), maxOwnerid, date);
				}
			}
			//crew
			Set<Integer> crewSet = crewTotalMap.keySet();
			for(int typeid:crewSet){
				Map<Integer,Integer> owneridMap = crewTotalMap.get(typeid);
				Set<Integer> owneridSet = owneridMap.keySet();
				List<Integer> crewList = new ArrayList<Integer>();
				int total = 0;
				int max = 0;
				int maxOwnerid = 0;
				int mid = 0;
				for(int ownerid:owneridSet){
					if(nameMap.containsKey("Crew"+typeid)){
						crewList.add(owneridMap.get(ownerid));
						total += owneridMap.get(ownerid);
						if(owneridMap.get(ownerid)>max){
							max = owneridMap.get(ownerid);
							maxOwnerid = ownerid;
						}
//						System.out.println("Name="+nameMap.get("Crew"+typeid)+",Ownerid="+ownerid+",Amount="+owneridMap.get(ownerid));
					}else{
						System.out.println("Cannot find name.");
					}
				}
				if(crewList!=null&&crewList.size()>0){
					Collections.sort(crewList);
					if(crewList.size()%2==0){
						mid = (crewList.get(crewList.size()/2)+crewList.get(crewList.size()/2-1))/2;
					}else{
						mid = crewList.get(crewList.size()/2);
					}
					System.out.println("Name="+nameMap.get("Crew"+typeid)+",Ownerid="+maxOwnerid+",Total="+total+",Average="+total*1.0/crewList.size()+",Mid="+mid+",Max="+max+",Min="+crewList.get(0));
					setStocks(nameMap.get("Crew"+typeid), total, total*1.0/crewList.size(), mid, max, crewList.get(0), maxOwnerid, date);
				}
			}
			
			//speaker
			Set<Integer> speakerSet = speakerTotalMap.keySet();
			for(int typeid:speakerSet){
				Map<Integer,Integer> owneridMap = speakerTotalMap.get(typeid);
				Set<Integer> owneridSet = owneridMap.keySet();
				List<Integer> speakerList = new ArrayList<Integer>();
				int total = 0;
				int max = 0;
				int maxOwnerid = 0;
				int mid = 0;
				for(int ownerid:owneridSet){
					if(nameMap.containsKey("Speaker"+typeid)){
						speakerList.add(owneridMap.get(ownerid));
						total += owneridMap.get(ownerid);
						if(owneridMap.get(ownerid)>max){
							max = owneridMap.get(ownerid);
							maxOwnerid = ownerid;
						}
//						System.out.println("Name="+nameMap.get("Speaker"+typeid)+",Ownerid="+ownerid+",Amount="+owneridMap.get(ownerid));
					}else{
						System.out.println("Cannot find name.");
					}
				}
				if(speakerList!=null&&speakerList.size()>0){
					Collections.sort(speakerList);
					if(speakerList.size()%2==0){
						mid = (speakerList.get(speakerList.size()/2)+speakerList.get(speakerList.size()/2-1))/2;
					}else{
						mid = speakerList.get(speakerList.size()/2);
					}
					System.out.println("Name="+nameMap.get("Speaker"+typeid)+",Ownerid="+maxOwnerid+",Total="+total+",Average="+total*1.0/speakerList.size()+",Mid="+mid+",Max="+max+",Min="+speakerList.get(0));
					setStocks(nameMap.get("Speaker"+typeid), total, total*1.0/speakerList.size(), mid, max, speakerList.get(0), maxOwnerid, date);
				}
			}
		}
	}
	
	//get weapon stocks total
	static public Map<Integer,Map<Integer,Integer>> getWeaponStocksTotal(String monetids) {
		Map<Integer,Map<Integer,Integer>> typeMap = new HashMap<Integer,Map<Integer,Integer>>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient1286.execSQLQuery(
							"select typeid,ownerid,count(*) as amount from weapon where status=0 and ownerid in ("+monetids+") group by typeid,ownerid",dbArgs);
			while(rs.next()) {
				int typeid = rs.getInt("typeid");
				int ownerid = rs.getInt("ownerid");
				int amount = rs.getInt("amount");
				if(typeMap.containsKey(typeid)){
					Map<Integer,Integer> ownerMap = typeMap.get(typeid);
					ownerMap.put(ownerid,amount);
					typeMap.put(typeid,ownerMap);
				}else{
					Map<Integer,Integer> ownerMap = new HashMap<Integer,Integer>();
					ownerMap.put(ownerid,amount);
					typeMap.put(typeid,ownerMap);
				}
			} 
		} catch (Exception e) {
			logger.error("getWeaponStocksMax", e);
		}
		return typeMap;
	}
	
	//get crew stocks total
	static public Map<Integer,Map<Integer,Integer>> getCrewStocksTotal(String monetids) {
		Map<Integer,Map<Integer,Integer>> typeMap = new HashMap<Integer,Map<Integer,Integer>>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient1286.execSQLQuery(
							"select typeid,ownerid,count(*) as amount from crew where status=0 and ownerid in ("+monetids+") group by typeid,ownerid",dbArgs);
			while(rs.next()) {
				int typeid = rs.getInt("typeid");
				int ownerid = rs.getInt("ownerid");
				int amount = rs.getInt("amount");
				if(typeMap.containsKey(typeid)){
					Map<Integer,Integer> ownerMap = typeMap.get(typeid);
					ownerMap.put(ownerid,amount);
					typeMap.put(typeid,ownerMap);
				}else{
					Map<Integer,Integer> ownerMap = new HashMap<Integer,Integer>();
					ownerMap.put(ownerid,amount);
					typeMap.put(typeid,ownerMap);
				}
			} 
		} catch (Exception e) {
			logger.error("getCrewStocksTotal", e);
		}
		return typeMap;
	}
	
	//get speaker stocks total
	static public Map<Integer,Map<Integer,Integer>> getSpeakerStocksTotal(String monetids) {
		Map<Integer,Map<Integer,Integer>> typeMap = new HashMap<Integer,Map<Integer,Integer>>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient1286.execSQLQuery(
							"select type,monetid,count from speaker where monetid in ("+monetids+")",dbArgs);
			while(rs.next()) {
				int typeid = rs.getInt("type");
				int ownerid = rs.getInt("monetid");
				int amount = rs.getInt("count");
				if(typeMap.containsKey(typeid)){
					Map<Integer,Integer> ownerMap = typeMap.get(typeid);
					ownerMap.put(ownerid,amount);
					typeMap.put(typeid,ownerMap);
				}else{
					Map<Integer,Integer> ownerMap = new HashMap<Integer,Integer>();
					ownerMap.put(ownerid,amount);
					typeMap.put(typeid,ownerMap);
				}
			} 
		} catch (Exception e) {
			logger.error("getSpeakerStocksTotal", e);
		}
		return typeMap;
	}
	
	static public void setStocks(String item,int stocks,double average,int mid,int maxS,int min,int monetid,Date fromTime) {
		try {
			Object[] dbArgs = new Object[] {item,stocks,average,mid,maxS,min,monetid,fromTime};
			dbClient167.execSQLUpdate(
							"insert into daustocks(item,amount,averageS,midS,maxS,minS,monetid,stime)values (?,?,?,?,?,?,?,?)",
							dbArgs);
			map.put(item,stocks);
		} catch (Exception e) {
			logger.error("setStocks with item=" + item + " stocks=" + stocks
					+ " fromTime=" + fromTime, e);
		}
		System.out.println("item="+item+",amount="+stocks+",maxS="+maxS+",monetid="+monetid);
	}
}
