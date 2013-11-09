package item;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.DBResultSet;
import util.MoDBRW;

public class ShipDistribution {
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
	
	public static void StartStats(Date date){
		getShipTypeDistribution(date);
	}
	
	public static void getShipTypeDistribution(Date date){
		try {
			Object[] dbArgs = new Object[] {};
			DBResultSet rs = dbClient.execSQLQuery(
							"select type,level,count(*) as amount from (select s.ownerid,max(s.type) as type,(case when max(b.buildinglevel)>0 then max(b.buildinglevel) else 1 end) as level from ship s left join building b on s.ownerid=b.monetid and b.buildingtype=1 group by s.ownerid)a group by type,level order by type,level",
							dbArgs);
			while(rs.next()) {
				int type = rs.getInt("type");
				int level = rs.getInt("level");
				int amount = rs.getInt("amount");
				SetShipTypeDistribution(type, level, amount, date);
			}
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
	static public void SetShipTypeDistribution(int type,int level,int user,Date date) {
		try {
			Object[] dbArgs = new Object[] {type,level,user,date};
			dbClient167.execSQLUpdate(
							"insert into ShipTypeDistribution(shiptype,userlevel,amount,sdate)values (?,?,?,?)",
							dbArgs);
		} catch (Exception e) {
			logger.error("", e);
		}
		logger.info("SetShipTypeDistribution");
	}
}
