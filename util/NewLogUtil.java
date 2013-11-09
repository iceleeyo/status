package util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class NewLogUtil {
	static final Logger logger = Logger.getLogger(NewLogUtil.class);
	static String dbWriteUrl = null;
	static String dbConfigUrl = null;
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient = null;
	static MoDBRW dbClient_config = null;
	static MoDBRW dbClient167 = null;
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			dbWriteUrl = serverConf.getString("dbWriteUrl");
			dbConfigUrl = serverConf.getString("dbConfigUrl");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			dbClient = new MoDBRW(dbWriteUrl,dbDriver);
			dbClient_config = new MoDBRW(dbConfigUrl,dbDriver);
			dbClient167 = new MoDBRW(dbReadUrls,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	static public String getFileMonetId(String temp) {
		String id = "";
		if(temp.contains("monetId=")){
			int start = 0;
			int end = 0;
			if(temp.indexOf("monetId=") != -1){
				start = temp.indexOf("monetId=") + 8;
				id = temp.substring(start);
				if(id.indexOf(",") != -1){
					end = id.indexOf(",");
					id = id.substring(0,end);
				}
			}
		}
		return id;
	}
	
	static public String getItemName(String temp) {
		String id = "";
		String type = "";
		if(temp.contains("item=")){
			int start = 0;
			int end = 0;
			if(temp.indexOf("item=") != -1){
				start = temp.indexOf("item=") + 5;
				id = temp.substring(start);
				if(id.indexOf(",") != -1){
					end = id.indexOf(",");
					id = id.substring(0,end);
				}
			}
		}
		if(temp.contains("itemType=")){
			int start = 0;
			int end = 0;
			if(temp.indexOf("itemType=") != -1){
				start = temp.indexOf("itemType=") + 9;
				type = temp.substring(start);
				if(type.indexOf(",") != -1){
					end = type.indexOf(",");
					type = type.substring(0,end);
				}
			}
		}
//		if(type.equals("Weapon")||type.equals("Crew")){
			return type+id;
//		}
//		return id;
	}
	
	static public String getItemNameId(String temp) {
		String id = "";
		if(temp.contains("item=")){
			int start = 0;
			int end = 0;
			if(temp.indexOf("item=") != -1){
				start = temp.indexOf("item=") + 5;
				id = temp.substring(start);
				if(id.indexOf(",") != -1){
					end = id.indexOf(",");
					id = id.substring(0,end);
				}
			}
		}
		return id;
	}
	
	static public String getDefenseNameTraceLog(String temp) {
		String name = "";
		if(temp.contains("DefenseWeapon=")){
			int start = 0;
			int end = 0;
			if(temp.indexOf("DefenseWeapon=") != -1){
				start = temp.indexOf("DefenseWeapon=") + "DefenseWeapon=/".length();
				name = temp.substring(start);
				if(name.indexOf("/") != -1){
					end = name.indexOf("/");
					name = name.substring(0,end);
					if(name.contains(":")){
						String[] names = name.split(":");
						if(names.length>=2){
							name = names[0]+names[1];	
						}
					}
				}
			}
		}
		else if(temp.contains("Card=")){
			int start = 0;
			int end = 0;
			if(temp.indexOf("Card=") != -1){
				start = temp.indexOf("Card=") + "Card=/".length();
				name = temp.substring(start);
				if(name.indexOf("/") != -1){
					end = name.indexOf("/");
					name = name.substring(0,end);
					if(name.contains(":")){
						String[] names = name.split(":");
						if(names.length>=2){
							name = "FishTackle"+names[1];	
						}
					}
				}
			}
		}
		return name;
	}
	
	public static Map<String,String> getNameMap(){
		Map<String,String> nameMap = new HashMap<String,String>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient_config.execSQLQuery(
							"select itemtype,itemtypeid,itemcredittext from shopitem where itemAmount=1",
							dbArgs);
			while(rs.next()){
				String type = rs.getString("itemtype");
				nameMap.put(type+rs.getInt("itemtypeid"),rs.getString("itemcredittext"));
//				System.out.println(type+rs.getInt("itemtypeid")+":"+rs.getString("itemcredittext"));
			}
		} catch (Exception e) {
			logger.error("getNameMap ", e);
		}
		return nameMap;
	}
	
	public static Map<String,String> getNameMapItemLog(){
		Map<String,String> nameMap = new HashMap<String,String>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient_config.execSQLQuery(
							"select itemid,itemcredittext from shopitem",
							dbArgs);
			while(rs.next()){
				nameMap.put(""+rs.getInt("itemid"),rs.getString("itemcredittext"));
			}
		} catch (Exception e) {
			logger.error("getNameMap ", e);
		}
		return nameMap;
	}
	
	static public String getPrice(String temp) {
		String id = "";
		if(temp.contains("price=")){
			int start = 0;
			int end = 0;
			if(temp.indexOf("price=") != -1){
				start = temp.indexOf("price=") + 6;
				id = temp.substring(start);
				if(id.indexOf(",") != -1){
					end = id.indexOf(",");
					id = id.substring(0,end);
				}
			}
		}
		return id;
	}
	
	public static int getStep(String log){
		String sign = "user get current step , step = ";
		log = log.substring(log.indexOf(sign)+sign.length());
		if(MyUtil.isMonetid(log.trim())){
			return Integer.parseInt(log.trim());
		}
		return 1;
	}
	
	static public int getInitOwner(String log) {
		if(log!=null){
			if(log.length()>0){
				if(log.indexOf("[") != -1){
					log = log.substring(0, log.indexOf("["));
					log = log.substring(log.lastIndexOf("-"),log.length());
					if(MyUtil.isMonetid(log.trim())){
						return Integer.parseInt(log.trim());
					}
				}
			}
		}
		return 0;
	}
	
	static public int getNewUserId(String log) {
		if(log!=null){
			if(log.length()>0){
				if(log.indexOf("new fisher") != -1){
					log = log.substring(0, log.indexOf("new fisher"));
					log = log.substring(log.lastIndexOf("-"),log.length());
					if(MyUtil.isMonetid(log.trim())){
						return Integer.parseInt(log.trim());
					}
				}
			}
		}
		return 0;
	}
	
	static public int getActionOwner(String log) {
		if(log!=null){
			if(log.length()>0){
				if(log.indexOf("user get") != -1){
					log = log.substring(0, log.indexOf("user get"));
					log = log.substring(log.lastIndexOf("-"),log.length());
					if(MyUtil.isMonetid(log.trim())){
						return Integer.parseInt(log.trim());
					}
				}
			}
		}
		return 0;
	}
	
}
