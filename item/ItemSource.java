package item;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.DBResultSet;
import util.MoDBRW;

public class ItemSource {
	Connection cn = null;
	CallableStatement cmd = null;
	Statement stmt = null;
	ResultSet rs = null;

	static Logger logger = Logger.getLogger(ItemSource.class);
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
	
	public static void StartStatsFromDB(Date date,Date date2){
		Map<String,String> itemName = new HashMap<String,String>();
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient.execSQLQuery(
							"select itemtype,itemtypeid,itemcredittext from shopitem where itemamount=1",
							dbArgs);
			while(rs.next()) {
				int typeid = rs.getInt("itemtypeid");
				String itemtype = rs.getString("itemtype");
				String itemdesc = rs.getString("itemcredittext");
				itemName.put(itemtype+typeid, itemdesc);
			}
		} catch (Exception e) {
		}
		
		try {
			Object[] dbArgs = new Object[] {date,date2};
			DBResultSet rs = dbClient.execSQLQuery(
							"select typeid,source,count(*) as amount from weapon where createDate>=? and createDate<? group by typeid,source",
							dbArgs);
			logger.info(":"+"typeid"+":"+"source"+":"+"amount");
			while(rs.next()) {
				int typeid = rs.getInt("typeid");
				String source = rs.getString("source");
				int amount = rs.getInt("amount");
				SetItemSource(source, itemName.get("Weapon"+typeid),"Weapon", amount, date);
			}
		} catch (Exception e) {
		}
		
		try {
			Object[] dbArgs = new Object[] {date,date2};
			DBResultSet rs = dbClient.execSQLQuery(
							"select typeid,source,count(*) as amount from crew where createDate>=? and createDate<? group by typeid,source",
							dbArgs);
			logger.info(":"+"typeid"+":"+"source"+":"+"amount");
			while(rs.next()) {
				int typeid = rs.getInt("typeid");
				String source = rs.getString("source");
				int amount = rs.getInt("amount");
				SetItemSource(source, itemName.get("Crew"+typeid),"Crew", amount, date);
			}
		} catch (Exception e) {
		}
	}
	
	static public void SetItemSource(String type,String item,String itemType,int amount,Date fromTime) {
		try {

			Object[] dbArgs = new Object[] {type,item,itemType,amount,fromTime};
			dbClient167.execSQLUpdate(
							"insert into itemsource(gettype,item,itemType,amount,fdate)values (?,?,?,?,?)",
							dbArgs);
			
		} catch (Exception e) {
			logger.error("SetFreeGift with type=" + type + " item=" + item
					+ " fromTime=" + fromTime, e);
		}
		logger.info("Type="+type+",item="+item+",itemType="+itemType+",amount=amount");
	}
}
