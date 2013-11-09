package egypt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import util.DBResultSet;
import util.MyUtil;
import both.basic.ConfigUtil;

public class GetEgyptUserInfo {
	static Logger logger = Logger.getLogger(GetEgyptUserInfo.class);
	public static void insertToEgypt(Date date) {
		Map<String, Integer> info = getInfo(date);
		int allUser = info.get("allUser");
		int reguser = info.get("reguser");
		int newuser = info.get("newuser");
		int actionuser = info.get("actionuser");
		int payPnum = info.get("payPnum");
		int payAmount = info.get("payAmount");
		try {
			Object[] dbArgs = new Object[] { date, "olduser", allUser };
			ConfigUtil.portal1db.execSQLUpdate(
							"insert into egypt_user(edate,egkey,egvalue) values(?,?,?)",
							dbArgs);
			dbArgs = new Object[] { date, "reguser", reguser };
			ConfigUtil.portal1db.execSQLUpdate(
							"insert into egypt_user(edate,egkey,egvalue) values(?,?,?)",
							dbArgs);
			dbArgs = new Object[] { date, "newuser", newuser };
			ConfigUtil.portal1db.execSQLUpdate(
							"insert into egypt_user(edate,egkey,egvalue) values(?,?,?)",
							dbArgs);
			dbArgs = new Object[] { date, "actionuser", actionuser };
			ConfigUtil.portal1db.execSQLUpdate(
							"insert into egypt_user(edate,egkey,egvalue) values(?,?,?)",
							dbArgs);
			dbArgs = new Object[] { date, "payPnum", payPnum };
			ConfigUtil.portal1db.execSQLUpdate(
							"insert into egypt_user(edate,egkey,egvalue) values(?,?,?)",
							dbArgs);
			dbArgs = new Object[] { date, "payAmount", payAmount };
			ConfigUtil.portal1db.execSQLUpdate(
							"insert into egypt_user(edate,egkey,egvalue) values(?,?,?)",
							dbArgs);

		} catch (Exception e) {
			logger.error("egypt:getInfo main insert ", e);
		}

		/*System.out.println(new Date() + "这天之前的总用户数：" + info.get("allUser"));
		System.out.println(new Date() + "这天注册用户数：" + info.get("reguser"));
		System.out.println(new Date() + "这天的新用户数：" + info.get("newuser"));
		System.out.println(new Date() + "这天的活跃用户数：" + info.get("actionuser"));
		System.out.println(new Date() + "这天的付费人数：" + info.get("payPnum"));
		System.out.println(new Date() + "这天的的总付费金额：" + info.get("payAmount"));*/
	}

	static private Map<String, Integer> getInfo(Date date) {
		Map<String, Integer> resultmap = new HashMap<String, Integer>();
		String dateBegin = MyUtil.DateToString(date);
		dateBegin = dateBegin.substring(0,10);
		dateBegin = dateBegin.replaceAll("-", "");
		
		String dateEnd = dateBegin + " 23:59:59";

		List<Integer> userlist = new ArrayList<Integer>();
		try {
			Object[] dbArgs = new Object[] { "egypt", dateEnd };
			DBResultSet rs = ConfigUtil.regdb
					.execSQLQuery(
							"select  user_id from  user_info where billing = ? and create_date < ?",
							dbArgs);
			while (rs.next()) {
				userlist.add(rs.getInt("user_id"));
			}
		} catch (Exception e) {
			logger.error("getInfo", e);
		}
		int alluser = userlist.size();
		resultmap.put("allUser", alluser);

		List<Integer> reguser = new ArrayList<Integer>();
		try {
			Object[] dbArgs = new Object[] { "egypt", dateBegin, dateEnd };
			DBResultSet rs = ConfigUtil.regdb
					.execSQLQuery(
							"select  user_id from  user_info where billing = ? and create_date > ? and create_date < ? ",
							dbArgs);
			while (rs.next()) {
				reguser.add(rs.getInt("user_id"));
			}
		} catch (Exception e) {
			logger.error("getInfo", e);
		}
		int reg = reguser.size();
		resultmap.put("reguser", reg);

		List<Integer> newuserlist = new ArrayList<Integer>();
		try {
			Object[] dbArgs = new Object[] { dateBegin, dateEnd };
			DBResultSet rs = ConfigUtil.server2db.execSQLQuery(
							"select distinct monetid  from fisher where newUserflag >? and newUserflag < ?",
							dbArgs);
			while (rs.next()) {
				newuserlist.add(rs.getInt("monetid"));
			}
		} catch (Exception e) {
			logger.error("getInfo", e);
		}
		int newuser = getretainForlist(newuserlist, userlist);
		resultmap.put("newuser", newuser);

		List<Integer> actionuserlist = new ArrayList<Integer>();
		try {
			Object[] dbArgs = new Object[] { dateBegin, dateEnd };
			DBResultSet rs = ConfigUtil.server2db.execSQLQuery(
							"select distinct monetid  from user_event where server_date > ? and server_date < ?",
							dbArgs);
			while (rs.next()) {
				actionuserlist.add(rs.getInt("monetid"));
			}
		} catch (Exception e) {
			logger.error("getInfo", e);
		}
		int actionuser = getretainForlist(actionuserlist, userlist);
		resultmap.put("actionuser", actionuser);

		Map<Integer, Integer> payAmountMap = new HashMap<Integer, Integer>();
		try {
			Object[] dbArgs = new Object[] { dateBegin, dateEnd };
			DBResultSet rs = ConfigUtil.server2db.execSQLQuery(
							"select  monet_id ,sum(amount)/100 Amount from callbackSTC where date > ? and date < ? group by monet_id",
							dbArgs);
			while (rs.next()) {
				payAmountMap.put(rs.getInt("monet_id"), rs.getInt("Amount"));
			}
		} catch (Exception e) {
			logger.error("getInfo", e);
		}
		int payAmount = getAmount(payAmountMap, userlist);

		resultmap.put("payPnum",
				getretainForlist(getListFromMapkay(payAmountMap), userlist));
		resultmap.put("payAmount", payAmount);

		return resultmap;
	}

	private static int getretainForlist(List<Integer> list1, List<Integer> list2) {
		list1.retainAll(list2);
		return list1.size();
	}

	private static int getAmount(Map<Integer, Integer> map, List<Integer> list) {
		int sum = 0;
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			int key = entry.getKey().intValue();
			for (int monetid : list) {
				if (monetid == key) {
					sum += entry.getValue().intValue();
				}
			}
		}
		return sum;
	}

	private static List<Integer> getListFromMapkay(Map<Integer, Integer> map) {
		List<Integer> list = new ArrayList<Integer>();
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			int key = entry.getKey().intValue();
			list.add(key);
		}
		return list;
	}

}
