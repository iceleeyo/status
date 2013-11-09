package user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import util.DBResultSet;
import util.MyUtil;
import basicStats.OAproKPI;
import both.basic.ConfigUtil;
import both.basic.Users;

public class LoginServiceDAU {
	static final Logger logger = Logger.getLogger(LoginServiceDAU.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	public static void startStats(Date date){
		ArrayList<Integer> testList = MyUtil.getTestAccount();
		BufferedReader br = null;
		BufferedWriter output_dau = null;
		try{
			
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, 1);
			Map<Integer,Integer> lgUser = Users.getMoLoginUser(date, c.getTime());
			StringBuffer sb = new StringBuffer("");
			for(int id:lgUser.keySet()){
				sb.append(id+",");
			}
			
			String idList = sb.toString();
			if(idList.length()>=1){
				idList = idList.substring(0,idList.length()-1);
			}
			
			DBResultSet ds = ConfigUtil.server2db.execSQLQuery("select distinct monetid from fisher where monetid in("+idList+") and newuserflag > '2013/6/22 18:30'", new Object[]{});
			while(ds.next()){
				Integer _i = ds.getInt("monetid");
				lgUser.remove(_i);
			}
			
			ds = ConfigUtil.server3db.execSQLQuery("select distinct monetid from fisher where monetid in("+idList+")", new Object[]{});
			while(ds.next()){
				Integer _i = ds.getInt("monetid");
				lgUser.remove(_i);
			}
			
			ds = ConfigUtil.server4db.execSQLQuery("select distinct monetid from fisher where monetid in("+idList+")", new Object[]{});
			while(ds.next()){
				Integer _i = ds.getInt("monetid");
				lgUser.remove(_i);
			}
			
			ds = ConfigUtil.server5db.execSQLQuery("select distinct monetid from fisher where monetid in("+idList+")", new Object[]{});
			while(ds.next()){
				Integer _i = ds.getInt("monetid");
				lgUser.remove(_i);
			}
			
			
			ds = ConfigUtil.server6db.execSQLQuery("select distinct monetid from fisher where monetid in("+idList+")", new Object[]{});
			while(ds.next()){
				Integer _i = ds.getInt("monetid");
				lgUser.remove(_i);
			}
			
			for(Integer id:testList){
				lgUser.remove(id);
			}
			
			Map<Integer,Integer> rqUser = Users.getNewOaRequestUser(date, c.getTime());
			
			OAproKPI.setKpi("DAU",lgUser.size(), date);
			OAproKPI.setKpi("RDAU",rqUser.size(), date);
			
			//Ð´ÈëÎÄ¼þ
			output_dau = new BufferedWriter(new FileWriter(ConfigUtil.dailyMonetidList+sdf.format(date)+"_newoa.txt"));
			
			for(int id:lgUser.keySet()){
				output_dau.write(""+id);
				output_dau.write("\r\n");
			}
			
			sb = new StringBuffer("");
			for(int id:lgUser.keySet()){
				sb.append(id+",");
			}
			
			if(sb!=null&&sb.length()>0){
				calcShipyarLevel(date, sb);	
				calcPetLevel(date, sb);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(br!=null)br.close();
				if(output_dau!=null)output_dau.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	


	private static void calcPetLevel(Date date, StringBuffer all_sb) {
		//pet
		Map<Integer,Integer> petLevelMap = getDailyUsersPetLevel(all_sb.substring(0,all_sb.length()-1));
		Map<Integer,List<Date>> petPlayTimeMap = getPetPlayTime(all_sb.substring(0,all_sb.length()-1));
		
		Set<Integer> petLevelSet = petPlayTimeMap.keySet();
		for(int level:petLevelSet){
			if(petPlayTimeMap.containsKey(level)){
				List<Date> dateList = petPlayTimeMap.get(level);
				
				if(dateList!=null&&dateList.size()>0){
					Date toTime = new Date(date.getTime()+1000*3600*24);
					long minTime = 0;
					long midTime = 0;
					long averageTime = 0;
					Collections.sort(dateList);
					for(Date tempdate:dateList){
						long temptime = (toTime.getTime()-tempdate.getTime())/1000;
						averageTime += temptime;
					}
					averageTime = averageTime/dateList.size();
					minTime = (toTime.getTime()-dateList.get(dateList.size()-1).getTime())/1000;
					if(dateList.size()%2==0){
						midTime = (toTime.getTime()-(dateList.get(dateList.size()/2).getTime()+dateList.get(dateList.size()/2-1).getTime())/2)/1000;
					}else{
						midTime = (toTime.getTime()-dateList.get(dateList.size()/2).getTime())/1000;
					}
					
					setDailyUsersLevel("Pet", level,petLevelMap.get(level), averageTime, midTime, minTime, date);
				}
			}
		}
	}


	private static void calcShipyarLevel(Date date, StringBuffer all_sb) {
		Map<Integer,Integer> shipyardLevelMap = getDailyUsersShipyardLevel(all_sb.substring(0,all_sb.length()-1));
		Map<Integer,List<Date>> shipyardPlayTimeMap = getShipyardLevelPlayTime(all_sb.substring(0,all_sb.length()-1));
		
		Set<Integer> shipyardLevelSet = shipyardPlayTimeMap.keySet();
		for(int level:shipyardLevelSet){
			if(shipyardPlayTimeMap.containsKey(level)){
				List<Date> dateList = shipyardPlayTimeMap.get(level);
				
				if(dateList!=null&&dateList.size()>0){
					Date toTime = new Date(date.getTime()+1000*3600*24);
					long minTime = 0;
					long midTime = 0;
					long averageTime = 0;
					Collections.sort(dateList);
					for(Date tempdate:dateList){
						long temptime = (toTime.getTime()-tempdate.getTime())/1000;
						averageTime += temptime;
					}
					averageTime = averageTime/dateList.size();
					minTime = (toTime.getTime()-dateList.get(dateList.size()-1).getTime())/1000;
					if(dateList.size()%2==0){
						midTime = (toTime.getTime()-(dateList.get(dateList.size()/2).getTime()+dateList.get(dateList.size()/2-1).getTime())/2)/1000;
					}else{
						midTime = (toTime.getTime()-dateList.get(dateList.size()/2).getTime())/1000;
					}
					
					setDailyUsersLevel("Shipyard", level,shipyardLevelMap.get(level), averageTime, midTime, minTime, date);
				}
			}
		}
	}
	
	public static java.util.Map<Integer,Integer> getDailyUsersShipyardLevel(String monetids){
		java.util.Map<Integer,Integer> map = new java.util.HashMap<Integer, Integer>();
		try {
			DBResultSet rs = ConfigUtil.myServerDb.execSQLQuery("select level,count(*) as amount from (select monetid,max(buildinglevel) level from building where monetid in ("+monetids+") and buildingtype=1 group by monetid)a group by level order by level",new Object[]{});
			while(rs.next()){
				map.put(rs.getInt("level"),rs.getInt("amount"));
			}
		} catch (Exception e) {
			logger.error("setDailyUsersLevel error", e);
		}
		return map;
	}
	
	public static java.util.Map<Integer,Integer> getDailyUsersPetLevel(String monetids){
		java.util.Map<Integer,Integer> map = new java.util.HashMap<Integer, Integer>();
		try {
			DBResultSet rs = ConfigUtil.myServerDb.execSQLQuery("select petLevel,count(*) as amount from pet where ownerid in ("+monetids+") group by petLevel order by petLevel",new Object[]{});
			while(rs.next()){
				map.put(rs.getInt("petLevel"),rs.getInt("amount"));
			}
		} catch (Exception e) {
			logger.error("setDailyUsersLevel error", e);
		}
		return map;
	}
	
	public static Map<Integer,List<Date>> getShipyardLevelPlayTime(String monetids){
		java.util.Map<Integer,List<Date>> map = new java.util.HashMap<Integer, List<Date>>();
		try {
			DBResultSet rs = ConfigUtil.myServerDb.execSQLQuery("select f.monetid,b.buildinglevel,f.newuserflag from fisher f,(select max(b.buildinglevel) as buildinglevel ,monetid from building b where b.monetid in("+monetids+") and b.buildingtype=1 group by monetid) b where f.monetid=b.monetid  order by b.buildinglevel,f.newuserflag desc",new Object[]{});
			while(rs.next()){
				int level = rs.getInt("buildinglevel");
				Date date = rs.getDate("newuserflag");
				if(map.containsKey(level)){
					List<Date> list = map.get(level);
					list.add(date);
					map.put(level,list);
				}else{
					List<Date> list = new ArrayList<Date>();
					list.add(date);
					map.put(level,list);
				}
			}
		} catch (Exception e) {
			logger.error("setDailyUsersLevel error", e);
		}
		return map;
	}
	
	public static Map<Integer,List<Date>> getPetPlayTime(String monetids){
		java.util.Map<Integer,List<Date>> map = new java.util.HashMap<Integer, List<Date>>();
		try {
			DBResultSet rs = ConfigUtil.myServerDb.execSQLQuery("select p.petlevel,f.newuserflag from pet p,fisher f where f.monetid in ("+monetids+") and f.monetid=p.ownerid",new Object[]{});
			while(rs.next()){
				int level = rs.getInt("petlevel");
				Date date = rs.getDate("newuserflag");
				if(map.containsKey(level)){
					List<Date> list = map.get(level);
					list.add(date);
					map.put(level,list);
				}else{
					List<Date> list = new ArrayList<Date>();
					list.add(date);
					map.put(level,list);
				}
			}
		} catch (Exception e) {
			logger.error("getPetPlayTime error", e);
		}
		return map;
	}
	
	public static void setDailyUsersLevel(String type,int level,int amount,long averagePlayTime,long midPlayTime,long minPlayTime,Date ctime){
		try {
			ConfigUtil.myPortalDb.execSQLUpdate("insert into dailyuserlevel(type,level,amount,averagePlayTime,midPlayTime,minPlayTime,ctime) values(?,?,?,?,?,?,?)",new Object[] {type,level,amount,averagePlayTime,midPlayTime,minPlayTime,ctime});
		} catch (Exception e) {
			logger.error("setDailyUsersLevel" , e);
		}
	}
	
	
	public static void main(String [] args){
		Date stime;
		Date etime;
		try {
			
			stime = sdf.parse("2013-10-29");
			etime = sdf.parse("2013-10-30");
			while(stime.getTime()<etime.getTime()){
				startStats(stime);
				Calendar c = Calendar.getInstance();
				c.setTime(stime);
				c.add(Calendar.DATE, 1);
				stime = c.getTime();
			}
			
			//startStats(etime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	static void recalcMau(Date date){
		try{
			Date ftime = sdf.parse("2013-08-07");
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			StringBuffer sb = new StringBuffer("");
			for(int i = 0;i<30;i++){
				if(c.getTime().getTime()>=ftime.getTime()){
					sb.append(ConfigUtil.dailyMonetidList+sdf.format(c.getTime())+"_newoa.txt ");
				}
				c.add(Calendar.DATE, -1);
			}
			
			
			String []cmdArray = new String[]{ "/bin/sh", "-c", "sort -u "+sb.toString()+"|wc -l"};
			System.out.println("---------------------------startProcess:"+"sort -u "+sb.toString()+"|wc -l");
			Process process = Runtime.getRuntime().exec(cmdArray);
			//process.waitFor();  
			InputStream  in = process.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in));
			String result = read.readLine();
			//System.out.println(result);
			int _r = new Integer(result);
			OAproKPI.setKpi("MAU",_r,date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
