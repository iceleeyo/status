package user;

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

public class ShipyardLevelWPD {
	static Logger logger = Logger.getLogger(ShipyardLevelWPD.class);
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
	
	public static Map<Integer,Integer> getMonetidShipyardLevelMap(){
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient.execSQLQuery(
							"select monetid,max(buildinglevel) as level from building where buildingtype=1 group by monetid",
							dbArgs);
			while(rs.next()) {
				map.put(rs.getInt("monetid"),rs.getInt("level"));
			}
		} catch (Exception e) {
			logger.error("getMonetidShipyardLevelMap ", e);
		}
		return map;
	}
	
	public static Map<Integer,Integer> getMonetidWarehouseMap(){
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient.execSQLQuery(
							"select monetid,max(buildinglevel) as level from building where buildingtype=3 group by monetid",
							dbArgs);
			while(rs.next()) {
				map.put(rs.getInt("monetid"),rs.getInt("level"));
			}
		} catch (Exception e) {
			logger.error("getMonetidShipyardLevelMap ", e);
		}
		return map;
	}
	
	public static Map<Integer,Integer> getMonetidPetMap(){
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient.execSQLQuery(
							"select ownerid,petlevel from pet",
							dbArgs);
			while(rs.next()) {
				map.put(rs.getInt("ownerid"),rs.getInt("petlevel"));
			}
		} catch (Exception e) {
			logger.error("getMonetidPetMap ", e);
		}
		return map;
	}
	
	public static Map<Integer,Integer> getMonetidDefenseSystemMap(){
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient.execSQLQuery(
							"select monetid,defense_level from new_defense_system",
							dbArgs);
			while(rs.next()) {
				map.put(rs.getInt("monetid"),rs.getInt("defense_level"));
			}
		} catch (Exception e) {
			logger.error("getMonetidDefenseSystemMap ", e);
		}
		return map;
	}
	
	public static void StartStats(Date date){
		Map<Integer,Integer> shipyardLevelMap = getMonetidShipyardLevelMap();
		Map<Integer,Integer> warehouseLevelMap = getMonetidWarehouseMap();
		Map<Integer,Integer> petLevelMap = getMonetidPetMap();
		Map<Integer,Integer> defenseLevelMap = getMonetidDefenseSystemMap();
		
		//warehouse
		Map<Integer,List<Integer>> warehouseMap = new HashMap<Integer,List<Integer>>();
		Set<Integer> monetidSet = warehouseLevelMap.keySet();
		for(int monetid:monetidSet){
			int shipyardlevel = 1;
			int warehouselevel = warehouseLevelMap.get(monetid);
			if(shipyardLevelMap.containsKey(monetid)){
				shipyardlevel = shipyardLevelMap.get(monetid);
			}
			if(warehouseMap.containsKey(shipyardlevel)){
				warehouseMap.get(shipyardlevel).add(warehouselevel);
			}else{
				List<Integer> list = new ArrayList<Integer>();
				list.add(warehouselevel);
				warehouseMap.put(shipyardlevel,list);
			}
		}
		Set<Integer> shipyardSet = warehouseMap.keySet();
		for(int level:shipyardSet){
			List<Integer> list = warehouseMap.get(level);
			Collections.sort(list);
			int max = list.get(list.size()-1);
			double avg = MyUtil.getAvg(list);
			double mid = MyUtil.getMid(list);
			//System.out.println("ShipyardLevel="+level+",MaxWarehouseLevel="+max+",AvgWarehouseLevel="+avg+",MidWarehouseLevel="+mid);
			setShipyardLevelWPD(level, "Warehouse", max, avg, mid, date);
		}
		
		System.out.println("------------------------------------------------------------");
		
		//pet
		Map<Integer,List<Integer>> petMap = new HashMap<Integer,List<Integer>>();
		monetidSet = petLevelMap.keySet();
		for(int monetid:monetidSet){
			int shipyardlevel = 1;
			int petlevel = petLevelMap.get(monetid);
			if(shipyardLevelMap.containsKey(monetid)){
				shipyardlevel = shipyardLevelMap.get(monetid);
			}
			if(petMap.containsKey(shipyardlevel)){
				petMap.get(shipyardlevel).add(petlevel);
			}else{
				List<Integer> list = new ArrayList<Integer>();
				list.add(petlevel);
				petMap.put(shipyardlevel,list);
			}
		}
		shipyardSet = petMap.keySet();
		for(int level:shipyardSet){
			List<Integer> list = petMap.get(level);
			Collections.sort(list);
			int max = list.get(list.size()-1);
			double avg = MyUtil.getAvg(list);
			double mid = MyUtil.getMid(list);
			//System.out.println("ShipyardLevel="+level+",MaxPetLevel="+max+",AvgPetLevel="+avg+",MidPetLevel="+mid);
			setShipyardLevelWPD(level, "Pet", max, avg, mid, date);
		}
		
		System.out.println("------------------------------------------------------------");
		
		//defense
		Map<Integer,List<Integer>> defenseMap = new HashMap<Integer,List<Integer>>();
		monetidSet = defenseLevelMap.keySet();
		for(int monetid:monetidSet){
			int shipyardlevel = 1;
			int defenselevel = defenseLevelMap.get(monetid);
			if(shipyardLevelMap.containsKey(monetid)){
				shipyardlevel = shipyardLevelMap.get(monetid);
			}
			if(defenseMap.containsKey(shipyardlevel)){
				defenseMap.get(shipyardlevel).add(defenselevel);
			}else{
				List<Integer> list = new ArrayList<Integer>();
				list.add(defenselevel);
				defenseMap.put(shipyardlevel,list);
			}
		}
		shipyardSet = defenseMap.keySet();
		for(int level:shipyardSet){
			List<Integer> list = defenseMap.get(level);
			Collections.sort(list);
			int max = list.get(list.size()-1);
			double avg = MyUtil.getAvg(list);
			double mid = MyUtil.getMid(list);
			//System.out.println("ShipyardLevel="+level+",MaxDefenseLevel="+max+",AvgDefenseLevel="+avg+",MidDefenseLevel="+mid);
			setShipyardLevelWPD(level, "DefenseSystem", max, avg, mid, date);
		}
	}
	
	public static void setShipyardLevelWPD(int shipyardlevel,String type,int max,double avg,double mid,Date date){
		try {
			Object[] dbArgs = new Object[] {shipyardlevel,type,max,avg,mid,date};
			dbClient167.execSQLUpdate(
							"insert into shipyardLevelWPD(shipyardlevel,type,maxLevel,avgLevel,midLevel,sdate) values(?,?,?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("setShipyardLevelWPD ", e);
		}
	}
}
