package user;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import util.DBResultSet;
import both.basic.ConfigUtil;
import both.basic.Users;

public class UserPlatformCount{
static Logger logger = Logger.getLogger(UserPlatformCount.class);
static SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
static DecimalFormat df =  new DecimalFormat("####.##");
	
	public static void startCount(Date dataTime){
		try {
			Map<Integer, String> newUserMap  = new HashMap<Integer, String>();
			Map<Integer,String> dauMap =  new HashMap<Integer,String>();
			
			Calendar c = Calendar.getInstance();
			c.setTime(dataTime);
			c.add(Calendar.DATE, 1);
			
			System.out.println(dataTime);
			System.out.println(c.getTime());
			
			String s1DauPath = ConfigUtil.dailyMonetidList+"/"+sdf.format(dataTime)+"_newoa.txt";
			BufferedReader br1 = new BufferedReader(new FileReader(s1DauPath));
			String temp = br1.readLine();
			while(temp!=null){
				dauMap.put(Integer.parseInt(temp),"");
				temp = br1.readLine();
			}
			br1.close();
			
			calcPlatForm(dauMap.keySet(), "DAU", dataTime);
			
			
			//----- 新用户
			DBResultSet rs =  ConfigUtil.myServerDb.execSQLQuery("select distinct monetid from fisher where newuserflag >= ? and newuserflag < ?",new Object[]{sdf.format(dataTime),sdf.format(c.getTime())});
			while(rs.next()){
				newUserMap.put(rs.getInt("monetid"), "");
			}
			calcPlatForm(newUserMap.keySet(), "NAU", dataTime);
			
			
			//----- 活跃用户
			Map<Integer,Integer> aauMap = Users.getTraceUser(dataTime, c.getTime());
			calcPlatForm(aauMap.keySet(), "AAU", dataTime);
			//----- 登陆成功用户
			Map<Integer,Integer> rdauMap = Users.getNewOaRequestUser(dataTime, c.getTime());
			calcPlatForm(rdauMap.keySet(), "RDAU", dataTime);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void calcPlatForm(Set<Integer>userSet,String key,Date dataTime){
		StringBuffer sb = new StringBuffer("");
		for(int monetid:userSet){
			sb.append(monetid+",");
		}
		
		String idList = sb.toString();
		if(idList.length()>=1){
			idList = idList.substring(0,idList.length()-1);
		}
		ConfigUtil.myPortalDb.execSQLUpdate("insert into UserPlatform (oakey,oavalue,ldate) select '"+key+"_' + platform,count(*) num,'"+sdf.format(dataTime)+"' from all_users where monetid in("+idList+") group by '"+key+"_' + platform", new Object[]{});
		ConfigUtil.myPortalDb.execSQLUpdate("insert into UserPlatform (oakey,oavalue,ldate) values(?,?,?)",new Object[]{key+"_ALL",userSet.size(),sdf.format(dataTime)});
	}
	
	
	
	public static void main(String args[]){
		try {
			Date dataTime = sdf.parse("2013-08-18");
			Calendar c = Calendar.getInstance();
			c.setTime(dataTime);
			c.add(Calendar.DATE, 1);
			Map<Integer,Integer> rdauMap = Users.getNewOaRequestUser(dataTime, c.getTime());
			calcPlatForm(rdauMap.keySet(), "RDAU", dataTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}