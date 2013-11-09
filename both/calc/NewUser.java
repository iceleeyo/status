package both.calc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import both.basic.ConfigUtil;
import both.basic.Dau;
import both.basic.NewUsers;

public class NewUser {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static void userLogin(Date date){
		Map<Integer,String> DauMap = Dau.getAllDau(date);
		Map<Integer,String> RegUserMap = NewUsers.getRegNewUsers(date);
		Map<Integer,String> DbNewUserMap =NewUsers.getDbNewUsers(date);
		
		
		//注册用户
		int allRegUserCount = RegUserMap.size();
		//注册用户登陆
		int allRegLoginCount = 0;
		//注册用户成为NewFish
		int allRegDbCount = 0;
		
		for(int id : RegUserMap.keySet()){
			if(DauMap.containsKey(id)){
				allRegLoginCount ++;
			}
			if(DbNewUserMap.containsKey(id)){
				allRegDbCount ++;
			}
		}
		
		
		ConfigUtil.portal1db.execSQLUpdate("insert into regUserConvert(oakey,oavalue,rdate) values (?,?,?)", new Object[]{"all_reg",allRegUserCount,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into regUserConvert(oakey,oavalue,rdate) values (?,?,?)", new Object[]{"all_login",allRegLoginCount,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into regUserConvert(oakey,oavalue,rdate) values (?,?,?)", new Object[]{"all_db",allRegDbCount,sdf.format(date)});

		//-------------------------------------------------------------------------------------------
		//inmobi注册用户
		Map<Integer,String> inmobiRegUserMap = NewUsers.getRegNewUsersByChannel(date, "inmobi");
		int inmobiRegUserCount = inmobiRegUserMap.size();
		int inmobiRegDbCount = 0;
		int inmobiRegLoginCount = 0;
		for(int id : inmobiRegUserMap.keySet()){
			if(DauMap.containsKey(id)){
				inmobiRegLoginCount ++;
			}
			if(DbNewUserMap.containsKey(id)){
				inmobiRegDbCount ++;
			}
		}
		
		
		ConfigUtil.portal1db.execSQLUpdate("insert into regUserConvert(oakey,oavalue,rdate) values (?,?,?)", new Object[]{"inmobi_reg",inmobiRegUserCount,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into regUserConvert(oakey,oavalue,rdate) values (?,?,?)", new Object[]{"inmobi_login",inmobiRegLoginCount,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into regUserConvert(oakey,oavalue,rdate) values (?,?,?)", new Object[]{"inmobi_db",inmobiRegDbCount,sdf.format(date)});

		//-------------------------------------------------------------------------------------------
		//Egypt注册用户
		
		
		Map<Integer,String> EgyptRegUserMap = NewUsers.getRegNewUsersByBilling(date, "Egypt");
		int EgyptRegUserCount = EgyptRegUserMap.size();
		int EgyptRegDbCount = 0;
		int EgyptRegLoginCount = 0;
		
		for(int id : EgyptRegUserMap.keySet()){
			if(DauMap.containsKey(id)){
				EgyptRegLoginCount ++;
			}
			if(DbNewUserMap.containsKey(id)){
				EgyptRegDbCount ++;
			}
		}
		
		ConfigUtil.portal1db.execSQLUpdate("insert into regUserConvert(oakey,oavalue,rdate) values (?,?,?)", new Object[]{"Egypt_reg",EgyptRegUserCount,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into regUserConvert(oakey,oavalue,rdate) values (?,?,?)", new Object[]{"Egypt_login",EgyptRegLoginCount,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into regUserConvert(oakey,oavalue,rdate) values (?,?,?)", new Object[]{"Egypt_db",EgyptRegDbCount,sdf.format(date)});
		
		//-------------------------------------------------------------------------------------------
		//Stc注册用户
		Map<Integer,String> stcRegUserMap = new HashMap<Integer,String>();
		stcRegUserMap.putAll(RegUserMap);
		for(int id : EgyptRegUserMap.keySet()){
			stcRegUserMap.remove(id);
		}
		
		for(int id : inmobiRegUserMap.keySet()){
			stcRegUserMap.remove(id);
		}
		int stcRegUserCount = stcRegUserMap.size();
		int stcRegDbCount = 0;
		int stcRegLoginCount = 0;
		
		for(int id : stcRegUserMap.keySet()){
			if(DauMap.containsKey(id)){
				stcRegLoginCount ++;
			}
			if(DbNewUserMap.containsKey(id)){
				stcRegDbCount ++;
			}
		}
		
		ConfigUtil.portal1db.execSQLUpdate("insert into regUserConvert(oakey,oavalue,rdate) values (?,?,?)", new Object[]{"stc_reg",stcRegUserCount,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into regUserConvert(oakey,oavalue,rdate) values (?,?,?)", new Object[]{"stc_login",stcRegLoginCount,sdf.format(date)});
		ConfigUtil.portal1db.execSQLUpdate("insert into regUserConvert(oakey,oavalue,rdate) values (?,?,?)", new Object[]{"stc_db",stcRegDbCount,sdf.format(date)});
		
	}
	
	public static void main(String[] args) {
		try {
			userLogin(sdf.parse(args[0]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
