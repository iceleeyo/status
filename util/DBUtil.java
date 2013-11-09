package util;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class DBUtil {
static final Logger logger = Logger.getLogger(DBUtil.class);
	
	static String dbWriteUrl = null;
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient = null;
	static MoDBRW dbClient1283 = null;
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration(
					"system.properties"));
			Configuration serverConf = settings.subset("service");

			dbWriteUrl = serverConf.getString("dbWriteUrl");
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			dbClient = new MoDBRW(dbReadUrls,dbDriver);
			dbClient1283 = new MoDBRW(dbWriteUrl,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	public static ArrayList<Integer> getNewUserList(Date fromTime,Date toTime){
		ArrayList<Integer> list = new ArrayList<Integer>();
		try {
			Object[] dbArgs = new Object[] {fromTime,toTime};
			DBResultSet rs = dbClient1283.execSQLQuery(
							"select monetid from fisher where newuserflag>=? and newuserflag<?",
							dbArgs);
			while(rs.next()) {
				list.add(rs.getInt("monetid"));
			}
		} catch (Exception e) {
			logger.error("getNewUserList ", e);
		}
		return list;
	}
}
