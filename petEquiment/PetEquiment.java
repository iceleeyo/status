package petEquiment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import both.basic.ConfigUtil;
import util.DBResultSet;

public class PetEquiment {
	/**
	 * 取得server1db的装备表的现有的数据，取monetid和装备id
	 * @return 每一个minetid对应的装备的数量
	 */
	private static Map<Integer, Map<Integer, Integer>> getEquiment() {
		Map<Integer, Map<Integer, Integer>> equimentMap = new HashMap<Integer, Map<Integer, Integer>>();
		DBResultSet ds = ConfigUtil.myServerDb.execSQL(
				"select monet_id,type_id from Equipment where expire_date > ? and type_id < 5 and status = 1  ", new Object[] { new Date()});
		try {
			while (ds.next()) {
				int monetid = ds.getInt("monet_id");
				if (equimentMap.containsKey(monetid)) {
					Map<Integer, Integer> resMap = equimentMap.get(monetid);
					int type_id = ds.getInt("type_id");
					if (resMap.containsKey(type_id)) {
						int count = resMap.get(type_id);
						resMap.put(type_id, count + 1);
					} else {
						resMap.put(type_id, 1);
					}
					equimentMap.put(ds.getInt("monet_id"), resMap);
				} else {
					Map<Integer, Integer> resMap = new HashMap<Integer, Integer>();
					int type_id = ds.getInt("type_id");
					resMap.put(type_id, 1);
					equimentMap.put(ds.getInt("monet_id"), resMap);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ds = ConfigUtil.myServerDb.execSQL(
				"select monet_id,type_id from Equipment where expire_date > ?  and type_id >= 5 ", new Object[] { new Date()});
		try {
			while (ds.next()) {
				int monetid = ds.getInt("monet_id");
				if (equimentMap.containsKey(monetid)) {
					Map<Integer, Integer> resMap = equimentMap.get(monetid);
					int type_id = ds.getInt("type_id");
					if (resMap.containsKey(type_id)) {
						int count = resMap.get(type_id);
						resMap.put(type_id, count + 1);
					} else {
						resMap.put(type_id, 1);
					}
					equimentMap.put(ds.getInt("monet_id"), resMap);
				} else {
					Map<Integer, Integer> resMap = new HashMap<Integer, Integer>();
					int type_id = ds.getInt("type_id");
					resMap.put(type_id, 1);
					equimentMap.put(ds.getInt("monet_id"), resMap);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return equimentMap;
	}
	
	private static Map<Integer, Map<Integer, Integer>> getActionMonetidEquiment() {
		
		Date endDate=new Date();//取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(endDate);
		calendar.add(Calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
		Date beginDate =calendar.getTime(); //这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String edate = formatter.format(endDate);
		String bdate = formatter.format(beginDate);
		
		Map<Integer, Map<Integer, Integer>> equimentMap = new HashMap<Integer, Map<Integer, Integer>>();
		DBResultSet ds = ConfigUtil.myServerDb.execSQL(
				"select monet_id,type_id from Equipment where expire_date > ? and type_id < 5 and status = 1 and  monet_id in ("
				+ "select distinct monetid   from user_event  where server_date > ? and server_date < ? )", new Object[] {new Date(),bdate,edate});
		try {
			while (ds.next()) {
				int monetid = ds.getInt("monet_id");
				if (equimentMap.containsKey(monetid)) {
					Map<Integer, Integer> resMap = equimentMap.get(monetid);
					int type_id = ds.getInt("type_id");
					if (resMap.containsKey(type_id)) {
						int count = resMap.get(type_id);
						resMap.put(type_id, count + 1);
					} else {
						resMap.put(type_id, 1);
					}
					equimentMap.put(ds.getInt("monet_id"), resMap);
				} else {
					Map<Integer, Integer> resMap = new HashMap<Integer, Integer>();
					int type_id = ds.getInt("type_id");
					resMap.put(type_id, 1);
					equimentMap.put(ds.getInt("monet_id"), resMap);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ds = ConfigUtil.myServerDb.execSQL(
				"select monet_id,type_id from Equipment where expire_date > ? and type_id >= 5 and  monet_id in ("
						+ "select distinct monetid   from user_event  where server_date > ? and server_date < ? )", new Object[] {new Date(),bdate,edate});
		try {
			while (ds.next()) {
				int monetid = ds.getInt("monet_id");
				if (equimentMap.containsKey(monetid)) {
					Map<Integer, Integer> resMap = equimentMap.get(monetid);
					int type_id = ds.getInt("type_id");
					if (resMap.containsKey(type_id)) {
						int count = resMap.get(type_id);
						resMap.put(type_id, count + 1);
					} else {
						resMap.put(type_id, 1);
					}
					equimentMap.put(ds.getInt("monet_id"), resMap);
				} else {
					Map<Integer, Integer> resMap = new HashMap<Integer, Integer>();
					int type_id = ds.getInt("type_id");
					resMap.put(type_id, 1);
					equimentMap.put(ds.getInt("monet_id"), resMap);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return equimentMap;
	}
	
	/**
	 * 取得server1confi库的装备类型的信息，取装备id和装备类型id
	 * @return 每一个装备id对应的类型id
	 */
	private static Map<Integer, Integer> getEquimentType() {
		DBResultSet ds = ConfigUtil.myConfigDb.execSQL(
				"select id,type from EquipmentType", new Object[] {});
		Map<Integer, Integer> equimentTypeMap = new HashMap<Integer, Integer>();
		try {
			while (ds.next()) {
				equimentTypeMap.put(ds.getInt("id"), ds.getInt("type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return equimentTypeMap;
	}
	/**
	 * 取得serverdb的每一个monetid对应的宠物等级
	 * @return monetid --> 宠物等级
	 */
	private static Map<Integer, Integer> getPetLevel() {
		DBResultSet ds = ConfigUtil.myServerDb.execSQL(
				"select ownerid,petLevel from pet", new Object[] {});
		Map<Integer, Integer> petLevelMap = new HashMap<Integer, Integer>();
		try {
			while (ds.next()) {
				petLevelMap.put(ds.getInt("ownerid"), ds.getInt("petLevel"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return petLevelMap;
	}
	/**
	 * 取得每一个monetid的对应的装备类型的数量
	 * @return
	 */
	private static Map<Integer, Map<Integer, Integer>> getEveryMonetIdEquimentTypeCount(Map<Integer, Map<Integer, Integer>> equimentMap) {
		Map<Integer, Map<Integer, Integer>> resultMap = new HashMap<Integer, Map<Integer, Integer>>();
		Map<Integer, Integer> equimentTypeMap = getEquimentType();

		for (int monetid : equimentMap.keySet()) {
			Map<Integer, Integer> typeMap = new HashMap<Integer, Integer>();
			Map<Integer, Integer> mMap = equimentMap.get(monetid);
			for (int id : mMap.keySet()) {
				int type = equimentTypeMap.get(id);
				if (typeMap.containsKey(type)) {
					int count = typeMap.get(type);
					typeMap.put(type, count + mMap.get(id));
				} else {
					typeMap.put(type, mMap.get(id));
				}
			}
			resultMap.put(monetid, typeMap);
		}
		return resultMap;
	}
	/**
	 * 取得每一个级别对应类型的数量
	 * @return
	 */
	private static Map<Integer, Map<Integer, Map<String, Integer>>> getLevelTypeCount(Map<Integer, Map<Integer, Integer>> equimentMap) {
		Map<Integer, Map<Integer, Integer>> monetidTypeCount = getEveryMonetIdEquimentTypeCount(equimentMap);
		Map<Integer, Integer> petLevelMap = getPetLevel();
		Map<Integer, Map<Integer, Map<String, Integer>>> levelTypeCountMap = new HashMap<Integer, Map<Integer, Map<String, Integer>>>();
		for (int monetid : monetidTypeCount.keySet()) {
			if (petLevelMap.get(monetid) != null) {
				int level = petLevelMap.get(monetid);
				Map<Integer, Map<String, Integer>> typeCountByLevel = null;
				if (levelTypeCountMap.containsKey(level)) {
					typeCountByLevel = levelTypeCountMap.get(level);
					Map<Integer, Integer> typeCountByMonetid = monetidTypeCount
							.get(monetid);
					for (int type : typeCountByMonetid.keySet()) {
						if (typeCountByLevel.containsKey(type)) {
							Map<String, Integer> pnMap = typeCountByLevel
									.get(type);
							pnMap.put("person", pnMap.get("person") + 1);
							pnMap.put("number", pnMap.get("number")
									+ typeCountByMonetid.get(type));
							typeCountByLevel.put(type, pnMap);
						} else {
							Map<String, Integer> pnMap = new HashMap<String, Integer>();
							pnMap.put("person", 1);
							pnMap.put("number", typeCountByMonetid.get(type));
							typeCountByLevel.put(type, pnMap);
						}
					}
				} else {
					typeCountByLevel = new HashMap<Integer, Map<String, Integer>>();
					Map<Integer, Integer> typeCountByMonetid = monetidTypeCount
							.get(monetid);
					for (int type : typeCountByMonetid.keySet()) {
						Map<String, Integer> pnMap = new HashMap<String, Integer>();
						pnMap.put("person", 1);
						pnMap.put("number", typeCountByMonetid.get(type));
						typeCountByLevel.put(type, pnMap);
					}
				}
				levelTypeCountMap.put(level, typeCountByLevel);
			}
		}
		return levelTypeCountMap;
	}

	public static void insertEquipment(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
		Date beginDate =calendar.getTime(); //这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String edate = formatter.format(beginDate);
		
		Map<Integer, Map<Integer, Integer>> actionMonetidEquimentMap = getActionMonetidEquiment();
		Map<Integer, Map<Integer, Map<String, Integer>>> levelTypeCountMap1 = getLevelTypeCount(actionMonetidEquimentMap);
		insertinto(levelTypeCountMap1,edate,1);
		
		Map<Integer, Map<Integer, Integer>> allEuimentMap = getEquiment();
		Map<Integer, Map<Integer, Map<String, Integer>>> levelTypeCountMap2 = getLevelTypeCount(allEuimentMap);
		
		insertinto(levelTypeCountMap2,edate,0);
		
	}
	
	private static  void insertinto(Map<Integer, Map<Integer, Map<String, Integer>>> insertMap,String date,int isAction){
		
		for (int level : insertMap.keySet()) {
			 Map<Integer, Map<String, Integer>> typeCount = insertMap.get(level);
			for (int type : typeCount.keySet()) {
				ConfigUtil.myPortalDb.execSQLUpdate(
								"insert into EquipmentInventory(petLevel,equimentType,equimentCount,equimentPerson,rdate,isAction)values(?,?,?,?,?,?)",
								new Object[] { level, type,typeCount.get(type).get("number"),typeCount.get(type).get("person"), date,isAction});
				System.out.println("level:" + level + "|type:" + type
						+ "|count:" + typeCount.get(type).get("number") + "|person:" + typeCount.get(type).get("person") + "|isAction:" + isAction);
			}
		}
	}
	public static void main(String[] args) {
		insertEquipment(new Date());
	}
}
