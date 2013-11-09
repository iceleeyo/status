package both.basic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import util.DBResultSet;

public class NewUsers {
	static Logger logger = Logger.getLogger(NewUsers.class);
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static Map<Integer, String> getRegNewUsers(Date date) {
		try {
			DBResultSet rs = ConfigUtil.regdb
					.execSQLQuery(
							"select distinct user_id monetid from user_info where convert(varchar(10),create_date,20)= ? ",
							new Object[] { sdf.format(date) });
			Map<Integer, String> resultMap = new HashMap<Integer, String>();
			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), "");
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, String>();
		}
	}

	public static Map<Integer, String> getRegNewUsersByChannel(Date date,
			String channel) {
		try {
			DBResultSet rs = ConfigUtil.regdb
					.execSQLQuery(
							"select distinct user_id monetid from user_info where convert(varchar(10),create_date,20)= ? and channel = ?",
							new Object[] { sdf.format(date), channel });
			Map<Integer, String> resultMap = new HashMap<Integer, String>();
			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), "");
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, String>();
		}
	}

	public static Map<Integer, Integer> getRegNewUsersByChannel(String channel) {
		try {
			DBResultSet rs = ConfigUtil.regdb
					.execSQLQuery(
							"select distinct user_id monetid from user_info where channel = ?",
							new Object[] { channel });
			Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), 0);
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, Integer>();
		}
	}

	public static Map<Integer, String> getRegNewUsersByBilling(Date date,
			String Billing) {
		try {
			DBResultSet rs = ConfigUtil.regdb
					.execSQLQuery(
							"select distinct user_id monetid from user_info where convert(varchar(10),create_date,20)= ? and billing = ?",
							new Object[] { sdf.format(date), Billing });
			Map<Integer, String> resultMap = new HashMap<Integer, String>();
			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), "");
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, String>();
		}
	}

	public static Map<Integer, Integer> getRegNewUsersByBilling(String billing) {
		try {
			DBResultSet rs = ConfigUtil.regdb
					.execSQLQuery(
							"select distinct user_id monetid from user_info where  billing = ?",
							new Object[] { billing });
			Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), 0);
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, Integer>();
		}
	}

	/*public static Map<Integer, String> getDbNewUsersServer1(Date date) {
		try {
			DBResultSet rs = ConfigUtil.server1db
					.execSQLQuery(
							"select distinct monetid monetid from fisher where convert(varchar(10),newuserflag,20)= ?",
							new Object[] { sdf.format(date) });

			Map<Integer, String> resultMap = new HashMap<Integer, String>();
			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), "");
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, String>();
		}
	}*/

	/*public static Map<Integer, String> getDbNewUsersServer2(Date date) {
		try {
			DBResultSet rs = ConfigUtil.server2db
					.execSQLQuery(
							"select distinct monetid monetid from fisher where convert(varchar(13),newuserflag,20)= ?",
							new Object[] { sdf.format(date) });

			Map<Integer, String> resultMap = new HashMap<Integer, String>();
			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), "");
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, String>();
		}
	}*/

	/*public static Map<Integer, String> getDbNewUsersServer3(Date date) {
		try {
			DBResultSet rs = ConfigUtil.server3db
					.execSQLQuery(
							"select distinct monetid monetid from fisher where convert(varchar(13),newuserflag,30)= ?",
							new Object[] { sdf.format(date) });

			Map<Integer, String> resultMap = new HashMap<Integer, String>();
			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), "");
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, String>();
		}
	}*/

	public static Map<Integer, String> getDbNewUsers(Date date) {
		try {
			DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select distinct monetid monetid from fisher where convert(varchar(10),newuserflag,20)= ?",new Object[] { sdf.format(date) });

			Map<Integer, String> resultMap = new HashMap<Integer, String>();
			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), "");
			}

			rs = ConfigUtil.server2db.execSQLQuery("select distinct monetid monetid from fisher where convert(varchar(10),newuserflag,20)= ?",new Object[] { sdf.format(date) });

			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), "");
			}

			rs = ConfigUtil.server3db.execSQLQuery("select distinct monetid monetid from fisher where convert(varchar(10),newuserflag,20)= ?",new Object[] { sdf.format(date) });

			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), "");
			}

			rs = ConfigUtil.server4db.execSQLQuery("select distinct monetid monetid from fisher where convert(varchar(10),newuserflag,20)= ?",new Object[] { sdf.format(date) });

			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), "");
			}
			rs = ConfigUtil.server5db.execSQLQuery("select distinct monetid monetid from fisher where convert(varchar(10),newuserflag,20)= ?",new Object[] { sdf.format(date) });

			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), "");
			}
			
			rs = ConfigUtil.server6db.execSQLQuery("select distinct monetid monetid from fisher where convert(varchar(10),newuserflag,20)= ?",new Object[] { sdf.format(date) });

			while (rs.next()) {
				resultMap.put(rs.getInt("monetid"), "");
			}

			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, String>();
		}
	}

	public static Map<Integer, Integer> getDbNewUsersByServer(Date stime,
			Date etime, String server) {
		try {
			Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
			if ("Server1".equals(server)) {
				DBResultSet rs = ConfigUtil.server1db.execSQLQuery("select distinct monetid monetid from fisher where newuserflag >= ? and newuserflag < ?",new Object[] { stime, etime });
				while (rs.next()) {
					resultMap.put(rs.getInt("monetid"), 0);
				}
			} else if ("Server2".equals(server)) {
				DBResultSet rs = ConfigUtil.server2db.execSQLQuery("select distinct monetid monetid from fisher where newuserflag >= ? and newuserflag < ?",new Object[] { stime, etime });
				while (rs.next()) {
					resultMap.put(rs.getInt("monetid"), 0);
				}
			} else if ("Server3".equals(server)) {
				DBResultSet rs = ConfigUtil.server3db.execSQLQuery("select distinct monetid monetid from fisher where newuserflag >= ? and newuserflag < ?",new Object[] { stime, etime });
				while (rs.next()) {
					resultMap.put(rs.getInt("monetid"), 0);
				}
			}else if ("Server4".equals(server)) {
				DBResultSet rs = ConfigUtil.server4db.execSQLQuery("select distinct monetid monetid from fisher where newuserflag >= ? and newuserflag < ?",new Object[] { stime, etime });
				while (rs.next()) {
					resultMap.put(rs.getInt("monetid"), 0);
				}
			}else if ("Server5".equals(server)) {
				DBResultSet rs = ConfigUtil.server5db.execSQLQuery("select distinct monetid monetid from fisher where newuserflag >= ? and newuserflag < ?",new Object[] { stime, etime });
				while (rs.next()) {
					resultMap.put(rs.getInt("monetid"), 0);
				}
			}else if ("Server6".equals(server)) {
				DBResultSet rs = ConfigUtil.server6db.execSQLQuery("select distinct monetid monetid from fisher where newuserflag >= ? and newuserflag < ?",new Object[] { stime, etime });
				while (rs.next()) {
					resultMap.put(rs.getInt("monetid"), 0);
				}
			}
			return resultMap;
		} catch (Exception e) {
			return new HashMap<Integer, Integer>();
		}
	}

}
