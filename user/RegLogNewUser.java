package user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.MoDBRW;
import util.MyUtil;

public class RegLogNewUser {
	static final Logger logger = Logger.getLogger(RegLogNewUser.class);
	
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient = null;
	static String mologinReg = null;
	static String idlog = null;
	
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration(
					"system.properties"));
			Configuration serverConf = settings.subset("service");
	
			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			dbClient = new MoDBRW(dbReadUrls,dbDriver);
			mologinReg = serverConf.getString("mologinReg");
			idlog = serverConf.getString("idlog");
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	public static void StartStats(Date date){
		String path = mologinReg + "stat.log."+MyUtil.DateToString(date);
		String tempPath = "";
		Set<Integer> newUser = new HashSet<Integer>();
		
		BufferedWriter output_new = null;
		
		try {
//			if(new File(idlog+MyUtil.DateToString(date)+"_newoaNewUser.txt").isFile()){
				output_new = new BufferedWriter(new FileWriter(idlog+MyUtil.DateToString(date)+"_newoaNewUser.txt"));
//			}
		} catch (Exception e1) {
			logger.error("",e1);
		}
		
		for(int index=0;index<24;index++){
			if(index<10){
				tempPath = path +"-0"+index;
			}else{
				tempPath = path +"-"+index;
			}
			if(tempPath!=null){
				if(tempPath.length()>0){
					if(new File(tempPath).isFile()){
						try {
							BufferedReader br = new BufferedReader(new FileReader(tempPath));
							String temp = br.readLine();
							while(temp!=null){
								// 2013-03-27 10:23:41,270 [INFO] MoPeerLoginService - oaprouser1364282711420_289@shabik.com login result: result=0,peerId=323,flag=0
								if(temp.contains("CreateUser monetId")){
									if(MyUtil.isMonetid(MyUtil.getValueOfKey(temp,"monetId", ","))){
										int peerid = Integer.parseInt(MyUtil.getValueOfKey(temp,"monetId", ","));
										newUser.add(peerid);
									}
								}
								
								temp = br.readLine();
							}
							br.close();
						}catch(Exception e){
							logger.error("",e);
						}
					}
				}
			}
		}
		
		for(int monetidd:newUser){
			try {
				output_new.write(""+monetidd);
				output_new.write("\r\n");
			} catch (IOException e) {
				logger.error("",e);
			}
		}
		
		if(output_new!=null){
			try {
				output_new.close();
			} catch (IOException e) {
				logger.info("",e);
			}
		}
	}
}
