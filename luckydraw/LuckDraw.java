package luckydraw;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import both.basic.ConfigUtil;

public class LuckDraw {
	
	static SimpleDateFormat sdfH = new SimpleDateFormat("yyyy-MM-dd-HH");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void calcNomal(Date stime){
		try{
			Map<String,Integer> dataMap = LuckDraw.luckyDrawNomal(stime);
			for(String item :dataMap.keySet()){
				int sum = dataMap.get(item);
				ConfigUtil.myPortalDb.execSQLUpdate("insert into luckyDrawNormal(itemType,itemTypeid,amount,ddate) values (?,?,?,?)",new Object[]{item.split("@")[0],item.split("@")[1],sum,stime});
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void calcEquipment(Date stime){
		try{
			Map<String,Integer> dataMap = LuckDraw.luckyDrawEquipment(stime);
			for(String item :dataMap.keySet()){
				int sum = dataMap.get(item);
				ConfigUtil.myPortalDb.execSQLUpdate("insert into luckyDrawEquipment(itemType,itemTypeid,amount,ddate) values (?,?,?,?)",new Object[]{item.split("@")[0],item.split("@")[1],sum,stime});
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Map<String,Integer> luckyDrawNomal(Date stime){
		Map<String,Integer> resultMap = new HashMap<String,Integer>();
		BufferedReader br = null;
		InputStream  in = null;
		try{
			String fileUrl = ConfigUtil.OaLogPath;
			if(fileUrl!=null){
				fileUrl = fileUrl + "trace.log";
				
				Calendar c = Calendar.getInstance();
				c.setTime(stime);
				
				c.set(Calendar.HOUR, 5);
				StringBuffer fileList = new StringBuffer("");
				
				for(int i = 1;i<=24;i++){
					String _fileName = fileUrl+"."+sdfH.format(c.getTime());
					fileList.append(_fileName+" ");
					c.add(Calendar.HOUR, 1);
				}
				
				
				String []cmdArray = new String[]{ "/bin/sh", "-c", "grep action=LuckyDraw "+fileList.toString()+"|grep -v action=LuckyDrawEquipment,|awk -F '[=,]' '{print substr($7,2,length($7)-2)}'"};
				System.out.println("---------------------------startProcess:"+"grep action=LuckyDraw "+fileList.toString()+"|grep -v action=LuckyDrawEquipment,|awk -F '[=,]' '{print substr($7,2,length($7)-2)}'");
				
				Process process = Runtime.getRuntime().exec(cmdArray);
				in = process.getInputStream();
				br = new BufferedReader(new InputStreamReader(in));
				
				String result = br.readLine();  
	            while (result != null) {
	            	String a [] = result.split(":");
	            	if(resultMap.containsKey(a[0]+"@"+a[1])){
	            		int _i = resultMap.get(a[0]+"@"+a[1]);
	            		resultMap.put(a[0]+"@"+a[1],1+_i);
	            	}else{
	            		resultMap.put(a[0]+"@"+a[1],1);
	            	}
	                result = br.readLine();
	            }
			}
			
			return resultMap;
		}catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String,Integer>();
		}finally{
			try {
				if(br!=null)br.close();
				if(in!=null)in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private static Map<String,Integer> luckyDrawEquipment(Date stime){
		Map<String,Integer> resultMap = new HashMap<String,Integer>();
		BufferedReader br = null;
		InputStream  in = null;
		try{
			String fileUrl = ConfigUtil.OaLogPath;
			if(fileUrl!=null){
				fileUrl = fileUrl + "trace.log";
				
				Calendar c = Calendar.getInstance();
				c.setTime(stime);
				
				c.set(Calendar.HOUR, 5);
				StringBuffer fileList = new StringBuffer("");
				
				for(int i = 1;i<=24;i++){
					String _fileName = fileUrl+"."+sdfH.format(c.getTime());
					fileList.append(_fileName+" ");
					c.add(Calendar.HOUR, 1);
				}
				
				
				String []cmdArray = new String[]{ "/bin/sh", "-c", "grep action=LuckyDrawEquipment "+fileList.toString()+"|awk -F '[=,]' '{print substr($7,2,length($7)-2)}'"};
				System.out.println("---------------------------startProcess:"+"grep action=LuckyDrawEquipment "+fileList.toString()+"|awk -F '[=,]' '{print substr($7,2,length($7)-2)}'");
				
				Process process = Runtime.getRuntime().exec(cmdArray);
				in = process.getInputStream();
				br = new BufferedReader(new InputStreamReader(in));
				
				String result = br.readLine();  
	            while (result != null) {
	            	String a [] = result.split(":");
            		if(resultMap.containsKey(a[0]+"@"+a[1])){
	            		int _i = resultMap.get(a[0]+"@"+a[1]);
	            		resultMap.put(a[0]+"@"+a[1],1+_i);
	            	}else{
	            		resultMap.put(a[0]+"@"+a[1],1);
	            	}
	                result = br.readLine();
	            }
			}
			
			return resultMap;
		}catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String,Integer>();
		}finally{
			try {
				if(br!=null)br.close();
				if(in!=null)in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) {
		try{
			/*Date d1 = sdf.parse("2013-09-01");
			Date d2 = sdf.parse("2013-09-29");
			Calendar c = Calendar.getInstance();
			c.setTime(d1);
			while(c.getTimeInMillis()< d2.getTime()){
				System.out.println(c.getTime());
				calcNomal(c.getTime());
				c.add(Calendar.DATE, 1);
			}*/
			Date d1 = sdf.parse("2013-10-13");
			Calendar c = Calendar.getInstance();
			c.setTime(d1);
			calcEquipment(c.getTime());
		}catch(Exception e){
			
		}
	}
}
