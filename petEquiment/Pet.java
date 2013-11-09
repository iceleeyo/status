package petEquiment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import util.DBResultSet;
import both.basic.ConfigUtil;

public class Pet {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void calcPetUpgradTime(Date date){
		try{
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, 1);
			
			
			DBResultSet ds = ConfigUtil.myPortalDb.execSQLQuery("select max(petlevel) maxpl from petupgrade_new where utime < ? ", new Object[]{c.getTime()});
			int maxlevel = 0;
			if(ds.next()){
				maxlevel = ds.getInt("maxpl");
			}
			
			if(maxlevel>1){
				for(int i = 1;i<maxlevel;i++){
					ds = ConfigUtil.myPortalDb.execSQLQuery("select cast(avg(dfs)/60/60/24 as float) dfd from (select b5.monetid,cast(datediff(ss,b1.lv1time,b5.lv5time) as float) dfs from (select monetid,utime lv5time from petupgrade_new where utime < ? and petlevel = ?  ) b5  join (select monetid,utime lv1time from petupgrade_new where petlevel = ?  ) b1 on b5.monetid = b1.monetid) f where dfs > 0 ", new Object[]{c.getTime(),i+1,i});
					if(ds.next()){
						ConfigUtil.myPortalDb.execSQLUpdate("insert into UpgradePetTime(oakey,oavalue,dtime) values (?,?,?)", new Object[]{"LV"+(i+1),ds.getFloat("dfd"),date});
					}
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[]){
		Date stime;
		Date etime;
		try {
			stime = sdf.parse("2013-09-11");
			etime = sdf.parse("2013-09-12");
			
			Calendar c = Calendar.getInstance();
			c.setTime(stime);
			
			while(c.getTimeInMillis()<etime.getTime()){
				calcPetUpgradTime(c.getTime());
				c.add(Calendar.DATE, 1);
			}
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
