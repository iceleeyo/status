package tribe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import both.basic.ConfigUtil;

public class MonsterCalc {
	
	static SimpleDateFormat sdfH = new SimpleDateFormat("yyyy-MM-dd-HH");
	static SimpleDateFormat sdfHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static void calcCreateTribeMonster(Date stime){
		try{
			Map<Integer,String[]> dataMap = MonsterCalc.getCreateMonster(stime);
			for(int monsterid :dataMap.keySet()){
				String _temp[] = dataMap.get(monsterid);
				//ConfigUtil.myPortalDb.execSQLUpdate("insert into TribeMonster(monsterid,tribeid,monsterType,weaponType,createTime) values (?,?,?,?,?)", new Object[]{_temp[0],_temp[1],_temp[2],_temp[3],sdfHms.parse(_temp[4])});
				ConfigUtil.myPortalDb.execSQLUpdate("insert into TribeMonster(monsterid,tribeid,monsterType,weaponType,createTime) values (?,?,?,?,?)",dataMap.get(monsterid));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void calcKillTribeMonster(Date stime){
		Map<Integer,String[]> dataMap = MonsterCalc.getKillMonster(stime);
		
		for(int monsterid :dataMap.keySet()){
			String _temp[] = dataMap.get(monsterid);
			int i= ConfigUtil.myPortalDb.execSQLUpdate("update TribeMonster set killid = ?,killTime = ? where monsterid = ? ", new Object[]{_temp[0],_temp[4],_temp[2]});
			if(i==0){
				ConfigUtil.myPortalDb.execSQLUpdate("insert into TribeMonster(killid,tribeid,monsterid,monsterType,killTime) values (?,?,?,?,?)", _temp);
			}
		}
	}
	
	private static Map<Integer,String[]> getCreateMonster(Date stime){
		Map<Integer,String[]> resultMap = new HashMap<Integer, String[]>();
		BufferedReader br = null;
		InputStream  in = null;
		try{
			String fileUrl = ConfigUtil.OaLogPath;
			if(fileUrl!=null){
				fileUrl = fileUrl + "action.log";
				
				Calendar c = Calendar.getInstance();
				c.setTime(stime);
				
				c.set(Calendar.HOUR, 5);
				StringBuffer fileList = new StringBuffer("");
				
				for(int i = 1;i<=24;i++){
					String _fileName = fileUrl+"."+sdfH.format(c.getTime());
					fileList.append(_fileName+" ");
					c.add(Calendar.HOUR, 1);
				}
				
				
				String []cmdArray = new String[]{ "/bin/sh", "-c", "grep action=createTribeMonster "+fileList.toString()+"|awk -F '[=,]' '{print $13\",\"$15\",\"$17\",\"$25\",\"substr($1,index($1,\"action.log\")+length(\"action.log\")+1,10)\" \"substr($1,length($1)-7,9)}'"};
				System.out.println("---------------------------startProcess:"+"grep action=createTribeMonster "+fileList.toString()+"|awk -F '[=,]' '{print $13\",\"$15\",\"$17\",\"$25\",\"substr($1,index($1,\"action.log\")+length(\"action.log\")+1,10)\" \"substr($1,length($1)-7,9)}'");
				
				Process process = Runtime.getRuntime().exec(cmdArray);
				in = process.getInputStream();
				br = new BufferedReader(new InputStreamReader(in));
				
				String result = br.readLine();  
	            while (result != null) {
	            	String a [] = result.split(",");
	            	resultMap.put(new Integer(a[0]),a);
	                result = br.readLine();
	            }
			}
			
			return resultMap;
		}catch (Exception e) {
			e.printStackTrace();
			return new HashMap<Integer, String[]>();
		}finally{
			try {
				if(br!=null)br.close();
				if(in!=null)in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private static Map<Integer,String[]> getKillMonster(Date stime){
		Map<Integer,String[]> resultMap = new HashMap<Integer, String[]>();
		BufferedReader br = null;
		InputStream  in = null;
		try{
			String fileUrl = ConfigUtil.OaLogPath;
			if(fileUrl!=null){
				fileUrl = fileUrl + "action.log";
				
				Calendar c = Calendar.getInstance();
				c.setTime(stime);
				
				c.set(Calendar.HOUR, 5);
				StringBuffer fileList = new StringBuffer("");
				
				for(int i = 1;i<=24;i++){
					String _fileName = fileUrl+"."+sdfH.format(c.getTime());
					fileList.append(_fileName+" ");
					c.add(Calendar.HOUR, 1);
				}
				
				
				String []cmdArray = new String[]{ "/bin/sh", "-c", "grep action=killTribeMonster "+fileList.toString()+"|awk -F '[=,]' '{print $3\",\"$13\",\"$15\",\"$17\",\"substr($1,index($1,\"action.log\")+length(\"action.log\")+1,10)\" \"substr($1,length($1)-7,9)}'"};
				System.out.println("---------------------------startProcess:"+"grep action=killTribeMonster "+fileList.toString()+"|awk -F '[=,]' '{print $3\",\"$13\",\"$15\",\"$17\",\"substr($1,index($1,\"action.log\")+length(\"action.log\")+1,10)\" \"substr($1,length($1)-7,9)}'");
				
				Process process = Runtime.getRuntime().exec(cmdArray);
				in = process.getInputStream();
				br = new BufferedReader(new InputStreamReader(in));
				
				String result = br.readLine();  
	            while (result != null) {
	            	String a [] = result.split(",");
	            	resultMap.put(new Integer(a[2]),a);
	                result = br.readLine();
	            }
			}
			
			return resultMap;
		}catch (Exception e) {
			e.printStackTrace();
			return new HashMap<Integer, String[]>();
		}finally{
			try {
				if(br!=null)br.close();
				if(in!=null)in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args){
		try{
			/*Date c = sdf.parse("2013-10-10");
			//calcCreateTribeMonster(c);
			calcKillTribeMonster(c);*/
			
			
			Date sd = sdf.parse("2013-09-15");
			
			Calendar c1 = Calendar.getInstance();
			c1.setTime(sd);
			
			while(c1.getTimeInMillis()<= sdf.parse("2013-10-23").getTime()){
				calcKillTribeMonster(c1.getTime());
				c1.add(Calendar.DATE, 1);
				/*if(c1.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY||c1.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY){
					Date[] _temp = {null,null,null};
					
					_temp[0]=c1.getTime();
					c1.add(Calendar.DATE, 1);
					_temp[1]=c1.getTime();
					c1.add(Calendar.DATE, 1);
					_temp[2]=c1.getTime();
					c1.add(Calendar.DATE, 1);
					String key = sdf.format(_temp[0])+"~"+sdf.format(_temp[2]);
					
					System.out.println(key);
					for(int i = 0;i<3;i++){
						System.out.println(_temp[i]);
					}
				}*/
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
