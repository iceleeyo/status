package shipyard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import util.DBResultSet;
import both.basic.ConfigUtil;

public class Shipyard {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void calcShipyardUpgradTime(Date date){
		try{
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, 1);
			
			
			DBResultSet ds = ConfigUtil.myServerDb.execSQLQuery("select max(buildinglevel) maxbl from building where buildingtype = 1 and time < ?", new Object[]{c.getTime()});
			int maxlevel = 0;
			if(ds.next()){
				maxlevel = ds.getInt("maxbl");
			}
			
			if(maxlevel>1){
				for(int i = 1;i<maxlevel;i++){
					ds = ConfigUtil.myServerDb.execSQLQuery("select cast(avg(dfs)/60/60/24 as float) dfd from (select b5.monetid,cast(datediff(ss,b1.lv1time,b5.lv5time) as float) dfs from (select monetid,time lv5time from building where buildingtype = 1 and time < ? and buildinglevel = ? ) b5 left join (select monetid,time lv1time from building where buildingtype = 1 and buildinglevel = ? ) b1 on b5.monetid = b1.monetid) f where dfs > 0 ", new Object[]{c.getTime(),i+1,i});
					if(ds.next()){
						ConfigUtil.myPortalDb.execSQLUpdate("insert into UpgradeShipyardTime(oakey,oavalue,dtime) values (?,?,?)", new Object[]{"LV"+(i+1),ds.getFloat("dfd"),date});
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
			stime = sdf.parse("2013-08-21");
			etime = sdf.parse("2013-08-29");
			
			Calendar c = Calendar.getInstance();
			c.setTime(stime);
			
			while(c.getTimeInMillis()<etime.getTime()){
				calcShipyardUpgradTime(c.getTime());
				c.add(Calendar.DATE, 1);
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
