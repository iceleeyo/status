package both.basic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import util.DBResultSet;

public class ReportPayment {
	public static Map<Integer,Integer> getPayUser(Date stime,Date etime){
		try{
			DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? group by monet_id",new Object[]{stime,etime}) ;
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
		 	rs = ConfigUtil.server2db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server3db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server4db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server5db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server6db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		}
	}
	
	public static Map<Integer,Integer> getSTCPayUser(Date stime,Date etime){
		try{
			DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'stc' group by monet_id",new Object[]{stime,etime}) ;
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
		 	rs = ConfigUtil.server2db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'stc' group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server3db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'stc' group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server4db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'stc' group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server5db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'stc' group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server6db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'stc' group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		}
	}
	
	public static Map<Integer,Integer> getSTCPayUserByServer(Date stime,Date etime,String server){
		try{
			DBResultSet rs = null;
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			if("server1".equals(server)){
				rs = ConfigUtil.server1db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'stc' group by monet_id",new Object[]{stime,etime}) ;
				
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("server2".equals(server)){
				rs = ConfigUtil.server2db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'stc' group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("server3".equals(server)){
				rs = ConfigUtil.server3db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'stc' group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("server4".equals(server)){
				rs = ConfigUtil.server4db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'stc' group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("server5".equals(server)){
				rs = ConfigUtil.server5db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'stc' group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("server6".equals(server)){
				rs = ConfigUtil.server6db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'stc' group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		}
	}
	
	
	public static Map<Integer,Integer> getEgyptPayUser(Date stime,Date etime){
		try{
			DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'egypt' group by monet_id",new Object[]{stime,etime}) ;
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
		 	rs = ConfigUtil.server2db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'egypt' group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server3db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'egypt' group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server4db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'egypt' group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			rs = ConfigUtil.server5db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'egypt' group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			rs = ConfigUtil.server6db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'egypt' group by monet_id",new Object[]{stime,etime}) ;
			while(rs.next()){
				int monetid = rs.getInt("monetid");
				if(resultMap.containsKey(monetid)){
					int amount = resultMap.get(monetid);
					resultMap.put(monetid, amount+rs.getInt("amount"));
				}else{
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		}
	}
	
	
	public static Map<Integer,Integer> getEgyptPayUserByServer(Date stime,Date etime,String server){
		try{
			DBResultSet rs = null;
			Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
			if("server1".equals(server)){
				rs = ConfigUtil.server1db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'egypt' group by monet_id",new Object[]{stime,etime}) ;
				
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("server2".equals(server)){
				rs = ConfigUtil.server2db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'egypt' group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("server3".equals(server)){
				rs = ConfigUtil.server3db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'egypt' group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("server4".equals(server)){
				rs = ConfigUtil.server4db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'egypt' group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}else if("server5".equals(server)){
				rs = ConfigUtil.server5db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'egypt' group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			else if("server6".equals(server)){
				rs = ConfigUtil.server6db.execSQLQuery("select sum(amount) amount,monet_id monetid from callbackstc where date>= ? and date <? and operator= 'egypt' group by monet_id",new Object[]{stime,etime}) ;
				while(rs.next()){
					resultMap.put(rs.getInt("monetid"),rs.getInt("amount"));
				}
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer,Integer>();
		}
	}
	
}
