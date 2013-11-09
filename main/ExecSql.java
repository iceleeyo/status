package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.MoDBRW;


public class ExecSql {
	static String portal1dbUrl = null;
	static String sqlFilePath = null;
	static MoDBRW portal1db = null;
	static String dbDriver = null;
	static Logger logger = Logger.getLogger(ExecSql.class);
	static SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");

			portal1dbUrl = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			sqlFilePath = serverConf.getString("sqlFilePath");
			
			portal1db = new MoDBRW(portal1dbUrl,dbDriver);
			
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	public static void main(String args[]){
		String filename = null;
		if(args.length == 1){
			filename =args[0];
		}else{
			return;
		}
		
		String file=filename;
		
		if(file!=null&&file.length()>0&&new File(file).isFile()){
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String temp = br.readLine();
				while(temp != null){
					portal1db.execSQLUpdate(temp, new Object[]{});
					temp = br.readLine();
				}
				br.close();
			}catch(Exception e){
				logger.error("readFile",e);
			}
		}
	}
}