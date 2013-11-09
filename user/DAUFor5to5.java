package user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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

import util.DBResultSet;
import util.MoDBRW;
import util.MyUtil;

public class DAUFor5to5 {
	static Logger logger = Logger.getLogger(DAUFor5to5.class);
	static String loginLog = null;
	static String mologinReg = null;
	static String idlog = null;
	static String server1dbUrl = null;
	static String dbDriver = null;
	static MoDBRW server1db = null;
	
	static {
		try {
			
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration("system.properties"));
			Configuration serverConf = settings.subset("service");
			loginLog = serverConf.getString("mologinLogin");
			mologinReg = serverConf.getString("mologinReg");
			idlog = serverConf.getString("idlog");
			
			server1dbUrl = serverConf.getString("dbWriteUrl");
			dbDriver = serverConf.getString("dbDriver");
			server1db = new MoDBRW(server1dbUrl,dbDriver);
			
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	
	static public int getNewUserId(String log) {
		if(log!=null){
			if(log.length()>0){
				if(log.indexOf("new fisher") != -1){
					log = log.substring(62, log.indexOf("new fisher"));
					if(MyUtil.isMonetid(log.trim())){
						return Integer.parseInt(log.trim());
					}
				}
			}
		}
		return 0;
	}
	
	public static void userTopup5To5(Date date){
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(idlog+MyUtil.DateToString(date)+"_topUp5-5.txt"));
			DBResultSet rs = server1db.execSQLQuery("select sum(amount)/100 amount,monet_id monetid from callbackstc where convert(varchar(10),dateadd(hh,-5,date),20)= ? group by monet_id", new Object[]{MyUtil.DateToString(date)});
			while(rs.next()){
				output.write(rs.getInt("monetid")+":"+rs.getInt("amount")+"\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				output.close();
			} catch (IOException e) {
				output = null;
			}
		}
	}
	
	public static void DAU5To5(Date date){
		String tempPath = "";
		
		Set<Integer> allUser = new HashSet<Integer>();
		BufferedWriter output_dau = null;
		try {
			output_dau = new BufferedWriter(new FileWriter(idlog+MyUtil.DateToString(date)+"_newoa5-5.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		///////////////////////////////////////  DAU  /////////////////////////////////////////////
		//data prepare
		String path = loginLog + "morange.log."+MyUtil.DateToString(date);
		for(int index=5;index<24;index++){
			if(index<10){
				tempPath = path +"-0"+index;
			}else{
				tempPath = path +"-"+index;
			}
			if(tempPath!=null){
				if(tempPath.length()>0){
					if(new File(tempPath).isFile()){
						System.out.println("tempPath = " + tempPath);
						try {
							BufferedReader br = new BufferedReader(new FileReader(tempPath));
							String temp = br.readLine();
							while(temp!=null){
								if(temp.contains("login result: result=0,")){
									if(MyUtil.isMonetid(MyUtil.getValueOfKey(temp,"peerId", ","))){
										int peerid = Integer.parseInt(MyUtil.getValueOfKey(temp,"peerId", ","));
										allUser.add(peerid);
									}
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
		
		//data prepare
		path = loginLog + "morange.log." + MyUtil.DateToString(new Date(date.getTime()+1000*3600*24));
		for(int index=0;index<5;index++){
			if(index<10){
				tempPath = path +"-0"+index;
			}else{
				tempPath = path +"-"+index;
			}
			if(tempPath!=null){
				if(tempPath.length()>0){
					if(new File(tempPath).isFile()){
						System.out.println("tempPath = " + tempPath);
						try {
							BufferedReader br = new BufferedReader(new FileReader(tempPath));
							String temp = br.readLine();
							while(temp!=null){
								if(temp.contains("login result: result=0,")){
									if(MyUtil.isMonetid(MyUtil.getValueOfKey(temp,"peerId", ","))){
										int peerid = Integer.parseInt(MyUtil.getValueOfKey(temp,"peerId", ","));
										allUser.add(peerid);
									}
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
		
		for(int monetid:allUser){
			try {
				output_dau.write(""+monetid);
				output_dau.write("\r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			output_dau.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void NewUser5To5(Date date){
		String tempPath = "";
		
		BufferedWriter output_new = null;
		try {
			output_new = new BufferedWriter(new FileWriter(idlog+MyUtil.DateToString(date)+"_newoaNewUser5-5.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		///////////////////////////////////////  DAU  /////////////////////////////////////////////
		//data prepare
		String path = mologinReg + "stat.log."+MyUtil.DateToString(date);
		for(int index=5;index<24;index++){
			if(index<10){
				tempPath = path +"-0"+index;
			}else{
				tempPath = path +"-"+index;
			}
			if(tempPath!=null){
				if(tempPath.length()>0){
					if(new File(tempPath).isFile()){
						System.out.println("tempPath = " + tempPath);
						try {
							BufferedReader br = new BufferedReader(new FileReader(tempPath));
							String temp = br.readLine();
							while(temp!=null){
								if(temp.contains("CreateUser monetId")){
									if(MyUtil.isMonetid(MyUtil.getValueOfKey(temp,"monetId", ","))){
										int peerid = Integer.parseInt(MyUtil.getValueOfKey(temp,"monetId", ","));
										output_new.write(""+peerid);
										output_new.write("\r\n");
									}
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
		
		//data prepare
		path = mologinReg + "stat.log." + MyUtil.DateToString(new Date(date.getTime()+1000*3600*24));
		for(int index=0;index<5;index++){
			if(index<10){
				tempPath = path +"-0"+index;
			}else{
				tempPath = path +"-"+index;
			}
			if(tempPath!=null){
				if(tempPath.length()>0){
					if(new File(tempPath).isFile()){
						System.out.println("tempPath = " + tempPath);
						try {
							BufferedReader br = new BufferedReader(new FileReader(tempPath));
							String temp = br.readLine();
							while(temp!=null){
								if(temp.contains("CreateUser monetId")){
									if(MyUtil.isMonetid(MyUtil.getValueOfKey(temp,"monetId", ","))){
										int peerid = Integer.parseInt(MyUtil.getValueOfKey(temp,"monetId", ","));
										output_new.write(""+peerid);
										output_new.write("\r\n");
									}
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
		
		try {
			output_new.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void StartStats(Date date){
		DAU5To5(date);
		NewUser5To5(date);
	}
	
	public static void main(String[] args) {

	}

}
