package both.basic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import util.DBResultSet;

public class Users {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd-HH");
	
	
	public static Map<Integer,Integer> getPlatformUserByServer(Date stime,String server,String platform){
		try{
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			if("Server1".equals(server)){
				DBResultSet rs = ConfigUtil.portal1db.execSQLQuery("select distinct monetid monetid from all_users where lastlogin >= ? and platform = ?",new Object[]{stime,platform}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),0);
				}
			}else if("Server2".equals(server)){
				DBResultSet rs = ConfigUtil.portal2db.execSQLQuery("select distinct monetid monetid from all_users where lastlogin >= ? and platform = ?",new Object[]{stime,platform}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),0);
				}
			}else if("Server3".equals(server)){
				DBResultSet rs = ConfigUtil.portal3db.execSQLQuery("select distinct monetid monetid from all_users where lastlogin >= ? and platform = ?",new Object[]{stime,platform}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),0);
				}
			}else if("Server4".equals(server)){
				DBResultSet rs = ConfigUtil.portal4db.execSQLQuery("select distinct monetid monetid from all_users where lastlogin >= ? and platform = ?",new Object[]{stime,platform}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),0);
				}
			}else if("Server5".equals(server)){
				DBResultSet rs = ConfigUtil.portal5db.execSQLQuery("select distinct monetid monetid from all_users where lastlogin >= ? and platform = ?",new Object[]{stime,platform}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),0);
				}
			}else if("Server6".equals(server)){
				DBResultSet rs = ConfigUtil.portal6db.execSQLQuery("select distinct monetid monetid from all_users where lastlogin >= ? and platform = ?",new Object[]{stime,platform}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),0);
				}
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		}
	}
	
	
	
public static Map<Integer,Integer> getLoginUserByServer(Date stime,Date etime,String server){
		
		BufferedReader br = null;
		Map<Integer,Integer> resultMap = null;
		try{
			resultMap = new HashMap<Integer,Integer>();
			String fileUrl = null;
			if("Server1".equals(server)){
				//fileUrl = "C:/Users/Administrator/Desktop/LOG/server1/trace.log";
				fileUrl = ConfigUtil.server1DauPath;
			}else if("Server2".equals(server)){
				fileUrl = ConfigUtil.server2DauPath;
			}else if("Server3".equals(server)){
				fileUrl = ConfigUtil.server3DauPath;
			}else if("Server4".equals(server)){
				fileUrl = ConfigUtil.server4DauPath;
			}else if("Server5".equals(server)){
				fileUrl = ConfigUtil.server5DauPath;
			}else if("Server6".equals(server)){
				fileUrl = ConfigUtil.server6DauPath;
			}
			
			if(fileUrl!=null){
				Calendar c = Calendar.getInstance();
				c.setTime(stime);
				StringBuffer fileList = new StringBuffer("");
				
				while(c.getTimeInMillis()<etime.getTime()){
					String _fileName = fileUrl+sdf.format(c.getTime())+"_newoa.txt ";
					System.out.println(_fileName);
					fileList.append(_fileName);
					c.add(Calendar.DATE, 1);
				}
				
				String []cmdArray = new String[]{ "/bin/sh", "-c", "cat "+fileList.toString() + " | sort -u"};
				
				System.out.println("[Run]:"+"sort -u "+fileList.toString());
				
				Process process = Runtime.getRuntime().exec(cmdArray);
				
				//process.waitFor();  
				InputStream  in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();  
	            while (result != null) {
	            	resultMap.put(new Integer(result), 0);
	                result = read.readLine();
	            }
			}
			System.out.println(resultMap.size());
			
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		} finally{
				try {
					if(br!=null)br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	
	public static Map<Integer,Integer> getTraceUserByServer(Date stime,Date etime,String server){
		
		BufferedReader br = null;
		Map<Integer,Integer> resultMap = null;
		try{
			resultMap = new HashMap<Integer,Integer>();
			String fileUrl = null;
			if("Server1".equals(server)){
				//fileUrl = "C:/Users/Administrator/Desktop/LOG/server1/trace.log";
				fileUrl = "/home/mozat/morange/oapro/OceanAgelogs/trace.log";
			}else if("Server2".equals(server)){
				fileUrl = "/mnt/192.168.1.107/oapro/OceanAgelogs/trace.log";
			}else if("Server3".equals(server)){
				fileUrl = "/mnt/192.168.1.112/oapro/OceanAgelogs/trace.log";
			}else if("Server4".equals(server)){
				fileUrl = "/mnt/192.168.1.116/oapro/OceanAgelogs/trace.log";
			}else if("Server5".equals(server)){
				fileUrl = "/mnt/192.168.1.120/oapro/OceanAgelogs/trace.log";
			}else if("Server6".equals(server)){
				fileUrl = "/mnt/192.168.1.122/oapro/OceanAgelogs/trace.log";
			}
			
			if(fileUrl!=null){
				Calendar c = Calendar.getInstance();
				c.setTime(stime);
				StringBuffer fileList = new StringBuffer("");
				
				
				while(c.getTimeInMillis()<etime.getTime()){
					String _fileName = fileUrl+"."+sdf.format(c.getTime());
					System.out.println(_fileName);
					fileList.append(_fileName+"* ");
					c.add(Calendar.DATE, 1);
				}
				
				String []cmdArray = new String[]{ "/bin/sh", "-c", "grep monetId "+fileList.toString()+"|awk -F '[=,]' '{print $3}'|sort -u"};
				
				System.out.println("---------------------------startProcess:"+"grep monetId "+fileList.toString()+"|awk -F '[=,]' '{print $3}'|sort -u");
				Process process = Runtime.getRuntime().exec(cmdArray);
				
				//process.waitFor();  
				InputStream  in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();  
	            while (result != null) {
	            	resultMap.put(new Integer(result), 0);
	                result = read.readLine();
	            }
			}
			System.out.println(resultMap.size());
			
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		} finally{
				try {
					if(br!=null)br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	public static Map<Integer,Integer> getTraceUserByServerHour(Date stime,Date etime,String server){
		
		BufferedReader br = null;
		Map<Integer,Integer> resultMap = null;
		try{
			resultMap = new HashMap<Integer,Integer>();
			String fileUrl = null;
			if("Server1".equals(server)){
				//fileUrl = "C:/Users/Administrator/Desktop/LOG/server1/trace.log";
				fileUrl = "/home/mozat/morange/oapro/OceanAgelogs/trace.log";
			}else if("Server2".equals(server)){
				fileUrl = "/mnt/192.168.1.107/oapro/OceanAgelogs/trace.log";
			}else if("Server3".equals(server)){
				fileUrl = "/mnt/192.168.1.112/oapro/OceanAgelogs/trace.log";
			}else if("Server4".equals(server)){
				fileUrl = "/mnt/192.168.1.116/oapro/OceanAgelogs/trace.log";
			}else if("Server5".equals(server)){
				fileUrl = "/mnt/192.168.1.120/oapro/OceanAgelogs/trace.log";
			}else if("Server6".equals(server)){
				fileUrl = "/mnt/192.168.1.122/oapro/OceanAgelogs/trace.log";
			}
			
			if(fileUrl!=null){
				Calendar c = Calendar.getInstance();
				c.setTime(stime);
				StringBuffer fileList = new StringBuffer("");
				
				
				while(c.getTimeInMillis()<etime.getTime()){
					String _fileName = fileUrl+"."+sdf1.format(c.getTime());
					//System.out.println(_fileName);
					fileList.append(_fileName+"* ");
					c.add(Calendar.HOUR, 1);
				}
				
				String []cmdArray = new String[]{ "/bin/sh", "-c", "grep monetId "+fileList.toString()+"|awk -F '[=,]' '{print $3}'|sort -u"};
				
				//System.out.println("---------------------------startProcess:"+"grep monetId "+fileList.toString()+"|awk -F '[=,]' '{print $3}'|sort -u");
				Process process = Runtime.getRuntime().exec(cmdArray);
				
				//process.waitFor();  
				InputStream  in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();  
	            while (result != null) {
	            	resultMap.put(new Integer(result), 0);
	                result = read.readLine();
	            }
			}
			System.out.println(resultMap.size());
			
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		} finally{
				try {
					if(br!=null)br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	
	
	
	public static Map<Integer,Integer> getTraceUser(Date stime,Date etime){
		BufferedReader br = null;
		Map<Integer,Integer> resultMap = null;
		try{
			resultMap = new HashMap<Integer,Integer>();
			String fileUrl = ConfigUtil.OaLogPath+"trace.log";
			
			if(fileUrl!=null){
				Calendar c = Calendar.getInstance();
				c.setTime(stime);
				StringBuffer fileList = new StringBuffer("");
				
				
				while(c.getTimeInMillis()<etime.getTime()){
					String _fileName = fileUrl+"."+sdf1.format(c.getTime());
					System.out.println(_fileName);
					fileList.append(_fileName+"* ");
					c.add(Calendar.HOUR, 1);
				}
				
				String []cmdArray = new String[]{ "/bin/sh", "-c", "grep monetId "+fileList.toString()+"|awk -F '[=,]' '{print $3}'|sort -u"};
				
				System.out.println("---------------------------startProcess:"+"grep monetId "+fileList.toString()+"|awk -F '[=,]' '{print $3}'|sort -u");
				Process process = Runtime.getRuntime().exec(cmdArray);
				
				//process.waitFor();  
				InputStream  in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();  
	            while (result != null) {
	            	resultMap.put(new Integer(result), 0);
	                result = read.readLine();
	            }
			}
			//System.out.println(resultMap.size());
			
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		} finally{
				try {
					if(br!=null)br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	public static Map<Integer,Integer> getNewOaRequestUser(Date stime,Date etime){
		BufferedReader br = null;
		Map<Integer,Integer> resultMap = null;
		try{
			resultMap = new HashMap<Integer,Integer>();
			String fileUrl = ConfigUtil.OaLogPath+"newOA_Request.log";
			
			if(fileUrl!=null){
				Calendar c = Calendar.getInstance();
				c.setTime(stime);
				StringBuffer fileList = new StringBuffer("");
				
				
				while(c.getTimeInMillis()<etime.getTime()){
					String _fileName = fileUrl+"."+sdf1.format(c.getTime());
					System.out.println(_fileName);
					fileList.append(_fileName+"* ");
					c.add(Calendar.HOUR, 1);
				}
				
				String []cmdArray = new String[]{ "/bin/sh", "-c", "grep -v 'Realm.Query' "+fileList.toString()+"|awk '{print $8}'|sort -u"};
				
				System.out.println("---------------------------startProcess:"+"grep -v 'Realm.Query' "+fileList.toString()+"|awk '{print $8}'|sort -u");
				Process process = Runtime.getRuntime().exec(cmdArray);
				
				//process.waitFor();  
				InputStream  in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();  
	            while (result != null) {
	            	resultMap.put(new Integer(result), 0);
	                result = read.readLine();
	            }
			}
			return resultMap;
			
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		} finally{
				try {
					if(br!=null)br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	
	public static Map<Integer,Integer> getMoLoginUser(Date stime,Date etime){
		BufferedReader br = null;
		Map<Integer,Integer> resultMap = null;
		try{
			resultMap = new HashMap<Integer,Integer>();
			String fileUrl = ConfigUtil.myLoginLogPath+"morange.log";
			
			if(fileUrl!=null){
				Calendar c = Calendar.getInstance();
				c.setTime(stime);
				StringBuffer fileList = new StringBuffer("");
				
				
				while(c.getTimeInMillis()<etime.getTime()){
					String _fileName = fileUrl+"."+sdf1.format(c.getTime());
					fileList.append(_fileName+"* ");
					c.add(Calendar.HOUR, 1);
				}
				
				System.out.println("grep 'login result: result=0' "+fileList.toString()+"|awk -F '[=,]' '{print $5}'|sort -u");
				String []cmdArray = new String[]{"/bin/sh", "-c",  "grep 'login result: result=0' "+fileList.toString()+"|awk -F '[=,]' '{print $5}'|sort -u"};
				
				Process process = Runtime.getRuntime().exec(cmdArray);
				//process.waitFor();  
				InputStream  in = process.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));
				String result = read.readLine();  
	            while (result != null) {
	            	resultMap.put(new Integer(result), 0);
	                result = read.readLine();
	            }
			}
			return resultMap;
			
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		} finally{
				try {
					if(br!=null)br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	
	public static void main(String[] args){
		
		Date stime;
		Date etime;
		try {
			stime = sdf.parse("2013-08-05");
			etime = sdf.parse("2013-08-20");
			
			Calendar c = Calendar.getInstance();
			c.setTime(stime);
			
			while(c.getTimeInMillis()<etime.getTime()){
				c.add(Calendar.DATE, 1);
			}
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
}
