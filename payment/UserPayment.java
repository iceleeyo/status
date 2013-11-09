package payment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import util.DBResultSet;
import both.basic.ConfigUtil;

public class UserPayment {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void calcTop10Payment(Date date){
		try{
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, 1);
			
			DBResultSet ds = ConfigUtil.myServerDb.execSQL("select top 10 sum(amount)/100 amount,monet_id monetid from callbackstc where date >=? and date < ? group by monet_id order by amount desc", new Object[]{date,c.getTime()});
			StringBuffer sb = new StringBuffer("");
			Map<Integer,Integer[]> resultMap = new HashMap<Integer,Integer[]>();
			
			while(ds.next()){
				int monetid = ds.getInt("monetid");
				int amount = ds.getInt("amount");
				Integer[] _t = {monetid,amount,0};
				sb.append(monetid+",");
				resultMap.put(monetid, _t);
			}
			
			String idList = sb.toString();
			if(idList.length()>=1){
				idList = idList.substring(0,idList.length()-1);
			}
			
			if(""!=idList){
				ds = ConfigUtil.myServerDb.execSQL("select sum(amount)/100 amount,monet_id monetid from callbackstc where date < ? group by monet_id", new Object[]{date});
				
				while(ds.next()){
					int monetid = ds.getInt("monetid");
					int amount = ds.getInt("amount");
					Integer [] _t = resultMap.get(monetid);
					if(_t!=null){
						_t[2]=amount;
						resultMap.put(monetid, _t);
					}
				}
			}
			
			for(int id : resultMap.keySet()){
				Integer [] _t = resultMap.get(id);
				ConfigUtil.myPortalDb.execSQLUpdate("insert into top10Payment(monetid,todayPay,hisPay,dtime) values (?,?,?,?)", new Object[]{_t[0],_t[1],_t[2],date});
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args){

		Date stime;
		Date etime;
		try {
			stime = sdf.parse("2013-08-01");
			etime = sdf.parse("2013-08-20");
			
			Calendar c = Calendar.getInstance();
			c.setTime(stime);
			
			while(c.getTimeInMillis()<etime.getTime()){
				calcTop10Payment(c.getTime());
				c.add(Calendar.DATE, 1);
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
