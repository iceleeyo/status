package both.basic;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.MoDBRW;

public class ConfigUtil {
	
static final Logger logger = Logger.getLogger(ConfigUtil.class);
	static String server1dbUrl = null;
	static String server2dbUrl = null;
	static String server3dbUrl = null;
	static String server4dbUrl = null;
	static String server5dbUrl = null;
	static String server6dbUrl = null;
	
	static String portal1dbUrl = null;
	static String portal2dbUrl = null;
	static String portal3dbUrl = null;
	static String portal4dbUrl = null;
	static String portal5dbUrl = null;
	static String portal6dbUrl = null;

	static String dbConfigUrl = null;
	static String testUrl = null;

	static String regdbUrl = null;
	static String dbDriver = null;
	
	public static MoDBRW server1db = null;
	public static MoDBRW server2db = null;
	public static MoDBRW server3db = null;
	public static MoDBRW server4db = null;
	public static MoDBRW server5db = null;
	public static MoDBRW server6db = null;
	
	public static MoDBRW portal1db = null;
	public static MoDBRW portal2db = null;
	public static MoDBRW portal3db = null;
	public static MoDBRW portal4db = null;
	public static MoDBRW portal5db = null;
	public static MoDBRW portal6db = null;
	
	public static MoDBRW myPortalDb = null;
	public static MoDBRW myServerDb = null;
	
	public static MoDBRW regdb = null;
	
	public static MoDBRW myConfigDb = null;
	public static String server1DauPath = null;
	public static String server2DauPath = null;
	public static String server3DauPath = null;
	public static String server4DauPath = null;
	public static String server5DauPath = null;
	public static String server6DauPath = null;
	
	public static String OaLogPath = null;
	public static String myLoginLogPath=null;
	public static String dailyMonetidList=null;
	public static String egpytUser=null;
	public static String serverFlag=null;
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
	
			server1dbUrl = serverConf.getString("server1dbUrl");
			server2dbUrl = serverConf.getString("server2dbUrl");
			server3dbUrl = serverConf.getString("server3dbUrl");
			server4dbUrl = serverConf.getString("server4dbUrl");
			server5dbUrl = serverConf.getString("server5dbUrl");
			server6dbUrl = serverConf.getString("server6dbUrl");
			
			portal1dbUrl = serverConf.getString("portal1dbUrl");
			portal2dbUrl = serverConf.getString("portal2dbUrl");
			portal3dbUrl = serverConf.getString("portal3dbUrl");
			portal4dbUrl = serverConf.getString("portal4dbUrl");
			portal5dbUrl = serverConf.getString("portal5dbUrl");
			portal6dbUrl = serverConf.getString("portal6dbUrl");
			dbConfigUrl = serverConf.getString("dbConfigUrl");
			
			testUrl = serverConf.getString("testing");

			regdbUrl=serverConf.getString("regdbUrl");
			dbDriver = serverConf.getString("dbDriver");
		
			server1db = new MoDBRW(server1dbUrl,dbDriver);
			server2db = new MoDBRW(server2dbUrl,dbDriver);
			server3db = new MoDBRW(server3dbUrl,dbDriver);
			server4db = new MoDBRW(server4dbUrl,dbDriver);
			server5db = new MoDBRW(server5dbUrl,dbDriver);
			server6db = new MoDBRW(server6dbUrl,dbDriver);
			
			portal1db = new MoDBRW(portal1dbUrl,dbDriver);
			portal2db = new MoDBRW(portal2dbUrl,dbDriver);
			portal3db = new MoDBRW(portal3dbUrl,dbDriver);
			portal4db = new MoDBRW(portal4dbUrl,dbDriver);
			portal5db = new MoDBRW(portal5dbUrl,dbDriver);
			portal6db = new MoDBRW(portal6dbUrl,dbDriver);
			
			regdb = new MoDBRW(regdbUrl,dbDriver);

			myConfigDb = new MoDBRW(dbConfigUrl,dbDriver);
			
			OaLogPath = serverConf.getString("OALog");
			myLoginLogPath = serverConf.getString("mologinLogin");
			dailyMonetidList = serverConf.getString("idlog");
			egpytUser = serverConf.getString("egpytUser");
			serverFlag = serverConf.getString("serverFlag");
			
			server1DauPath = serverConf.getString("server1DauPath");
			server2DauPath = serverConf.getString("server2DauPath");
			server3DauPath = serverConf.getString("server3DauPath");
			server4DauPath = serverConf.getString("server4DauPath");
			server5DauPath = serverConf.getString("server5DauPath");
			server6DauPath = serverConf.getString("server6DauPath");
			
			myPortalDb = portal1db;
			myServerDb = server1db;
		} catch (Exception e) {
			logger.error("init database error", e);
			}
		}
		
		
	}
