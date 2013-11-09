package egypt;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import util.DBResultSet;
import both.basic.ConfigUtil;

public class EgyptUser {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void printEgptyUser(Date date){
		FileWriter  fr = null;
		try{
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, 1);
			
			DBResultSet ds = ConfigUtil.regdb.execSQLQuery("select user_id monetid from user_info where billing = 'Egypt' and create_date >= ? and create_date < ?", new Object[]{date,c.getTime()});
			fr = new FileWriter(ConfigUtil.egpytUser,true);
			while(ds.next()){
				fr.write(ds.getInt("monetid")+"\r\n");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(fr!=null)fr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public static void calcEgyptUser(Date date){
		printEgptyUser(date);
		InputStream  in = null;
		BufferedReader read  = null;
		try{
			// 活跃用户
			String []cmdArray = new String[]{ "/bin/sh", "-c", "grep -xFf "+ConfigUtil.egpytUser+" "+ConfigUtil.dailyMonetidList+sdf.format(date)+"_newoa.txt"+"|wc -l"};
			System.out.println("[Run]"+"grep -xFf "+ConfigUtil.egpytUser+" "+ConfigUtil.dailyMonetidList+sdf.format(date)+"_newoa.txt"+"|wc -l");
			Process process = Runtime.getRuntime().exec(cmdArray);
			in = process.getInputStream();
			read = new BufferedReader(new InputStreamReader(in));
			String _s = read.readLine();
			Integer dau = 0;
			if(_s!=null){
				dau = new Integer(_s);
			}
			System.out.println(dau);
			
			
			//历史用户
			cmdArray = new String[]{ "/bin/sh", "-c", "cat "+ConfigUtil.egpytUser+"|wc -l"};
			System.out.println("[Run]"+"cat "+ConfigUtil.egpytUser+"|wc -l");
			
			process = Runtime.getRuntime().exec(cmdArray);
			in = process.getInputStream();
			read = new BufferedReader(new InputStreamReader(in));
			_s = read.readLine();
			Integer hau = 0;
			if(_s!=null){
				hau = new Integer(_s);
			}
			System.out.println(hau);
			
			//注册用户
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, 1);
			
			Integer rau = 0;  
			DBResultSet ds = ConfigUtil.regdb.execSQLQuery("select count(*) amount from user_info where billing = 'Egypt' and create_date >= ? and create_date < ?", new Object[]{date,c.getTime()});
			if(ds.next()){
				rau = ds.getInt("amount");
			}
			
			//新用户
			StringBuffer sb = new StringBuffer("");
			ds = ConfigUtil.myServerDb.execSQLQuery("select monetid from fisher where newuserflag >= ? and newuserflag < ?", new Object[]{date,c.getTime()});
			while(ds.next()){
				sb.append(ds.getInt("monetid")+",");
			}
			String idList = sb.toString();
			if(idList.length()>=1){
				idList = idList.substring(0,idList.length()-1);
			}
			
			int nau = 0;
			if(idList.length()>0){
				ds = ConfigUtil.regdb.execSQLQuery("select count(*) amount from user_info where user_id in("+idList+") and billing = 'Egypt'", new Object[]{});
				if(ds.next()){
					nau = ds.getInt("amount");
				}
			}
			//付费用户和付费金额
			
			int pau = 0;
			int topup = 0;
			ds = ConfigUtil.myServerDb.execSQLQuery("select sum(amount)/100 amount,monet_id monetid from  callbackstc where date >= ? and date < ? group by monet_id", new Object[]{date,c.getTime()});
			Map<Integer,Integer> puser = new HashMap<Integer,Integer>();
			while(ds.next()){
				puser.put(ds.getInt("monetid"), ds.getInt("amount"));
			}
			sb = new StringBuffer("");
			for(int id:puser.keySet()){
				sb.append(id+",");
			}
			
			idList = sb.toString();
			if(idList.length()>=1){
				idList = idList.substring(0,idList.length()-1);
			}
			
			if(idList.length()>0){
				ds = ConfigUtil.regdb.execSQLQuery("select user_id monetid from user_info where user_id in("+idList+") and billing = 'Egypt'", new Object[]{});
				while(ds.next()){
					topup += puser.get(ds.getInt("monetid"));
					pau ++;
				}
			}
			
			setData(hau, "HAU", date);
			setData(rau, "DRU", date);
			setData(dau, "DAU", date);
			setData(nau, "DNU", date);
			setData(pau, "DPU", date);
			setData(topup, "TOPUP", date);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(in!=null)in.close();
				if(read!=null)read.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	static void setData(int value,String key,Date date){
		ConfigUtil.myPortalDb.execSQLUpdate("insert into egyptuser (dtime,oakey,oavalue) values (?,?,?)", new Object[]{date,key,value});
	}
	
	public static void main(String[] args){
		Date stime;
		Date etime;
		try {
			stime = sdf.parse("2013-07-12");
			etime = sdf.parse("2013-08-21");
			
			Calendar c = Calendar.getInstance();
			c.setTime(stime);
			
			while(c.getTimeInMillis()<etime.getTime()){
				calcEgyptUser(c.getTime());
				c.add(Calendar.DATE, 1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/*Date stime;
		try {
			stime = sdf.parse("2013-08-20");
			calcEgyptUser(stime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
