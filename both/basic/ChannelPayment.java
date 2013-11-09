package both.basic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import util.DBResultSet;

public class ChannelPayment {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Map<Integer,Integer> getFortumoPayment(Date date){
		try{
			
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where (config_id=101 or config_id=102 or config_id=106) and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			
			rs = ConfigUtil.server2db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where (config_id=101 or config_id=102 or config_id=106) and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server3db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where (config_id=101 or config_id=102 or config_id=106) and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server4db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where (config_id=101 or config_id=102 or config_id=106) and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server5db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where (config_id=101 or config_id=102 or config_id=106) and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server6db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where (config_id=101 or config_id=102 or config_id=106) and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			return resultMap;
		}catch (Exception e) {
			return  new HashMap<Integer,Integer>();
		}
	}
	
	public static Map<Integer,Integer> getGoogleWalletPayment(Date date){
		try{
			
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			
			rs = ConfigUtil.server2db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server3db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server4db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server5db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server6db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			return resultMap;
		}catch (Exception e) {
			return  new HashMap<Integer,Integer>();
		}
	}
	
	public static Map<Integer,Integer> getGoogleWalletPayment(Date stime,Date etime){
		try{
			
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and date >=? and date < ? group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			
			rs = ConfigUtil.server2db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and date >=? and date < ? group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			
			rs = ConfigUtil.server3db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and date >=? and date < ? group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			
			rs = ConfigUtil.server4db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and date >=? and date < ? group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			
			rs = ConfigUtil.server5db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and date >=? and date < ? group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			
			rs = ConfigUtil.server6db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and date >=? and date < ? group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			
			return resultMap;
		}catch (Exception e) {
			return  new HashMap<Integer,Integer>();
		}
	}
	
	public static Map<Integer,Integer> getGoogleWalletPaymentByServer(Date stime,Date etime,String server){
		try{
			
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			
			if("Server1".equals(server)){
				DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and date >=? and date < ? group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("Server2".equals(server)){
				DBResultSet rs = ConfigUtil.server2db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and date >=? and date < ? group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("Server3".equals(server)){
				DBResultSet rs = ConfigUtil.server3db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and date >=? and date < ? group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("Server4".equals(server)){
				DBResultSet rs = ConfigUtil.server4db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and date >=? and date < ? group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("Server5".equals(server)){
				DBResultSet rs = ConfigUtil.server5db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and date >=? and date < ? group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("Server6".equals(server)){
				DBResultSet rs = ConfigUtil.server6db.execSQLQuery("select sum(price) amount,monet_id monetid from google_wallet where status=1 and date >=? and date < ? group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			return resultMap;
		}catch (Exception e) {
			return  new HashMap<Integer,Integer>();
		}
	}
	
	
	
	public static Map<Integer,Integer> getStcPayment(Date date){
		try{
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where config_id!=101 and config_id !=102 and config_id !=106 and config_id != 201 and convert(varchar(10),date,20)= ? and operator = 'stc' group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			
			
			rs = ConfigUtil.server2db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where config_id!=101 and  config_id !=102 and config_id !=106 and config_id != 201 and convert(varchar(10),date,20)= ?  and operator = 'stc' group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server3db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where config_id!=101 and  config_id !=102 and config_id !=106 and config_id != 201 and convert(varchar(10),date,20)= ?  and operator = 'stc' group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			
			rs = ConfigUtil.server4db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where config_id!=101 and  config_id !=102 and config_id !=106 and config_id != 201 and convert(varchar(10),date,20)= ?  and operator = 'stc' group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server5db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where config_id!=101 and  config_id !=102 and config_id !=106 and config_id != 201 and convert(varchar(10),date,20)= ?  and operator = 'stc' group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server6db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where config_id!=101 and  config_id !=102 and config_id !=106 and config_id != 201 and convert(varchar(10),date,20)= ?  and operator = 'stc' group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			return resultMap;
		}catch (Exception e) {
			return  new HashMap<Integer,Integer>();
		}
	}
	
	
	public static Map<Integer,Integer> getPayPalPayment(Date date){
		try{
			
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where config_id=201 and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
			}
			
			rs = ConfigUtil.server2db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where config_id=201 and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server3db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where config_id=201 and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server4db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where config_id=201 and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			
			rs = ConfigUtil.server5db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where config_id=201 and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server6db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where config_id=201 and convert(varchar(10),date,20)= ? group by monet_id",new Object[]{sdf.format(date)}) ;
			while(rs.next()){
				int _monetid = rs.getInt("monetid");
				if(resultMap.containsKey(_monetid)){
					int count = resultMap.get(_monetid);
					resultMap.put(_monetid, rs.getInt("amount") + count);
				}else{
					resultMap.put(_monetid, rs.getInt("amount"));
				}
			}
			
			return resultMap;
		}catch (Exception e) {
			return  new HashMap<Integer,Integer>();
		}
	}
	
	
	public static void main(String[] args){
		try{
			Date d = sdf.parse("2013-09-08");
			Map<Integer,Integer> a = ChannelPayment.getStcPayment(d);
			System.out.println(a.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
