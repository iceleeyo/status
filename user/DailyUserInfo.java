package user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.MoDBRW;
import util.MyUtil;
import util.NewLogUtil;

public class DailyUserInfo {
	static final Logger logger = Logger.getLogger(DailyUserInfo.class);
	
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
	
	public static void StartStats(Date date){
		logger.info("Daily monetid list start "+new Date());
		String tempPth = "";
		int monetid = 0;
		
		//all action (step and actions)
		Set<Integer> allAction = new HashSet<Integer>();
		//step2 - step19
		Set<Integer> step2_19 = new HashSet<Integer>();
		//init action
		Set<Integer> initAction = new HashSet<Integer>();
		//new user
		Set<Integer> newUser = new HashSet<Integer>();
		
		//question
		Set<Integer> questionSet = new HashSet<Integer>();
		
		BufferedWriter output_dau = null;
		BufferedWriter output_newUser = null;
		try {
			output_dau = new BufferedWriter(new FileWriter("/home/vivas/morange/OceanAgeDailyMonetidList/"+MyUtil.DateToString(date)+"_newoa.txt"));
			output_newUser = new BufferedWriter(new FileWriter("/home/vivas/morange/OceanAgeDailyMonetidList/"+MyUtil.DateToString(date)+"_newoaNewUser.txt"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//data prepare
		String path = "/home/vivas/morange/OA_NewServer/deployment/OceanAgelogs/newOA_Request.log."+MyUtil.DateToString(date);
		int step = 1;
		for(int index=0;index<24;index++){
			if(index<10){
				tempPth = path +"-0"+index;
			}else{
				tempPth = path +"-"+index;
			}
			if(tempPth!=null){
				if(tempPth.length()>0){
					if(new File(tempPth).isFile()){
						try {
							BufferedReader br = new BufferedReader(new FileReader(tempPth));
							String temp = br.readLine();
							while(temp!=null){
								if(temp.contains("user get current step , step = ")){
									monetid = NewLogUtil.getActionOwner(temp);
									allAction.add(monetid);
									step = NewLogUtil.getStep(temp);
									if(step>1&&step<20){
										step2_19.add(monetid);
									}
								}
								if(temp.contains("Init")){
									monetid = NewLogUtil.getInitOwner(temp);
									initAction.add(monetid);
								}
								if(temp.contains("new fisher")){
									monetid = NewLogUtil.getNewUserId(temp);
									newUser.add(monetid);
								}
								if(temp.contains("Action]time")){
									allAction.add(monetid);
								}
								temp = br.readLine();
							}
							br.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else{
						logger.info("File is not exists! path="+path);
					}
				}
			}
		}
		
		//play
		for(int id:step2_19){
			initAction.add(id);
		}
		
		//question
		for(int id:allAction){
			if(!initAction.contains(id)){
				questionSet.add(id);
			}
		}
		
		for(int monetidd:allAction){
			try {
				output_dau.write(""+monetidd);
				output_dau.write("\r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer bs = new StringBuffer();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		for(int monetidd:allAction){
			try {
				output_dau.write(""+monetidd);
				output_dau.write("\r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			bs.append("exec addnewoaalluser ");
			bs.append(monetidd);
			bs.append(",'");
			bs.append(sf.format(date));
			bs.append("'");
			sqlList.add(bs.toString());
			System.out.println(bs.toString());
			bs.delete(0,bs.length());
		}
		MyUtil.toDo(sqlList);
		
		for(int monetidd:newUser){
			try {
				output_newUser.write(""+monetidd);
				output_newUser.write("\r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			output_dau.close();
			output_newUser.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Daily monetid list end "+new Date());
	}
}
