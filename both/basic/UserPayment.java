package both.basic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import util.MyUtil;
import util.DBResultSet;

public class UserPayment {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/*public static Map<Integer,Integer> getServer1PayUser(Date date){
		try{
			DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		}
	}*/
	
	
	/*public static Map<Integer,Integer> getServer1PayUser(Date stime,Date etime){
		try{
			DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? group by monet_id",new Object[]{stime,etime}) ;
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		}
	}
	*/
	
	/*public static Map<Integer,Integer> getServer2PayUser(Date date){
		try{
			DBResultSet rs = ConfigUtil.server2db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		}
	}*/
	
	
	/*public static Map<Integer,Integer> getServer2PayUser(Date stime,Date etime){
		try{
			DBResultSet rs = ConfigUtil.server2db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? group by monet_id",new Object[]{stime,etime}) ;
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		}
	}*/
	
	public static Map<Integer,Integer> getPayUser(Date date){
		try{
			DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			
		 	rs = ConfigUtil.server2db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
		 	while(rs.next()){
				int _monetid = rs.getInt("monet_id");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
		 	
		 	rs = ConfigUtil.server3db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
		 	while(rs.next()){
				int _monetid = rs.getInt("monet_id");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
		 	
		 	rs = ConfigUtil.server4db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
		 	while(rs.next()){
				int _monetid = rs.getInt("monet_id");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
		 	
		 	rs = ConfigUtil.server5db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
		 	while(rs.next()){
				int _monetid = rs.getInt("monet_id");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
		 	rs = ConfigUtil.server6db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
		 	while(rs.next()){
				int _monetid = rs.getInt("monet_id");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
		 	
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		}
	}
	
	
	
	public static Map<Integer,Integer> getPayUserByChannel(String channel,Map<Integer,Integer> userMap){
		try{
			String idList = getUserList(userMap);
			if("".equals(idList)){
				return new HashMap<Integer,Integer>();
			}
			
			DBResultSet rs = ConfigUtil.regdb.execSQLQuery("select distinct user_id monetid from user_info where channel = ? and user_id in("+idList+")",new Object[]{channel}) ;
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				resultMap.put(_monetid,userMap.get(_monetid));
			}
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<Integer,Integer>();
		}
	}
	
	
	/*public static Map<Integer,Integer> getPayUserByBilling(String billing,Map<Integer,Integer> userMap){
		try{
			String idList = getUserList(userMap);
			
			if("".equals(idList)){
				return new HashMap<Integer,Integer>();
			}
			
			DBResultSet rs = ConfigUtil.regdb.execSQLQuery("select distinct user_id monetid from user_info where billing = ? and user_id in("+idList+")",new Object[]{billing}) ;
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				resultMap.put(_monetid,userMap.get(_monetid));
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		}
	}
	*/

	public static Map<Integer,Integer> getEgyptBilling(Date date){
		String begingDate = MyUtil.DateToString(date);
		String endDate = begingDate + " 23:59:59";
		try{
			DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select  monet_id ,amount/100 amount from callbackstc where date >= ? and date <= ? and operator = 'egypt'",new Object[]{begingDate,endDate}) ;
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			while(rs.next()){
				int _monetid = rs.getInt("monet_id");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server2db.execSQLQuery("select  monet_id ,amount/100 amount from callbackstc where date >= ? and date <= ? and operator = 'egypt'",new Object[]{begingDate,endDate}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monet_id");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server3db.execSQLQuery("select  monet_id ,amount/100 amount from callbackstc where date >= ? and date <= ? and operator = 'egypt'",new Object[]{begingDate,endDate}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monet_id");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server4db.execSQLQuery("select  monet_id ,amount/100 amount from callbackstc where date >= ? and date <= ? and operator = 'egypt'",new Object[]{begingDate,endDate}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monet_id");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			rs = ConfigUtil.server5db.execSQLQuery("select  monet_id ,amount/100 amount from callbackstc where date >= ? and date <= ? and operator = 'egypt'",new Object[]{begingDate,endDate}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monet_id");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server6db.execSQLQuery("select  monet_id ,amount/100 amount from callbackstc where date >= ? and date <= ? and operator = 'egypt'",new Object[]{begingDate,endDate}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monet_id");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		}
	}
	

	private static String getUserList(Map<Integer,Integer> userMap){
		StringBuffer sb = new  StringBuffer("");
		if(userMap!=null&&userMap.size()>0){
			for(int id : userMap.keySet()){
				sb.append(id+",");
			}
			return sb.toString().substring(0,sb.toString().length()-1);
		}else{
			return "";	
		}
	}
}
