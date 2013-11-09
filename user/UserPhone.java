package user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import util.MyUtil;

public class UserPhone {
	static Logger logger = Logger.getLogger(UserPhone.class);
	
	static public String getMonetId(String temp) {
		temp  = temp.substring(temp.indexOf("StatLogger.java"));
		temp = temp.substring(temp.indexOf("[")+1);
		temp = temp.substring(0,temp.indexOf("]"));
		return temp;
	}
	
	static public JSONObject getjsonObj(String log) {
		log = log.substring(log.indexOf("{"));
		log = log.substring(0,log.indexOf("}")+1);
		if(log.contains("{")&&log.contains("}")){
			JSONObject jsonObj = JSONObject.fromObject(log);
			return jsonObj;
		}
		return null;
	}
	
	public static void StartStats(Date date){
		String path = "/var/log/gamefeedback/gameServerSys.log."+MyUtil.DateToString(date);
		BufferedReader br = null;
		String tempPth = "";
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer bs = new StringBuffer();
		
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
							br = new BufferedReader(new FileReader(tempPth));
							String temp = br.readLine();
							while(temp!=null){
								if(temp.contains("UserAgent=")){
									if(MyUtil.isMonetid(getMonetId(temp))){
										int monetid = 0;
										monetid = Integer.parseInt(getMonetId(temp));
										
										String model = "";
										String version = "";
										String platform = "";
										JSONObject json = getjsonObj(temp);
										if(json!=null){
											model = json.getString("model");
											platform = json.getString("platform");
											if(temp.contains("client")){
												version = json.getString("client");
											}else{
												version = "[1,1,0]";
											}
											
											bs.append("exec addalluser ");
											bs.append(monetid);
											bs.append(",'");
											bs.append(platform);
											bs.append("','");
											bs.append(model);
											bs.append("','");
											bs.append(version);
											bs.append("','");
											bs.append(MyUtil.DateToString(date));
											bs.append("'");
											sqlList.add(bs.toString());
											bs.delete(0,bs.length());
										}
									}
								}
								temp = br.readLine();
							}
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else{
//						logger.info("File is not exists! path="+tempPth);
					}
				}
			}
		}
		MyUtil.toDo(sqlList);
	}
	
	
     public static void main(String[] args) {
    	 String temp = " INFO 27 Apr 11:08:12,809 - [erviceBase runThread] (          StatLogger.java:  11) - [2839]    Action=IsSendReport,Value=31,UserAgent={\"width\":320,\"height\":240,\"platform\":\"BB\",\"density\":\"4\",\"lang\":\"ar\",\"model\":\"9300\",\"client\":\"[1,1,8]\"}";
    	 System.out.println(getMonetId(temp));
		
	}
}
