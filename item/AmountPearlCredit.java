package item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import util.MoDBRW;
import util.MyUtil;

public class AmountPearlCredit {
	static final Logger logger = Logger.getLogger(GoldStats.class);
	static String dbReadUrls = null;
	static String dbDriver = null;
	static MoDBRW dbClient = null;
	static MoDBRW dbClient81 = null;
	static String dbWriteUrl = null;
	static String oaLog = null;
	static {
		try {
			CompositeConfiguration settings = new CompositeConfiguration();
			settings.addConfiguration(new PropertiesConfiguration(
					"system.properties"));
			Configuration serverConf = settings.subset("service");

			dbReadUrls = serverConf.getString("dbReadUrls");
			dbDriver = serverConf.getString("dbDriver");
			dbWriteUrl = serverConf.getString("dbWriteUrl");
			oaLog = serverConf.getString("OALog");

			dbClient = new MoDBRW(dbReadUrls,dbDriver);
			dbClient81 = new MoDBRW(dbWriteUrl,dbDriver);
		} catch (Exception e) {
			logger.error("init database error", e);
		}
	}
	//根据日志 拿取  珍珠  数据
	public static void countPearlProduce(Date date){
		String path = oaLog + "trade.log."+GetDateToString(date);
		String tempPath = "";
		BufferedReader br = null;
		Map<String,Double> addMap = new HashMap<String,Double>();
		Map<String,Double> subMap = new HashMap<String,Double>();
		for(int index=0;index<24;index++){
			if(index<10){
				tempPath = path + "-0" + index;
			}
			else{
				tempPath = path + "-" + index;
			}
			try{
				if(new File(tempPath).isFile()){
					br = new BufferedReader(new FileReader(tempPath));
					String log = br.readLine();
					while(log != null){
						if(log.contains("action=addPearl")){
							String from = MyUtil.getValueOfKey(log, "from", ",");
							double num = Double.parseDouble(MyUtil.getValueOfKey(log, "num", ","));
							num = Math.abs(num);
							if(addMap.containsKey(from)){
								addMap.put(from, addMap.get(from)+num);
							}else{
								addMap.put(from, num);
							}
						}else if(log.contains("action=subPearl")){
							String to = MyUtil.getValueOfKey(log, "to", ",");
							double num = Double.parseDouble(MyUtil.getValueOfKey(log, "num", ","));
							num = Math.abs(num);
							if(subMap.containsKey(to)){
								subMap.put(to, subMap.get(to) + num);
							}else{
								subMap.put(to, num);
							}
						}
						log = br.readLine();
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		Object a[] = addMap.keySet().toArray();
		for(int i = 0; i < addMap.size(); i++) {
			setAddPearlCredit("add",a[i],addMap.get(a[i]),date);
			System.out.println("add" +":"+ a[i]+":"+addMap.get(a[i]));
		}
		Object s[] = subMap.keySet().toArray();
		for(int i = 0; i < subMap.size(); i++) {
			setAddPearlCredit("sub",s[i],subMap.get(s[i]),date);
			System.out.println("sub" +":"+ s[i]+":"+subMap.get(s[i]));
		}
	}
	
	static public void setAddPearlCredit(String type,Object a,double amount,Date date){
		try{
			Object[] dbArgs = new Object[] {type,a,amount,date};
			dbClient.execSQLUpdate("insert into AmountPearlCredit(type,item,amount,gtime) values(?,?,?,?)", dbArgs);
		}catch (Exception e) {
			
		}
	}

	public static String GetDateToString(Date date){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}
}
