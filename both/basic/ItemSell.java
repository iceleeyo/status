package both.basic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import util.DBResultSet;
import util.MyUtil;


public class ItemSell {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	static ArrayList<Integer> testList = MyUtil.getTestAccount();
	
	
	public static Map<String,Object[]> getGroupUpByServer(Date date,String server){
		try{
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, 1);
			
			String testList = getTestIdList();
			String splitId = "";
			if(testList!=null&&!"".equals(testList)){
				splitId = " and monetid not in ("+testList+") ";
			}
			
			DBResultSet rs = ConfigUtil.myPortalDb.execSQLQuery("select sum(price) price,count(distinct monetid) users,count(*) amount,itemtype from(select i.*,i1.itemtype from itembuy i,itemconfig i1 where i.item=i1.itemname and btime >= ? and btime < ? and server = ? "+splitId+") t group by itemtype",new Object[]{date,c.getTime(),server}) ;
			Map<String,Object[]> resultMap = new HashMap<String,Object[]>();
			while(rs.next()){
				Object[] _temp = {0,0,0,rs.getString("itemtype"),sdf.format(date)};
				_temp[0]=rs.getInt("price");
				_temp[1]=rs.getInt("users");
				_temp[2]=rs.getInt("amount");
				resultMap.put(rs.getString("itemtype"), _temp);
			}
			
			
			rs = ConfigUtil.myPortalDb.execSQLQuery("select sum(price) price,count(distinct monetid) users,count(*) amount from(select i.*,i1.itemtype from itembuy i,itemconfig i1 where i.item=i1.itemname and btime >= ? and btime < ? and server = ? "+splitId+") t ",new Object[]{date,c.getTime(),server}) ;
			if(rs.next()){
				Object[] _temp = {0,0,0,"ALL",sdf.format(date)};
				_temp[0]=rs.getInt("price");
				_temp[1]=rs.getInt("users");
				_temp[2]=rs.getInt("amount");
				resultMap.put("ALL", _temp);
			}
			return resultMap;
		}catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String,Object[]>();
		}
	}
		
	
	
	public static void main(String [] args){
		try {
			Date t = sdf.parse("2013-08-05");
			System.out.println(getGroupUpByServer(t,"server1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	static String getTestIdList(){
		StringBuffer sb = new StringBuffer("");
		for(int id:testList){
			sb.append(id+",");
		}
		String returnStr = sb.toString();
		
		if(returnStr!=null&&returnStr.length()>=1){
			returnStr = returnStr.substring(0,returnStr.length()-1);
			return returnStr;
		}else{
			return "";
		}
	}
}
