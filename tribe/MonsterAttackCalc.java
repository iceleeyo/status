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

public class MonsterAttackCalc {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
	
	
	public static void calcAttackTime(Date stime){
		Map<Integer,Integer> dataMap = MonsterAttackCalc.attackTribeMonster(stime);
		Map<Integer,Integer> resultMap = new HashMap<Integer, Integer>();
		for(int monetid : dataMap.keySet()){
			int _time = dataMap.get(monetid);
			if(resultMap.containsKey(_time)){
				int _count = resultMap.get(_time);
				_count +=1;
				resultMap.put(_time, _count);
			}else{
				resultMap.put(_time, 1);
			}
		}
		
		for(int _time:resultMap.keySet()){
			ConfigUtil.myPortalDb.execSQLUpdate("insert into monsterAttackUserCount(times,users,dtime) values(?,?,?)", new Object[]{_time,resultMap.get(_time),stime});
		}
	}
	
	
	private static Map<Integer,Integer> attackTribeMonster(Date stime){
		Map<Integer,Integer> resultMap = new HashMap<Integer, Integer>();
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
					String _fileName = fileUrl+"."+sdf.format(c.getTime());
					fileList.append(_fileName+" ");
					c.add(Calendar.HOUR, 1);
				}
				
				
				String []cmdArray = new String[]{ "/bin/sh", "-c", "grep action=tribeMonsterSelectPosition "+fileList.toString()+"|grep -v 'oldPosition=, newPosition=0'|grep oldPosition=0|awk -F '[=,]' '{print $3}'|sort -b|uniq -c|awk '{print $2\",\"$1}'"};
				System.out.println("---------------------------startProcess:"+"grep action=tribeMonsterSelectPosition "+fileList.toString()+"|grep -v 'oldPosition=, newPosition=0'|grep oldPosition=0|awk -F '[=,]' '{print $3}'|sort -b|uniq -c|awk '{print $2\",\"$1}'");
				
				Process process = Runtime.getRuntime().exec(cmdArray);
				in = process.getInputStream();
				br = new BufferedReader(new InputStreamReader(in));
				
				String result = br.readLine();  
	            while (result != null) {
	            	String a [] = result.split(",");
	            	if(a.length==2){
	            		resultMap.put(new Integer(a[0]),new Integer(a[1]));
	            	}
	                result = br.readLine();
	            }
			}
			System.out.println(resultMap);
			return resultMap;
		}catch (Exception e) {
			e.printStackTrace();
			return new HashMap<Integer, Integer>();
		}finally{
			try {
				if(br!=null)br.close();
				if(in!=null)in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String args[]){
		try{
			Date c = sdf.parse("2013-09-25-00");
			MonsterAttackCalc.calcAttackTime(c);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
