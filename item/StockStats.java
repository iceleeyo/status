package item;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import util.NewLogUtil;

public class StockStats {
	static Logger logger = Logger.getLogger(StockStats.class);
	static String dbWriteUrl = null;
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient1286 = null;
	static MoDBRW dbClient167 = null;
	static java.util.Map<String,Integer> map = new java.util.HashMap<String,Integer>();
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			dbWriteUrl = serverConf.getString("dbWriteUrl");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");

			dbClient1286 = new MoDBRW(dbWriteUrl,dbDriver);
			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
//	static public void deleteStocks(Date fromTime){
//		Object[] dbArgs2 = new Object[] {fromTime};
//		try {
//			dbClient167.execSQLUpdate("delete from stocks where stime=?", dbArgs2);
//		} catch (Exception e) {
//			logger.error("deleteSelling fromTime=" + fromTime, e);
//		}
//	}
	
	static public void StartStats(Date fTime) {
		Date fromTime = new Date();
		Calendar fromWhen = Calendar.getInstance();
		fromWhen.setTime(fromTime);
		GregorianCalendar gc = new GregorianCalendar(fromWhen.get(Calendar.YEAR), fromWhen.get(Calendar.MONTH), fromWhen.get(Calendar.DAY_OF_MONTH));
		gc.add(Calendar.DATE,-1);
		fromTime = (Date) gc.getTime();
		Map<String,String> nameMap = NewLogUtil.getNameMap();
		if(fromTime.equals(fTime)){
//			deleteStocks(fromTime);
			
			Map<Integer,Integer> weaponTotalMap = new HashMap<Integer,Integer>();
			Map<Integer,Integer> crewTotalMap = new HashMap<Integer,Integer>();
			Map<Integer,Integer> cardTotalMap = new HashMap<Integer,Integer>();
			Map<Integer,Integer> speakerTotalMap = new HashMap<Integer,Integer>();

			weaponTotalMap = getWeaponStocksTotal();
			crewTotalMap = getCrewStocksTotal();
			cardTotalMap = getCardStocksTotal();
			speakerTotalMap = getSpeakerStocksTotal();
			
			Set<Integer> tempSet = new HashSet<Integer>();
			
			tempSet = weaponTotalMap.keySet();
			Map<Integer,Map<String,Integer>> weaponMaxMap = new HashMap<Integer,Map<String,Integer>>();
			weaponMaxMap = getWeaponStocksMax(tempSet);
			
			tempSet = crewTotalMap.keySet();
			Map<Integer,Map<String,Integer>> crewMaxMap = new HashMap<Integer,Map<String,Integer>>();
			crewMaxMap = getCrewStocksMax(tempSet);
			
			Map<Integer,Map<String,Integer>> cardMaxMap = new HashMap<Integer,Map<String,Integer>>();
			cardMaxMap = getCardStocksMax();
			
			Map<Integer,Map<String,Integer>> speakerMaxMap = new HashMap<Integer,Map<String,Integer>>();
			speakerMaxMap = getSpeakerStocksMax();
			
			//weapon
			tempSet = weaponTotalMap.keySet();
			for(int i:tempSet){
				Map<String,Integer> ownerMap = weaponMaxMap.get(i);
				if(ownerMap!=null){
					Set<String> ownerSet = ownerMap.keySet();
					if(ownerSet!=null){
						for(String owner:ownerSet){
							setStocks(nameMap.get("Weapon"+i), weaponTotalMap.get(i), ownerMap.get(owner), owner, fromTime);
						}
					}
				}
			}
			
			//crew
			tempSet = crewTotalMap.keySet();
			for(int i:tempSet){
				Map<String,Integer> ownerMap = crewMaxMap.get(i);
				if(ownerMap!=null){
					Set<String> ownerSet = ownerMap.keySet();
					if(ownerSet!=null){
						for(String owner:ownerSet){
							setStocks(nameMap.get("Crew"+i), crewTotalMap.get(i), ownerMap.get(owner), owner, fromTime);
						}
					}
				}
			}
			
			//Card
			tempSet = cardTotalMap.keySet();
			for(int i:tempSet){
				Map<String,Integer> ownerMap = cardMaxMap.get(i);
				if(ownerMap!=null){
					Set<String> ownerSet = ownerMap.keySet();
					if(ownerSet!=null){
						for(String owner:ownerSet){
							setStocks(nameMap.get("FishTackle"+i), cardTotalMap.get(i), ownerMap.get(owner), owner, fromTime);
						}
					}
				}
			}
			
			//Speaker
			tempSet = speakerTotalMap.keySet();
			for(int i:tempSet){
				Map<String,Integer> ownerMap = speakerMaxMap.get(i);
				if(ownerMap!=null){
					Set<String> ownerSet = ownerMap.keySet();
					if(ownerSet!=null){
						for(String owner:ownerSet){
							setStocks("Speaker"+i, speakerTotalMap.get(i), ownerMap.get(owner), owner, fromTime);
						}
					}
				}
			}
		}
	}
	
	static public Map<Integer,Map<String,Integer>> getWeaponStocksMax(Set<Integer> set) {
		Map<Integer,Map<String,Integer>> typeMap = new HashMap<Integer,Map<String,Integer>>();
		try {
			for(int i:set){
				Object[] dbArgs = new Object[] {i};
				DBResultSet rs = dbClient1286.execSQLQuery(
								"select top 1 ownerid,count(*) as amount from weapon where typeid=? and status=0 group by ownerid order by amount desc",dbArgs);
				while(rs.next()) {
					Map<String,Integer> owneridMap = new HashMap<String,Integer>();
					owneridMap.put(rs.getInt("ownerid").toString(),rs.getInt("amount"));
					typeMap.put(i,owneridMap);
				} 
			}
		} catch (Exception e) {
			logger.error("getWeaponStocksMax", e);
		}
		return typeMap;
	}
	
	static public Map<Integer,Integer> getWeaponStocksTotal() {
		Map<Integer,Integer> typeMap = new HashMap<Integer,Integer>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient1286.execSQLQuery(
							"select typeid,count(*) as amount from weapon where status=0 group by typeid",dbArgs);
			while(rs.next()) {
				typeMap.put(rs.getInt("typeid"),rs.getInt("amount"));
			} 
		} catch (Exception e) {
			logger.error("getWeaponStocksMax", e);
		}
		return typeMap;
	}
	
	static public Map<Integer,Map<String,Integer>> getCrewStocksMax(Set<Integer> set) {
		Map<Integer,Map<String,Integer>> typeMap = new HashMap<Integer,Map<String,Integer>>();
		try {
			for(int i:set){
				Object[] dbArgs = new Object[] {i};
				DBResultSet rs = dbClient1286.execSQLQuery(
								"select top 1 ownerid,count(*) as amount from crew where typeid=? and status=0  group by ownerid order by amount desc",dbArgs);
				while(rs.next()) {
					Map<String,Integer> owneridMap = new HashMap<String,Integer>();
					owneridMap.put(rs.getInt("ownerid").toString(),rs.getInt("amount"));
					typeMap.put(i,owneridMap);
				} 
			}
		} catch (Exception e) {
			logger.error("getCrewStocksMax", e);
		}
		return typeMap;
	}
	
	static public Map<Integer,Integer> getCrewStocksTotal() {
		Map<Integer,Integer> typeMap = new HashMap<Integer,Integer>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient1286.execSQLQuery(
							"select typeid,count(*) as amount from crew where status=0  group by typeid",dbArgs);
			while(rs.next()) {
				typeMap.put(rs.getInt("typeid"),rs.getInt("amount"));
			} 
		} catch (Exception e) {
			logger.error("getCrewStocksTotal", e);
		}
		return typeMap;
	}
	
	static public Map<Integer,Map<String,Integer>> getSpeakerStocksMax() {
		Map<Integer,Map<String,Integer>> typeMap = new HashMap<Integer,Map<String,Integer>>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient1286.execSQLQuery(
							"select speaker.type as type,speaker.monetId,speaker.count as amount from speaker ,(select max(count) num,type from speaker group by type) c where speaker.type = c.type and speaker.count=c.num",dbArgs);
			while(rs.next()) {
				Map<String,Integer> owneridMap = new HashMap<String,Integer>();
				owneridMap.put(rs.getInt("monetId").toString(),rs.getInt("amount"));
				typeMap.put(rs.getInt("type"),owneridMap);
			} 
		} catch (Exception e) {
			logger.error("getSpeakerStocksMax", e);
		}
		return typeMap;
	}
	
	static public Map<Integer,Integer> getSpeakerStocksTotal() {
		Map<Integer,Integer> typeMap = new HashMap<Integer,Integer>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient1286.execSQLQuery(
							"select type,count(*) as amount from speaker group by type",dbArgs);
			while(rs.next()) {
				typeMap.put(rs.getInt("type"),rs.getInt("amount"));
			} 
		} catch (Exception e) {
			logger.error("getSpeakerStocksTotal", e);
		}
		return typeMap;
	}
	
	static public Map<Integer,Map<String,Integer>> getCardStocksMax() {
		Map<Integer,Map<String,Integer>> typeMap = new HashMap<Integer,Map<String,Integer>>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient1286.execSQLQuery(
							"select a.typeid,a.monetid,a.amount from(select top 100000 typeid,monetid,count(*) as amount from fishtackle where status=0 group by typeid,monetid order by amount desc)a inner   join (select typeid,max(amount) as amount from (select top 100000 typeid,monetid,count(*) as amount from fishtackle where status=0 group by typeid,monetid order by amount desc)a group by typeid )b on a.typeid=b.typeid and a.amount=b.amount order by typeid ",dbArgs);
			while(rs.next()) {
				Map<String,Integer> owneridMap = new HashMap<String,Integer>();
				owneridMap.put(rs.getInt("monetid").toString(),rs.getInt("amount"));
				typeMap.put(rs.getInt("typeid"),owneridMap);
			} 
		} catch (Exception e) {
			logger.error("getCardStocksMax", e);
		}
		return typeMap;
	}
	
	static public Map<Integer,Integer> getCardStocksTotal() {
		Map<Integer,Integer> typeMap = new HashMap<Integer,Integer>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient1286.execSQLQuery(
							"select typeid,count(*) as amount from fishtackle group by typeid",dbArgs);
			while(rs.next()) {
				typeMap.put(rs.getInt("type"),rs.getInt("amount"));
			} 
		} catch (Exception e) {
			logger.error("getCardStocksTotal", e);
		}
		return typeMap;
	}
	
	static public void setStocks(String item,int stocks,int maxS,String monetid,Date fromTime) {
		try {
			Object[] dbArgs = new Object[] {item,stocks,maxS,monetid,fromTime};
			dbClient167.execSQLUpdate(
							"insert into stocks(item,amount,maxS,monetid,stime)values (?,?,?,?,?)",
							dbArgs);
			map.put(item,stocks);
		} catch (Exception e) {
			logger.error("setStocks with item=" + item + " stocks=" + stocks
					+ " fromTime=" + fromTime, e);
		}
		System.out.println("item="+item+",amount="+stocks+",maxS="+maxS+",monetid="+monetid);
	}
	
}
